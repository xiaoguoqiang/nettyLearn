package examination.netty.tcp.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter {
	
	public SubReqClientHandler() {
		
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for(int i=0 ; i <= 10 ; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}
	
	private SubscribeProto.SubscribeReq subReq(int i){
		SubscribeProto.SubscribeReq.Builder builder = SubscribeProto.SubscribeReq.newBuilder();
		builder.setSubReqId(i);
		builder.setProductName("liqiang");
		builder.setUserName("nothing");
		builder.setAddress("god");
		return builder.build();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.print("Client Receive message is " + msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
