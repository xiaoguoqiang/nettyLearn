package examination.netty.tcp.tearSticky;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	
	private byte[] bytes;

	private int counter = 0;

	public TimeClientHandler() {
		bytes = "QUERY TIME ORDER".getBytes();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ByteBuf buf = null;
		for (int i = 0; i < 100; i++) {
			buf = Unpooled.buffer(bytes.length);
			buf.writeBytes(bytes);
			ctx.writeAndFlush(buf);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String time = new String(bytes, "UTF-8");
		System.out.println("Now is : " + time + " and The counter is : " + ++counter);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warning("Exception is " + cause.getMessage());
		ctx.close();
	}

}
