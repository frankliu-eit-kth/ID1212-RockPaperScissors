package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import net.model.JmsReceiver;
import net.model.JmsSender;
import net.model.JmsServerAddress;
import net.model.GameMessage;
/**
 * 
 * @author Liming Liu
 *
 */
public class AddressTest {

	@Test
	public void test() {
		JmsServerAddress serverAddress=new JmsServerAddress("127.0.0.1",3035);
		GameMessage msg=new GameMessage("start",null,serverAddress);
		
		String topic="topic1";
		NaiveMsgHandler testMsgHandler=new NaiveMsgHandler();
		NaiveAddressHandler testAddressHandler=new NaiveAddressHandler();
		
		JmsSender sender=new JmsSender(serverAddress,topic);
		JmsReceiver receiver=new JmsReceiver(serverAddress, topic, testMsgHandler, testAddressHandler);
		Thread listener= new Thread(receiver);
		listener.start();
		
	
		
		sender.sendMsg(serverAddress);
		while(testAddressHandler.getRcvdAddress()==null) {}
		assertEquals(serverAddress, testAddressHandler.getRcvdAddress());
		
		sender.close();
		receiver.close();
	}

}
