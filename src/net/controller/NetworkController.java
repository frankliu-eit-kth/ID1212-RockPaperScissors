package net.controller;

import net.model.ForkNode;
import net.model.MsgHandler;

public class NetworkController {
	
	private ForkNode thisNode;
	private MsgHandler msgHandler;
	
	public NetworkController(int portNo, MsgHandler msgHandler) {
		this.msgHandler=msgHandler;
		thisNode=new ForkNode(portNo, msgHandler);
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
