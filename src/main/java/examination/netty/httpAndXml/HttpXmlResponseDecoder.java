package examination.netty.httpAndXml;

import java.util.List;

import org.jibx.runtime.JiBXException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse>{
	
	protected HttpXmlResponseDecoder (Class<?> clazz) {
		this(clazz,false);
	}
	
	protected HttpXmlResponseDecoder(Class<?> clazz,boolean isPrint) {
		super(clazz,isPrint);
	}
	
	protected void decode(ChannelHandlerContext ctx,DefaultFullHttpResponse msg, List<Object> out) throws JiBXException {
		HttpXmlResponse response = new HttpXmlResponse(msg,decoder0(ctx,msg.content()));
		out.add(response);
	}
	
	
}
