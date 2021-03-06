package examination.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * 多路复用器
 * 
 * 
 * **/

public class MultiplexerTimeServer implements Runnable {
	
	private Selector selector;
	
	private ServerSocketChannel servChannel;
	
	private volatile boolean stop;
	
	public MultiplexerTimeServer(int port){
		try {
			
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			//设置为非阻塞
			servChannel.configureBlocking(false);
			//绑定本地ip和对应端口
			servChannel.socket().bind(new InetSocketAddress(port), 1024);
			//向selector注册channel，并监听accept事件
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("The time server is start in port :" + port);
			
		} catch (IOException e) {
			e.printStackTrace();
			//异常，则程序退出
			System.exit(1);
		}
		
	}
	
	public void stop(){
		this.stop = false;
	}
	
	@Override
	public void run(){
		while(!stop){
			try {
				
				selector.select(1000);
				//selector获取就绪状态的channel集合
				Set<SelectionKey> selectedKey = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKey.iterator();
				
				SelectionKey key = null;
				
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try{
						handleInput(key);
					}catch(Exception e){
						if(key != null){
							key.cancel();
						}
						if(key.channel() != null){
							key.channel().close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		if(selector != null){
			try{
				//selector 关闭时会自动释放注册在其上的资源（channel、pipe）
				selector.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理就绪状态的channel
	 * @throws IOException 
	 * 
	 * **/
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//accept事件处理
			if(key.isAcceptable()){
				//三次握手
				SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
				//设置为非阻塞
				sc.configureBlocking(false);
				//selector中注册Read
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel)key.channel();
				//初始化buffer
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int read = sc.read(buffer);
				if( read > 0){
					//将buffer的limit和position交换位置
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					
					String message = new String(bytes,"UTF-8");
					
					System.out.println("The time server receive order : " + message);
					
					String returnMessage = message.equalsIgnoreCase("QUERY TIME ORDER") ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					
					doWriter(sc,returnMessage);
				}
				else if (read < 0){
					//对端链路关闭
					key.cancel();
					sc.close();
				}else{
					//0 不做处理
				}
				
			}
		}
	}
	
	/**
	 * socket发送信息
	 * **/
	private void doWriter(SocketChannel sc, String message) throws IOException{
		if(message != null && message.trim().length() > 0){
			byte[] bytes = message.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			sc.write(buffer);
		}
	}

}
