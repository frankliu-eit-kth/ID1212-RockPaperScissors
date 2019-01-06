package net.model;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.adaptor.AddressMsgHandler;
import net.adaptor.GameMsgHandler;
/**
 * publish-subscribe model receiver
 * 
 * use a jms MessageConsumer instance to listen to the message from jms server
 * 
 * each receiver only subscribes to 1 topic from 1 jms server.
 * 
 * sender publish the message of same topic into a message queue
 * 
 * @author Liming Liu
 *
 */
public class JmsReceiver implements Runnable {
	//configuration
	private Context context;
    private Connection connection;
    private Session session;
    private String topic;
    
    //receiver
    private MessageConsumer receiver;
    
   //message handlers
    private GameMsgHandler msgHandler;
    private AddressMsgHandler addressHandler;
    
    //keep thread alive
    private volatile boolean running;
    
  
    //listening, when receiving message, first check the class type, then pass the message to corresponding handlers
    @Override
    public void run(){
    	
    	if(this.receiver==null||this.msgHandler==null||this.addressHandler==null) {
    		System.out.println("system error");
    		this.close();
    		return;
    	}
    	
    	while(running) {
    		try {
    			
				Message msg=receiver.receive();
				String msgClassName=null;
				Object obj;
				
				try {
					msgClassName = msg.getJMSType();
					ObjectMessage objectmessage = (ObjectMessage)msg;
					obj = objectmessage.getObject();
					
					if(msgClassName.equals(JmsServerAddress.class.getName())) {
						this.addressHandler.handleAddress((JmsServerAddress)obj);
						
					}else {
						if(msgClassName.equals(GameMessage.class.getName())) {
							this.msgHandler.handleMsg((GameMessage)obj);
						}
					}
				} catch (Exception e) {
					
					close();
				}
				
				
			} catch (JMSException e) {
				e.printStackTrace();
				this.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		
    	}
    	
    }
    /**
     * create new jms receiver(subscriber)
     * @param serverAddress the serverAddress the receiver listens to
     * @param topic
     * @param msgHandler
     * @param addressHandler
     */
	public JmsReceiver(JmsServerAddress serverAddress,String topic,GameMsgHandler msgHandler,AddressMsgHandler addressHandler) {
		
		this.running=true;
		this.msgHandler=msgHandler;
		this.addressHandler=addressHandler;
		this.topic=topic;
		
		String ip=serverAddress.ip;
		int port=serverAddress.port;
		
		//context parameters
	    Hashtable<String, String> properties = new Hashtable();
	    properties.put(Context.INITIAL_CONTEXT_FACTORY, 
	                   "org.exolab.jms.jndi.InitialContextFactory");
	    String addressString= "tcp://"+ip+":"+port+"/";
	    properties.put(Context.PROVIDER_URL,addressString);
	    //System.out.println("test: property"+addressString);
	    
	    try {
			this.context = new InitialContext(properties);
			
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			this.connection = factory.createConnection();
			connection.start();
			
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
			
			Destination destination = (Destination) context.lookup(topic);
			
			this.receiver = session.createConsumer(destination);
			
		} catch (NamingException e) {
			
			System.out.println("node intialization error, please check the input");
			e.printStackTrace();
			this.close();
		} catch (JMSException e) {
			
			System.out.println("node intialization error, please check the input");
			e.printStackTrace();
			this.close();
		}
	}
	/**
	 * close all variables
	 * stop thread
	 * 
	 */
	public void close() {
		
		this.running = false;
		if (context != null) {
            try {
                context.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }

        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JmsServerAddress server=new JmsServerAddress("localhost",3035);
		JmsReceiver receiver=new JmsReceiver(server, "topic1", null,null);
		new Thread(receiver).start();
		receiver.close();
	}
	
	

}
