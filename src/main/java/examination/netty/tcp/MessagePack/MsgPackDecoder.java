package examination.netty.tcp.MessagePack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 
 * 自定义解码器
 * **/
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		final int length = msg.readableBytes();
		final byte[] raw;
		raw = new byte[length];
		msg.getBytes(msg.readerIndex(), raw, 0, length);
		MessagePack msgPack = new MessagePack();
		out.add(msgPack.read(raw));
	}

}
