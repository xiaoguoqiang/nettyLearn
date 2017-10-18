package examination.netty.tcp.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter {
	
	public SubReqClientHandler() {
		
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for(int i=0 ; i <1 ; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}
	
	private SubscribeReq subReq(int i){
		SubscribeReq req = new SubscribeReq();
		req.setRequId(i);
		req.setProductName("liqiang");
		req.setUserName("nothing");
		req.setAddress("god");
		return req;
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
