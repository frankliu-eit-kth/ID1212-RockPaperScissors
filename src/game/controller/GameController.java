package game.controller;

import java.util.ArrayList;

import game.adaptor.GameInterface;
import game.model.GameLogic;
import game.model.Gesture;
import game.model.Player;
import game.model.PlayerStatus;

public class GameController implements GameInterface {
	
	ArrayList<Player> players;
	ArrayList<Player> readyPlayers;
	GameLogic game;
	ConsoleOutput output;
	private final int CODE_WAITING_READY=1;
	private final int CODE_WAITING_MOVE=2;
	private int gameRoomStatus;
	
	public GameController(ConsoleOutput output) {
		this.players=new ArrayList<Player>();
		this.game=new GameLogic();
		this.output=output;
		gameRoomStatus=CODE_WAITING_READY;
	}
	
	@Override
	public void removeById(long id) {
		// TODO Auto-generated method stub
		Player p=findById(id);
		if(p!=null) {
			output.output("player "+p.getName()+" quitted game");
			players.remove(p);
			p=null;
		}
	}
	
	@Override
	public void executeOneRound() {
		// TODO Auto-generated method stub
		try {
			game.gameOn(players);
			ArrayList<String> result=new ArrayList<String>();
			for(Player p: players) {
				result.add(p.toString());
				p.reset();
			}
			output.outputMultiple(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addPlayer(Player player) {
		// TODO Auto-generated method stub
		if(gameRoomStatus!=CODE_WAITING_READY) {
			output.output("game on, no more players");
			return;
		}
		this.players.add(player);
		output.output("new player joined "+player.toString());
	}
	
	@Override
	public void changePlayerName(long id, String name) {
		// TODO Auto-generated method stub
		if(gameRoomStatus!=CODE_WAITING_READY) {
			output.output("game on, cannot change name");
			return;
		}
		Player p=findById(id);
		if(p!=null) {
			p.setName(name);
		}
		output.output("player id="+p.getId()+" changed name to "+name);
	}
	
	@Override
	public void changePlayerStatus(long id, PlayerStatus status) {
		// TODO Auto-generated method stub
		
		Player p=findById(id);
		if(p!=null) {
			p.setStatus(status);
		}
		output.output("player "+p.getName()+" is now "+status.name());
		
		int numOfReadyPlayers=0;
		for(Player player:players) {
			if(player.getStatus()==PlayerStatus.READY) {
				numOfReadyPlayers++;
			}
		}
		if(numOfReadyPlayers==players.size()) {
			output.output("all players ready, you can show your hand gesture");
		}
	}
	
	@Override
	public void setGesture(long id, Gesture gesture) {
		// TODO Auto-generated method stub
		Player p=findById(id);
		if(p!=null) {
			p.setStatus(PlayerStatus.GESTURED);
			p.setGesture(gesture);
		}
		output.output("player "+p.getName()+" showed his move");
		
		int numOfFinishedPlayers=0;
		for(Player player:players) {
			if(player.getStatus()==PlayerStatus.GESTURED) {
				numOfFinishedPlayers++;
			}
		}
		if(numOfFinishedPlayers==players.size()) {
			output.output("all finished move");
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
