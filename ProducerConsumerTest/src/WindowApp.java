import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/*Program okienkowy Producent-Konsument 
Autor: Micha³ Polak  
Data:  grudzieñ 2017 r.*/

public class WindowApp extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public static void main (String arg[])
	{
		new WindowApp();
	}
	
	public static final String ABOUT_MESSAGE = 
			"Program okienkowy Producent-Konsument \n" + 
			"Autor: Micha³ Polak \n" + 
			"Data:  grudzieñ 2017 r.\n";
	
	private final String[] intArrayFromComboBox = {"1","2","3","4","5","6","7","8"};
	
	private JMenu[] menu = { new JMenu("Zmiana czasu"),
            new JMenu("Informacje")};

    private JMenuItem[] items = { new JMenuItem("Minimalny czas konsumpcji"),
                 new JMenuItem("Maksymalny czas konsumpcji"),
                 new JMenuItem("Minimalny czaas produkcji"),
                 new JMenuItem("Maksymalny czas produkcji"),
                 new JMenuItem("O programie"),
                 };
	
	JButton startButton = new JButton("Start");
	JButton stopButton = new JButton("Stop");
	
	JButton clearConsole = new JButton("Czyszczenie konsoli");
	JButton aboutButton = new JButton("O programie");
	
	JLabel comboBuffer = new JLabel("Wielkoœæ bufora");
	JLabel comboProducer = new JLabel("Iloœæ producentów");
	JLabel comboConsumer = new JLabel("Iloœæ konsumentów");
	
	JComboBox<String> comboIntBuffer = new JComboBox<String> (intArrayFromComboBox);
	JComboBox<String> comboIntProducer = new JComboBox<String> (intArrayFromComboBox);
	JComboBox<String> comboIntConsumer = new JComboBox<String> (intArrayFromComboBox);
	
	JTextArea  textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textArea);
	
	Buffer buffer = new Buffer(textArea);
	List<Producer> producerList = new ArrayList<Producer>();
	List<Consumer> consumerList = new ArrayList<Consumer>();
	
	int bufferListSize = 2;
	int producerListSize = 2;
	int consumerListSize = 2;
	
	public WindowApp ()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 650);
		setTitle("Producent-Konsument (Symulacja)");
		
		for (int i = 0; i < items.length; i++)
	      	items[i].addActionListener(this);

	      menu[0].add(items[0]);
	      menu[0].add(items[1]);
	      menu[0].add(items[2]);
	      menu[0].add(items[3]);
	      
	      menu[1].add(items[4]);
	      
	      JMenuBar menubar = new JMenuBar();
	      for (int i = 0; i < menu.length; i++)
	      	menubar.add(menu[i]);
	      setJMenuBar(menubar);
		
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		clearConsole.addActionListener(this);
		aboutButton.addActionListener(this);
		comboIntBuffer.addActionListener(this);
		comboIntProducer.addActionListener(this);
		comboIntConsumer.addActionListener(this);
		
		JPanel panel = new JPanel ();
		
		panel.add(startButton);
		panel.add(stopButton);
		
		
		panel.add(comboBuffer);
		panel.add(comboIntBuffer);
		panel.add(comboConsumer);
		panel.add(comboIntConsumer);
		panel.add(comboProducer);
		panel.add(comboIntProducer);
		
		panel.add(scrollPane);
		
		panel.add(clearConsole);
		panel.add(aboutButton);
		
		textArea.setEditable(false);
		textArea.setRows(25);
		textArea.setColumns(40);
		
		comboIntBuffer.setSelectedIndex(1);
		buffer.setBuffer(2);
		comboIntProducer.setSelectedIndex(1);
		comboIntConsumer.setSelectedIndex(1);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		setProducerAndConsumer();
		
        setContentPane(panel);
		setVisible(true);
	}
	
	private void setProducerAndConsumer() 
	{
		producerList = new ArrayList <Producer>();
		consumerList = new ArrayList <Consumer>();
		
		for (int i = 0; i < producerListSize; i++)
		{
			producerList.add(new Producer ("Producent" + i, buffer, textArea));
		}
		
		for (int i = 0; i < consumerListSize; i++)
		{
			consumerList.add(new Consumer ("Konsument" + i, buffer, textArea));
		}
	}

	private void StartSimulation ()
	{
		for (Consumer consumer : consumerList)
		{
			consumer.start();
		}
		
		for (Producer producer : producerList)
		{
			producer.start();
		}
	}
	
	private void StopSimulation()
	{
		for (Consumer consumer : consumerList)
		{
			consumer.StopExec();
			consumer.interrupt();
		}
		
		for (Producer producer : producerList)
		{
			producer.StopExec();
			producer.interrupt();
		}
		
		Worker.itemID = 1;
	}
	
	public static int parseDefault(String number, int defaultValue) 
	{
		  try 
		  {
		    return Integer.parseInt(number);
		  } 
		  catch (NumberFormatException e) 
		  {
		    return defaultValue;
		  }
		}
	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(source == startButton)
		{
			StartSimulation();
		}
		
		if(source == stopButton)
		{
			StopSimulation();
			setProducerAndConsumer();
		}
		
		if(source == comboIntBuffer)
		{
			StopSimulation();
			setProducerAndConsumer();
			bufferListSize = Integer.parseInt((String)comboIntBuffer.getSelectedItem());
			buffer.setBuffer(bufferListSize);
		}
		
		if(source == comboIntProducer)
		{
			StopSimulation();
			setProducerAndConsumer();
			consumerListSize = Integer.parseInt((String)comboIntProducer.getSelectedItem());
		}
		
		if(source == comboIntConsumer)
		{
			StopSimulation();
			setProducerAndConsumer();
			producerListSize = Integer.parseInt((String)comboIntConsumer.getSelectedItem());
		}
		
		if(source == clearConsole)
		{
			textArea.setText("");
		}
		
		if(source == aboutButton)
		{
			 JOptionPane.showMessageDialog(this, ABOUT_MESSAGE);
		}
		
		if(source == items[0])
		{
			StopSimulation();
			setProducerAndConsumer();
			Worker.MIN_CONSUMER_TIME = parseDefault(JOptionPane.showInputDialog("MIN_CONSUMER_TIME-Podaj czas w ms:"),Worker.MIN_CONSUMER_TIME); 
		}
		
		if(source == items[1])
		{
			StopSimulation();
			setProducerAndConsumer();
			Worker.MAX_CONSUMER_TIME = parseDefault(JOptionPane.showInputDialog("MAX_CONSUMER_TIME-Podaj czas w ms:"),Worker.MAX_CONSUMER_TIME);
		}
		
		if(source == items[2])
		{
			StopSimulation();
			setProducerAndConsumer();
			Worker.MIN_PRODUCER_TIME = parseDefault(JOptionPane.showInputDialog("MIN_PRODUCER_TIME-Podaj czas w ms:"),Worker.MIN_PRODUCER_TIME);
		}
		
		if(source == items[3])
		{
			StopSimulation();
			setProducerAndConsumer();
			Worker.MAX_PRODUCER_TIME = parseDefault(JOptionPane.showInputDialog("MAX_PRODUCER_TIME-Podaj czas w ms:"),Worker.MAX_PRODUCER_TIME);
		}
		
		if(source == items[4])
		{
			JOptionPane.showMessageDialog(this, ABOUT_MESSAGE);
		}
	}

}
