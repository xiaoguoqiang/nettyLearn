package examination.netty.tcp.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqServerHander extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) {
		SubscribeReq req = (SubscribeReq)msg;
		if(req.getProductName().equalsIgnoreCase("liqiang")) {
			System.out.println("The server receiver req is " + req.toString());
			ctx.writeAndFlush(sendResponse(req.getRequId()));
		}
	}
	
	private SubsribeResp sendResponse(int id) {
		SubsribeResp resp = new SubsribeResp();
		resp.setRespId(id);
		resp.setMessage("Nettt Marshalling Response");
		return resp;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
