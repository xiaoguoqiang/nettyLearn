package examination.netty.customprotocol.marshalling;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import examination.common.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;

public class MarshallingDecoder {
	private final Unmarshaller unmarshaller;

	public MarshallingDecoder() throws IOException {
		unmarshaller = MarshallingCodeCFactory.buildUnmarshaller();
	}

	public Object decode(ByteBuf in) throws IOException, ClassNotFoundException {
		int objectSize = in.readInt();
		ByteBuf buf = in.slice(in.readerIndex(), objectSize);
		ByteInput input = new ChannelBufferByteInput(buf);
		try {
			unmarshaller.start(input);
			Object obj = unmarshaller.readObject();
			unmarshaller.finish();
			in.readerIndex(in.readerIndex() + objectSize);
			return obj;
		} finally {
			unmarshaller.close();
		}
	}

}
