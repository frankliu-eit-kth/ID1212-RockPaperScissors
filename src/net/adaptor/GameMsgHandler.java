package net.adaptor;

import net.model.GameMessage;
/**
 * provides the methods to handle Message instances from network( to be specific, the JMS server)
 * @author Liming Liu
 *
 */
public interface GameMsgHandler {
	
	public void handleMsg(GameMessage msg);
	
	
	
}
