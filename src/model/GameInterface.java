package model;

public interface GameInterface {
	public void newGame();
	public void addPlayer();
	public void executeOneRound();
	public void changePlayerName();
	public void changePlayerStatus();
	public void setGesture();
	public Player findPlayerByClientAddress();
}
