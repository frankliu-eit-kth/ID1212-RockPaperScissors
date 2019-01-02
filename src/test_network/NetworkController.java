package test_network;

public class NetworkController {
	ForkNode thisNode;
	
	
	public void startNewNode(int portNo) {
		ForkNode node=new ForkNode(portNo);
		thisNode=node;
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
