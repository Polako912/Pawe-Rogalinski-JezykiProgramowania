import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

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

class Banknot {
	
	String waluta;
	int wartosc;
	
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
	
	public String toString()
	{
		return waluta + "" + String.valueOf(wartosc);
	}

	public Banknot(String waluta, int wartosc) {
		this.waluta = waluta;
		this.wartosc = wartosc;
}

// Tu proszê dopisaæ brakuj¹ce metody, które s¹ niezbêdne,
// by obiekty klasy Banknot mog³y byæ przechowywane w kolekcji


} // koniec klasy Banknot



// KLASA POMOCNICZA DO ZADAÑ 4, 5
//W TEJ KLASIE NIE TRZEBA NIC MODYFIKOWAÆ !!!
class WidokKolekcji extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private JTable tabela;
	private DefaultTableModel modelTabeli;
	Collection<Banknot> kolekcja;

/* Do konstruktora nale¿y przekazaæ referencjê
* na kolekcjê, której zawartoœæ ma byæ wyœwietlana
* na panelu
*/
WidokKolekcji(Collection kolekcja) {
	String[] kolumny = { "Nazwa:", "Rozmiar" };
	modelTabeli = new DefaultTableModel(kolumny, 0);
	tabela = new JTable(modelTabeli);
	tabela.setRowSelectionAllowed(false);
	this.kolekcja = kolekcja;
	setViewportView(tabela);
	setPreferredSize(new Dimension(150, 200));
	setBorder(BorderFactory.createTitledBorder("Pudelka"));
}

/* Metodê refresh() nale¿y wywo³aæ po ka¿dej
* modyfikacji zawartoœci wyœwietlanej kolejcji
*/
	void refresh(){
		modelTabeli.setRowCount(0);
		for (Banknot p : kolekcja) {
			String[] s = { p.waluta, ""+p.wartosc };
			modelTabeli.addRow(s);
		}
	}
} // koniec klasy WidokKolekcji 

class Panel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	boolean isButtonPressed = false;
	int x=50, y=150;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.setColor(Color.black);
		
		if(x > getSize().getWidth()-25)
		{
			x=-25;
		}
		else if (x < -25)
		{
			x = getSize().width-25;
		}
		
		if(y > getSize().getHeight()-25)
		{
			y=-25;
		}
		else if (y < -25)
		{
			y = getSize().height-25;
		}
		
		g.drawOval(x, y, 50, 50);
		
		if(isButtonPressed)
		{
			g.fillOval(x, y, 50, 50);
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		isButtonPressed = true;
		
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			move(0, -10);
			break;
		case KeyEvent.VK_DOWN:
			move(0, 10);
			break;
		case KeyEvent.VK_LEFT:
			move(-10, 0);
			break;
		case KeyEvent.VK_RIGHT:
			move(10, 0);
			break;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isButtonPressed = false;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		isButtonPressed = true;
		repaint();
	}

	public void move(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
} // koniec klasy Panel


public class Okno extends JFrame implements ActionListener {

private static final long serialVersionUID = 1L;

private Panel panel = new Panel();

private JButton authorButton = new JButton("Autor");

private JMenuBar menuBar = new JMenuBar();
private JMenu plik = new JMenu("Plik");
private JMenuItem authorItem = new JMenuItem("Autor");
private JMenuItem closeItem = new JMenuItem("Zamknij");

private HashSet<Banknot> currency = new HashSet<>();
WidokKolekcji kolekcja = new WidokKolekcji(currency);
private JButton addMoney = new JButton("Dodaj walutê");
private JButton deleteMoney = new JButton("Usuñ");
private JButton deleteAllMoney = new JButton("Usuñ wszyskie");
private JLabel moneyLabel = new JLabel("Waluta");
private JTextField moneyField = new JTextField(5);
private JLabel valueLabel = new JLabel("Wartoœæ");
private JTextField valueField = new JTextField(5);

