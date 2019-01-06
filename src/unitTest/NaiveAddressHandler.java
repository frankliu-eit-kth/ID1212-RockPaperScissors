package unitTest;

import net.adaptor.AddressMsgHandler;
import net.model.JmsServerAddress;
/**
 * 
 * @author Liming Liu
 *
 */
public class NaiveAddressHandler implements AddressMsgHandler {
	private JmsServerAddress rcvdAddress;
	

	@Override
	public void handleAddress(JmsServerAddress newAddress) {
		// TODO Auto-generated method stub
		System.out.println(newAddress.toString());
		this.rcvdAddress=newAddress;
	}


	public JmsServerAddress getRcvdAddress() {
		return rcvdAddress;
	}


	public void setRcvdAddress(JmsServerAddress rcvdAddress) {
		this.rcvdAddress = rcvdAddress;
	}
	
	

}
