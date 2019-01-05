package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import net.common.InetId;
import net.common.NetMessage;
import net.jms.model.JmsReceiver;
import net.jms.model.JmsSender;

public class AddressTest {

	@Test
	public void test() {
		InetId serverAddress=new InetId("127.0.0.1",3035);
		NetMessage msg=new NetMessage("start",null,serverAddress);
		
		String topic="topic1";
		TestMsgHandler testMsgHandler=new TestMsgHandler();
		TestAddressHandler testAddressHandler=new TestAddressHandler();
		
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