	public static void main(String[] args) {
		new Okno();
		Drukarka drukarka = new Drukarka();
		Producent producent_pierwszy = new Producent(drukarka, "Micha³", 1);
		Producent producent_drugi = new Producent(drukarka, "Polak", 2);
		
		producent_pierwszy.start();
		producent_drugi.start();
	}

	public Okno ()
	{
		super("Micha³ Polak");
		setSize(400,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		panel.add(authorButton);
		authorButton.addActionListener(this);
		
		setJMenuBar(menuBar);
		menuBar.add(plik);
		plik.add(authorItem);
		plik.add(closeItem);
		authorItem.addActionListener(this);
		closeItem.addActionListener(this);
		
		panel.add(moneyLabel);
		panel.add(moneyField);
		moneyField.setEditable(true);
		panel.add(valueLabel);
		panel.add(valueField);
		valueField.setEditable(true);
		panel.add(addMoney);
		addMoney.addActionListener(this);
		panel.add(deleteMoney);
		deleteMoney.addActionListener(this);
		panel.add(deleteAllMoney);
		deleteAllMoney.addActionListener(this);
		panel.add(kolekcja);
		
		panel.addKeyListener(panel);
		panel.setFocusable(true);
		
		setContentPane(panel);
		setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if(source == authorButton || source == authorItem)
		{
			JOptionPane.showMessageDialog(this, "Micha³ Polak 235046");
		}
		
		if(source == addMoney)
		{
			try 
			{
				String money = moneyField.getText();
				int value = Integer.parseInt(valueField.getText());
				
				if (money.length()>0)
				{
					currency.add(new Banknot(money, value));
					kolekcja.refresh();
				}
				else 
				{
					JOptionPane.showMessageDialog(this, "Pole musi zawieraæ nie pusty ci¹g znaków");
				}
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Pole wartosc musi zawieraæ liczbê ca³kowit¹");
			}
		}
		
		if(source == deleteMoney)
		{
			try 
			{
				String money = moneyField.getText();
				int value = Integer.parseInt(valueField.getText());
				
				if (money.length()>0)
				{
					currency.remove(new Banknot(moneyField.getText(), Integer.parseInt(valueField.getText())));
					kolekcja.refresh();
				}
				else 
				{
					JOptionPane.showMessageDialog(this, "Pole musi zawieraæ nie pusty ci¹g znaków");
				}
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Pole wartosc musi zawieraæ liczbê ca³kowit¹");
			}
		}
		
		if(source == deleteAllMoney)
		{
			currency.removeAll(currency);
			kolekcja.refresh();
		}
		
		if(source == closeItem)
		{
			System.exit(0);
		}
		
		panel.requestFocus();
	}

} // koniec klasy Okno


//KLASA POMOCNICZA DO ZADANIA NR 6
class Drukarka {

	volatile int id = 0; // id Producenta, który ostatnio korzysta³ z drukarki
	int nr = 0; // numer zadania (automatycznie incrementowany). 

// Proszê zmodyfikowaæ metodê drukuj zgodnie z wytycznymi do zadania 6.

	synchronized void drukuj(String tekst, int id) {
		if(this.id == id)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else 
		{
			System.out.println(nr++ + ": " + tekst);
			this.id = id;
		}
		notify();
	}
} // koniec klasy Drukarka


// KLASA POMOCNICZA DO ZADANIA NR 6
// W TEJ KLASIE NIE TRZEBA NIC MODYFIKOWAÆ!!!
class Producent extends Thread {	
	Drukarka drukarka;
	String tekst;
	int id;

	public Producent(Drukarka drukarka, String tekst, int id) {
		this.drukarka = drukarka;
		this.tekst = tekst;
		this.id = id;
	}

	@Override
	public void run() {
		Random random = new Random();
		while (true){
			drukarka.drukuj(tekst, id);
			try {
				sleep(500 + random.nextInt(3000));
			} catch (InterruptedException e) { }
		}
	}	
} // koniec klasy Producent