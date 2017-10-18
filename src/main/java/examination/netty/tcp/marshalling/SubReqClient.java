package examination.netty.tcp.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder())
					.addLast(MarshallingCodeCFactory.buildMarshallingEncoder())
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
