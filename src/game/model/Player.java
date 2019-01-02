package game.model;

public class Player {
	private long id;
	private String name;
	private int score;
	private PlayerStatus status;
	private Gesture gesture;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public PlayerStatus getStatus() {
		return status;
	}
	public void setStatus(PlayerStatus status) {
		this.status = status;
	}
	public Gesture getGesture() {
		return gesture;
	}
	public void setGesture(Gesture gesture) {
		this.gesture = gesture;
	}
	public void reset() {
		if(this.status==PlayerStatus.GESTURED) {
			this.status=PlayerStatus.JOINED;
			this.gesture=null;
		}
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", score=" + score + ", status=" + status + ", gesture="
				+ gesture + "]";
	}
	
	
	
	
}
