package pkgMain;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pkgException.BookException;
import pkgLibrary.Book;
import pkgLibrary.Catalog;

public class XMLReader {

	public static void main(String[] args) {

		Catalog cat = null;
		
		//	Read the XML catalog into 'cat'
		cat = ReadCatalog();
		
		//	Increase the price of each book
		IncreasePrice(cat,0.10);
		
		//Set initial cost at 80% of price
		SetCost(cat,0.80);
		
		//	Write the XML file from 'cat' object
		WriteXMLFile(cat);
		
	}


	private static Catalog SetCost(Catalog cat,double cost) {
		//Set initial cost
		for (Book b : cat.getBooks()) {
			double newCost = (b.getPrice() * cost);			
			b.setPrice(Math.round(newCost * 100.0) / 100.0);
		}
		return cat;
	}
	
	public static Catalog ReadCatalog() {
		Catalog cat = ReadXMLFile();
		
		System.out.println("cat ID " + cat.getId());
		System.out.println("Book count: " + cat.getBooks().size());

		return cat;		
	}

	public static Book getBook(String id) throws BookException{
		Catalog cat = ReadCatalog();
		int i = 0;
		Book Book1 = null;
		
		for(Book b : cat.getBooks()) {
			if(b.getId() == id) {
				i++;
				Book1 = b;
			}
		}
		if(i == 0) {
			throw new BookException("This book doesn't exist");
		}
		else {
			return Book1;
		}
	}
	
	public static void AddBook(int CatalogID, Book d) throws BookException{
		Catalog cat = ReadCatalog();
		cat.setId(CatalogID);
		int i = 0;
		
		for (Book b : cat.getBooks()) {
			if (b.getId() == d.getId()) {
				i++;
				throw new BookException("This book is already in the catalog");
			}
		}
		if(i == 0) {
			ArrayList<Book> Books = cat.getBooks();
			Books.add(d);
			cat.setBooks(Books);
			WriteXMLFile(cat);
		}	
	}
	
	private static Catalog IncreasePrice(Catalog cat, double PriceIncrease)
	{
		for (Book b : cat.getBooks()) {
			double newPrice = (b.getPrice() * PriceIncrease) + b.getPrice();			
			b.setPrice(Math.round(newPrice * 100.0) / 100.0);
		}
		
		return cat;
	}
	
	private static void WriteXMLFile(Catalog cat) {
		try {

			String basePath = new File("").getAbsolutePath();
			basePath = basePath + "\\src\\main\\resources\\XMLFiles\\Books.xml";
			File file = new File(basePath);

			JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(cat, file);
			jaxbMarshaller.marshal(cat, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static Catalog ReadXMLFile() {

		Catalog cat = null;

		String basePath = new File("").getAbsolutePath();
		basePath = basePath + "\\src\\main\\resources\\XMLFiles\\Books.xml";
		File file = new File(basePath);

		System.out.println(file.getAbsolutePath());
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			cat = (Catalog) jaxbUnmarshaller.unmarshal(file);
			System.out.println(cat);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return cat;

	}

}
