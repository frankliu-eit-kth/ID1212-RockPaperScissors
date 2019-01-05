package net.common;

import java.io.Serializable;
import java.util.ArrayList;

public class NetMessage implements Serializable{
	//private final String MSG_DELIMETER="##";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String header;
	private ArrayList<String> body;
	private InetId fromAddresss;
	
	public NetMessage(String header, ArrayList<String> body, InetId fromAddress) {
		this.header=header;
		this.body=body;
		this.fromAddresss=fromAddress;
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
	

	public InetId getFromAddresss() {
		return fromAddresss;
	}

	public void setFromAddresss(InetId fromAddresss) {
		this.fromAddresss = fromAddresss;
	}

	@Override
	public String toString() {
		return "Message [header=" + header + ", body=" + body + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((fromAddresss == null) ? 0 : fromAddresss.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetMessage other = (NetMessage) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (fromAddresss == null) {
			if (other.fromAddresss != null)
				return false;
		} else if (!fromAddresss.equals(other.fromAddresss))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}
	
	
	
	
	

}
