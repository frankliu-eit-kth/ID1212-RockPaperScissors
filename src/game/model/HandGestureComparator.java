package game.model;
/**
 * Hand gesture comparator
 * 
 * @author Liming Liu
 *
 */
public class HandGestureComparator{
	/**
	 * compares 2 gestures, return the result
	 * @param g1
	 * @param g2
	 * @return
	 * @throws Exception
	 */
	public int compare(HandGesture g1,HandGesture g2) throws Exception {
		if(g1==HandGesture.ROCK) {
			if(g2==HandGesture.ROCK) {
				return 0;
			}
			else if(g2==HandGesture.PAPER) {
				return -1;
			}
			else if(g2==HandGesture.SCISSORS) {
				return 1;
			}
			else {
				throw new Exception("unknown gesture");
			}
		}
		
		else if(g1==HandGesture.PAPER) {
			if(g2==HandGesture.ROCK) {
				return 1;
			}
			else if(g2==HandGesture.PAPER) {
				return 0;
			}
			else if(g2==HandGesture.SCISSORS) {
				return -1;
			}
			else {
				throw new Exception("unknown gesture");
			}
		}
		
		else if(g1==HandGesture.SCISSORS) {
			if(g2==HandGesture.ROCK) {
				return -1;
			}
			else if(g2==HandGesture.PAPER) {
				return 1;
			}
			else if(g2==HandGesture.SCISSORS) {
				return 0;
			}
			else {
				throw new Exception("unknown gesture");
			}
		}
		else {
			throw new Exception("unknown gesture");
		}
		
	}
	
}