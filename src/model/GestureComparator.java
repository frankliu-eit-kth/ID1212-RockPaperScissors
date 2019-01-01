package model;

public class GestureComparator{
	
	public int compare(Gesture g1,Gesture g2) throws Exception {
		if(g1==Gesture.ROCK) {
			if(g2==Gesture.ROCK) {
				return 0;
			}
			else if(g2==Gesture.PAPER) {
				return -1;
			}
			else if(g2==Gesture.SCISSORS) {
				return 1;
			}
			else {
				throw new Exception("unknown gesture 2");
			}
		}
		
		else if(g1==Gesture.PAPER) {
			if(g2==Gesture.ROCK) {
				return 1;
			}
			else if(g2==Gesture.PAPER) {
				return 0;
			}
			else if(g2==Gesture.SCISSORS) {
				return -1;
			}
			else {
				throw new Exception("unknown gesture 2");
			}
		}
		
		else if(g1==Gesture.SCISSORS) {
			if(g2==Gesture.ROCK) {
				return -1;
			}
			else if(g2==Gesture.PAPER) {
				return 1;
			}
			else if(g2==Gesture.SCISSORS) {
				return 0;
			}
			else {
				throw new Exception("unknown gesture 2");
			}
		}
		else {
			throw new Exception("unknown gesture 1");
		}
		
	}
	
}