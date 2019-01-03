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
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * io stream not flush or reset
 * @author m1339
 *
 */

public class ForkNode {
	
	private ServerSocket forkHandle;
	private final List<ClientListener> clientHandlers = new ArrayList<>();
	
	private CopyOnWriteArrayList<ServerConnection> forkBranches;
	
	private InetId thisAddress;
	private final InetId bootstrapAddress=new InetId("localhost",8080);
	
	private MsgHandler msgHandler;
	
	private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    
    private boolean bootstrapped=false;
    private boolean running=false;
    
    private static final String SERVER_ADDRESS_CLASS_NAME="net.model.InetId";
    private static final String MESSAGE_CLASS_NAME="net.model.Message";
    
   
    
    public ForkNode(int serverPort,MsgHandler msgHandler) {
		
    	this.thisAddress=new InetId("127.0.0.1", serverPort);
		this.msgHandler=msgHandler;
		
		try {
            this.forkHandle = new ServerSocket(serverPort);
            startServerSocket(forkHandle);
            System.out.println("node inited, server socket running");
		 } catch (IOException e) {
	            System.err.println("Server failure.");
	        }
		System.out.println("bootstrpping");
		this.forkBranches=new CopyOnWriteArrayList<>();
		bootstrap();
		
	}
    
    
    public void broadcastMsg(Object msg) {
    	for(ServerConnection conn:forkBranches) {
			conn.send(msg);
    	}
    }
    public void stop(Object quitMsg) {
    	this.running=false;
    	broadcastMsg(quitMsg);
    	for(ServerConnection conn:forkBranches) {
    		conn.stop();
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
		
        try {
        	ServerConnection newBranch=new ServerConnection(serverAddress);
        	Socket socket = newBranch.getSocket();
			socket.setSoTimeout(TIMEOUT_HALF_HOUR);
			System.out.println("new branch created, connect to "+ socket.getPort());
			newBranch.send(thisAddress);
			System.out.println("sent local server address to new peer's server");
			forkBranches.add(newBranch);
			
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
		new Thread(listener).start();
		clientHandlers.add(listener);
		System.out.println("new handler created and added");
	}
	
	
	private class ClientListener implements Runnable{
		
		private Socket clientSocket;
		private MsgHandler msgHandler;
		private ObjectInputStream fromPeer;
		
		public ClientListener(Socket clientSocket,MsgHandler handler) {
			this.clientSocket=clientSocket;
			this.msgHandler=handler;
			try {
				this.fromPeer=new ObjectInputStream(this.clientSocket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
                for (;;) {
                	
                    Object msg=fromPeer.readObject();
                    System.out.println("received new ojb:"+msg.getClass().getName());
                    if(msg.getClass().getName().equals(SERVER_ADDRESS_CLASS_NAME)) {
                    	InetId newAddress=(InetId)msg;
                    	System.out.println("received server address: "+ newAddress.toString());
                    	String newIp=newAddress.ip;
                    	int newPort=newAddress.port;
                    	boolean flag=false;
                    	for(ServerConnection branch:forkBranches) {
                    		Socket socket=branch.getSocket();
                    		//System.out.println(branch.getInetAddress().getHostAddress());
                    		if(socket.getInetAddress().getHostAddress().equals(newIp)&&socket.getPort()==newPort) {
                    			flag=true;
                    		}
                    	}
                    	if(!flag) {
                    		System.out.println("new address received, creating new branch");
                    		initNewForkBranch(newAddress);
                    		
										for (ServerConnection branch : forkBranches) {
											branch.send(newAddress);
										}
									
								
                    	}
                    }else {
                    	if(msg.getClass().getName().equals(MESSAGE_CLASS_NAME)) {
                    		InetId netId=new InetId(this.clientSocket.getInetAddress().toString(),this.clientSocket.getPort());
                    		msgHandler.handleMsg((Message)msg,netId);
                    	}else {
                    		throw new Exception("unknown message type "+msg.getClass().getName());
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
					 System.out.println("receive new socket from "+clientSocket.getPort());
				     startHandler(clientSocket);
				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class ServerConnection {
		private Socket clientSocket;
		private ObjectOutputStream toPeer;
		private boolean connected=false;
		
		public ServerConnection(InetId serverAddress) {
			// TODO Auto-generated constructor stub
			try {
				this.clientSocket=new Socket();
				this.clientSocket.connect(new InetSocketAddress(serverAddress.ip, serverAddress.port), TIMEOUT_HALF_MINUTE);
				connected=true;
				//boolean autoFlush = true;
				this.toPeer=new ObjectOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void send(Object msg) {
			try {
				synchronized (toPeer) {
					toPeer.writeObject(msg);
					toPeer.flush();
					//toPeer.reset();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void stop() {
			try {
				toPeer.close();
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		public Socket getSocket() {
			return this.clientSocket;
		}

	}

}
