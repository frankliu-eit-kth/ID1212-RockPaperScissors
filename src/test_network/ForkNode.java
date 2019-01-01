package test_network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.net.ClientHandler;

public class ForkNode {
	private ServerSocket handle;
	private ArrayList<Socket> branches;
	 private final List<ClientHandler> clientHandlers = new ArrayList<>();
	private ServerAddress thisServerAddress;
	private final ServerAddress bootstrapAddress=new ServerAddress("localhost",8080);
	
	private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private boolean bootstrapped=false;
	
    
    
	public ForkNode(int serverPort) {
		// TODO Auto-generated constructor stub
		this.thisServerAddress=new ServerAddress("localhost", serverPort);
		initNewSocket(bootstrapAddress);
		bootstrapped=true;
		try {
            this.handle = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = handle.accept();
                startHandler(clientSocket);
            }
            
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private void initNewSocket(ServerAddress serverAddress) throws Exception {
		Socket socket=new Socket();
		socket.connect(new InetSocketAddress(serverAddress.ip, serverAddress.port), TIMEOUT_HALF_MINUTE);
		socket.setSoTimeout(TIMEOUT_HALF_HOUR);
		boolean autoFlush = true;
		branches.add(socket);
		ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
		toServer.writeObject(thisServerAddress);
		braodcastNewAddress();
		
	}
	
	
	public class ServerAddress{
		public String ip;
		public int port;
		public ServerAddress(String ip , int port) {
			this.ip=ip;
			this.port=port;
		}
	}
}
