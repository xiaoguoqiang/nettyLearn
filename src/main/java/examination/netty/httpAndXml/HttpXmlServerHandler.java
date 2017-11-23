package examination.netty.httpAndXml;

import java.util.ArrayList;
import java.util.List;

import examination.netty.httpAndXml.pojo.Address;
import examination.netty.httpAndXml.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
		FullHttpRequest request = msg.getRequest();
		Order order = (Order) msg.getBody();
		System.out.println("the server client receive request is: " + order);
		doBusiness(order);
		HttpXmlResponse response = new HttpXmlResponse(null, order);
		ChannelFuture cf = ctx.writeAndFlush(response);

		if (!HttpUtil.isKeepAlive(request)) {
			cf.addListener(new GenericFutureListener<Future<? super Void>>() {

				@Override
				public void operationComplete(Future<? super Void> future) throws Exception {
					ctx.close();
				}

			});
		}

	}

	private void doBusiness(Order order) {
		order.getCustomer().setFirstName("di");
		order.getCustomer().setLastName("renjie");
		List<String> midName = new ArrayList<String>(1);
		midName.add("liyuanfang");
		order.getCustomer().setMidleNames(midName);
		Address address = order.getBillTo();
		address.setCity("luoyang");
		address.setCountry("zhongguo");
		address.setState("henandao");
		address.setPostCode("123456");
		order.setBillTo(address);
		order.setShipTo(address);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("shibai : " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
