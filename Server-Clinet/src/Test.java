/*Autor: Micha³ Polak 235046
 * data wykonania: styczeñ 2018*/

public class Test 
{
	public static void main(String[] args)
    {
        PhoneBookServer serwer = new PhoneBookServer();

        String name = "Test";
        String host = "localhost";

        new PhoneBookClient(name, host);
    }
}
