package examination.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {

	private final static String DEAFAULT_URL = "D:\\test";

	public void initServer(String ip, Integer port, String url) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("http-decoder", new HttpRequestDecoder())
									.addLast("http-aggregator", new HttpObjectAggregator(65535))
									.addLast("http-encoder", new HttpResponseEncoder())
									.addLast("http-chunked", new ChunkedWriteHandler())
									.addLast("fileserverhandler", new HttpFileServerHandler(url));
						}

					});

			ChannelFuture cf = b.bind(ip, port).sync();
			System.out.println("Server Start url is : http://" + ip + ":" + port + url);
			cf.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		String url = DEAFAULT_URL;
		if (args.length > 0) {
			url = args[0];
		}

		new HttpFileServer().initServer("localhost", 9527, url);
	}

}
