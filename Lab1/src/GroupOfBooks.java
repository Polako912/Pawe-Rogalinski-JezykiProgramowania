import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/*
 * Program: Klasa GroupOfBoks
 *          
 *    Plik: GroupOfBooks.java
 *
 *   Autor: Micha³ Polak
 *    Data: listopad 2017 r.
 *
 */

/**Klasa pozwalaj¹ca na definiowanie ró¿nych wartoœci dla grup obiektów, jest to typ wyliczeniowy.
 * 
 * @author Micha³ Polak
 *
 */
enum GroupType 
	{
		VECTOR("Lista   (klasa Vector)"),
		ARRAY_LIST("Lista   (klasa ArrayList)"),
		LINKED_LIST("Lista   (klasa LinkedList)"),
		HASH_SET("Zbiór   (klasa HashSet)"),
		TREE_SET("Zbiór   (klasa TreeSet)");

		/**Nazwa rodzaju kolekcji */
		String typeName;

		
		private GroupType(String type_name) 
		{
			typeName = type_name;
		}

		/**Przeddefiniowana metoda toString()*/
		@Override
		public String toString() 
		{
			return typeName;
		}

		/**Metoda sprawdza czy typ podany przez u¿ytkownika znajduje siê w klasie enum
		 * @param type_name nazwa typu kolekcji
		 * */	
		public static GroupType find(String type_name)
		{
			for(GroupType type : values())
			{
				if (type.typeName.equals(type_name))
				{
					return type;
				}
			}
			return null;
		}

		/**Tworzenie kolekcji*/
		public Collection<Book> createCollection() throws BookException 
		{
			switch (this) 
			{
			case VECTOR:      return new Vector<Book>();
			case ARRAY_LIST:  return new ArrayList<Book>();
			case HASH_SET:    return new HashSet<Book>();
			case LINKED_LIST: return new LinkedList<Book>();
			case TREE_SET:    return new TreeSet<Book>();
			default:          throw new BookException("Podany typ kolekcji nie zosta³ zaimplementowany.");
			}
		}

	}
	/**Klasa zajmuj¹ca siê operacajmi na kolekcjach*/
	public class GroupOfBooks implements Iterable<Book>, Serializable
	{

		private static final long serialVersionUID = 1L;
		
		/**Nazwa kolekcji*/
		private String name;
		/**Typ kolekcji*/
		private GroupType type;
		/**Kolekcja*/
		private Collection<Book> collection;

		/**Komstruktor*/
		public GroupOfBooks(GroupType type, String name) throws BookException 
		{
			setName(name);
			if (type==null)
			{
				throw new BookException("Nieprawid³owy typ kolekcji.");
			}
			this.type = type;
			collection = this.type.createCollection();
		}

		/**Konstruktor*/
		public GroupOfBooks(String type_name, String name) throws BookException 
		{
			setName(name);
			GroupType type = GroupType.find(type_name);
			if (type==null)
			{
				throw new BookException("Nieprawid³owy typ kolekcji.");
			}
			this.type = type;
			collection = this.type.createCollection();
		}

		/**Metoda zwraca nazwê kolekcji*/
		public String getName() 
		{
			return name;
		}
		
		/**Metoda ustawia zmienn¹ od u¿ytkownika na nazwe kolekcji*/
		public void setName(String name) throws BookException 
		{
			if ((name == null) || name.equals(""))
				throw new BookException("Nazwa grupy musi byæ okreœlona.");
			this.name = name;
		}

		/**Metoda zwraca typ kolekcji*/
		public GroupType getType() 
		{
			return type;
		}

		/**Metoda ustawia zmienn¹ od u¿ytkownika na typ kolekcji*/
		public void setType(GroupType type) throws BookException 
		{
			if (type == null) 
			{
				throw new BookException("Typ kolekcji musi byæ okreœlony.");
			}
			if (this.type == type)
				return;
			Collection<Book> oldCollection = collection;
			collection = type.createCollection();
			this.type = type;
			for (Book book : oldCollection)
				collection.add(book);
		}
		
		/**Metoda ustawia zmienn¹ od u¿ytkownika na*/
		public void setType(String type_name) throws BookException 
		{
			for(GroupType type : GroupType.values())
			{
				if (type.toString().equals(type_name)) 
				{
					setType(type);
					return;
				}
			}
			throw new BookException("Nie ma takiego typu kolekcji.");
		}
		
		/**Metoda dodaj¹ca ksi¹¿ki do kolekcji*/
		public boolean add(Book e) 
		{
			return collection.add(e);
		}

		/**Iterator kolekcji*/
		public Iterator<Book> iterator() 
		{
			return collection.iterator();
		}
		
		/**Zwraca rozmiar kolekcji*/
		public int size() 
		{
			return collection.size();
		}

		/**Metoda sortuj¹ca wed³ug nazwy ksi¹¿ki*/
		public void sortName() throws BookException 
		{
			if (type==GroupType.HASH_SET|| type==GroupType.TREE_SET )
			{
				throw new BookException("Kolekcje typu SET nie mog¹ byæ sortowane.");
			}
			Collections.sort((List<Book>) collection);
		}
		
		/**Metoda sortuj¹ca wed³ug roku wydania ksi¹¿ki*/
		public void sortYear() throws BookException 
		{
			if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
				throw new BookException("Kolekcje typu SET nie mog¹ byæ sortowane.");
			}
			Collections.sort((List<Book>) collection, new Comparator<Book>() 
			{
				@Override
				public int compare(Book o1, Book o2) 
				{
					if (o1.getBookYear() < o2.getBookYear())
						return -1;
					if (o1.getBookYear() > o2.getBookYear())
						return 1;
					return 0;
				}

			});
		}
		
		/**Metoda sortuj¹ca wed³ug rodzaju ksi¹¿ki*/
		public void sortType() throws BookException 
		{
			if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) 
			{
				throw new BookException("Kolekcje typu SET nie mog¹ byæ sortowane.");
			}
			Collections.sort((List<Book>) collection, new Comparator<Book>() 
			{

				@Override
				public int compare(Book o1, Book o2) 
				{
					return o1.getType().toString().compareTo(o2.getType().toString());
				}

			});
		}

		/**Graficzne przedstawienie koleckcji*/
		@Override
		public String toString() {
			return name + "  [" + type + "]";
		}

		/**Zapis do pliku
		 * @param writer obiekt klasy PrintWriter
		 * @param group obiekt klasy GroupOfBooks
		 * */
		public static void printToFile(PrintWriter writer, GroupOfBooks group) 
		{
			writer.println(group.getName());
			writer.println(group.getType());
			for (Book book : group.collection)
				Book.PrintToFile(writer, book);
		}

		/**Zpais do pliku
		 * @param file_name nazwa pliku
		 * @param group obiekt klasy GroupOfBooks
		 * 
		 * */
		public static void printToFile(String file_name, GroupOfBooks group) throws BookException 
		{
			try (PrintWriter writer = new PrintWriter(file_name)) 
			{
				printToFile(writer, group);
			} 
			catch (FileNotFoundException e)
			{
				throw new BookException("Nie odnaleziono pliku " + file_name);
			}
		}

		/**Odczyt z pliku
		 * @param reader obiekt klasy BufferedReader
		 * */
		public static GroupOfBooks readFromFile(BufferedReader reader) throws BookException
		{
			try {
				String group_name = reader.readLine();
				String type_name = reader.readLine();
				GroupOfBooks groupOfPeople = new GroupOfBooks(type_name, group_name);

				Book book;
				while((book = Book.ReadFromFile(reader)) != null)
					groupOfPeople.collection.add(book);
				return groupOfPeople;
			}
			catch(IOException e)
			{
				throw new BookException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
			}
		}

		/**Odczyt z pliku
		 * @param file_name nazwa pliku
		 * */
		public static GroupOfBooks readFromFile(String file_name) throws BookException 
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) 
			{
				return GroupOfBooks.readFromFile(reader);
			} 
			catch (FileNotFoundException e)
			{
				throw new BookException("Nie odnaleziono pliku " + file_name);
			} 
			catch(IOException e)
			{
				throw new BookException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
			}
		}

		/**Metoda tworz¹ca sumê
		 * @param g1 obiekt klasy GroupOfBooks
		 * @param g2 obiekt klasy GroupOfBooks
		 * */
		public static GroupOfBooks createGroupUnion(GroupOfBooks g1,GroupOfBooks g2) throws BookException 
		{
			String name = "(" + g1.name + " OR " + g2.name +")";
			GroupType type;
			if (g2.collection instanceof Set && !(g1.collection instanceof Set) )
			{
				type = g2.type;
			} 
			else 
			{
				type = g1.type;
			}
			GroupOfBooks group = new GroupOfBooks(type, name);
			group.collection.addAll(g1.collection);
			group.collection.addAll(g2.collection);
			return group;
		}
		
		/**Metoda tworz¹ca czêœc wspóln¹
		 * @param g1 obiekt klasy GroupOfBooks
		 * @param g2 obiekt klasy GroupOfBooks
		 * */
		public static GroupOfBooks createGroupIntersection(GroupOfBooks g1,GroupOfBooks g2) throws BookException 
		{
			String name = "(" + g1.name + " AND " + g2.name +")";
			GroupType type;
			if (g2.collection instanceof Set && !(g1.collection instanceof Set) )
			{
				type = g2.type;
			} 
			else 
			{
				type = g1.type;
			}
			GroupOfBooks group = new GroupOfBooks(type, name);
			group.collection.addAll(g1.collection);
			group.collection.retainAll(g2.collection);
			return group;
		}
		
		/**Metoda tworz¹ca ró¿nicê
		 * @param g1 obiekt klasy GroupOfBooks
		 * @param g2 obiekt klasy GroupOfBooks
		 * */
		public static GroupOfBooks createGroupDifference(GroupOfBooks g1,GroupOfBooks g2) throws BookException 
		{
			String name = "(" + g1.name + " SUB " + g2.name +")";
			GroupType type;
			if (g2.collection instanceof Set && !(g1.collection instanceof Set) )
			{
				type = g2.type;
			} else 
			{
				type = g1.type;
			}
			GroupOfBooks group = new GroupOfBooks (type, name);
			group.collection.addAll(g1.collection);
			group.collection.removeAll(g2.collection);
			return group;
		}

		/**Metoda tworz¹aca ró¿nicê symetryczn¹
		 * @param g1 obiekt klasy GroupOfBooks
		 * @param g2 obiekt klasy GroupOfBooks
		 * */
		public static GroupOfBooks createGroupSymmetricDiff(GroupOfBooks g1,GroupOfBooks g2) throws BookException 
		{
			String name = "(" + g1.name + " XOR " + g2.name +")";
			GroupType type;
			if (g2.collection instanceof Set && !(g1.collection instanceof Set) )
			{
				type = g2.type;
			} else 
			{
				type = g1.type;
			}

			GroupOfBooks group = new GroupOfBooks(type, name);
			group.collection.addAll(g1.collection);
			group.collection.addAll(g2.collection);
			GroupOfBooks g3 =  new GroupOfBooks(type, name);
			g3.collection.addAll(g1.collection);
			g3.collection.retainAll(g2.collection);
			group.collection.removeAll(g3.collection);
			return group;
		}
		
	}
