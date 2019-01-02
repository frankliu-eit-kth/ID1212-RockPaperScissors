package net.model;

import java.util.ArrayList;

public class Message {
	//private final String MSG_DELIMETER="##";
	private String header;
	private ArrayList<String> body;
	
	public Message(String header, ArrayList<String> body) {
		this.header=header;
		this.body=body;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ArrayList<String> getBody() {
		return body;
	}

	public void setBody(ArrayList<String> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [header=" + header + ", body=" + body + "]";
	}
	
	
	
	
	

}
