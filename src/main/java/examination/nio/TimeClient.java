package examination.nio;

public class TimeClient {
	
	private static int port = 9527;
	
	public static void main(String[] args){
		
		if(args != null && args.length > 0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		new Thread(new TimeClientHandler("127.0.0.1",port),"TimeClient-001").start();
	}

}
