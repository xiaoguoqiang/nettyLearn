package examination.netty.customprotocol;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private EventLoopGroup group = new NioEventLoopGroup();

	private void connect(String ip, int port) throws InterruptedException {
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4))
									.addLast(new NettyMessageEncoder()).addLast(new ReadTimeoutHandler(50))
									.addLast(new LoginAuthReqHandler()).addLast(new HeartBeatReqHandler());
						}

					});

			ChannelFuture future = b.connect(new InetSocketAddress(ip, port)).sync();
			future.channel().closeFuture().sync();

		} finally {
			executor.execute(() -> {
				try {
					TimeUnit.SECONDS.sleep(5);
					try {
						connect("127.0.0.1", 9527);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});

		}

	}

	public static void main(String[] args) throws InterruptedException {
		new NettyClient().connect("127.0.0.1", 9527);
	}

}
