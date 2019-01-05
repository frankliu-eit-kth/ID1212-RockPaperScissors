package net.jms.model;

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

import net.common.AddressMsgHandler;
import net.common.GameMsgHandler;
import net.common.InetId;
import net.common.NetMessage;

public class JmsReceiver implements Runnable {
	
	private Context context;
    private Connection connection;
    private MessageConsumer receiver;
    
    private String topic;
    private InetId destServer;
    private Session session;
    private GameMsgHandler msgHandler;
    private AddressMsgHandler addressHandler;
    private volatile boolean running;
    
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
				//System.out.println(msg.toString());
				String msgClassName=null;
			
				Object obj;
				try {
					msgClassName = msg.getJMSType();
					
					//System.out.println(msgClassName);
					ObjectMessage objectmessage = (ObjectMessage)msg;
					obj = objectmessage.getObject();
					if(msgClassName.equals(InetId.class.getName())) {
						this.addressHandler.handleAddress((InetId)obj);
						
					}else {
						if(msgClassName.equals(NetMessage.class.getName())) {
							this.msgHandler.handleMsg((NetMessage)obj);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					close();
				}
				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		
    	}
    	
    }
    
	public JmsReceiver(InetId serverAddress,String topic,GameMsgHandler msgHandler,AddressMsgHandler addressHandler) {
		this.running=true;
		this.msgHandler=msgHandler;
		this.addressHandler=addressHandler;
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
			this.receiver = session.createConsumer(destination);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("node intialization error, please check the input");
			e.printStackTrace();
			this.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			System.out.println("node intialization error, please check the input");
			e.printStackTrace();
			this.close();
		}
	}
	
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
		InetId server=new InetId("localhost",3035);
		JmsReceiver receiver=new JmsReceiver(server, "topic1", null,null);
		new Thread(receiver).start();
		receiver.close();
	}
	
	

}
