import java.net.SocketException;
import java.net.UnknownHostException;


public class TestClass {
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		Client client = new Client("localhost",1289);
		Server srv = new Server(1289);
		
		srv.start();
		client.start();
		
	}
}
