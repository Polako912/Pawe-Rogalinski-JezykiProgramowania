import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

class Buffer 
{
	private List<Integer> contents = new ArrayList<Integer>();
	private JTextArea textArea;
	private final String newline = "\n";
	
	public Buffer(JTextArea textA)
	{
		this.textArea = textA;
	}

	void setBuffer(int buffor_size)
	{
		for (int i = 0; i < contents.size(); i++) 
		{
			contents.remove(i);
		}
		
		contents = new ArrayList<Integer>();
		
		for (int i = 0; i < buffor_size; i++) 
		{
			contents.add(i);
		}
	}
	
	public synchronized int get(Consumer consumer)
	{
		textArea.append(newline + "Konsument <" + consumer.name + "> chce zabrac");
		while (isAvailableGet())
		{
			try 
			{ 
				textArea.append(newline + "Konsument <" + consumer.name + ">   bufor pusty - czekam");
				wait();
			} 
			catch (InterruptedException e) 
			{ 
			}
		}
		int item = contents.get(isAvailableToGet());
		contents.set(isAvailableToGet(),0);
		textArea.append(newline + "Konsument <" + consumer.name + ">      zabral: " + contents);
		notifyAll();
		return item;
	}

	public synchronized void put(Producer producer, int item)
	{
		textArea.append(newline + "Producent <" + producer.name + ">  chce oddac: " + item);
		while (isAvailablePut())
		{
			try { 
					textArea.append(newline + "Producent <" + producer.name + ">   bufor zajety - czekam");
					wait();
				} 
			catch (InterruptedException e) 
			{ 
			}
		}
		contents.set(isAvailableToPut(), item);
		textArea.append(newline + "Producent <" + producer.name + ">       oddal: " + item);
		notifyAll();
	}
	
	private int isAvailableToPut() 
	{
		for (Integer integer : contents) 
		{
			if(integer!=0) 
			{
				return contents.indexOf(integer);
			}
		}
		return 0;
	}

	private boolean isAvailablePut() 
	{
		for (Integer integer : contents) 
		{
			if(integer==0) 
			{
				return false;
			}
		}
		return true;
	}

	private boolean isAvailableGet()
	{
		for (Integer integer : contents) 
		{
			if(integer==0) 
			{
				return false;
			}
		}
		return true;
	}

	private int isAvailableToGet() 
	{
		for (Integer integer : contents) 
		{
			if(integer!=0) 
			{
				return contents.indexOf(integer);
			}
		}
		return 0;
	}

}