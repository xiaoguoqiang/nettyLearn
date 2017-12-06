package examination.netty.customprotocol.marshalling;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import examination.common.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;

public class MarshallingEncoder {
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	Marshaller marshaller;
	
	public MarshallingEncoder() throws IOException {
		marshaller = MarshallingCodeCFactory.buildMarshaller();
	}
	
	public void encode(Object msg, ByteBuf buf) throws IOException {
		try {
			int lengthPos = buf.writerIndex();
			buf.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(buf);
			marshaller.start(output);
			marshaller.writeObject(msg);
			marshaller.finish();
			buf.setIndex(lengthPos, buf.writerIndex() - lengthPos -4);
			
		}finally {
			marshaller.close();
		}
		
	}
	
	
	
	

}
