package net.jms.controller;

import java.io.Serializable;

import net.common.GameMsgHandler;
import net.jms.model.ForkNode;

public class NetworkController {
	private ForkNode thisNode;
	private GameMsgHandler msgHandler;
	
	public NetworkController(int portNo, GameMsgHandler msgHandler) {
		this.msgHandler=msgHandler;
		thisNode=new ForkNode(portNo, msgHandler);
	}
	
	
	public void broadcast(Serializable msg) {
		try {
			thisNode.broadcastMsg(msg);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(Serializable quitMsg) {
		if(thisNode!=null) {
			thisNode.stop(quitMsg);
		}
	}
	
	

}
