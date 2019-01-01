package model;

public class Player {
	public int id;
	public int score;
	public String serverAddress;
	public String clientAddress;
	public String name;
	public PlayerStatus currentStatus;
	public Gesture currentGesture;
	
	public Player() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PlayerStatus getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(PlayerStatus currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Gesture getCurrentGesture() {
		return currentGesture;
	}
	public void setCurrentGesture(Gesture currentGesture) {
		this.currentGesture = currentGesture;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) throws Exception {
		String[] components=serverAddress.split(":");
		if(components.length!=2) {
			throw new Exception("address format error: wrong address "+serverAddress);
		}
		this.serverAddress = serverAddress;
	}
	public String getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(String clientAddress) throws Exception{
		String[] components=clientAddress.split(":");
		if(components.length!=2) {
			throw new Exception("address format error: wrong address "+clientAddress);
			
		}
		this.serverAddress = clientAddress;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", score=" + score + ", serverAddress=" + serverAddress + ", clientAddress="
				+ clientAddress + ", name=" + name + ", currentStatus=" + currentStatus + ", currentGesture="
				+ currentGesture + "]";
	}
	
	
	
}
