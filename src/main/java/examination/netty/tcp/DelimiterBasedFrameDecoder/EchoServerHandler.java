package examination.netty.tcp.DelimiterBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	private int counter = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg ) {
		String body = (String)msg;
		System.out.println("Recevie " + ++counter + "time message " + body);
		ByteBuf buffer = Unpooled.copiedBuffer((body + "$_").getBytes());
		ctx.writeAndFlush(buffer);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable able) {
		able.printStackTrace();
		ctx.close();
	}
	

}
 