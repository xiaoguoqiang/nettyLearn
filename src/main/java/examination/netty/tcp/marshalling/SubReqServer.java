package examination.netty.tcp.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {
	public void bind(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024).handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder())
					.addLast(MarshallingCodeCFactory.buildMarshallingEncoder())
					.addLast(new SubReqServerHander());
				}
				
			});
			
			//绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		int port = 9527;
		if(args.length > 0) {
			port = Integer.valueOf(args[0]);
		}
		new SubReqServer().bind(port);
	}

}
