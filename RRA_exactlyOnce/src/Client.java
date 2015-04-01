import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class Client extends Thread {

	DatagramSocket sock;
	int srvPort;
	InetAddress addr;
	
	
	public Client(String hostName,int port) throws UnknownHostException, SocketException
	{
		this.srvPort = port;
		addr = InetAddress.getByName(hostName);
		sock = new DatagramSocket();
	}
	
	@Override
	public void run() {
		int actualId;
		
		try{
			for(actualId=0;actualId<10;)
			{
				String strToSend = actualId + "";
				DatagramPacket packToSend = new DatagramPacket(strToSend.getBytes(),strToSend.getBytes().length,addr,srvPort);
				sock.send(packToSend);
				
				DatagramPacket packToReceive = new DatagramPacket(new byte[128],128);
				sock.setSoTimeout(1000);
				try
				{
					sock.receive(packToReceive);
					//notimeout
					String rcvString = new String(packToReceive.getData(),packToReceive.getOffset(),packToReceive.getLength());
					
					int idRichiestaRicevuta = Integer.parseInt(rcvString);
					if(idRichiestaRicevuta < actualId)
					{
						System.out.println("RICEVUTA RICHIESTA DUPLICATA");
						continue;
					}
					System.out.println("CLIENT: INVIATO"+strToSend);
					System.out.println("CLIENT: RICEVUTO"+rcvString);
					actualId = idRichiestaRicevuta+1;
				}
				catch(SocketTimeoutException ex)
				{
					System.out.println("TIMEOUT CLIENT");
					continue;
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	
	}
	
}
