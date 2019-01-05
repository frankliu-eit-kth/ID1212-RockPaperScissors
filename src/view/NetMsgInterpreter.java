package view;

import game.controller.ConsoleOutput;
import game.controller.GameController;
import game.model.Gesture;
import game.model.Player;
import game.model.PlayerStatus;
import net.common.InetId;
import net.common.NetMessage;
import net.common.GameMsgHandler;

public class NetMsgInterpreter implements GameMsgHandler {
	GameController gameController;
	ConsoleOutput output;
	
	public NetMsgInterpreter(GameController gameController,ConsoleOutput output) {
		// TODO Auto-generated constructor stub
		this.gameController=gameController;
		this.output=output;
	}
	
	@Override
	public void handleMsg(NetMessage msg)  {
		// TODO Auto-generated method stub
		String header=msg.getHeader().toLowerCase();
		long id=msg.getFromAddresss().id;
		
		try {
			switch(header) {
				case "join":
					System.out.println("test: player id"+id);
					if(!checkId(id)) {
						Player newPlayer=new Player();
						newPlayer.setId(id);
						newPlayer.setName("player"+id);
						newPlayer.setScore(0);
						newPlayer.setStatus(PlayerStatus.JOINED);
						newPlayer.setGesture(null);
						gameController.addPlayer(newPlayer);
					}else {
						output.output("repeat player");
					}
					break;
				case "username":
					if(checkId(id)) {
						String newName=msg.getBody().get(0);
						gameController.changePlayerName(id, newName);
					}else {
						output.output("player not found");
					}
					break;
				case "ready":
					if(checkId(id)) {
						gameController.changePlayerStatus(id, PlayerStatus.READY);
					}else {
						output.output("player not found");
					}
					break;
				case "rock":
					if(checkId(id)) {
						gameController.setGesture(id, Gesture.ROCK);
					}else {
						output.output("player not found");
					}
					break;
				case "paper":
					if(checkId(id)) {
						gameController.setGesture(id, Gesture.PAPER);
					}else {
						output.output("player not found");
					}
					break;
				case "scissors":
					if(checkId(id)) {
						gameController.setGesture(id, Gesture.SCISSORS);
					}else {
						output.output("player not found");
					}
					break;
				case "quit":
					if(checkId(id)) {
						gameController.changePlayerStatus(id, PlayerStatus.QUITED);
						gameController.removeById(id);
					}else {
						output.output("player not found");
					}
					break;
				default:
					throw new Exception("unknown message");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				
				
	
	}
		
	private boolean checkId(long id) {
		
		if(gameController.findById(id)==null) {
			return false;
		}else {
			return true;
		}
	}
	
	
	

}
