package net.adaptor;

import net.model.JmsServerAddress;
/**
 * provides the methods to handle NetAddress message from network( to be specific, the JMS server)
 * @author Liming Liu
 *
 */
public interface AddressMsgHandler {
	
	public void handleAddress(JmsServerAddress newAddress);
	
}
