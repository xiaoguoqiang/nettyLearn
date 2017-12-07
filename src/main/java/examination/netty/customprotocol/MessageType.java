package examination.netty.customprotocol;

public enum MessageType {
	
	BUSINESS_REQ("business request",(byte)0),
	BUSINESS_RSP("business response",(byte)1),
	ONE_WAY("one way",(byte)2),
	LOGIN_REQ("login request",(byte)3),
	LOGIN_RSP("login response",(byte)4),
	PING("ping",(byte)5),
	PONG("pong",(byte)6);
	
	private String name;
	
	private byte value;
	
	private MessageType(String name, byte value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
}
