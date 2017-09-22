package examination.netty.tcp.FixedLengthFrameDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private int counter = 0;

	private static final String code = "Hi,Li qiang . Wecome to Netty";

	public EchoClientHandler() {

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(code.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("Client recevie " + ++counter + "times message " + msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable able) {
		able.printStackTrace();
		ctx.close();
	}
}
