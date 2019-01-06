package view;

import java.util.ArrayList;
import java.util.Scanner;

import game.controller.ConsoleOutput;
import game.controller.GameController;
import net.controller.NetworkController;
import net.model.GameMessage;

/**
 * Reads and interprets user commands. The command interpreter will run in a separate thread, which
 * is started by calling the <code>start</code> method. Commands are executed in a thread pool, a
 * new prompt will be displayed as soon as a command is submitted to the pool, without waiting for
 * command execution to complete.
 * 
 */
public class NonBlockingInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;
    private NetworkController netController;
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private GameController gameController;
    private GameMsgHandlerImpl msgHandler;
    /**
     * Starts the interpreter. The interpreter will be waiting for user input when this method
     * returns. Calling <code>start</code> on an interpreter that is already started has no effect.
     */
    public void start() {
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        gameController=new GameController(new ScreenOutput());
        new Thread(this).start();
    }

    /**
     * Interprets and performs user commands.
     */
    @Override
    public void run() {
    	outMgr.println("Welcome to rock paper scissors game");
    	printInstruction();
        while (receivingCmds) {
            try {
                CmdLineHandler cmdLine = new CmdLineHandler(readNextLine());
                switch (cmdLine.getCmd()) {
                	case CONNECT:
                    if(netController==null) {
                    	String str=cmdLine.getParameter(0);
                    	if(str!=null) {
                    		int portNo=Integer.parseInt(str);
                    		GameMsgHandlerImpl msgHandler=new GameMsgHandlerImpl(this.gameController,new ScreenOutput());
                    		this.netController=new NetworkController(portNo, msgHandler);
                    	}else {
                    		outMgr.println("wrong parameter, try again");
                    		printInstruction();
                    	}
                    }
                    break;
                    case QUIT:
                        receivingCmds = false;
                        GameMessage msg=new GameMessage("quit",null,null);
                        netController.stop(msg);
                        this.receivingCmds=false;
                        break;
                    case USERNAME:
                    	if(cmdLine.getParameter(0)!=null) {
                    		ArrayList<String> tempList=new ArrayList<String>();
                    		tempList.add(cmdLine.getParameter(0));
                    		GameMessage userNameMsg=new GameMessage("USERNAME",tempList,null);
                    		netController.broadcastMsg(userNameMsg);
                    	}       
                        break;
                    case JOIN:
                    	netController.broadcastMsg(new GameMessage("JOIN",null,null));
                    	break;
                    case READY:
                    	netController.broadcastMsg(new GameMessage("READY",null,null));
                    	break;
                    case ROCK:
                    	netController.broadcastMsg(new GameMessage("ROCK",null,null));
                    	break;
                    case PAPER:
                    	netController.broadcastMsg(new GameMessage("PAPER",null,null));
                    	break;
                    case SCISSORS:
                    	netController.broadcastMsg(new GameMessage("SCISSORS",null,null));
                    	break;
                    default:
                        outMgr.println("unknown command");
                        printInstruction();
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }
    
    private void printInstruction() {
    	outMgr.println("notice:Before start game, make sure Jms server is running on port 3035");
    	outMgr.println("		command list");
    	outMgr.println("	CONNECT+ (your node's  jms server address)");
    	outMgr.println("	JOIN");
    	outMgr.println("	READY");
    	outMgr.println("	USERNAME + (new user name)");
    	outMgr.println("	ROCK");
    	outMgr.println("	PAPER");
    	outMgr.println("	SCISSORS");
    	outMgr.println("	QUIT");
    }
    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }
    /**
     * @role: the main logic for UI
     * @UI logic: 
     * 		firstly check the game state( win, lose, continue), different system notice for different state
     * 		then print all the details of the game status
     * 		all the print are thread safe by using the thread safe standard output
     */
    private class ScreenOutput implements ConsoleOutput {
       @Override
        public void output(String msg) {
        	// TODO Auto-generated method stub
        	outMgr.println(msg);
        }
       @Override
    	public void outputMultipleStrings(ArrayList<String> msg) {
    		// TODO Auto-generated method stub
    		for(String s:msg) {
    			outMgr.println(s);
    		}
    	}
       public void handlerQuit(long id) {
    	   netController.remove(id);
       }
    }
}
