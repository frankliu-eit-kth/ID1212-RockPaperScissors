package test_network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import test_network.ForkNode.ServerAddress;

public class ClientHandler implements Runnable{
	
	private final ForkNode forkNode;
	private final Socket clientSocket;
	 private ObjectInputStream fromClient;
	 private boolean connected;
	 
	 
	public ClientHandler(ForkNode forkNode, Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.forkNode = forkNode;
        this.clientSocket = clientSocket;
        connected = true;
	}
	@Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient =  new ObjectInputStream(clientSocket.getInputStream());
            //toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        while (connected) {
            try {
                Object rcvdObj=fromClient.readObject();
                System.out.println("test: msg type"+rcvdObj.getClass().getName());
                switch (rcvdObj.getClass().getName()) {
                    case "ServerAddress":
                    	ServerAddress msgAddress=(ServerAddress)rcvdObj;
                    	if(!checkBranchDestination(msgAddress)) {
                    		initNewSocket(msgAddress);
                    		ss
                    	}
                        break;
                 
                    default:
                        throw new MessageException("Received corrupt message" );
                }
            } catch (IOException ioe) {
                disconnectClient();
                throw new MessageException(ioe);
            }
        }
    }
	
	
}
