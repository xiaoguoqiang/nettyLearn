package examination.netty.httpAndXml;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T>{
	
	private IBindingFactory factory = null;
	private StringReader reader = null;
	private Class<?> clazz = null;
	private boolean isPrint;
	
	private final static String CHARSET_NAME="UTF-8";
	private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);
	
	
	protected AbstractHttpXmlDecoder (Class<?> clazz) {
		this(clazz,false);
	}
	
	protected AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
		this.clazz = clazz;
		this.isPrint = isPrint;
	}
	
	protected Object decoder0(ChannelHandlerContext ctx,ByteBuf buf) throws JiBXException {
		factory = BindingDirectory.getFactory(clazz);
		String content = buf.toString(UTF_8);
		if(isPrint) {
			System.out.println(content);
		}
		
		reader = new StringReader(content);
		IUnmarshallingContext unmarshCtx = factory.createUnmarshallingContext();
		Object object = unmarshCtx.unmarshalDocument(reader);
		reader.close();
		reader = null;
		return object;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		if(reader != null) {
			reader.close();
			reader = null;
		}
	}
	
	
	
	

}
