package examination.netty.tcp.marshalling;

import java.io.Serializable;

public class SubscribeReq implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145934915942149667L;

	private int requId;
	
	private String userName;
	
	private String productName;
	
	private String address;

	public int getRequId() {
		return requId;
	}

	public void setRequId(int requId) {
		this.requId = requId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "SubscribeReq [requId=" + requId + ", userName=" + userName + ", productName=" + productName
				+ ", address=" + address + "]";
	}
	
	

}
