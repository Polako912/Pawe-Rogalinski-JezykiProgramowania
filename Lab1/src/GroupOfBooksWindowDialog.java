import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia 
 *          modyfikacjê grup obiektów klasy Book.
 *    Plik: GroupOfBooksWindowDialog.java
 *          
 *   Autor: Micha³ Polak
 *    Data: listopad 2017 r.
 */

class ViewBooksList extends JScrollPane 
{
	private static final long serialVersionUID = 1L;
	
	private GroupOfBooks group;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewBooksList(GroupOfBooks group, int width, int height)
	{
		this.group = group;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista ksi¹¿ek:"));
		
		String[] tableHeader = { "Autor", "Tytu³", "Rok wydania", "Rodzaj ksi¹¿ki" };
		tableModel = new DefaultTableModel(tableHeader, 0);
		table = new JTable(tableModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex)
			{
				return false;
			}
		};
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	void refreshView()
	{
		tableModel.setRowCount(0);
		for (Book book : group) 
		{
			if (book != null) 
			{
				String[] row = {book.getBookAuthor(), book.getBookTitle(), ""+(book.getBookYear()), book.getType().toString()};
				tableModel.addRow(row);
			}
		}
	}
	
	int getSelectedIndex()
	{
		int index = table.getSelectedRow();
		if (index<0) 
		{
			JOptionPane.showMessageDialog(this, "¯adana ksi¹¿ka nie jest zaznaczona.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}

public class GroupOfBooksWindowDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String GREETING_MESSAGE=
			"Program do modyfikowania grup ksi¹zek"+
			"- wersja okienkowa \n"+
			"Micha³ Polak \n"+
			"listopad 2017 r. \n";
	
	private GroupOfBooks currentGroup;

	Font font = new Font ("MonoSpaced", Font.BOLD, 12);
	
	JLabel BookGroupNameLabel = new JLabel ("Nazwa grupy: ");
	JLabel BookCollectionTypeLabel = new JLabel ("Rodzaj kolekcji: ");
	
	JTextField BookGroupNameField = new JTextField (10);
	JTextField BookCollectionTypeField = new JTextField (10);
	
	JMenuBar menuBar = new JMenuBar();
	JMenu menuBookList = new JMenu("Lista ksi¹¿ek");
	JMenu menuSort = new JMenu("Sortowanie");
	JMenu menuProsperities = new JMenu ("W³aœciwoœci");
	JMenu menuAbout = new JMenu("O programie");
	
	JMenuItem menuNewBook = new JMenuItem("Dodaj now¹ ksi¹¿kê");
	JMenuItem menuEditBook = new JMenuItem("Edytuj ksi¹¿kê");
	JMenuItem menuDeleteBook= new JMenuItem("Usuñ ksi¹¿kê");
	JMenuItem menuLoadBook = new JMenuItem("Za³aduj ksi¹¿kê z pliku");
	JMenuItem menuSaveBook = new JMenuItem("Zapisz ksi¹¿kê do pliku");
	
	JMenuItem menuSortAlphabet = new JMenuItem("Sortowanie alfabetyczne");
	JMenuItem menuSortYear = new JMenuItem("Sortowanie wg roku wydania");
	JMenuItem menuSortType = new JMenuItem("Sortowanie wg rodzju ksi¹¿ki");
	
	JMenuItem menuChangeName = new JMenuItem ("Zmieñ nazwê");
	JMenuItem menuChangeCollection = new JMenuItem ("Zmieñ nazwê kolekcji");

	JMenuItem menuInfo = new JMenuItem("O programie");
	
	JButton buttonNewBook = new JButton("Dodaj now¹ ksi¹¿kê");
	JButton buttonEditBook = new JButton("Edytuj ksi¹¿kê");
	JButton buttonDeleteBook = new JButton("Usuñ ksi¹¿kê");
	JButton buttonLoadBook = new JButton("Wczytaj ksi¹¿kê z pliku");
	JButton buttonSaveBook = new JButton("Zapisz ksi¹¿kê do pliku");
	
	ViewBooksList listBook;
	
	public GroupOfBooksWindowDialog (Window parent, GroupOfBooks group)
	{
		super (parent, Dialog.ModalityType.DOCUMENT_MODAL);
		
		setTitle("Modyfikacja grup ksi¹zek");
		setSize(450, 420);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.currentGroup = group;
		
		BookGroupNameField.setEditable(false);
		BookCollectionTypeField.setEditable(false);
		BookGroupNameField.setText(currentGroup.getName());
		BookCollectionTypeField.setText(currentGroup.getType().toString());
		
		setJMenuBar(menuBar);
		menuBar.add(menuBookList);
		menuBar.add(menuSort);
		menuBar.add(menuProsperities);
		menuBar.add(menuAbout);
		
		menuBookList.add(menuNewBook);
		menuBookList.add(menuEditBook);
		menuBookList.add(menuDeleteBook);
		menuBookList.addSeparator();
		menuBookList.add(menuLoadBook);
		menuBookList.add(menuSaveBook);
		
		menuSort.add(menuSortAlphabet);
		menuSort.add(menuSortYear);
		menuSort.add(menuSortType);
		
		menuProsperities.add(menuChangeName);
		menuProsperities.add(menuChangeCollection);
		
		menuAbout.add(menuInfo);
		
		menuNewBook.addActionListener(this);
		menuEditBook.addActionListener(this);
		menuDeleteBook.addActionListener(this);
		menuLoadBook.addActionListener(this);
		menuSaveBook.addActionListener(this);
		menuSortAlphabet.addActionListener(this);
		menuSortYear.addActionListener(this);
		menuSortType.addActionListener(this);
		menuChangeName.addActionListener(this);
		menuChangeCollection.addActionListener(this);
		menuInfo.addActionListener(this);
		
		buttonNewBook.addActionListener(this);
		buttonEditBook.addActionListener(this);
		buttonDeleteBook.addActionListener(this);
		buttonLoadBook.addActionListener(this);
		buttonSaveBook.addActionListener(this);
		
		listBook = new ViewBooksList(group, 400, 250);
		listBook.refreshView();
		
		JPanel panel = new JPanel();
		
		panel.add(BookGroupNameLabel);
		panel.add(BookGroupNameField);
		panel.add(BookCollectionTypeLabel);
		panel.add(BookCollectionTypeField);
		panel.add(listBook);
		panel.add(buttonNewBook);
		panel.add(buttonEditBook);
		panel.add(buttonDeleteBook);
		panel.add(buttonLoadBook);
		panel.add(buttonSaveBook);
		
		setContentPane(panel);
		
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		try
		{
			if(source == buttonNewBook || source == menuNewBook)
			{
				Book book = WindowBookDialog.createNewBook(this);
				if(book!=null)
				{
					currentGroup.add(book);
				}
			}
		
			if(source == buttonEditBook || source == menuEditBook)
			{
				int index = listBook.getSelectedIndex();
				if (index >= 0)
				{
					Iterator <Book> iterator = currentGroup.iterator();
					while (index-->= 0)
						iterator.next();
					WindowBookDialog.changeBookData(this, iterator.next());
				}
			}
			if (source == buttonDeleteBook || source == menuDeleteBook)
			{
				int index = listBook.getSelectedIndex();
				if (index >= 0)
				{
					Iterator<Book> iterator = currentGroup.iterator();
					while (index--> 0)
						iterator.next();
					iterator.remove();
				}
			}
			if(source == buttonLoadBook || source == menuLoadBook)
			{
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					Book book = Book.readFromFileBin(chooser.getSelectedFile().getName());
					currentGroup.add(book);
				}
			}
			if(source == buttonSaveBook || source == menuSaveBook)
			{
				int index = listBook.getSelectedIndex();
				if (index >= 0) 
				{
					Iterator<Book> iterator = currentGroup.iterator();
					while (index--> 0)
						iterator.next();
					Book book = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						Book.printToFileBin( chooser.getSelectedFile().getName(), book );
					}
				}
			}
			if(source == menuSortAlphabet)
			{
				currentGroup.sortName();
			}
			if(source == menuSortYear)
			{
				currentGroup.sortYear();
			}
			if(source == menuSortType)
			{
				currentGroup.sortType();
			}
			if(source == menuChangeName)
			{
				String inputValue = JOptionPane.showInputDialog("Wpisz nazwê grupy");
				currentGroup.setName(inputValue);
				BookGroupNameField.setText(currentGroup.getName());
			}
			if(source == menuChangeCollection)
			{
				GroupType type = null;
				GroupOfBooks groupofbooks = null;
				type = (GroupType)JOptionPane.showInputDialog(null, "Wybierz typ kolekcji", "Typ kolekcji", JOptionPane.PLAIN_MESSAGE, null, GroupType.values(), null);
				currentGroup.setType(type);
				BookCollectionTypeField.setText(currentGroup.getType().toString());
			}
			if(source == menuInfo)
			{
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
		}
		catch (BookException e)
		{
				JOptionPane.showMessageDialog(this, e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
		}
		listBook.refreshView();
	}

	public static GroupOfBooks createNewGroupOfBooks(Window parent)
	{
		String name;
		GroupType type = null;
		name = JOptionPane.showInputDialog("Wpisz nazwê grupy");
		GroupOfBooks groupofbooks = null;
		if(name==null || name.equals(""))
		{
			return groupofbooks;
		}
		type = (GroupType)JOptionPane.showInputDialog(parent, "Wybierz typ kolekcji", "Typ kolekcji", JOptionPane.PLAIN_MESSAGE, null, GroupType.values(), null);
		try
		{
			groupofbooks = new GroupOfBooks (type, name);
		}
		catch (BookException e)
		{
			e.getMessage();
		}
		GroupOfBooksWindowDialog dialog = new GroupOfBooksWindowDialog (parent, groupofbooks);
		return dialog.currentGroup;
	}
}
