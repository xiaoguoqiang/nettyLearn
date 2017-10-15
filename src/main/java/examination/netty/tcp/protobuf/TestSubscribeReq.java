package examination.netty.tcp.protobuf;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestSubscribeReq {
	private static byte[] encode(SubscribeProto.SubscribeReq req) {
		return req.toByteArray();
	}
	
	private static SubscribeProto.SubscribeReq decode (byte[] body) throws InvalidProtocolBufferException{
		return SubscribeProto.SubscribeReq.parseFrom(body);
	}
	
	private static SubscribeProto.SubscribeReq createSubscribeProto(){
		SubscribeProto.SubscribeReq.Builder builder = SubscribeProto.SubscribeReq.newBuilder();
		builder.setSubReqId(1);
		builder.setUserName("liqiang");
		builder.setProductName("Netty book");
		builder.setAddress("beijing");
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeProto.SubscribeReq req = createSubscribeProto();
		System.out.println("befor code req is " + req.toString());
		SubscribeProto.SubscribeReq req2 = decode(encode(req));
		System.out.println("after code req is " + req2.toString());
		System.out.println("alert ->" + req2.equals(req) );
	}

}
