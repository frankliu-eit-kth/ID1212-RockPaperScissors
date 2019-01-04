package net.socket.controller;

import net.common.GameMsgHandler;
import net.socket.model.ForkNode;
import net.socket.model.ForkNode_old;

public class NetworkController_old {
	
	private ForkNode_old thisNode;
	private GameMsgHandler msgHandler;
	
	public NetworkController_old(int portNo, GameMsgHandler msgHandler) {
		this.msgHandler=msgHandler;
		thisNode=new ForkNode_old(portNo, msgHandler);
	}
	
	
	public void broadcast(Object msg) {
		try {
			thisNode.broadcastMsg(msg);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(Object quitMsg) {
		if(thisNode!=null) {
			thisNode.stop(quitMsg);
		}
	}

}
