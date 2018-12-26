import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Book.
 *    Plik: WindowBookApp.java
 *          
 *   Autor: Micha³ Polak
 *    Data: pazdziernik 2017 r.
 */

public class WindowBookApp extends JFrame implements ActionListener 
{
	
	private static final long serialVersionUID = 1L;

	private static final String GREETING_MESSAGE = 
			"Program Book - wersja okienkowa\n" + 
	        "Autor: Micha³ Polak\n" + 
			"Data:  paŸdziernik 2017 r.\n";
	
	
	public static void main(String[] args) 
	{
		new WindowBookApp();
	}

	private Book CurrentBook;
	
	Font font = new Font ("MonoSpaced", Font.BOLD,12);
	
	JLabel BookTitleLabel = new JLabel ("Tytul ksiazki: ");
	JLabel BookAuthorLabel = new JLabel ("Autor ksiazki: ");
	JLabel BookYearLabel = new JLabel ("Rok wydania: ");
	JLabel TypeLabel = new JLabel ("Rodzaj: ");
	
	JTextField BookTitleField = new JTextField (10);
	JTextField BookAuthorField = new JTextField (10);
	JTextField BookYearField = new JTextField (10);
	JTextField TypeField = new JTextField (10);
	
	JButton newButton = new JButton ("Dodaj nowa ksiazke");
	JButton editButton = new JButton ("Edytuj ksiazke");
	JButton saveButton = new JButton ("Zapisz do pliku");
	JButton loadButton = new JButton ("Otworz z pliku");
	JButton SavebinButton = new JButton ("Zapisz do pliku bin");
	JButton LoadbinButton = new JButton ("Otworz z pliku bin");
	JButton deleteButton = new JButton ("Usun ksiazke");
	JButton infoButton = new JButton ("O aplikacji");
	JButton exitButton = new JButton ("Wyjscie");
	
	
	
	public WindowBookApp ()
	{
		setTitle ("WindowBookApp");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(270, 350);
		setResizable(false);
		setLocationRelativeTo(null);
		
		BookTitleLabel.setFont(font);
		BookAuthorLabel.setFont(font);
		BookYearLabel.setFont(font);
		TypeLabel.setFont(font);
		
		BookTitleField.setEditable(false);
		BookAuthorField.setEditable(false);
		BookYearField.setEditable(false);
		TypeField.setEditable(false);
		
		newButton.addActionListener(this);
		editButton.addActionListener(this);
		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		SavebinButton.addActionListener(this);
		LoadbinButton.addActionListener(this);
		deleteButton.addActionListener(this);
		infoButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		JPanel panel = new JPanel();
		
		panel.add(BookTitleLabel);
		panel.add(BookTitleField);
		
		panel.add(BookAuthorLabel);
		panel.add(BookAuthorField);
		
		panel.add(BookYearLabel);
		panel.add(BookYearField);
		
		panel.add(TypeLabel);
		panel.add(TypeField);
		
		panel.add(newButton);
		panel.add(editButton);
		panel.add(saveButton);
		panel.add(loadButton);
		panel.add(SavebinButton);
		panel.add(LoadbinButton);
		panel.add(deleteButton);
		panel.add(infoButton);
		panel.add(exitButton);
		
		setContentPane(panel);
		
		//JMenuBar
		
		JMenuBar menu = new JMenuBar();
		
		setJMenuBar(menu);
		
		JMenu fileMenu = new JMenu("Menu");
        
        menu.add(fileMenu);
        
        JMenuItem newButtonMenuItem = new JMenuItem("Nowa ksiazka");
        JMenuItem saveButtonMenuItem = new JMenuItem("Zapisz do pliku");
        JMenuItem loadButtonMenuItem = new JMenuItem("Otworz z pliku");
        JMenuItem SavebinButtonMenuItem = new JMenuItem("Zapisz do pliku bin");
        JMenuItem LoadbinButtonMenuItem = new JMenuItem("Otworz z pliku bin");
        JMenuItem deleteButtonMenuItem = new JMenuItem("Usun");
        JMenuItem infoButtonMenuItem = new JMenuItem("O programie");
        JMenuItem exitButtonMenuItem = new JMenuItem("Wyjscie");
        
        fileMenu.add(newButtonMenuItem);
        fileMenu.add(saveButtonMenuItem);
        fileMenu.add(loadButtonMenuItem);
        fileMenu.add(SavebinButtonMenuItem);
        fileMenu.add(LoadbinButtonMenuItem);
        fileMenu.add(deleteButtonMenuItem);
        fileMenu.add(infoButtonMenuItem);
        fileMenu.add(exitButtonMenuItem);
        
        newButtonMenuItem.addActionListener((ActionEvent event)->
        {
        	CurrentBook = WindowBookDialog.createNewBook(this);
        });
        
        saveButtonMenuItem.addActionListener((ActionEvent event)->
        {
        	try
        	{
        	JFileChooser chooser = new JFileChooser();
		    chooser.setCurrentDirectory(new File("/home/me/Documents"));
		    int retrival = chooser.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) 
		    {
		    	String filename = chooser.getSelectedFile().getAbsolutePath();
		    	Book.PrintToFile(filename, CurrentBook);
		    }
        	}
		    catch (BookException e)
		    {
		    	JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		    }
        });
        
