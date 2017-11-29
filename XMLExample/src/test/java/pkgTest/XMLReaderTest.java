package pkgTest;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pkgException.*;
import pkgLibrary.*;
import pkgMain.*;

public class XMLReaderTest {
	
	@Before
	public void setUp() throws Exception{
	}
	
	//Create BookIDs
	static String BookID1 = "bk101";
	
	@Test(expected = BookException.class)
	public void testgetBook() throws BookException{
		
		assertTrue(XMLReader.getBook("BookID1").getId() == "bk101");
		XMLReader.getBook("BookID2");
	}
	
	static Book Book1 = new Book();
	static Book Book2 = new Book();
	static int CatalogID = XMLReader.ReadCatalog().getId();
	
	@Before
	public void setUp1() throws Exception{
	
	//publish dates
	Calendar cal1 = Calendar.getInstance();	
	cal1.set(2000,9,1); 
	Date published = cal1.getTime();
	
	//create instances of books
	Book Book1 = new Book("bk101",35.96,"Gambardella, Matthew","XML Developer's Guide",
			"Computer",44.95,published,"An in-depth look at creating applications\r\n" + 
			"with XML.");
	Book Book2 = new Book("bk118",35.96,"Doe, John","ABCD","Childrens",44.95,
			published,"Yes");
	}
	
	@Test(expected = BookException.class)
	public void testAddBook() throws BookException{
		
		XMLReader.AddBook(CatalogID,Book1);
		XMLReader.AddBook(CatalogID,Book2);
	}
}
