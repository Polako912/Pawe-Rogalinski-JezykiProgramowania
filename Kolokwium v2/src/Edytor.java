import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*=======  TU PROSZE SIE PODPISAC ==============================
 *      Imie:
 *  Nazwisko:
 *Nr indeksu:
 *==============================================================
 */

class Odcinek extends Rysunek 
{
	private static final long serialVersionUID = 1L;

	private int x1, y1, x2, y2;

	/*
	 * Konstruktor tworz¹cy nowy obiekt Odcinek px, py - wspó³rzêdne pocz¹tku
	 * odcinka kx, ky - wspó³rzêdne koñca odcinka
	 */
	public Odcinek(int px, int py, int kx, int ky) {
		x1 = px;
		y1 = py;
		x2 = kx;
		y2 = ky;
	}

	public void przesun(int dx, int dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}

	public void rysuj(Graphics g) {
		g.drawLine(x1, y1, x2, y2);
	}
}

class Rysunek extends JPanel implements KeyListener, MouseListener, MouseMotionListener, Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	
	int x;
	
	int mouseX, mouseY, mouseXlast, mouseYlast;
	
	private LinkedList<Odcinek> paintings = new LinkedList<>();
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.YELLOW);
		g.fillOval(x, 200, 25, 25);
		
		if(paintings!=null)
		{
			for(Odcinek line : paintings)
			{
				g.setColor(Color.BLACK);
				line.rysuj(g);
			}
		}
	}

	public void actionPerformed(ActionEvent evt) {

	}
	
	// ***obs³uga zdarzeñ przez s³uchacza zdarzeñ KeyListener ***
	public void keyPressed(KeyEvent e) 
	{
		int source = e.getKeyCode();
					
		for (Odcinek odcinek : paintings)
			{
				if(source == KeyEvent.VK_UP)
				{
					odcinek.przesun(0,-10);
				}
						
				if(source == KeyEvent.VK_DOWN)
				{
					odcinek.przesun(0, 10);
				}
						
				if(source == KeyEvent.VK_LEFT)
				{
					odcinek.przesun(-10, 0);
				}
						
				if(source == KeyEvent.VK_RIGHT)
				{
					odcinek.przesun(10, 0);
				}
				repaint();
			}
		}

	public void keyReleased(KeyEvent evt) {
				
	}

	public void keyTyped(KeyEvent evt) 
	{
		if(evt.getKeyChar()=='k' || evt.getKeyChar()=='K')
			{
				paintings.removeAll(paintings);
			}
	}
				// ***obs³uga zdarzeñ przez s³uchacza zdarzeñ MouseListener
	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
						
	}

	public void mouseExited(MouseEvent e) {
					
	}

	public void mousePressed(MouseEvent e) 
	{
		if(e.isShiftDown())
		{
			mouseX = mouseXlast;
			mouseY = mouseYlast;
		}
		else 
		{			
			mouseX = e.getX();
			mouseY = e.getY();	
		}
		repaint();
	}

	public void mouseReleased(MouseEvent e) 
	{
		mouseXlast = e.getX();
		mouseYlast = e.getY();
		Odcinek line = new Odcinek(mouseX, mouseY, mouseXlast, mouseYlast);
		paintings.add(line);
		repaint();
	}

	// ***obs³uga zdarzeñ przez s³uchacza zdarzeñ MouseMotionListener***
	public void mouseDragged(MouseEvent e) {
		repaint();
	}
	
	public void mouseMoved(MouseEvent e) {
		repaint();
	}
				
	public void move(int pozX, int pozY)
	{
					
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
			if(x>375)
			{
				x=-25;
			}
			x++;
			repaint();
		}
	}

	public int getListSize() 
	{
		return paintings.size();
	}

	public void usun() 
	{
		paintings.removeAll(paintings);
		repaint();
	}
}

public class Edytor extends JFrame implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;

	private final String OPIS = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n" + "   strzalki   ==> przesuwanie rysunku\n"
			+ "   k , K  ==> kasowanie rysunku\n" + "\nOperacje myszka:\n" + "   przeciaganie   ==>  rysowanie lini\n"
			+ "   SHIFT + przeciaganie   ==>  zakreœlanie pola"
			+ "\nWIDAC ZE KOLO SIE PORUSZA NA EKRANIE TYLKO GDY RUSZA SIE MYSZKA";
	
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("Plik");
	JMenuItem author = new JMenuItem("Autor");
	JMenuItem info = new JMenuItem("Opis programu");
	JMenuItem close = new JMenuItem("Zamknij");
	
	JTextField ilosc = new JTextField(10);
	JButton DeleteButton = new JButton("Usuñ");
	
	private Rysunek panel = new Rysunek();
	
		public Edytor()
		{
			setTitle("Micha³ Polak");
			setSize(400, 400);
			setResizable(false);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			setJMenuBar(menuBar);
			menuBar.add(file);
			file.add(author);
			file.add(info);
			file.add(close);
			
			ilosc.setEditable(false);
			panel.add(ilosc);
			panel.add(DeleteButton);
			DeleteButton.addActionListener(this);
			
			panel.addMouseListener(panel);
			panel.addMouseMotionListener(panel);
			panel.addKeyListener(panel);
			panel.setFocusable(true);
			
			author.addActionListener(this);
			info.addActionListener(this);
			close.addActionListener(this);
			
			setContentPane(panel);
			setVisible(true);
			
			new Thread(this).start();
			panel.run();
		}
	
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();
			if(source == author)
			{
				JOptionPane.showMessageDialog(this, "Micha³ Polak 235046");
			}
			
			if(source == info)
			{
				JOptionPane.showMessageDialog(this, OPIS);
			}
			
			if(source == close)
			{
				System.exit(0);
			}
			
			if(source == DeleteButton)
			{
				panel.usun();
			}
			
			panel.requestFocus();
		}

	public static void main(String[] args) 
	{
		new Edytor();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			ilosc.setText(""+panel.getListSize());
		}
		
	}
}