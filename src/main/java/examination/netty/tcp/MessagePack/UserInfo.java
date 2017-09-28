package examination.netty.tcp.MessagePack;

import org.msgpack.annotation.Message;

/**
 * 注解一定要加，这个被坑了很久
 * **/

@Message
public class UserInfo {

	private String name;

	private int edge;

	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEdge() {
		return edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "UserInfo [name=" + name + ", edge=" + edge + ", address=" + address + "]";
	}

}
