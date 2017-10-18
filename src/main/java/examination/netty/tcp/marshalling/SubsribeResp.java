package examination.netty.tcp.marshalling;

import java.io.Serializable;

public class SubsribeResp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6796248017472948339L;

	private int respId;
	
	private String message;

	public int getRespId() {
		return respId;
	}

	public void setRespId(int respId) {
		this.respId = respId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SubsribeResp [respId=" + respId + ", message=" + message + "]";
	}
	
}
