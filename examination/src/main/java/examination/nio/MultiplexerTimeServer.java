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
 * 澶嶇敤鍣ㄦ椂闂存湇鍔″櫒
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
			//璁剧疆闃诲妯″紡
			servChannel.configureBlocking(false);
			//璁剧疆缁戝畾IP銆佺鍙ｏ紝骞惰缃敹鍙戞秷鎭椂鏁版嵁鐨勬渶澶у��
			servChannel.socket().bind(new InetSocketAddress(port), 1024);
			//鏈嶅姟绔痗hannel娉ㄥ唽鍒皊elector锛屽苟鐩戝惉accept浜嬩欢
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("The time server is start in port :" + port);
			
		} catch (IOException e) {
			e.printStackTrace();
			//鍒濆鍖栧け璐ワ紝绯荤粺鎺ㄥ嚭
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
				//selector涓彇鍑�
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
				//selector 鍏抽棴鏃朵細鑷姩鍏抽棴鎺夊叾涓婄殑channel鍜宲ipe
				selector.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * selector鐩戝惉鍒颁簨浠跺悗鐨勪簨浠跺鐞嗙▼搴�
	 * @throws IOException 
	 * 
	 * **/
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//鑻ユ槸閾炬帴璇锋眰
			if(key.isAcceptable()){
				//瀹屾垚涓夋鎻℃墜
				SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
				//閰嶇疆闃诲妯″紡
				sc.configureBlocking(false);
				//閾炬帴鎴愬姛鍚庯紝鐩戝惉read浜嬩欢
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel)key.channel();
				//鍒濆鍖朾uffer
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int read = sc.read(buffer);
				if( read > 0){
					//璋冩暣buffer鐨刾osition鍜宭imit鐢ㄤ簬璇诲彇buffer
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					
					String message = new String(bytes,"UTF-8");
					
					System.out.println("The time server receive order : " + message);
					
					String returnMessage = message.equalsIgnoreCase("QUERY TIME ORDER") ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					
					doWriter(sc,returnMessage);
				}
				else if (read < 0){
					//閾捐矾鍏抽棴
					key.cancel();
					sc.close();
				}else{
					//0 鍒欏拷鐣�
				}
				
			}
		}
	}
	
	/**
	 * socket鍙戦�佹秷鎭�
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
