package game.controller;

import java.util.ArrayList;

import game.adaptor.GameInterface;
import game.model.GameLogic;
import game.model.HandGesture;
import game.model.Player;
import game.model.PlayerStatus;
/**
 * The game controller
 * Stores all players insances
 * Execute game
 * Update player status
 * Deliver message to the upper view
 * @author Liming Liu
 *
 */
public class GameController implements GameInterface {
	
	
	ArrayList<Player> players;
	ArrayList<Player> readyPlayers;
	
	GameLogic game;
	
	ConsoleOutput viewOutput;
	
	
	private final int CODE_WAITING_FOR_ALL_READY=1;
	private final int CODE_WAITING_FOR_MOVE=2;
	
	private int gameRoomStatus;
	
	public GameController(ConsoleOutput output) {
		
		this.players=new ArrayList<Player>();
		this.game=new GameLogic();
		this.viewOutput=output;
		gameRoomStatus=CODE_WAITING_FOR_ALL_READY;
		
	}
	
	@Override
	public void removeById(long id) {
		
		Player p=findById(id);
		
		if(p!=null) {
			viewOutput.output("player "+p.getName()+" quitted game");
			players.remove(p);
			p=null;
		}
	}
	
	@Override
	public void executeOneRound() {
	
		try {
			
			game.oneRound(players);
			ArrayList<String> result=new ArrayList<String>();
			for(Player p: players) {
				result.add(p.toString());
				p.reset();
			}
			viewOutput.outputMultipleStrings(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addPlayer(Player player) {
		
		if(gameRoomStatus!=CODE_WAITING_FOR_ALL_READY) {
			viewOutput.output("game round executing, please join later");
			return;
		}
		
		this.players.add(player);
		
		viewOutput.output("new player joined "+player.toString());
	}
	
	@Override
	public void changePlayerName(long id, String name) {
	
		
		if(gameRoomStatus!=CODE_WAITING_FOR_ALL_READY) {
			viewOutput.output("game on, cannot change name");
			return;
		}
		
		Player p=findById(id);
		
		if(p!=null) {
			p.setName(name);
			viewOutput.output("player id="+p.getId()+" changed name to "+name);
		}
	}
	
	@Override
	public void changePlayerStatus(long id, PlayerStatus status) {
		
		/*
		 * change status
		 */
		Player p=findById(id);
		if(p!=null) {
			p.setStatus(status);
			viewOutput.output("player "+p.getName()+" is now "+status.name());
		}
		
		/*
		 * check whether all players are ready
		 */
		
		int numOfReadyPlayers=0;
		for(Player player:players) {
			if(player.getStatus()==PlayerStatus.READY) {
				numOfReadyPlayers++;
			}
		}
		if(numOfReadyPlayers==players.size()) {
			viewOutput.output("all players ready, you can show your hand gesture");
		}
		
	}
	
	@Override
	public void setHandGesture(long id, HandGesture gesture) {
		/*
		 * set player hand gesture
		 */
		Player p=findById(id);
		if(p!=null) {
			p.setStatus(PlayerStatus.GESTURED);
			p.setGesture(gesture);
			viewOutput.output("player "+p.getName()+" showed his move");
		}else {return;}
		
		/*
		 * check if all players showed gestures. if yes execute one round
		 */
		int numOfFinishedPlayers=0;
		for(Player player:players) {
			if(player.getStatus()==PlayerStatus.GESTURED) {
				numOfFinishedPlayers++;
			}
		}
		if(numOfFinishedPlayers==players.size()) {
			viewOutput.output("all finished move, showing result");
			executeOneRound();
		}
		
	}
	
	@Override
	public Player findById(long id) {
		for(Player p:players) {
			if(p.getId()==id) {
				return p;
			}
		}
		return null;
	}

}
