package net.common;

import java.io.Serializable;

public class InetId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public String ip;
	public int port;
	public InetId(String ip , int port) {
		this.ip=ip;
		this.port=port;
		this.id=System.currentTimeMillis();
	}
	@Override
	public String toString() {
		return "ServerAddress [ip=" + ip + ", port=" + port + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
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
		InetId other = (InetId) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
	
}