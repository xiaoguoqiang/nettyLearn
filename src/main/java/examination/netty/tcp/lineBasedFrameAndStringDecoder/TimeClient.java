package examination.netty.tcp.lineBasedFrameAndStringDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {

	public void connect(int port, String host) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) {
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new TimeClientHandler());
						}
					});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
			
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 9527;
		try {
			new TimeClient().connect(port, "127.0.0.1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
