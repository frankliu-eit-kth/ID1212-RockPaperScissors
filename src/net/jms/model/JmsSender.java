package net.jms.model;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.common.InetId;

public class JmsSender {
	private Context context;
    private Connection connection;
    private MessageProducer sender;
    
    private String topic;
    private InetId destServer;
    private Session session;
    
	public InetId getDestServer() {
		return destServer;
	}
	public void setDestServer(InetId destServer) {
		this.destServer = destServer;
	}
	public JmsSender(InetId serverAddress,String topic) {
		// TODO Auto-generated constructor stub
		this.destServer=serverAddress;
		this.topic=topic;
		String ip=serverAddress.ip;
		int port=serverAddress.port;
		
	    Hashtable<String, String> properties = new Hashtable();
	    properties.put(Context.INITIAL_CONTEXT_FACTORY, 
	                   "org.exolab.jms.jndi.InitialContextFactory");
	    String addressString= "tcp://"+ip+":"+port+"/";
	    properties.put(Context.PROVIDER_URL,addressString);
	    System.out.println("test: property"+addressString);
	    try {
			this.context = new InitialContext(properties);
			
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			this.connection = factory.createConnection();
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
			Destination destination = (Destination) context.lookup(topic);
			this.sender = session.createProducer(destination);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.close();
		}
	}
	public void close() {
		try {
			if (sender != null) sender.close();
			if (connection != null) connection.close();
			if (context != null) context.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendMsg(Serializable msg) {
		try {
			if(this.sender==null) {
				return;
			}
			Message newMsg=this.session.createObjectMessage(msg);
			newMsg.setJMSType(msg.getClass().getName());
			this.sender.send(newMsg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InetId server=new InetId("localhost",3035);
		JmsSender sender1=new JmsSender(server, "topic1");
		sender1.sendMsg("123");
	}

}
