package unitTest;

import net.common.AddressMsgHandler;
import net.common.InetId;

public class TestAddressHandler implements AddressMsgHandler {
	private InetId rcvdAddress;
	

	@Override
	public void handleAddress(InetId newAddress) {
		// TODO Auto-generated method stub
		System.out.println(newAddress.toString());
		this.rcvdAddress=newAddress;
	}


	public InetId getRcvdAddress() {
		return rcvdAddress;
	}


	public void setRcvdAddress(InetId rcvdAddress) {
		this.rcvdAddress = rcvdAddress;
	}
	
	

}
