package examination.netty.customprotocol.marshalling;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import examination.common.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;

public class MarshallingEncoder {
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	MarshallerProvider marshallerProvider;
//	Marshaller marshaller;
	
	public MarshallingEncoder() throws IOException {
		marshallerProvider = MarshallingCodeCFactory.buildMarshallerProvider();
//		marshaller = MarshallingCodeCFactory.buildMarshalling();
	}
	
	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws Exception {
		try {
			Marshaller marshaller = marshallerProvider.getMarshaller(ctx);
			int lengthPos = buf.writerIndex();
			buf.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(buf);
			marshaller.start(output);
			marshaller.writeObject(msg);
			marshaller.finish();
			buf.setIndex(lengthPos, buf.writerIndex() - lengthPos -4);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

}
