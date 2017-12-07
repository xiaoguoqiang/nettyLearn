package examination.netty.customprotocol;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();

	private String[] whiteList = { "127.0.0.1", "192.168.90.68" };

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.getValue()) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			if (nodeCheck.containsKey(nodeIndex)) {
				loginResp = buildResponse((byte) -1);
			}

			InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

			String ip = address.getAddress().getHostAddress();

			boolean isOk = false;

			for (String WIP : whiteList) {
				if (WIP.equals(ip)) {
					isOk = true;
					break;
				}
			}

			loginResp = isOk ? buildResponse((byte) 0) : buildResponse((byte) -1);

			if (isOk) {
				nodeCheck.put(nodeIndex, true);
			}

			System.out.println("The login response is " + loginResp + ";the message body is " + loginResp.getBody());

		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RSP.getValue());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

}
