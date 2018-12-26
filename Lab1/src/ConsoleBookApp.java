import java.util.Arrays;

/*
 * Program: Aplikacja dzia³aj¹ca w oknie konsoli, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Book.
 *    Plik: ConsoleBookApp.java
 *          
 *   Autor: Micha³ Polak
 *    Data: pazdziernik 2017 r.
 */

public class ConsoleBookApp 
{
	private static final String GREETING_MESSAGE=
		 "Program Book - wersja konsolowa\n" + 
			        "Autor: Micha³ Polak\n" + 
					"Data:  paŸdziernik 2017 r.\n";
	
 	private static final String MENU =
 			"MENU GLOWNE \n" +
 			"1 - Podaj dane nowej ksiazki \n" +
 			"2 - Usun kasizke \n" +
 			"3 - Modyfikuj ksiazke \n" +
 			"4 - Wczytaj dane z pliku \n" +
 			"5 - Zapisz dane do pliku \n" +
 			"6 - Wczytaj z pliku bin \n" +
 			"7 - Zapisz do pliku bin \n" +
 			"0 - Zakoncz program \n";
 
 	private static final String CHANGE_MENU=
 				"Co zmienic \n"+
 		"1 - Tytul \n"+
 				"2 - Autor \n"+
 		"3 - Rok wydania \n"+
 				"4 - Rodzaj ksiazki \n"+
 		"5 - Powrot do menu glownego \n";
 	
 	private static ConsoleWindow UI = new ConsoleWindow();
 	
 	public static void main (String [] arg)
 	{
 		ConsoleBookApp application = new ConsoleBookApp();
 		application.runMainLoop();
 	}
 	
 	private Book CurrentBook = null;
 	
 	public void runMainLoop()
 	{
 		UI.printMessage(GREETING_MESSAGE);
 		
 		while (true)
 		{
 			UI.clearConsole();
 			showCurrentBook();
 			
 			try 
 			{
 				switch (UI.enterInt(MENU + "=>"))
 				{
 				case 1:
 					CurrentBook=CreateNewBook();
 					break;
 	
 				case 2:
 					CurrentBook=null;
 					UI.printInfoMessage("Dane aktualnej ksiazki zostly usuniete");
 					break;
 						
 				case 3:
 					if(CurrentBook==null) throw new BookException ("Zadna ksiazka nie zostala utworzona");
 					changeBookData(CurrentBook);
 					break;
 				
 				case 4:
 					String file_name = UI.enterString("Podaj nazwe pliku: ");
 					CurrentBook=Book.ReadFromFile(file_name);
 					UI.printInfoMessage("Dane ksiazki zosatly wczytane z pliku" + file_name);
 					break;
 					
 				case 5:
 					String file_name1 = UI.enterString("Podaj nazwe pliku: ");
 					Book.PrintToFile(file_name1, CurrentBook);
 					UI.printInfoMessage("Dane aktualnej ksiazki zosatly zapisane do pliku" + file_name1);
 					break;
 				
 				case 6:
 					String file_name11=UI.enterString("Podaj nazwe pliku: ");
 					Book.readFromFileBin(file_name11);
 					UI.printInfoMessage("Dane ksiazki zosatly wczytane z pliku" + file_name11);
 					break;
 				
 				case 7:
 					String file_name111 = UI.enterString("Podaj nazwe pliku: ");
					Book.printToFileBin(file_name111, CurrentBook);
					UI.printInfoMessage("Dane aktualnej ksiazki zosatly zapisane do pliku" + file_name111);
					break;
 					
 				case 0:
 					UI.printInfoMessage("\nProgram zakoñczy³ dzia³anie!");
					System.exit(0);
 				}
 			}
 			catch (BookException e)
 			{
 				UI.printErrorMessage(e.getMessage());
 			}
 		}
 	}
 	void showCurrentBook()
 	{
 		showBook(CurrentBook);
 	}
 	static void showBook(Book book)
 	{
 		StringBuilder sb = new StringBuilder();
 		
 		if(book!=null)
 		{
 			sb.append("Aktualna ksaizka: \n");
 			sb.append("Tytul: " + book.getBookTitle() + "\n");
 			sb.append("Autor: " + book.getBookAuthor() + "\n");
 			sb.append("Rok wydania: " + book.getBookYear() + "\n");
 			sb.append("Typ ksiazki: " + book.getType() + "\n");
 		}
 		else 
 		{
 			sb.append("Brak danych ksiazki" + "\n");
 			UI.printMessage( sb.toString() );
 		}
 	}
 	
 	static Book CreateNewBook()
 	{
 		String BookTitle = UI.enterString("Podaj tytul: ");
 		String BookAuthor = UI.enterString("Podaj autora: ");
 		String BookYear = UI.enterString("Podaj rok wydania: ");
 		UI.printMessage("Dozwolone typy: "+ Arrays.deepToString(BookType.values()));
 		String book_type = UI.enterString("Podaj typ ksiazki: ");
 		
 		Book book;
 		
 		try 
 		{
 			book = new Book (BookTitle, BookAuthor);
 			book.SetBookYear(BookYear);
 			try
 			{
 			book.SetType(book_type);
 			}
 			catch (Exception e)
 			{
 				throw new BookException ("Nie ma takiego typu ksiazki");
 			}
 		}
 		catch (BookException e)
 		{
 			UI.printErrorMessage(e.getMessage());
			return null;
 		}
 		return book;
 	}
 	
 	static void changeBookData(Book book)
 	{
 		while (true)
 		{
 			UI.clearConsole();
			showBook(book);
			try 
			{
				switch (UI.enterInt(CHANGE_MENU + "=>"))
				{
				case 1:
					book.SetBookTitle(UI.enterString("Podaj tytul ksaizki: "));
					break;
				case 2:
					book.SetBookAuthor(UI.enterString("Podaj autora ksaizki: "));
					break;
				case 3:
					book.SetBookYear(UI.enterString("Podaj rok wydania: "));
					break;
				case 4:
					UI.printMessage("Dozwolone typy:" + Arrays.deepToString(BookType.values()));
					try 
					{
					book.SetType(UI.enterString("Podaj rodzaj ksiazki: "));
					}
					catch (Exception e)
					{
						throw new BookException ("Nie ma takiego typu ksiazki");
					}
					break;
				case 0:
					return;
				}
			}
			catch (BookException e)
			{
				UI.printErrorMessage(e.getMessage());
			}
 		}
 	}
}
