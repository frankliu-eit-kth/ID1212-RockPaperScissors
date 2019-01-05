package net.jms.model;

import java.util.HashMap;
import java.util.Map.Entry;

import net.common.AddressMsgHandler;
import net.common.GameMsgHandler;
import net.common.InetId;
import net.common.NetMessage;

public class ForkNode {
	private JmsReceiver receiver=null;;
	private HashMap<Long,JmsSender> senders=null;;
	private InetId thisAddress=null;;
	private final InetId bootstrapAddress=new InetId("127.0.0.1",3035);
	private GameMsgHandler msgHandler=null;;
	private AddressHandler addressHandler=new AddressHandler();
	
	private static final String TOPIC="topic1";
	
	private boolean bootstrapped=false;
	private boolean running=false;
   
	public ForkNode(int serverPort,GameMsgHandler msgHandler) {
		
    	this.thisAddress=new InetId("127.0.0.1", serverPort);
		this.msgHandler=msgHandler;
		this.senders=new HashMap<Long,JmsSender>();
		
		
        this.receiver=new JmsReceiver(thisAddress, TOPIC, msgHandler, addressHandler);
        new Thread(receiver).start();
        System.out.println("start listening to server");
        //initNewForkBranch(thisAddress);
        System.out.println("node inited");
		 
		System.out.println("bootstrpping");
		bootstrap();
	}
	
	 public void broadcastMsg(NetMessage msg) {
		 msg.setFromAddresss(thisAddress);
	    	for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().sendMsg(msg);
	    	}
	 }
	 public void broadcastAddress(InetId msg) {
		 for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().sendMsg(msg);
	    	}
	 }
	 
	 public void stop(NetMessage quitMsg) {
	    	this.running=false;
	    	broadcastMsg(quitMsg);
	    	for(Entry<Long,JmsSender> sender:senders.entrySet()) {
				sender.getValue().close();
	    	}
	 }
	 private void bootstrap() {
			if(this.bootstrapped) {
				return;
			}
			
			initNewForkBranch(bootstrapAddress);
			System.out.println("bootstrapped");
	}
	 private void initNewForkBranch(InetId serverAddress)  {

	        	JmsSender newSender=new JmsSender(serverAddress,TOPIC);
				System.out.println("new branch created, connect to "+ serverAddress.port);
				newSender.sendMsg(thisAddress);
				System.out.println("sent local server address to new peer's server");
				this.senders.put(serverAddress.id, newSender);
			
	 }
	 
	 private class AddressHandler implements AddressMsgHandler{
		 
		@Override
		public void handleAddress(InetId newAddress) {
			// TODO Auto-generated method stub
			boolean flag=false;
			for(Entry<Long,JmsSender> entry:senders.entrySet()) {
				InetId destAddress=entry.getValue().getDestServer();
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
