import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/*
 * Program: Klasa Book.
 *    Plik: Book.java
 *          
 *   Autor: Micha³ Polak
 *    Data: pazdziernik 2017 r.
 */

/**
 * Klasa pozwalaj¹ca na definiowanie ró¿nych wartoœci dla obiektów, jest to typ wyliczeniowy.
 * 
 *  @author Micha³ Polak
 */

enum BookType

{
	UNKNOWN("---------"),
	COMEDY("Komedia"),
	THRILLER("Thriller"),
	FANTASY("Fantazy"),
	CUSTOM("Obyczajowa"),
	CRIMINAL("Kryminal");
	
	/** rodzaj ksi¹¿ki. */
	String typename;
	
	
	private BookType(String type_name)
	{
		typename=type_name;
	}
	
	/** przedefiniowana metoda toString() */
	@Override
	public String toString()
	{
		return typename;
	}
}

/** defenicja klasy wyj¹tków dla klasy Book */
class BookException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public BookException(String message) 
	{
		super(message);
	}
}
/**Definicja klasy Book z mo¿liwoœciami zapisu, odczytu z pliku tekstowego/binarnego, pobierania zmiennych od u¿ytkownika i modyfikowania ich 
 * 
 * @author Micha³ Polak
 *
 */
public class Book implements java.io.Serializable, Comparable<Book>
{
	private static final long serialVersionUID = 1L;
	
	/**Tytu³ ksi¹¿ki */
	private String BookTitle;
	/**Autor ksi¹¿ki */
	private String BookAuthor;
	/**Rok wydania ksi¹¿ki */
	private int BookYear;
	/**Rodzaj ksi¹¿ki */
	private BookType type;
	
	/** Konstruktor*/
	public Book(String BookTitle, String BookAuthor) throws BookException 
	{
		SetBookTitle(BookTitle);
		SetBookAuthor(BookAuthor);
		type = BookType.UNKNOWN;
	}
	/**Metoda zwraca tytu³ ksi¹¿ki*/
	public String getBookTitle()
	{
		return BookTitle;
	}
	
	/** Metoda ustawai zmienn¹ od u¿ytkownika na tytu³ ksi¹¿ki i sprawdza czy jest zgodna z jej za³o¿eniem.*/
	public void SetBookTitle(String book_title) throws BookException
	{
		if((book_title==null)||(book_title.equals("")))
				{
					throw new BookException("Pole <Tytul ksiazki> nie moze byc puste");
				}
		this.BookTitle=book_title;
	}
	
	/**Metoda zwraca autora ksi¹¿ki*/
	public String getBookAuthor()
	{
		return BookAuthor;
	}
	
	/** Metoda ustawia zmienn¹ od u¿ytkownika na autor ksi¹¿ki i sprawdza czy jest zgodna z jej za³o¿eniem*/
	public void SetBookAuthor(String book_author) throws BookException
	{
		if((book_author==null)||(book_author.equals("")))
		{
			throw new BookException("Pole <Autor ksaizki> nie moze byc puste");
		}
		this.BookAuthor=book_author;
	}
	
	/**Metoda zwraca rok wydnania ksi¹¿ki*/
	public int getBookYear()
	{
		return BookYear;
	}
	
	/** Metoda ustawia zmienn¹ od u¿ytkownika na reok wydanai ksi¹¿ki i sprawdza czy jest zgodna z jej za³o¿eniem*/
	public void SetBookYear (int book_year) throws BookException
	{
		if((book_year!=0)&&(book_year<0)||(book_year>2017))
		throw new BookException("Pole <Rok wydania> musi zawierac sie w przedziale od 0 do 2017");
		this.BookYear=book_year;
	}
	
	/** Metoda sprawdza czy zmienna podana od u¿ytkownika nie jest ci¹giem pustych znaków i czy jest liczb¹ ca³kowit¹*/
	public void SetBookYear (String book_year) throws BookException
	{
		if((book_year==null)||(book_year.equals("")))
		{
			 SetBookYear(0);
			 return;
		}
		try
		{
			SetBookYear (Integer.parseInt(book_year));
		}
		catch (NumberFormatException e) 
		{
			throw new BookException ("Rok wydania musi byc liczba calkowita");
		}
	}
	
	/**Metoda zwraca rodzaj ksi¹¿ki*/
	public BookType getType () 
	{
		return type;
	}
	
	/** Metoda ustawia zmienn¹ od u¿ytkownika na rodzaj ksi¹¿ki*/
	public void setType (BookType type)
	{
		this.type=type;
	}
	
