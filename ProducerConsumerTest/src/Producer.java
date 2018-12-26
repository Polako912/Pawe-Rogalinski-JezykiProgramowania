import javax.swing.JTextArea;

class Producer extends Worker 
{

	public Producer(String name , Buffer buffer, JTextArea textA)
	{ 
		this.name = name;
		this.buffer = buffer;
		this.textArea = textA;
	}
	
	@Override
	public void run()
	{ 
		running = true;
		int item;
		while(running)
		{
			item = itemID++;
			textArea.append(newline + "Producent <" + name + ">   produkuje: " + item);
			sleep(MIN_PRODUCER_TIME, MAX_PRODUCER_TIME);
		
			buffer.put(this, item);
		}
	}
	
	public void StopExec()
	{
		running = false;
	}
}