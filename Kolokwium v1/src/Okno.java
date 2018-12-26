import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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

public class Okno extends JFrame implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static void main (String[] arg)
	{
		new Okno();
	}
	
	Panel panel = new Panel();
	
	JMenuItem AutorButton = new JMenuItem("Autor");
	JMenuItem CloseButton = new JMenuItem("Zamknij");
	
	JLabel SecondsLabel = new JLabel("Sekundy:");
	JTextField SecondsField = new JTextField(10);
	JButton ResetButton = new JButton("Zeruj");
	int time = 0;
	
	JButton DeleteMoney = new JButton("Usuñ banknot");
	JButton AddMoneyButton = new JButton("Dodaj banknot");
	JButton DeleteAllButton = new JButton("Usuñ wszystkie");
	JLabel MoneyLabel = new JLabel("Wlauta:");
	JTextField MoneyField = new JTextField (10);
	JLabel ValueLabel = new JLabel("Wartoœæ:");
	JTextField ValueField = new JTextField (10);
	
	HashSet<Banknot> currency = new HashSet<>();
	WidokKolekcji collection = new WidokKolekcji(currency);
	

	public Okno()
	{
		super("Micha³ Polak");
		setSize(600,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu file = new JMenu ("Plik");
		menu.add(file);
		file.add(AutorButton);
		file.add(CloseButton);
		
		AutorButton.addActionListener(this);
		CloseButton.addActionListener(this);
		
		panel.setPreferredSize(new Dimension (320, 200));
		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		
		panel.add(SecondsLabel);
		panel.add(SecondsField);
		SecondsField.setEditable(false);
		panel.add(ResetButton);
		ResetButton.addActionListener(this);
		
		panel.add(AddMoneyButton);
		panel.add(DeleteMoney);
		panel.add(DeleteAllButton);
		AddMoneyButton.addActionListener(this);
		DeleteMoney.addActionListener(this);
		DeleteAllButton.addActionListener(this);
		panel.add(MoneyLabel);
		panel.add(MoneyField);
		MoneyField.setEditable(true);
		panel.add(ValueLabel);
		ValueField.setEditable(true);
		panel.add(ValueField);
		panel.add(collection);
		
		setContentPane(panel);
		setVisible(true);
		
		new Thread(this).start();
		panel.run();
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
		if(source == AutorButton)
		{
			JOptionPane.showMessageDialog(null, "Micha³ Polak 235046");
		}
		
		if(source == CloseButton)
		{
			System.exit(0);
		}
		
		if(source == AddMoneyButton)
		{
			try
			{
				String money = MoneyField.getText();
				int value = Integer.parseInt(ValueField.getText());
				if (money.length() > 0)
				{
					currency.add(new Banknot(money,value));
					collection.refresh();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Waluta nie jest ci¹giem znaków");
				}
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Wartoœæ nie jest liczb¹ ca³kowit¹");
			}
		}
		
		if(source == DeleteMoney)
		{
			try
			{
				String money = MoneyField.getText();
				int value = Integer.parseInt(ValueField.getText());
				if (money.length() > 0)
				{
					currency.remove(new Banknot(MoneyField.getText(), Integer.parseInt(ValueField.getText())));
					collection.refresh();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Waluta nie jest ci¹giem znaków");
				}
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Nie zaznaczono ¿adnego elementu");
			}
		}
		
		if(source == DeleteAllButton) 
		{
			currency.removeAll(currency);
			collection.refresh();
		}
		
		if(source == ResetButton)
		{
			time = 0;
			SecondsField.setText(String.valueOf(time));
		}
	}

	@Override
	public void run() 
	{
		while (true)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			time++;
			SecondsField.setText(String.valueOf(time));
		}
	}
}

class Panel extends JPanel implements MouseListener, MouseMotionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	int pozX, pozY;
	int x=0;
	boolean isInsidePanel = false;
	boolean isButtonPressed = false;
	boolean isInPanelArea = false;
	boolean visible = false;
	
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.YELLOW);
		g2.fillOval(x, 0, 50, 50);
		
		if(isButtonPressed && isInsidePanel)
		{
			g2.setColor(Color.BLACK);
			g2.fillOval(pozX-25, pozY-25, 50, 50);
		}
		else if(isInsidePanel)
		{
			g2.setColor(Color.BLACK);
			g2.drawOval(pozX-25, pozY-25, 50, 50);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(isInsidePanel && isButtonPressed)
		{
			move(arg0.getX(), arg0.getY());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		if(isInsidePanel)
		{
			move(arg0.getX(), arg0.getY());
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		setVisible(true);
		isInsidePanel = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		setVisible(false);
		isInsidePanel = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		isButtonPressed = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		isButtonPressed = false;
		repaint();
	}
	
	
	public void move(int pozX, int pozY) 
	{
		this.pozX = pozX;
		this.pozY = pozY;
	}

	public void setVisible(boolean b) 
	{
		visible = b;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			try 
			{
				Thread.sleep(10);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			if(x>600) {
				x=0;
			}
			x++;
			repaint();
		}
	}
}

class Banknot 
{
	String waluta;
	int wartosc;
	
	public Banknot (String waluta, int wartosc)
	{
		this.waluta = waluta;
		this.wartosc = wartosc;
	}
	
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((waluta == null) ? 0 : waluta.hashCode());
		result = prime * result + wartosc;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Banknot other = (Banknot) obj;
		if (waluta == null) 
		{
			if (other.waluta != null)
				return false;
		} 
		else if (!waluta.equals(other.waluta))
			return false;
		if (wartosc != other.wartosc)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return waluta + "" + String.valueOf(wartosc);
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
        String[] kolumny = {"Nazwa:", "Rozmiar"};
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