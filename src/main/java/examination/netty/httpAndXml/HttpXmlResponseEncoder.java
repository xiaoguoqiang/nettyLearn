package examination.netty.httpAndXml;

import java.io.IOException;
import java.util.List;

import org.jibx.runtime.JiBXException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws JiBXException, IOException {
		ByteBuf body = encode0(ctx, msg.getBody());
		FullHttpResponse response = msg.getHttpResponse();
		if (response == null) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
		} else {
			//没有提供动态设置content的接口，因此需要重新构建
			response = new DefaultFullHttpResponse(msg.getHttpResponse().protocolVersion(),
					msg.getHttpResponse().status(), body);

		}
		
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html");
		HttpUtil.setContentLength(response, body.readableBytes());
		out.add(response);
	}

}
