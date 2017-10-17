package examination.netty.tcp.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class SubReqClient {
	
	public void connect(int port , String host) throws InterruptedException {
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(loopGroup).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
					.addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()))
					.addLast(new ProtobufVarint32LengthFieldPrepender())
					.addLast(new ProtobufEncoder())
					.addLast(new SubReqClientHandler());
					
				}
				
			});
			
			ChannelFuture f = b.connect(host , port).sync(); 
			f.channel().closeFuture().sync();
			
		}finally {
			loopGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new SubReqClient().connect(9527, "127.0.0.1");
	}

}
