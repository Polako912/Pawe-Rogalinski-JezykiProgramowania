import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Program: Aplikacja okienkowa z GUI, kt�ra umo�liwia 
 *          zarz�dzanie grupami obiekt�w klasy Book.
 *    Plik: GroupManagerApp.java
 *          
 *   Autor: Micha� Polak
 *    Data: listopad 2017 r.
 */

public class GroupManagerApp extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
    private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN";
    
	private static final String GREETING_MESSAGE=
			"Program do zarz�dzania grupami ksi�zek"+
			"- wersja okienkowa \n"+
			"Micha� Polak \n"+
			"listopad 2017 r. \n";
	
	public static void main (String arg[])
	{
		new GroupManagerApp();
	}

	WindowAdapter windowListener = new WindowAdapter()
	{
		@Override
		public void windowClosed (WindowEvent e)
		{
			JOptionPane.showMessageDialog(null, "Program zako�czy� dzia�anie!");
		}
		
		@Override
		public void windowClosing (WindowEvent e)
		{
			windowClosed(e);
		}
	};
	
	private List<GroupOfBooks> currentList = new ArrayList<GroupOfBooks> ();
	
	JMenuBar menuBar = new JMenuBar();
	JMenu menuGroups = new JMenu("Grupy");
	JMenu menuSpecialGroups = new JMenu("Grupy specjalne");
	JMenu menuAbout = new JMenu("O programie");
	
	JMenuItem menuNewGroup = new JMenuItem("Utw�rz grup�");
	JMenuItem menuEditGroup = new JMenuItem("Edytuj grup�");
	JMenuItem menuDeleteGroup= new JMenuItem("Usu� grup�");
	JMenuItem menuLoadGroup = new JMenuItem("Za�aduj grup� z pliku");
	JMenuItem menuSaveGroup = new JMenuItem("Zapisz grup� do pliku");
	
	JMenuItem menuGroupUnion = new JMenuItem("Po��czenie grup");
	JMenuItem menuGroupIntersection = new JMenuItem("Cz�� wsp�lna grup");
	JMenuItem menuGroupDifference = new JMenuItem("Ro�nica grup");
	JMenuItem menuGroupSymmetricDiff = new JMenuItem("R�nica symetryczna grup");

	JMenuItem menuAuthor = new JMenuItem("Autor");
	
	JButton buttonNewGroup = new JButton("Utw�rz");
	JButton buttonEditGroup = new JButton("Edytuj");
	JButton buttonDeleteGroup = new JButton("Usu� ");
	JButton buttonLoadGroup = new JButton("Otw�rz");
	JButton buttonSaveGroup = new JButton("Zapisz");

	JButton buttonUnion = new JButton("Suma");
	JButton buttonIntersection = new JButton("Iloczyn");
	JButton buttonDifference = new JButton("R�nica");
	JButton buttonSymmetricDiff = new JButton("R�nica symetryczna");
	
	ViewGroupList viewList;
	
	public GroupManagerApp()
	{
		setTitle("GroupManager - zarz�dzanie grupami ksi��ek");
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			@Override 
			public void windowClosed (WindowEvent event)
			{
				try
				{
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Dane zosta�y zapisane do pliku " + ALL_GROUPS_FILE);
				}
				catch (BookException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
			@Override
			public void windowClosing(WindowEvent event)
			{
				windowClosed(event);
			}
			
		});
		
		try
		{
			loadGroupListFromFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + ALL_GROUPS_FILE);
		}
		catch (BookException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}
		
		setJMenuBar(menuBar);
		menuBar.add(menuGroups);
		menuBar.add(menuSpecialGroups);
		menuBar.add(menuAbout);
		
		menuGroups.add(menuNewGroup);
		menuGroups.add(menuEditGroup);
		menuGroups.add(menuDeleteGroup);
		menuGroups.addSeparator();
		menuGroups.add(menuLoadGroup);
		menuGroups.add(menuSaveGroup);
		
		menuSpecialGroups.add(menuGroupUnion);
		menuSpecialGroups.add(menuGroupIntersection);
		menuSpecialGroups.add(menuGroupDifference);
		menuSpecialGroups.add(menuGroupSymmetricDiff);
		
		menuAbout.add(menuAuthor);
		
		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuLoadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);
		menuGroupUnion.addActionListener(this);
		menuGroupIntersection.addActionListener(this);
		menuGroupDifference.addActionListener(this);
		menuGroupSymmetricDiff.addActionListener(this);
		menuAuthor.addActionListener(this);
		
		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonLoadGroup.addActionListener(this);
		buttonSaveGroup.addActionListener(this);
		buttonUnion.addActionListener(this);
		buttonIntersection.addActionListener(this);
		buttonDifference.addActionListener(this);
		buttonSymmetricDiff.addActionListener(this);
		
		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();
		
		JPanel panel = new JPanel();
		
		panel.add(viewList);
		panel.add(buttonNewGroup);
		panel.add(buttonEditGroup);
		panel.add(buttonDeleteGroup);
		panel.add(buttonLoadGroup);
		panel.add(buttonSaveGroup);
		panel.add(buttonUnion);
		panel.add(buttonIntersection);
		panel.add(buttonDifference);
		panel.add(buttonSymmetricDiff);
		
		setContentPane(panel);
		
		setVisible(true);
		
	}
	
	@SuppressWarnings("unchecked")
	void loadGroupListFromFile (String file_name) throws BookException
	{
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name)))
		{
			currentList=(List<GroupOfBooks>)in.readObject();
		}
		catch (FileNotFoundException e)
		{
			throw new BookException("Nie odnaleziono pliku " + file_name);
		}
		catch (Exception e)
		{
			throw new BookException ("Wyst�pi� b��d podczas odczytu z pliku");
		}
	}
	
	void saveGroupListToFile (String file_name) throws BookException
	{
		try (ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (file_name)))
		{
			out.writeObject(currentList);
		}
		catch (FileNotFoundException e)
		{
			throw new BookException("Nie odnaleziono pliku " + file_name);
		}
		catch (IOException e)
		{
			throw new BookException ("Wyst�pi� b��d podczas zapisu do pliku");
		}
	}
	
	private GroupOfBooks chooseGroup (Window parent, String message)
	{
		Object[] groups = currentList.toArray();
		GroupOfBooks group = (GroupOfBooks)JOptionPane.showInputDialog(
		                    parent, message,
		                    "Wybierz grup�",
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    groups,
		                    null);
		return group;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
		try
		{
			if (source == menuNewGroup || source == buttonNewGroup)
			{
				GroupOfBooks group = GroupOfBooksWindowDialog.createNewGroupOfBooks(this);
				
				if (group != null) 
				{
					currentList.add(group);
				}
			}
			
			if (source == menuEditGroup || source == buttonEditGroup)
			{
				int index = viewList.getSelectedIndex();
				if (index >= 0)
				{
					Iterator<GroupOfBooks> iterator = currentList.iterator();
					while (index--> 0)
						iterator.next();
					new GroupOfBooksWindowDialog(this, iterator.next());
				}
			}
			
			if (source == menuDeleteGroup || source == buttonDeleteGroup)
			{
				int index = viewList.getSelectedIndex();
				if (index >= 0)
				{
					Iterator<GroupOfBooks> iterator = currentList.iterator();
					while (index-->= 0)
						iterator.next();
					iterator.remove();
				}
			}
		
			if (source == menuLoadGroup || source == buttonLoadGroup)
			{
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					GroupOfBooks group = GroupOfBooks.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}
			
			if (source == menuSaveGroup || source == buttonSaveGroup)
			{
				int index = viewList.getSelectedIndex();
				if (index >= 0) 
				{
					Iterator<GroupOfBooks> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfBooks group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						GroupOfBooks.printToFile( chooser.getSelectedFile().getName(), group );
					}
				}
			}
			if (source == menuGroupUnion || source == buttonUnion)
			{
				String message1 = 
						"SUMA GRUP\n\n" + 
			            "Tworzenie grupy zawieraj�cej wszystkie ksi��ki z grupy pierwszej\n" + 
						"oraz wszystkie osoby z grupy drugiej.\n" + 
			            "Wybierz pierwsz� grup�:";
				String message2 = 
						"SUMA GRUP\n\n" + 
					    "Tworzenie grupy zawieraj�cej wszystkie ksi�zki z grupy pierwszej\n" + 
						"oraz wszystkie osoby z grupy drugiej.\n" + 
					    "Wybierz drug� grup�:";
				GroupOfBooks group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfBooks group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfBooks.createGroupUnion(group1, group2) );
			}
			if (source == menuGroupIntersection || source == buttonIntersection) {
				String message1 = 
						"ILOCZYN GRUP\n\n" + 
				        "Tworzenie grupy ksi��ek, kt�re nale�� zar�wno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" + 
				        "Wybierz pierwsz� grup�:";
				String message2 = 
						"ILOCZYN GRUP\n\n" + 
						"Tworzenie grupy os�b, kt�re nale�� zar�wno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" + 
						"Wybierz drug� grup�:";
				GroupOfBooks group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfBooks group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfBooks.createGroupIntersection(group1, group2) );
			}
			
			if (source == menuGroupDifference || source == buttonDifference) 
			{
				String message1 = 
						"RӯICA GRUP\n\n" + 
				        "Tworzenie grupy ksi��ek, kt�re nale�� do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" + 
				        "Wybierz pierwsz� grup�:";
				String message2 = 
						"RӯNICA GRUP\n\n" + 
						"Tworzenie grupy os�b, kt�re nale�� do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" + 
						"Wybierz drug� grup�:";
				GroupOfBooks group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfBooks group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfBooks.createGroupDifference(group1, group2) );
			}
			
			if (source == menuGroupSymmetricDiff || source == buttonSymmetricDiff) 
			{
				String message1 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej ksi�zki nal��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz pierwsz� grup�:";
				String message2 = "RӯNICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj�cej ksi�zki nale��ce tylko do jednej z dw�ch grup,\n"
						+ "Wybierz drug� grup�:";
				GroupOfBooks group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfBooks group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfBooks.createGroupSymmetricDiff(group1, group2) );
			}
			
			if (source == menuAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			
		} catch (BookException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}
		
		viewList.refreshView();
		
	}
	
}

class ViewGroupList extends JScrollPane 
{
	private static final long serialVersionUID = 1L;
	
	private List<GroupOfBooks> list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewGroupList(List<GroupOfBooks> list, int width, int height)
	{
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));
		
		String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba ksi��ek" };
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
	
	void refreshView(){
		tableModel.setRowCount(0);
		for (GroupOfBooks group : list) {
			if (group != null) 
			{
				String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
				tableModel.addRow(row);
			}
		}
	}
	
	int getSelectedIndex()
	{
		int index = table.getSelectedRow();
		if (index<0) 
		{
			JOptionPane.showMessageDialog(this, "�adana grupa nie jest zaznaczona.", "B��d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}