package examination.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

	public void run(int port) throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(ServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpServerCodec()).addLast(new HttpObjectAggregator(65535))
									.addLast(new ChunkedWriteHandler()).addLast(new WebSocketServerHandler());

						}	

					});

			ChannelFuture f = b.bind("localhost", port).sync();
			System.out.println("Web socket server started at port : " + port);

			f.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new WebSocketServer().run(9527);
	}

}
