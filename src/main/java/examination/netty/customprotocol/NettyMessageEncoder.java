package examination.netty.customprotocol;

import java.io.IOException;
import java.util.Map;

import examination.netty.customprotocol.marshalling.MarshallingEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage>{ //此处父类为 MessageToByteEncoder   
	
	MarshallingEncoder marshallingEncoder;
	
	public NettyMessageEncoder() throws IOException {
		marshallingEncoder = new MarshallingEncoder();
	}
	

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf)throws Exception {
		if(msg == null || msg.getHeader() == null) {
				throw new Exception("The code message is null");
		}
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionID());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for(Map.Entry<String, Object> param: msg.getHeader().getAttachment().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			marshallingEncoder.encode(ctx, value, sendBuf);
		}
		
		key = null;
		keyArray = null;
		value = null;
		if(msg.getBody() != null) {
			marshallingEncoder.encode(ctx, msg.getBody(), sendBuf);
		}else {
			sendBuf.writeInt(0);
			sendBuf.setInt(4 , sendBuf.readableBytes()-8);  //此处长度设置为长度字段之后的长度
		}
	}
}
