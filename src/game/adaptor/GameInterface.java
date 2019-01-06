package game.adaptor;

import game.model.HandGesture;
import game.model.Player;
import game.model.PlayerStatus;
/**
 * provides all the functions needed for game execution and management
 * @author Liming Liu
 *
 */
public interface GameInterface {
	
	public void addPlayer(Player player);
	
	public void executeOneRound();
	
	public void changePlayerName(long id, String name);
	
	public void changePlayerStatus(long id,PlayerStatus status);
	
	public void setHandGesture(long id,HandGesture gesture);
	
	public Player findById(long id);
	
	public void removeById(long id);
	
	
}
