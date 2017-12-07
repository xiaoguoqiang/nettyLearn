package examination.netty.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(buildLoginReq());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) {
		NettyMessage message = (NettyMessage)msg;
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RSP.getValue()) {
			byte logResult = (byte)message.getBody();
			if(logResult != (byte)0) {
				ctx.close();
			}else {
				System.out.println("Login is ok, " + message);
				ctx.fireChannelRead(msg);
			}
		}else {
			ctx.fireChannelRead(msg);
		}
	}
	
	protected NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.getValue());
		message.setHeader(header);
		return message;
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}
	
	
	
	

}
