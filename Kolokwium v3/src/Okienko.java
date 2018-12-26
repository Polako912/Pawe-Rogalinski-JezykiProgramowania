import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Okienko extends JFrame implements ActionListener, Runnable
{
	public static void main (String[] arg)
	{
		new Okienko();
	}

	private static final long serialVersionUID = 1L;
	
	Panel panel = new Panel();
	
	private JButton authorButton = new JButton("Autor");
	private JMenuItem authorItem = new JMenuItem("Autor");
	private JMenuItem closeItem = new JMenuItem("Zamknij");
	
	private int time = 0;
	private JTextField secondsField = new JTextField(10);
	private JLabel secondsLabel = new JLabel("Sekundy");
	private JButton resetButton = new JButton("Resetuj czas");
	
	HashSet<Banknot> currency = new HashSet<>();
	WidokKolekcji kolekcja = new WidokKolekcji(currency);
	
	private JTextField moneyField = new JTextField(10);
	private JLabel moneyLabel = new JLabel("Waluta");
	private JTextField valueField = new JTextField(10);
	private JLabel valueLabel = new JLabel("Wartosc");
	private JButton addButton = new JButton("Dodaj banknot");
	private JButton deleteButton = new JButton("Usuñ");
	private JButton deleteAllButton = new JButton("Usuñ wszystkie");
	
	public Okienko() {
		super("Micha³ Polak");
		setSize(600,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		panel.add(authorButton);
		authorButton.addActionListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu file = new JMenu("Plik");
		menuBar.add(file);
		file.add(authorItem);
		file.add(closeItem);
		authorItem.addActionListener(this);
		closeItem.addActionListener(this);
		
		panel.setPreferredSize(new Dimension(320, 300));
		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		
		secondsField.setEditable(false);
		panel.add(secondsLabel);
		panel.add(secondsField);
		panel.add(resetButton);
		resetButton.addActionListener(this);
		
		panel.add(moneyLabel);
		panel.add(moneyField);
		panel.add(valueLabel);
		panel.add(valueField);
		panel.add(addButton);
		panel.add(deleteButton);
		panel.add(deleteAllButton);
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		deleteAllButton.addActionListener(this);
		panel.add(kolekcja);
		
		setContentPane(panel);
		setVisible(true);
		
		new Thread(this).run();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if(source == authorButton)
		{
			JOptionPane.showMessageDialog(this, "Micha³ Polak 235046");
		}
		
		if(source == authorItem)
		{
			JOptionPane.showMessageDialog(this, "Micha³ Polak 235046");
		}
		
		if(source == resetButton)
		{
			time = 0;
			secondsField.setText(String.valueOf(time));
		}
		
		if(source == addButton)
		{
			try
			{
			String money = moneyField.getText();
			int value = Integer.parseInt(valueField.getText());
			if(money.length()>0)
			{
				currency.add(new Banknot(money, value));
				kolekcja.refresh();
			}
			else 
			{
				JOptionPane.showMessageDialog(this, "Waluta nie jest ci¹giem znaków");
			}
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Waluta nie jest ci¹giem znaków");
			}
		}
		
		if(source == deleteButton)
		{
			try
			{
			String money = moneyField.getText();
			int value = Integer.parseInt(valueField.getText());
			if(money.length()>0)
			{
				currency.remove(new Banknot(moneyField.getText(), Integer.parseInt(valueField.getText())));
				kolekcja.refresh();
			}
			else 
			{
				JOptionPane.showMessageDialog(this, "Waluta nie jest ci¹giem znaków");
			}
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Waluta nie jest ci¹giem znaków");
			}
		}
		
		if(source == deleteAllButton)
		{
			currency.removeAll(currency);
			kolekcja.refresh();
		}
		
		if(source == closeItem)
		{
			System.exit(0);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time++;
			secondsField.setText(String.valueOf(time));
		}
	}
}

class Panel extends JPanel implements MouseListener,MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	int pozX, pozY;
	boolean isInsidePanel = false;
	boolean isButtonPressed = false;
	boolean visible = false;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(isInsidePanel && isButtonPressed)
		{
			g.setColor(Color.BLACK);
			g.fillOval(pozX-25, pozY-25, 50, 50);
		}
		
		if(isInsidePanel)
		{
			g.setColor(Color.black);
			g.drawOval(pozX-25, pozY-25, 50, 50);
		}
	}
	
	@Override
	public void mouseDragged(java.awt.event.MouseEvent arg0) {
		if(isInsidePanel && isButtonPressed)
		{
			move(arg0.getX(), arg0.getY());
			repaint();
		}
		
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent arg0) {
		if(isInsidePanel)
		{
			move(arg0.getX(), arg0.getY());
			repaint();
		}
		
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {
		
		setVisible(true);
		isInsidePanel = true;
		repaint();
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {
		
		setVisible(false);
		isInsidePanel = false;
		repaint();
		
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent arg0) {
		
		isButtonPressed = true;
		repaint();
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {
		
		isButtonPressed = false;
		repaint();
	}
	
	public void move (int pozX, int pozY)
	{
		this.pozX = pozX;
		this.pozY = pozY;
	}
	
	public void setVisible(boolean b)
	{
		visible = b;
	}
}	

class Banknot
{
	String waluta;
	int wartosc;
	
	public Banknot(String waluta, int wartosc) {
		this.waluta = waluta;
		this.wartosc = wartosc;
	}
	
	@Override
	public String toString() {
		return waluta + "" + String.valueOf(wartosc);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((waluta == null) ? 0 : waluta.hashCode());
		result = prime * result + wartosc;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Banknot other = (Banknot) obj;
		if (waluta == null) {
			if (other.waluta != null)
				return false;
		} else if (!waluta.equals(other.waluta))
			return false;
		if (wartosc != other.wartosc)
			return false;
		return true;
	}
	
}

class WidokKolekcji extends JScrollPane 
{
    private static final long serialVersionUID = 1L;

    private JTable tabela;
    private DefaultTableModel modelTabeli;
    Collection<Banknot> kolekcja;

    WidokKolekcji(Collection<Banknot> kolekcja) 
    {
        String[] kolumny = {"Nazwa:", "Wartoœæ"};
        modelTabeli = new DefaultTableModel(kolumny, 0);
        tabela = new JTable(modelTabeli);
        tabela.setRowSelectionAllowed(false);
        this.kolekcja = kolekcja;
        setViewportView(tabela);
        setPreferredSize(new Dimension(150, 200));
        setBorder(BorderFactory.createTitledBorder("Pude³ ka"));
    }

    void refresh() 
    {
        modelTabeli.setRowCount(0);
        for (Banknot p : kolekcja)
        {
            String[] s = {p.waluta, "" + p.wartosc};
            modelTabeli.addRow(s);
        }
    }
}