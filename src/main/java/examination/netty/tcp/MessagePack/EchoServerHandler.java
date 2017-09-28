package examination.netty.tcp.MessagePack;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	private int counter = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg ) {
		@SuppressWarnings("unchecked")
		List<UserInfo> body = (List<UserInfo>)msg;
		System.out.println("Recevie " + ++counter + "time message " + body);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable able) {
		able.printStackTrace();
		ctx.close();
	}
	

}
 