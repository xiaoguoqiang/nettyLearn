package examination.netty.httpAndXml;


import examination.netty.httpAndXml.pojo.Order;
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

public class HttpXmlServer {

	public void run(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bs = new ServerBootstrap();
			bs.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpRequestDecoder())
										 .addLast(new HttpObjectAggregator(65535))
										 .addLast(new HttpXmlRequestDecoder(Order.class,true))
										 .addLast(new HttpResponseEncoder())
										 .addLast(new HttpXmlResponseEncoder())
										 .addLast(new HttpXmlServerHandler());
						}

					});
			ChannelFuture cf = bs.bind("localhost",9527).sync();
			System.out.println("The Server start in " + port);
			cf.channel().closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new HttpXmlServer().run(9527);
	}

}
