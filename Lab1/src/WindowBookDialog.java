import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Program: Klasa implementuj¹ca interfejs okienka.
 *    Plik: WindowBookDialog.java
 *          
 *   Autor: Micha³ Polak
 *    Data: pazdziernik 2017 r.
 */

public class WindowBookDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private Book book;
	
	Font font = new Font ("MonoSpaced", Font.BOLD, 12);
	
	JLabel BookTilteLabel = new JLabel ("Tytul ksiazki: ");
	JLabel BookAuthorLabel = new JLabel ("Autor ksiazki: ");
	JLabel BookYearLabel = new JLabel ("Rok wydania: ");
	JLabel TypeLabel = new JLabel ("Rodzaj ksaizki: ");
	
	JTextField BookTitleField = new JTextField(10);
	JTextField BookAuthorField = new JTextField(10);
	JTextField BookYearField = new JTextField(10);
	JComboBox<BookType> TypeBox = new JComboBox <BookType> (BookType.values());
	
	JButton OKButton = new JButton ("OK");
	JButton CancelButton = new JButton ("Anuluj");
	
private WindowBookDialog (Window parent, Book book)
{	
	super (parent, Dialog.ModalityType.DOCUMENT_MODAL);
	
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setSize(220, 200);
	setLocationRelativeTo(parent);
	
	this.book=book;
	
	if(book==null)
	{
		setTitle("Nowa ksiazka");
	}
	else 
	{
		setTitle(book.toString());
		BookTitleField.setText(book.getBookTitle());
		BookAuthorField.setText(book.getBookAuthor());
		BookYearField.setText("" + book.getBookYear());
		TypeBox.setSelectedItem("" + book.getType());
	}
	OKButton.addActionListener( this );
	CancelButton.addActionListener( this );
	
	JPanel panel = new JPanel();
	
	panel.setBackground(Color.green);

	panel.add(BookAuthorField);
	panel.add(BookAuthorLabel);
	
	panel.add(BookTitleField);
	panel.add(BookTilteLabel);
	
	panel.add(BookYearField);
	panel.add(BookYearLabel);
	
	panel.add(TypeLabel);
	panel.add(TypeBox);
	
	panel.add(OKButton);
	panel.add(CancelButton);
	
	setContentPane(panel);
	
	setVisible(true);
}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object source = event.getSource();
		
			if (source == OKButton) 
			{
				try 
				{
					if(book==null)
					{
						book = new Book (BookTitleField.getText(), BookAuthorField.getText());
					}
					else
					{
						book.SetBookTitle(BookTitleField.getText());
						book.SetBookAuthor(BookAuthorField.getText());
					
					book.SetBookYear(BookYearField.getText());
					book.setType((BookType) TypeBox.getSelectedItem());
					dispose();
					}
				}
				catch (BookException e)
				{
					JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (source == CancelButton)
			{
				dispose();
			}
	}
	public static Book createNewBook(Window parent) 
	{
		WindowBookDialog dialog = new WindowBookDialog (parent, null);
				return dialog.book;
	}
	public static void changeBookData (Window parent, Book book)
	{
		new WindowBookDialog(parent, book);
	}
}
