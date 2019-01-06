package game.model;

import java.util.ArrayList;
/**
 * The game logic for rock paper scissors game
 * 
 * @author Liming Liu
 *
 */
public class GameLogic {
	/**
	 * pass the player list, for each player: compare with every other player, add or deduct score according to the result of each pair
	 * rule: for each pari, winner gets 1 point, loser lose 1 point, even result gets 0 point
	 * @param players
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Player> oneRound(ArrayList<Player> players) throws Exception{
		
		
		HandGestureComparator comparator=new HandGestureComparator();
		
		for(Player p1:players) {
			if(p1.getGesture()==null) {
				continue;
			}
			int scoreP1=p1.getScore();
			for(Player p2: players) {
				if(p2.getGesture()==null) {
					continue;
				}
				HandGesture gestureP1=p1.getGesture();
				HandGesture gestureP2=p2.getGesture();
				if(gestureP1==null||gestureP2==null) {
					throw new Exception("null gesture");
				}
				if(comparator.compare(gestureP1, gestureP2)==1) {
					scoreP1=scoreP1+1;
					p1.setScore(scoreP1);
				}
			}
		}
		return players;
	}
	
	
	
}
