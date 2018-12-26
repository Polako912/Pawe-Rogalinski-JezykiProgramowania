import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/* Autor: Micha³ Polak 235046
 * Klasa PhoneBookServer z interfejsem graficznym
 * data wykonania: styczeñ 2018*/

public class PhoneBookServer extends JFrame implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	static final int SERVER_PORT = 25000;
	
	public static void main (String [] arg)
	 {
		new PhoneBookServer();
	 }
	
	private JLabel clientLabel = new JLabel ("Odbiorca: ");
	private JLabel messageLabel = new JLabel ("Napisz: ");
	private JLabel textAreaLabel = new JLabel ("Dialog: ");
	private JComboBox<ClientThread> clientMenu = new JComboBox<ClientThread>();
	private boolean koniecNas³uchiwania = false;
    private PhoneBook phoneBook = new PhoneBook();
	private JTextField messageField = new JTextField(20);
	private JTextArea textArea = new JTextArea (15,18);
	private JScrollPane scroll = new JScrollPane(textArea,
												ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
												ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
	
	PhoneBookServer()
	{
		super("PhoneBookServer");
		setSize(320,370);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.add(clientLabel);
		clientMenu.setPrototypeDisplayValue(new ClientThread("#######################################"));
		panel.add(clientMenu);
		panel.add(messageField);
		panel.add(messageLabel);
		messageField.addActionListener(this);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel.add(textAreaLabel);
		textArea.setEditable(false);
		panel.add(scroll);
		setContentPane(panel);
		setVisible(true);
		new Thread(this).start();
	}
	
	synchronized public void printReceivedMessage(ClientThread client, String message)
	{
		String text = textArea.getText();
		String[] splitedMessage = message.split(" ");
		try
	        {
	            if(splitedMessage[0].equals("LOAD"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.LOAD(splitedMessage[1]));
	            }
	            else if(splitedMessage[0].equals("SAVE"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.SAVE(splitedMessage[1]));
	            }
	            else if (splitedMessage[0].equals("GET"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.GET(splitedMessage[1]));
	            }
	            else if(splitedMessage[0].equals("PUT"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.PUT(splitedMessage[1], splitedMessage[2]));
	            }
	            else if (splitedMessage[0].equals("REPLACE"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.REPLACE(splitedMessage[1], splitedMessage[2]));
	            }
	            else if (splitedMessage[0].equals("DELETE"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.DELETE(splitedMessage[1]));
	            }
	            else if(message.equals("BYE"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage("OK BYE");
	                RemoveClient(client);
	            }
	            else if(message.equals("CLOSE"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                this.koniecNas³uchiwania = true;
	                client.SendMessage("OK CLOSE");
	                System.exit(0);
	            }
	            else if(message.equals("LIST"))
	            {
	                textArea.setText(client.getName() + " > " + message + "\n" + text);
	                client.SendMessage(phoneBook.LIST());
	            }
	            else
	            {
	                textArea.setText(client.getName() + " > ERROR nieznane polecenie\n" + text);
	            }
	            repaint();
	        }
	        catch (Exception e)
	        {

	        }
	}
	
	synchronized public void printSentMessage(ClientThread client, String message)
	
	{
		String text = textArea.getText();
		textArea.setText(client.getName() + ">>> " + message + "\n" + text);
	}
	
	synchronized void AddClient(ClientThread client)
	{
		clientMenu.addItem(client);
	}
	
	synchronized void RemoveClient(ClientThread client)
	{
		clientMenu.removeItem(client);
	}
	
	@Override
	public void run() 
	{
		boolean socket_created = false;
		
		try (ServerSocket server = new ServerSocket(SERVER_PORT))
		{
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("Serwer zosta³ uruchomiony na hoscie " + host);
			socket_created = true;
			
			while(true)
			{
				Socket socket = server.accept();
				if (socket != null && !koniecNas³uchiwania)
				{
					new ClientThread(this, socket);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
			if (!socket_created)
			{
				JOptionPane.showMessageDialog(null, "Gniazdo serwera nie mo¿e zostaæ utworzone");
				System.exit(0);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		String message;
		Object source = event.getSource();
	}
}