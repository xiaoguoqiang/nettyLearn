package examination.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {
	
	private AsynchronousSocketChannel channel;
	
	public ReadCompletionHandler(AsynchronousSocketChannel channel){
		if(this.channel == null){
			this.channel = channel;
		}
	}
	
	@Override
	public void completed(Integer result,ByteBuffer attachment){
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try{
			String req = new String(body,"UTF-8");
			System.out.println("The time server receive order :" + req);
			String message = req.equalsIgnoreCase("QUERY TIME ORDER") ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
			doWrite(message);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	public void doWrite(String message){
		if(message != null && message.length() > 0){
			byte[] bytes = message.getBytes();
			ByteBuffer buffer =  ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			channel.write(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>(){
				@Override
				public void completed(Integer result,ByteBuffer buffer){
					if(buffer.hasRemaining()){
						channel.write(buffer, buffer, this);
					}
				}
				
				@Override
				public void failed(Throwable exc,ByteBuffer buffer){
					try{
						channel.close();
					}catch(IOException e){
						
					}
				}
			});
		}
	}
	
	@Override
	public void failed(Throwable exc,ByteBuffer attachment){
		try{
			this.channel.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
