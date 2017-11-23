package examination.netty.httpAndXml;


import examination.netty.httpAndXml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HttpXmlClient {

	public void connect(int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("httpdecoder",new HttpResponseDecoder())
							   		.addLast(new HttpObjectAggregator(65535))
									.addLast("xmldecoder",new HttpXmlResponseDecoder(Order.class, true))
									.addLast(new HttpRequestEncoder())
									.addLast(new HttpXmlRequestEncoder())
									.addLast(new HttpXmlClientHandler());
						}

					});

			ChannelFuture cf = boot.connect("192.168.137.88",9527).sync();
			cf.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new HttpXmlClient().connect(9527);
	}

}
