package unitTest;

import net.adaptor.GameMsgHandler;
import net.model.GameMessage;
/**
 * 
 * @author Liming Liu
 *
 */
public class NaiveMsgHandler implements GameMsgHandler {
	private GameMessage rcvdMsg;
	

	@Override
	public void handleMsg(GameMessage msg) {
		// TODO Auto-generated method stub
		System.out.println(msg.toString());
		this.rcvdMsg=msg;
	}


	public GameMessage getRcvdMsg() {
		return rcvdMsg;
	}


	public void setRcvdMsg(GameMessage rcvdMsg) {
		this.rcvdMsg = rcvdMsg;
	}
	
	

}
