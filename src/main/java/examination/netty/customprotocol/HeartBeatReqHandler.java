package examination.netty.customprotocol;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		NettyMessage message = (NettyMessage) msg;

		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RSP.getValue()) {

			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000,
					TimeUnit.MILLISECONDS);

		} else if (message.getHeader() != null && message.getHeader().getType() == MessageType.PONG.getValue()) {

			System.out.println("Client receive server heart beat message:--->" + message);

		} else {
			ctx.fireChannelRead(msg);
		}

	}

	private class HeartBeatTask implements Runnable {

		private final ChannelHandlerContext ctx;

		private HeartBeatTask(final ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void run() {
			NettyMessage message = buildHeartBeatReq();
			System.out.println("client sent heart beat message to server --->" + message);
			ctx.writeAndFlush(message);
		}

		private NettyMessage buildHeartBeatReq() {
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.PING.getValue());
			message.setHeader(header);
			return message;
		}

	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if(heartBeat !=null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		
		ctx.fireExceptionCaught(cause);
	}
	
	

}