	/** Metoda ustawia zmienn¹ od u¿ytkownika na rodzaj ksi¹¿ki i sprawdza czy jest zgodna z jej za³o¿eniem i porównuje czy rodzaj ksi¹zki podany
	 * od u¿ytkowanika jest w klasie enum*/
	public void SetType(String book_type) throws Exception
	{
		if ((book_type==null) || (book_type.equals("")))
				{
					this.type=BookType.UNKNOWN;
					return;
				}
		for (BookType type : BookType.values())
		{
			if(type.typename.equals(book_type))
			{
				this.type=type;
				return;
			}
		}
		throw new BookException ("Nie ma takieo typu ksiazki");
	}
	
	/**Metoda zwraca posatæ obiektu ksi¹¿ki w formie tekstowej  */
	@Override
	public String toString()
	{  
		return BookTitle + " " + BookAuthor;
	}
	
	/**przedefiniowana metoda compareTo() */
	@Override
	public int compareTo(Book book)
	{
		int compare = BookTitle.compareTo(book.BookTitle);
		if (compare == 0)
		{
			return BookAuthor.compareTo(book.BookAuthor);
		}
		else
		return compare;
	}
	
	/**przedefiniowana metoda hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BookAuthor == null) ? 0 : BookAuthor.hashCode());
		result = prime * result + ((BookTitle == null) ? 0 : BookTitle.hashCode());
		result = prime * result + BookYear;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	/**przedefiniowana metoda equals() */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (BookAuthor == null) 
		{
			if (other.BookAuthor != null)
				return false;
		} 
		else if (!BookAuthor.equals(other.BookAuthor))
			return false;
		if (BookTitle == null) 
		{
			if (other.BookTitle != null)
				return false;
		} 
		else if (!BookTitle.equals(other.BookTitle))
			return false;
		if (BookYear != other.BookYear)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	 /**Zapis do pliku 
	  * @param writer obiekt klasy PrintWriter 
	  * @param book obiekt klasy Book
	  * */
	public static void PrintToFile (PrintWriter writer, Book book)
	{
		writer.println(book.BookAuthor+ "#" + book.BookTitle + "#"+ book.BookYear + "#" + book.type);
	}
	
	/** Zapis do pliku 
	 * 
	 * @param file_name String nazwa pliku do którego zapisany jest obiekt
	 * @param book obiekt klasy Book
	 * @throws BookException wyj¹tek klasy BookException
	 */
	public static void PrintToFile (String file_name, Book book) throws BookException
	{
		try (PrintWriter writer = new PrintWriter(file_name))
		{
			PrintToFile(writer, book);
		} catch (FileNotFoundException e)
		{
			throw new BookException("Nie odnaleziono pliku " + file_name);
		}
	}
	
	/** Odzcyt z pliku
	 * 
	 * @param reader obiekt klasy BufferedReader
	 * @return
	 * @throws BookException wyj¹tek klasy BookException
	 */
	public static Book ReadFromFile (BufferedReader reader) throws BookException
	{
		
		try
		{
			String line=reader.readLine();
			String[] txt =line.split("#");
			Book book = new Book (txt[0], txt[1]);
			book.SetBookYear(txt[2]);
			book.SetType(txt[3]);
			return book;
		}
		catch (Exception e)
		{
			throw new BookException ("Wystapil blad prz odczycie z pliku");
		}
	}
	
	/**Odczyt z pliku
	 * 
	 * @param file_name String nazwa pliku z kótrego jest odczyt danych
	 * @return
	 * @throws BookException wyj¹tek klasy BookException
	 */
	public static Book ReadFromFile(String file_name) throws BookException
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) 
		{
			return Book.ReadFromFile(reader);
		}
		catch (FileNotFoundException e)
		{
			throw new BookException ("Nie znalaziono pliku");
		}
		catch (IOException e)
		{
			throw new BookException ("Wysatpil blad podczas odczytu danych");
		}
	}
	
	/** Zapis do pliku binarengo
	 * 
	 * @param file_name String nazwa pliku do którego zapisany jest obiekt
	 * @param book obiekt klasy Book
	 * @throws BookException wyj¹tek klasy BookException
	 */
	public static void printToFileBin(String file_name, Book book)throws BookException 
	{
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) 
		{
			outputStream.writeObject(book.BookTitle + "#" + book.BookAuthor + "#" + book.BookYear + "#" + book.type);
			outputStream.close();
		} 
		catch (IOException e) 
		{
			throw new BookException("Wyst¹pi³ b³¹d");
		}
	}
	 
	/**Odczyt z pliku binarnego
	 * 
	 * @param file_name String nazwa pliku z kótrego jest odczyt danych
	 * @return
	 * @throws BookException wyj¹tek klasy BookException
	 */
	public static Book readFromFileBin(String file_name) throws BookException 
	{
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file_name))) 
		{
			String line = ("" + objectInputStream.readObject());
			String []txt = line.split("#");
			Book book = new Book(txt[0],txt[1]);
			book.SetBookYear(txt[2]);
			book.SetType(txt[3]);
			return book;
		} catch (Exception e) 
		{
			throw new BookException("Wyst¹pi³ b³¹d");
		} 
	}

}