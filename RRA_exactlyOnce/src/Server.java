import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class Server extends Thread {
	
	DatagramSocket sock;
	int myPort;
	
	public Server(int myPort) throws SocketException
	{
		this.myPort = myPort;
		sock = new DatagramSocket(myPort);
	}
	
	@Override
	public void run() {
		
		DatagramPacket packToReceive=null,packToSend=null;
		String strRcv=null,strToSend = null;
		int clientPort = -1;
		InetAddress clientAddr = null;
		int id=1,rcvId;
		
		for(;;)
		{
			packToReceive = new DatagramPacket(new byte[128],128);
			try{
				sock.receive(packToReceive);
				clientPort = packToReceive.getPort();
				clientAddr = packToReceive.getAddress();
				strRcv = new String(packToReceive.getData(),packToReceive.getOffset(),packToReceive.getLength());
				rcvId = Integer.parseInt(strRcv);
				if(rcvId > id)
				{
					id = rcvId +1;
				}
				if(id == 2)
					continue;
			}
			catch(SocketTimeoutException ex)
			{
				System.out.println("SERVER TIMEOUT");
				continue;
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.exit(0);
			}
			finally
			{
				strToSend = id + "";
				packToSend = new DatagramPacket(strToSend.getBytes(),strToSend.getBytes().length,clientAddr,clientPort);
				
				try{
					sock.send(packToSend);
					sock.setSoTimeout(1000);
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				
			}
			
		}	

	}
}
