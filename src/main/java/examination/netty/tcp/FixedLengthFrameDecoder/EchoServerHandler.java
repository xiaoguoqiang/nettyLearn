package examination.netty.tcp.FixedLengthFrameDecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	private int counter = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg ) {
		String body = (String)msg;
		System.out.println("Recevie " + ++counter + "time message " + body);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable able) {
		able.printStackTrace();
		ctx.close();
	}
	

}
 