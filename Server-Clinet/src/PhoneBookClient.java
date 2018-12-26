import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/*autor: Micha³ Polak 235046
 * klasa PhoneBookClient i PhoneBook
 * data wykonania: styczeñ 2018*/

public class PhoneBookClient extends JFrame implements Runnable, ActionListener
{

	private static final long serialVersionUID = 1L;

	public static void main (String [] arg)
	{
		String name;
		String host;
		
		host = JOptionPane.showInputDialog("Podaj adres serwera: ");
		name = JOptionPane.showInputDialog("Podaj nazwê klienta: ");
		
		if(name != null && !name.equals(""))
		{
			new PhoneBookClient(name, host);
		}
	}
	
	private JTextField messageField = new JTextField(20);
	private JTextArea textArea = new JTextArea(18,20);
	
	final static int SERVER_PORT = 25000;
	
	private String name;
	private String serverHost;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	PhoneBookClient (String name, String host)
	{
		super(name);
		this.name = name;
		this.serverHost = host;
		setSize(320, 380);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener((WindowListener) new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				try
				{
					output.close();
					input.close();
					socket.close();
				}
				catch (IOException e)
				{
					System.out.println(e);
				}
			}
			public void windowClosed(WindowEvent event)
			{
				windowClosing(event);
			}
		});
	
	JPanel panel = new JPanel();
	JLabel messageLabel = new JLabel("Napisz: ");
	JLabel textAreaLabel = new JLabel("Dialog: ");
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	
	panel.add(messageLabel);
	panel.add(messageField);
	messageField.addActionListener(this);
	panel.add(textAreaLabel);
	JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	panel.add(scroll_bars);
	setContentPane(panel);
	setVisible(true);
	new Thread(this).start(); 
	}

	synchronized public void printReceivedMessage(String message)
	{
	String tmp_text = textArea.getText();
		textArea.setText(tmp_text + ">> " + message + "\n");
	}

	synchronized public void printSentMessage(String message)
	{
	String text = textArea.getText();
 	textArea.setText(text + "<< " + message + "\n");
	}

	public void actionPerformed(ActionEvent event)
	{ 
		String message;
		Object source = event.getSource();
		if (source==messageField)
		{
			try
			{ 
				message = messageField.getText();
				output.writeObject(message);
				printSentMessage(message);
				
				if (message.equals("exit"))
				{
					input.close();
					output.close();
					socket.close();
					setVisible(false);
					dispose();
					return;
				}
			}
			catch(IOException e)
			{ 
				System.out.println("Wyjatek klienta "+e);
			}
		}
		repaint();
	}

	public void run()
	{
		if (serverHost.equals("")) 
		{
			serverHost = "localhost";
		}
		try
		{
			socket = new Socket(serverHost, SERVER_PORT);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(name);
		} 
		catch(IOException e)
		{ 
			JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
			setVisible(false);
			dispose();
			return;
		}
		try
		{
			while(true)
			{
				String message = (String)input.readObject();
				printReceivedMessage(message);
				if(message.equals("exit"))
				{
					input.close();
					output.close();
					socket.close();
					setVisible(false);
					dispose();
					break;
				}
			}
		} 
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
			setVisible(false);
			dispose();
		}	
	}
}

class PhoneBook
{
	private String imie;
	private int numer_telefonu;
	private Map<String, String> mapa = new ConcurrentHashMap<String, String>();
	
	public String LOAD (String nazwa_pliku) throws Exception
	{
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(nazwa_pliku)))
		{
			mapa = (ConcurrentHashMap<String,String>) input.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "ERROR";
		}
		return "OK";
	}
	
	public String SAVE (String nazwa_pliku)
	{
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(nazwa_pliku)))
		{
			output.writeObject(mapa);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "ERROR";
		}
		return "OK";
	}
	
	public String GET (String imie)
	{
		Iterator<String> iterator = mapa.keySet().iterator();
		
		while (iterator.hasNext())
		{
			String key = iterator.next();
			
			if(key.equals(imie))
			{
				return key.toString() + " " + mapa.get(key);
			}
		}
		
		return "OK";
	}
	
	public String PUT (String imie, String numer)
	{
		mapa.put(imie, numer);
		return "OK";
	}
	
	public String REPLACE (String imie, String numer)
	{
		Iterator<String> iterator = mapa.keySet().iterator();

		while (iterator.hasNext())
		{
			String key = iterator.next();
			
			if(key.equals(imie))
			{
				mapa.remove(key);
				mapa.put(imie, numer);
			}
		}
		
		return "OK";
	}
	
	public String DELETE (String imie)
	{
		Iterator<String> iterator = mapa.keySet().iterator();
		
		while (iterator.hasNext())
		{
			String key = iterator.next();
			
			if(key.equals(imie))
			{
				mapa.remove(imie);
			}
		}
		
		return "OK";
	}
	
	public String LIST () 
	{
		String zwroc = "";
		zwroc += mapa.toString();
		
		return "OK \n" + zwroc;
	}
}