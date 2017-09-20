package examination.aio;

public class TimeClient {
	
	public static void main(String[] args){
		int port = 9527;
		String host = "127.0.0.1";
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		AsyncTimeClientHandler client = new AsyncTimeClientHandler(host,port);
		new Thread(client,"AysncTimeClientHandler-001").start();
		
	}

}
