package examination.netty.httpAndXml;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
	
	IBindingFactory factory = null;
	StringWriter writer = null;
	final static String CHARSET_NAME="UTF-8";
	final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
	
	protected ByteBuf encode0(ChannelHandlerContext ctx,Object body) throws JiBXException, IOException {
		
		factory = BindingDirectory.getFactory(body.getClass());
		writer = new StringWriter();
		IMarshallingContext marshallingCtx = factory.createMarshallingContext();
		marshallingCtx.setIndent(2);
		marshallingCtx.marshalDocument(body, CHARSET_NAME, null, writer);
		String xmlStr = writer.toString();
		writer.close();
		writer = null;
		ByteBuf encodeBuf = Unpooled.copiedBuffer(xmlStr,UTF_8);
		return encodeBuf;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws IOException {
		if(writer != null) {
			writer.close();
			writer = null;
		}
	}
	

}
