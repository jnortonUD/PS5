package pkgMain;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pkgLibrary.Book;

public class XMLXPath {

	public static void main(String[] args) {

		ReadCatalog();

	}

	private static void ReadCatalog() {
		ReadXMLFile();
	}

	private static void ReadXMLFile() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;

		String basePath = new File("").getAbsolutePath();
		basePath = basePath + "\\src\\main\\resources\\XMLFiles\\Books.xml";
		File file = new File(basePath);

		try {
			builder = factory.newDocumentBuilder();

			doc = builder.parse(file);

			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();

			// Create XPath object
			XPath xpath = xpathFactory.newXPath();

			String name = getBookTitleById(doc, xpath, "bk107");
			System.out.println("Title with ID bk107: " + name);

			ArrayList<Book> books = getBooksByCost(doc, xpath, 25);

			System.out.println("size of books: " + books.size());

			for (Book b : books) {
				System.out.println(b.getId());
				System.out.println(b.getAuthor());
				System.out.println(b.getGenre());
				System.out.println(b.getTitle());
				System.out.println(b.getPrice());
				System.out.println(b.getPublish_date());
				System.out.println(b.getDescription());
			}



		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}



	private static ArrayList<Book> getBooksByCost(Document doc, XPath xpath, double cost) {
		ArrayList<Book> books = new ArrayList<Book>();
		try {

			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/catalog/book[price>" + cost + "]";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					DateFormat lFormatter = new SimpleDateFormat("yyyy-mm-dd");

					Book b = new Book(eElement.getAttribute("id"),
							Double.parseDouble(eElement.getElementsByTagName("Cost").item(0).getTextContent()),
							eElement.getElementsByTagName("author").item(0).getTextContent(),
							eElement.getElementsByTagName("title").item(0).getTextContent(),
							eElement.getElementsByTagName("genre").item(0).getTextContent(),
							Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent()),
							(Date)lFormatter.parse(eElement.getElementsByTagName("publish_date").item(0).getTextContent()),							
							eElement.getElementsByTagName("description").item(0).getTextContent());
					books.add(b);

				}
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return books;
	}

	private static String getBookTitleById(Document doc, XPath xpath, String BookID) {
		String Title = null;
		try {

			XPathExpression expr = xpath.compile("/catalog/book[@id='" + BookID + "']/title/text()");
			Title = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return Title;
	}
}
