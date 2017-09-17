package examination.netty.tcp.lineBasedFrameAndStringDecoder;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	
	private int counter = 0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		String recevieMsg = (String)msg;
		System.out.println("The time server receive order is : " + recevieMsg + "; the recevie counter is :" + ++counter);
		String currentTime = recevieMsg.equals("QUERY TIME ORDER")
				? new java.util.Date(System.currentTimeMillis()).toString()
				: "BAD ORDER";
				currentTime += "\n";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable etx) {
		etx.printStackTrace();
		ctx.close();
	}

}
