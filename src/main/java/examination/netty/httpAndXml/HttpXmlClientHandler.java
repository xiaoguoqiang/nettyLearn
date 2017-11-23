package examination.netty.httpAndXml;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(Long.valueOf("123")));
		ctx.writeAndFlush(request);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
		System.out.println("The client recevie response header is " + msg.getHttpResponse().headers().names());
		System.out.println("The client receive response body is " + msg.getBody());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
