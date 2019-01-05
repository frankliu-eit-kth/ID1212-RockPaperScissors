package unitTest;

import net.common.GameMsgHandler;
import net.common.NetMessage;

public class TestMsgHandler implements GameMsgHandler {
	private NetMessage rcvdMsg;
	

	@Override
	public void handleMsg(NetMessage msg) {
		// TODO Auto-generated method stub
		System.out.println(msg.toString());
		this.rcvdMsg=msg;
	}


	public NetMessage getRcvdMsg() {
		return rcvdMsg;
	}


	public void setRcvdMsg(NetMessage rcvdMsg) {
		this.rcvdMsg = rcvdMsg;
	}
	
	

}
