package common;

import java.util.Arrays;

public class Message {
	private final String MSG_DELIMETER="##";
	private String msg;
	private String[] parts;
	private MsgType msgType;
	
	public Message(String msg) throws Exception{
		// TODO Auto-generated constructor stub
		this.msg=msg;
		String[] componentList=msg.split(MSG_DELIMETER);
		if(componentList.length==0) {
			throw new Exception("wrong message format :" +msg);
		}
		this.parts=componentList;
		String header=parts[0].toLowerCase();
		if(header.equals("start")||header.equals("quit")||header.equals("peer_ip")) {
			this.msgType=MsgType.CONTROL;
		}else {
			if(header.equals("username")||header.equals("ready")||header.equals("reset")||header.equals("join")||header.equals("username")) {
				this.msgType=MsgType.PLAYER_STATUS;
			}else {
				if(header.equals("rock")||header.equals("paper")||header.equals("scissors")) {
					this.msgType=MsgType.GESTURE;
				}else {
					throw new Exception("unrecognized message type :"+ header);
				}
			}
		}
	}

	
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String[] getParts() {
		return parts;
	}


	public void setParts(String[] parts) {
		this.parts = parts;
	}


	public MsgType getMsgType() {
		return msgType;
	}


	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}


	@Override
	public String toString() {
		return "Message [MSG_DELIMETER=" + MSG_DELIMETER + ", msg=" + msg + ", parts=" + Arrays.toString(parts)
				+ ", msgType=" + msgType + "]";
	}

}