        loadButtonMenuItem.addActionListener((ActionEvent event)->
        {	
        	try
        	{
        		JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	Book.ReadFromFile(fileName);
			    }
        	}
        	catch (BookException e)
		    {
		    	JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		    }
        	
        });
        
        SavebinButtonMenuItem.addActionListener((ActionEvent)->
        {
        	try
        	{
        		JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	Book.printToFileBin(fileName,CurrentBook);
			    }
        	}
        	catch (BookException e)
		    {
		    	JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		    }
        	
        });
        
        LoadbinButtonMenuItem.addActionListener((ActionEvent)->
        {
        	try
        	{
        		JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	Book.readFromFileBin(fileName);
			    }
        	}
        	catch (BookException e)
		    {
		    	JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		    }
        });
        
        deleteButtonMenuItem.addActionListener((ActionEvent event)->
        {	
        	CurrentBook = null;
        });
        
        infoButtonMenuItem.addActionListener((ActionEvent event)->
        {	
        	JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
        });
        
        exitButtonMenuItem.addActionListener((ActionEvent event)->
        {	
        	System.exit(0);
        });
        
        //koniec JMenuBar
        
		showCurrentBook();
		
		setVisible(true);
	}
		
		void showCurrentBook ()
		{
			if(CurrentBook==null)
			{
				BookTitleField.setText("");
				BookAuthorField.setText("");
				BookYearField.setText("");
				TypeField.setText("");
			}
			else 
			{
				BookTitleField.setText(CurrentBook.getBookTitle());
				BookAuthorField.setText(CurrentBook.getBookAuthor());
				BookYearField.setText("" + CurrentBook.getBookYear());
				TypeField.setText("" + CurrentBook.getType());
			}
		}
		
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object eventSource = event.getSource();
		
		try 
		{
			if((eventSource==newButton))
			{
				CurrentBook = WindowBookDialog.createNewBook(this);
			}
			if(eventSource==deleteButton)
			{
				CurrentBook = null;
			}
			if (eventSource==saveButton)
			{
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showSaveDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION) 
			    {
			    	String filename = chooser.getSelectedFile().getAbsolutePath();
			    	Book.PrintToFile(filename, CurrentBook);
			    }
	
			}
			if (eventSource==loadButton)
			{
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	CurrentBook=Book.ReadFromFile(fileName);
			    }
			}
			if(eventSource==SavebinButton)
			{
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	Book.printToFileBin(fileName,CurrentBook);
			    }
			}
			if(eventSource==LoadbinButton)
			{
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    int retrival = chooser.showOpenDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION)
			    {
			    	String fileName = chooser.getSelectedFile().getAbsolutePath();
			    	Book.readFromFileBin(fileName);
			    }
			}
			if (eventSource==editButton)
			{
				if(CurrentBook==null) throw new BookException("Zadna ksiazka nie zostala utworzona");
				{
					WindowBookDialog.changeBookData(this, CurrentBook);
				}
			}
			if (eventSource == infoButton) 
			{
					JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			if (eventSource == exitButton)
			{
					System.exit(0);
			}	
		} 
		catch (BookException e)
		{
				JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		}
		showCurrentBook();
	}
}