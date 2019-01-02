package net.model;

public interface MsgHandler {
	public void handleMsg(Message msg, InetId netId);
}
