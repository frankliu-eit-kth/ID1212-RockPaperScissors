package view;

import game.controller.ConsoleOutput;
import game.controller.GameController;
import game.model.HandGesture;
import game.model.Player;
import game.model.PlayerStatus;
import net.adaptor.GameMsgHandler;
import net.model.JmsServerAddress;
import net.model.ForkNode;
import net.model.GameMessage;
/**
 * Implementation of game message handler
 * 
 * bridge between network and game logic
 * 
 * Control flow:  user command->non blocking interpreter->network->this GameMsgHandlerImpl->game controller->console
 * 
 * @author Liming Liu
 *
 */
public class GameMsgHandlerImpl implements GameMsgHandler {
	
	GameController gameController;
	ConsoleOutput output;
	
	public GameMsgHandlerImpl(GameController gameController,ConsoleOutput output) {
		this.gameController=gameController;
		this.output=output;
	}
	/**
	 * read game message, then operate on game controller
	 */
	@Override
	public void handleMsg(GameMessage msg)  {
		
		String header=msg.getHeader().toLowerCase();
		long id=msg.getFromAddresss().id;
		
		try {
			switch(header) {
				case "join":
					
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
						gameController.setHandGesture(id, HandGesture.ROCK);
					}else {
						output.output("player not found");
					}
					break;
				case "paper":
					if(checkId(id)) {
						gameController.setHandGesture(id, HandGesture.PAPER);
					}else {
						output.output("player not found");
					}
					break;
				case "scissors":
					if(checkId(id)) {
						gameController.setHandGesture(id, HandGesture.SCISSORS);
					}else {
						output.output("player not found");
					}
					break;
				case "quit":
					if(checkId(id)) {
						output.output("player "+ gameController.findById(id).getName()+" left game");
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
