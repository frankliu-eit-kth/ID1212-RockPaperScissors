package view;

import java.util.ArrayList;
import java.util.Scanner;

import game.controller.ConsoleOutput;
import game.controller.GameController;
import net.common.NetMessage;
import net.jms.controller.NetworkController;

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
    private NetMsgInterpreter msgHandler;
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
        while (receivingCmds) {
            try {
                CmdLineHandler cmdLine = new CmdLineHandler(readNextLine());
                switch (cmdLine.getCmd()) {
                    case QUIT:
                        receivingCmds = false;
                        NetMessage msg=new NetMessage("quit",null);
                        netController.stop(msg);
                        break;
                    case CONNECT:
                        if(netController==null) {
                        	String str=cmdLine.getParameter(0);
                        	if(str!=null) {
                        		int portNo=Integer.parseInt(str);
                        		NetMsgInterpreter msgHandler=new NetMsgInterpreter(this.gameController,new ScreenOutput());
                        		this.netController=new NetworkController(portNo, msgHandler);
                        	}else {
                        		System.out.println("wrong parameter, input connect+(port number)");
                        	}
                        }
                        break;
                    case USERNAME:
                    	if(cmdLine.getParameter(0)!=null) {
                    		ArrayList<String> tempList=new ArrayList<String>();
                    		tempList.add(cmdLine.getParameter(0));
                    		NetMessage userNameMsg=new NetMessage("USERNAME",tempList);
                    		netController.broadcast(userNameMsg);
                    	}       
                        break;
                    case JOIN:
                    	netController.broadcast(new NetMessage("JOIN",null));
                    	break;
                    case READY:
                    	netController.broadcast(new NetMessage("READY",null));
                    	break;
                    case ROCK:
                    	netController.broadcast(new NetMessage("ROCK",null));
                    	break;
                    case PAPER:
                    	netController.broadcast(new NetMessage("PAPER",null));
                    	break;
                    case SCISSORS:
                    	netController.broadcast(new NetMessage("SCISSORS",null));
                    	break;
                    default:
                        throw new Exception("unknown command");
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }
    /*
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
    	public void outputMultiple(ArrayList<String> msg) {
    		// TODO Auto-generated method stub
    		for(String s:msg) {
    			outMgr.println(s);
    		}
    	}
    }
}
