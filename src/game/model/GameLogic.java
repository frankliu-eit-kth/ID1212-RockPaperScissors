package game.model;

import java.util.ArrayList;

public class GameLogic {
	public ArrayList<Player> gameOn(ArrayList<Player> players) throws Exception{
		GestureComparator comparator=new GestureComparator();
		
		for(Player p1:players) {
			int scoreP1=p1.getScore();
			for(Player p2: players) {
				Gesture gestureP1=p1.getGesture();
				Gesture gestureP2=p2.getGesture();
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
	
	public static void main(String args[]) throws Exception{
		ArrayList<Player> players=new ArrayList<Player>();
		GameLogic gl=new GameLogic();
		/*
		Player p1=new Player(1,0,Gesture.PAPER);
		Player p2=new Player(2,0,Gesture.ROCK);
		Player p3=new Player(3,0,Gesture.PAPER);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p3.toString());
		gl.gameOn(players);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p3.toString());
		p1.setCurrentGesture(Gesture.PAPER);
		p2.setCurrentGesture(Gesture.SCISSORS);
		p3.setCurrentGesture(Gesture.PAPER);
		gl.gameOn(players);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p3.toString());
		p1.setCurrentGesture(Gesture.ROCK);
		p2.setCurrentGesture(Gesture.ROCK);
		p3.setCurrentGesture(Gesture.ROCK);
		gl.gameOn(players);
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p3.toString());
		*/
	}
	
}
