package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private boolean isBootStrap;
	private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();//to store the multiple threads
    private int portNo;
    private GameController gameController;
	
	public Server(boolean isBootStrap, int portNo) {
		// TODO Auto-generated constructor stub
		this.isBootStrap=isBootStrap;
		this.portNo=portNo;
		this.gameController=new GameController();
	}

	private void serve() {
        try {
            ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
            
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
    }
	
	private void startHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler handler = new ClientHandler(this, clientSocket,gameController);
        synchronized (clientHandlers) {
            clientHandlers.add(handler);
        }
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
	
	void removeHandler(ClientHandler handler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(handler);
        }
    }

}
