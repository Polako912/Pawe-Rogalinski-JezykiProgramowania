import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*autor: Micha³ Polak 235046
 * klasa ClientThread
 * data wykonania: styczeñ 2018*/

public class ClientThread implements Runnable
{
	private Socket socket;
	private String name;
	private PhoneBookServer myServer;
	
	private ObjectOutputStream outputStream = null;
	
	ClientThread(String prototypeDisplayValue)
	{
		name = prototypeDisplayValue;
	}
	
	ClientThread(PhoneBookServer server, Socket socket)
	{
		myServer = server;
		this.socket = socket;
		new Thread(this).start();
	}
	
	public String getName()
	{ 
		return name; 
	}
	
	public String toString()
	{ 
		return name; 
	}
	
	public void SendMessage(String message)
	{
		try
		{
			outputStream.writeObject(message);
			if (message.equals("exit"))
			{
				myServer.RemoveClient(this);
				socket.close();
				socket = null;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		String message;
		try(ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());)
		{
			outputStream = output;
			name = (String)input.readObject();
			myServer.AddClient(this);
			while (true)
			{
				message = (String)input.readObject();
				myServer.printReceivedMessage(this, message);
				if(message.equals("exit"))
				{
					myServer.RemoveClient(this);
					break;
				}
				if(message.equals("serwer"))
                {
                    System.out.println("tutaj dostaje wiadomoœæ od serwera!");
                }
			}
			socket.close();
			socket = null;
		}
		catch (Exception e)
		{
			myServer.RemoveClient(this);
		}
	}
}