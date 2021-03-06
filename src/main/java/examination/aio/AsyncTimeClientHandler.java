package examination.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements CompletionHandler<Void,AsyncTimeClientHandler>, Runnable {
	
	private String host;
	
	private int port;
	
	private AsynchronousSocketChannel client;
	
	private CountDownLatch latch;
	
	public AsyncTimeClientHandler(String host,int port){
		this.host = host;
		this.port = port;
		try {
			client = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		latch = new CountDownLatch(1);
		this.client.connect(new InetSocketAddress(host,port),this,this);
		try{
			latch.await();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		try{
			this.client.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}
	
	@Override
	public void completed(Void result,AsyncTimeClientHandler attachment){
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		client.write(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){
			@Override
			public void completed(Integer result,ByteBuffer buffer){
				if(buffer.hasRemaining()){
					client.write(buffer, buffer, this);
				}
				else{
					ByteBuffer read = ByteBuffer.allocate(1024);
					client.read(read, read, new CompletionHandler<Integer,ByteBuffer>(){
						@Override
						public void completed(Integer result,ByteBuffer readBuffer){
							readBuffer.flip();
							byte[] bytes = new byte[readBuffer.remaining()];
							readBuffer.get(bytes);
							String getMessage;
							try {
								getMessage = new String(bytes,"UTF-8");
								System.out.println("Now is :" + getMessage);
								latch.countDown();
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						@Override
						public void failed(Throwable exc,ByteBuffer buffer){
							try {
								client.close();
								latch.countDown();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
			@Override
			public void failed(Throwable exc,ByteBuffer attachment){
				try {
					client.close();
					latch.countDown();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
	@Override
	public void failed(Throwable exc,AsyncTimeClientHandler attachment){
		exc.printStackTrace();
		try {
			client.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
