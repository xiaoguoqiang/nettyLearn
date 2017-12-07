package examination.netty.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		NettyMessage message = (NettyMessage) msg;

		if (message.getHeader() != null && message.getHeader().getType() == MessageType.PING.getValue()) {
			System.out.println("server receive heart beat request --->" + message);
			NettyMessage response = buildHeartBeatResp();
			System.out.println("send heart beat response -->" + response);
			ctx.writeAndFlush(response);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildHeartBeatResp() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.PONG.getValue());
		message.setHeader(header);
		return message;
	}

}
