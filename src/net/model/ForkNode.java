package net.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
/**
 * io stream not flush or reset
 * @author m1339
 *
 */

public class ForkNode {
	private ServerSocket forkHandle;
	private ArrayList<Socket> forkBranches;
	private final List<ClientListener> clientHandlers = new ArrayList<>();
	private InetId thisAddress;
	private final InetId bootstrapAddress=new InetId("127.0.0.1",8080);
	private MsgHandler msgHandler;
	
	private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private boolean bootstrapped=false;
    private boolean running=false;
    private static final String SERVER_ADDRESS_CLASS_NAME="net.model.ServerAddress";
    private static final String MESSAGE_CLASS_NAME="net.model.Message";
    
    public ForkNode(int serverPort,MsgHandler msgHandler) {
		// TODO Auto-generated constructor stub
		this.thisAddress=new InetId("127.0.0.1", serverPort);
		this.msgHandler=msgHandler;
		try {
            this.forkHandle = new ServerSocket(serverPort);
		 } catch (IOException e) {
	            System.err.println("Server failure.");
	        }
		this.forkBranches=new ArrayList<Socket>();
		System.out.println("node inited, server socket created");
		startServerSocket(forkHandle);
		System.out.println("server running");
		bootstrap();
		
	}
    
    
    public void broadcastMsg(Object obj) {
    	try {
			for(Socket branch: forkBranches) {
				ObjectOutputStream toPeer=new ObjectOutputStream(branch.getOutputStream());
				toPeer.writeObject(obj);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void stop(Object quitMsg) {
    	this.running=false;
    	broadcastMsg(quitMsg);
    	for(Socket branch: forkBranches) {
    		try {
				branch.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		forkBranches.remove(branch);
    		branch=null;
    	}
    }
    
	
	
	private void startServerSocket(ServerSocket serverSocket) {
		this.running=true;
		Server server=new Server(serverSocket);
		new Thread(server).start();
	}

	private void bootstrap() {
		if(this.bootstrapped) {
			return;
		}
		try {
			initNewForkBranch(bootstrapAddress);
			System.out.println("bootstrapped");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.bootstrapped=true;
		}

	private void initNewForkBranch(InetId serverAddress) throws Exception {
		Socket socket = new Socket();
        try {
        	forkBranches.add(socket);
        	
			socket.connect(new InetSocketAddress(serverAddress.ip, serverAddress.port), TIMEOUT_HALF_MINUTE);
			socket.setSoTimeout(TIMEOUT_HALF_HOUR);
			ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("new branch created, to"+ socket.getPort());
			toServer.writeObject(thisAddress);
			System.out.println("sent local server address to new peer's server");
			toServer.flush();
	        toServer.reset();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	private void startHandler(Socket clientSocket) {
		ClientListener listener=new ClientListener(clientSocket,this.msgHandler);
		new Thread(listener).run();
		clientHandlers.add(listener);
		System.out.println("new handler created and added");
	}
	
	
	private class ClientListener implements Runnable{
		private Socket clientSocket;
		private MsgHandler msgHandler;
		public ClientListener(Socket clientSocket,MsgHandler handler) {
			this.clientSocket=clientSocket;
			this.msgHandler=handler;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
                for (;;) {
                	ObjectInputStream fromServer=new ObjectInputStream(this.clientSocket.getInputStream());
                    Object msg=fromServer.readObject();
                    System.out.println("received new ojb:"+msg.getClass().getName());
                    if(msg.getClass().getName().equals(SERVER_ADDRESS_CLASS_NAME)) {
                    	InetId newAddress=(InetId)msg;
                    	System.out.println("received server address: "+ newAddress.toString());
                    	String newIp=newAddress.ip;
                    	int newPort=newAddress.port;
                    	boolean flag=false;
                    	for(Socket branch:forkBranches) {
                    		//System.out.println(branch.getInetAddress().getHostAddress());
                    		if(branch.getInetAddress().getHostAddress().equals(newIp)&&branch.getPort()==newPort) {
                    			flag=true;
                    		}
                    	}
                    	if(!flag) {
                    		System.out.println("new address received, creating new branch");
                    		initNewForkBranch(newAddress);
                    		
                    		for(Socket branch:forkBranches) {
                    			ObjectOutputStream toPeer=new ObjectOutputStream(branch.getOutputStream());
                    			toPeer.writeObject(newAddress);
                    		}
                    	}
                    }else {
                    	if(msg.getClass().getName().equals(MESSAGE_CLASS_NAME)) {
                    		InetId netId=new InetId(this.clientSocket.getInetAddress().toString(),this.clientSocket.getPort());
                    		msgHandler.handleMsg((Message)msg,netId);
                    	}else {
                    		throw new Exception("unknown message type");
                    	}
                    	
                    }
                }
            } catch (Throwable connectionFailure) {
                connectionFailure.printStackTrace();
            }
		}
	}
	private class Server implements Runnable{
		ServerSocket serverSocket;
		public Server(ServerSocket serverSocket) {
			this.serverSocket=serverSocket;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(running) {
					 Socket clientSocket = serverSocket.accept();
				     startHandler(clientSocket);
				     System.out.println("receive new socket from "+clientSocket.getPort());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
