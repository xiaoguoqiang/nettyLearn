package examination.netty.tcp.MessagePack;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private int counter = 0;

	private UserInfo[] infos = GetUser();

	public EchoClientHandler() {

	}

	private UserInfo[] GetUser() {
		UserInfo[] data = new UserInfo[50];
		UserInfo one;
		for (int i = 0; i < data.length; i++) {
			one = new UserInfo();
			one.setName(i + "name");
			one.setEdge(i);
			one.setAddress(i + "address");
			data[i] = one;
		}
		return data;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (UserInfo ee : infos) {
			ctx.write(ee);
		}
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("Client recevie " + ++counter + "times message " + msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable able) {
		able.printStackTrace();
		ctx.close();
	}
}
