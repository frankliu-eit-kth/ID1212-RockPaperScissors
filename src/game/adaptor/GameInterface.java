package game.adaptor;

import game.model.Gesture;
import game.model.Player;
import game.model.PlayerStatus;

public interface GameInterface {
	
	public void addPlayer(Player player);
	public void executeOneRound();
	public void changePlayerName(long id, String name);
	public void changePlayerStatus(long id,PlayerStatus status);
	public void setGesture(long id,Gesture gesture);
	public Player findById(long id);
	public void removeById(long id);
	
	
}
