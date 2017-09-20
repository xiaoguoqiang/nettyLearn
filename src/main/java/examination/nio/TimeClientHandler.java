package examination.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable  {
	
	private String host;
	
	private int port;
	
	private Selector selector;
	
	private SocketChannel socketChannel;
	
	private volatile boolean stop;
	
	public TimeClientHandler(String host, int port){
		this.host = host == null ? "127.0.0.1":host;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);			
			
			} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop(){
		this.stop = true;
	}
	
	@Override
	public void run(){
		try{
			doConnect();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		while(!stop){
			try{
				selector.select(1000);
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
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
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * selector中注册read或者connect
	 * 
	 * **/
	private void doConnect() throws IOException{
		if(socketChannel.connect(new InetSocketAddress(host,port))){
			socketChannel.register(selector, SelectionKey.OP_READ);
		}
		else{
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	/**
	 * 处理就绪的channel
	 * **/
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			SocketChannel sc = (SocketChannel)key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				}
				else{
					System.exit(1);
				}
			}
			if(key.isReadable()){
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int read = sc.read(buffer);
				if(read > 0){
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("The time is " + body);
					this.stop = true;
				}
				else if (read < 0){
					key.cancel();
					sc.close();
				}else{
					//nothing
				}
			}
		}
	}
	
	/**
	 * 发送信息
	 * */
	private void doWrite(SocketChannel sc) throws IOException{
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		sc.write(buffer);
		if(!buffer.hasRemaining()){
			System.out.println("Send order 2 server succeed");
		}
	}
	
}
