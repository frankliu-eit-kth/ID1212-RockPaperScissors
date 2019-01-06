package net.controller;

import net.adaptor.GameMsgHandler;
import net.model.ForkNode;
import net.model.GameMessage;
/**
 * the controller and manager of network
 * create and operate on fork node to send\receive message
 * @author Liming Liu
 *
 */
public class NetworkController {
	
	private ForkNode thisNode;
	
	private GameMsgHandler msgHandler;
	
	public NetworkController(int portNo, GameMsgHandler msgHandler) {
		this.msgHandler=msgHandler;
		thisNode=new ForkNode(portNo, msgHandler);
	}
	
	
	public void broadcastMsg(GameMessage msg) {
		try {
			thisNode.broadcastGameMsg(msg);
		} catch (NullPointerException e) {
			
			e.printStackTrace();
		}
	}
	
	public void stop(GameMessage quitMsg) {
		if(thisNode!=null) {
			thisNode.stop(quitMsg);
		}
	}
	
	public void remove(long id) {
		thisNode.removeSender(id);
	}
	
	

}
