package examination.netty.general;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	
	private final ByteBuf firstMessage;
	
	public TimeClientHandler() {
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(bytes.length);
		firstMessage.writeBytes(bytes);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(firstMessage);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		ByteBuf buf = (ByteBuf)msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String time = new String(bytes,"UTF-8");
		//打印时间
		System.out.println("Now is : " + time);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		logger.warning("Exception is " + cause.getMessage());
		ctx.close();
	}
	
	
}
