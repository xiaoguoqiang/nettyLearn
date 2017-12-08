package examination.netty.customprotocol.marshalling;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import examination.common.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingDecoder {
	private final UnmarshallerProvider unmarshallerProvider;

	public MarshallingDecoder() throws IOException {
		unmarshallerProvider = MarshallingCodeCFactory.buildUnmarshallerProvider();
	}

	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		Unmarshaller unmarshaller = unmarshallerProvider.getUnmarshaller(ctx);
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
