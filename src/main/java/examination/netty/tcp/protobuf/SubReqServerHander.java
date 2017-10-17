package examination.netty.tcp.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqServerHander extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) {
		SubscribeProto.SubscribeReq req = (SubscribeProto.SubscribeReq)msg;
		if(req.getProductName().equalsIgnoreCase("liqiang")) {
			System.out.println("The server receiver req is " + req.toString());
			ctx.writeAndFlush(sendResponse(req.getSubReqId()));
		}
	}
	
	private SubscribeRespProto.SubscribeResp sendResponse(int id) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setRespCode(0);
		builder.setSubReqId(id);
		builder.setDesc("Netty ProtoBuf Response");
		return builder.build();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
