package net.model;

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
/**
 * jms publisher
 * 
 * publishes message to a topic to a certain jms server
 * 
 * MessageProducer: publish-subscribe model publisher
 * 
 * @author Liming Liu
 *
 */
public class JmsSender {
	
	private Context context;
    private Connection connection;
    private JmsServerAddress destServer;
    private Session session;
    
    private String topic;
    
    private MessageProducer sender;
    
	public JmsServerAddress getDestServer() {
		return destServer;
	}
	
	public void setDestServer(JmsServerAddress destServer) {
		this.destServer = destServer;
	}
	
	public JmsSender(JmsServerAddress serverAddress,String topic) {
		
		this.destServer=serverAddress;
		this.topic=topic;
		
		String ip=serverAddress.ip;
		int port=serverAddress.port;
		
		//context parameters setup
	    Hashtable<String, String> properties = new Hashtable();
	    properties.put(Context.INITIAL_CONTEXT_FACTORY, 
	                   "org.exolab.jms.jndi.InitialContextFactory");
	    String addressString= "tcp://"+ip+":"+port+"/";
	    properties.put(Context.PROVIDER_URL,addressString);
	    
	    //create sender
	    try {
			this.context = new InitialContext(properties);
			
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			this.connection = factory.createConnection();
			connection.start();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
			Destination destination = (Destination) context.lookup(topic);
			this.sender = session.createProducer(destination);
		} catch (NamingException e) {
			System.out.println("cannot connect to new node");
			e.printStackTrace();
			this.close();
		} catch (JMSException e) {
			System.out.println("cannot connect to new node");
			e.printStackTrace();
			this.close();
		}
	}
	
	/**
	 * close all variables
	 */
	public void close() {
		try {
			if (sender != null) sender.close();
			if (connection != null) connection.close();
			if (context != null) context.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * send message
	 * @param msg
	 */
	public void sendMsg(Serializable msg) {
		try {
			if(this.sender==null) {
				return;
			}
			Message newMsg=this.session.createObjectMessage(msg);
			newMsg.setJMSType(msg.getClass().getName());
			this.sender.send(newMsg);
		} catch (JMSException e) {
			System.out.println("error sending message");
			e.printStackTrace();
			this.close();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JmsServerAddress server=new JmsServerAddress("localhost",3035);
		JmsSender sender1=new JmsSender(server, "topic1");
		sender1.sendMsg("123");
	}

}
