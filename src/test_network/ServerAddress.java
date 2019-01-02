package test_network;

import java.io.Serializable;

public class ServerAddress implements Serializable{
	public String ip;
	public int port;
	public ServerAddress(String ip , int port) {
		this.ip=ip;
		this.port=port;
	}
	@Override
	public String toString() {
		return "ServerAddress [ip=" + ip + ", port=" + port + "]";
	}
	
	
}