package examination.netty.tcp.FixedLengthFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
	public void bind(int port) {
		EventLoopGroup bossgroup = new NioEventLoopGroup();
		EventLoopGroup workgroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossgroup, workgroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new FixedLengthFrameDecoder(20)).addLast(new StringDecoder())
									.addLast(new EchoServerHandler());

						}
					});

			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossgroup.shutdownGracefully();
			workgroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		int port = 9527;
		if (args != null && args.length > 0) {
			port = Integer.valueOf(args[0]);
		}
		new EchoServer().bind(port);

	}

}
