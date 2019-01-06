package net.model;

import java.util.HashMap;
import java.util.Map.Entry;

import net.adaptor.AddressMsgHandler;
import net.adaptor.GameMsgHandler;
/**
 * The key element of p2p network
 * Each fork node has 1 receiver to its own JMS(java message service) server and multiple( num of nodes) senders to all the JSM servers, just like a fork with 1 handle and multiple branches
 * 
 * The working mechanism :
 * 1. New node join:
 * 		create a new receiver and a new sender to local server
 * 		create a new sender to the bootstrap node's server 
 * 		send local server's address to the boot strap's
 * 		bootstrap node will create a new sender to new node's server, then broadcast the new node's server address to all nodes
 * 2. Communicate:
 * 		( working mechanism of JMS discussed in sender JmsSender and JmsReceiver)
 * 		node communicates game message through broadcast to all peer nodes
 * 		all nodes share the same topic, therefore will receive the same message
 * 
 * @author Liming Liu
 *
 */
public class ForkNode {
	
	private JmsReceiver receiver=null;
	private HashMap<Long,JmsSender> senders=null;
	
	private JmsServerAddress thisAddress=null;
	private final JmsServerAddress bootstrapAddress=new JmsServerAddress("127.0.0.1",3035);
	
	private GameMsgHandler msgHandler=null;
	private AddressMsgHandlerImpl addressHandler=new AddressMsgHandlerImpl();
	
	private static final String TOPIC="topic1";
	
	private boolean bootstrapped=false;
	
	
	/**
	 * initiate member variables
	 * 
	 * @param serverPort  user input
	 * @param msgHandler
	 */
	public ForkNode(int serverPort,GameMsgHandler msgHandler) {
		
    	this.thisAddress=new JmsServerAddress("127.0.0.1", serverPort);
		this.msgHandler=msgHandler;
		
		this.senders=new HashMap<Long,JmsSender>();
        this.receiver=new JmsReceiver(thisAddress, TOPIC, msgHandler, addressHandler);
        new Thread(receiver).start();
        
        //System.out.println("start listening to server");
       
        //System.out.println("node inited");
		 
		//System.out.println("bootstrpping");
		bootstrap();
		//System.out.println("bootstrapped");
		System.out.println("successfully connected to the player network");
	}
	
	 public void broadcastGameMsg(GameMessage msg) {
		 msg.setFromAddresss(thisAddress);
	    	for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().sendMsg(msg);
	    	}
	 }
	 
	 
	 public void broadcastAddress(JmsServerAddress msg) {
		 for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().sendMsg(msg);
	    	}
	 }
	 
	 public void stop(GameMessage quitMsg) {
	    	quitMsg.setFromAddresss(thisAddress);
	    	broadcastGameMsg(quitMsg);
	    	for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().close();
	    	}
	    	this.receiver.close();
	 }
	 
	 private void bootstrap() {
			if(this.bootstrapped) {
				return;
			}
			initNewForkBranch(bootstrapAddress);
			
	}
	 
	 private void initNewForkBranch(JmsServerAddress serverAddress)  {

	        	JmsSender newSender=new JmsSender(serverAddress,TOPIC);
				//System.out.println("new branch created, connect to "+ serverAddress.port);
				newSender.sendMsg(thisAddress);
				//System.out.println("sent local server address to new peer's server");
				this.senders.put(serverAddress.id, newSender);
			
	 }
	 
	 public void removeSender(long id) {
		 if(senders.get((Long)id)==null){
			 return;
		 }else {
			 senders.remove((Long)id);
		 }
	 }
	 /**
	  * The implementation of Address message handling
	  * @author Liming Liu
	  *
	  */
	 private class AddressMsgHandlerImpl implements AddressMsgHandler{
		 /**
		  * when receive a new address, check if has the sender to the address already, if not start a new branch to the new address
		  */
		@Override
		public void handleAddress(JmsServerAddress newAddress) {
			
			boolean flag=false;
			
			for(Entry<Long,JmsSender> entry:senders.entrySet()) {
				JmsServerAddress destAddress=entry.getValue().getDestServer();
				if(newAddress.ip.equals(destAddress.ip)&&newAddress.port==destAddress.port) {
					flag=true;
				}
			}
			if(flag==false) {
				initNewForkBranch(newAddress);
				broadcastAddress(newAddress);
			}
		}
	 }

}
