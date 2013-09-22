import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

/**
 * The phrase class wraps around a string and gives useful information to the interpreter.
 * @author M94
 *
 */

public class Phrase {
	
	public final String string;
	public final Type type;
	public final String keyword;
	
	private static final String API_KEY = "f883996a525267c2bffcb1073f63d7474e42a827"; // API Key for AlchemyAPI
	private static AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(API_KEY);;
	
	public static enum Type 
	{
		NONSENSE, STATEMENT, YES_NO_QUESTION, DEFINITION_QUESTION
	}

	
	public Phrase(String string)
	{
		this.string = string;
		this.type = getPhraseType();
		this.keyword = getKeyword();
		System.out.println("Keyword:" + keyword);
	}
	
	private Type getPhraseType()  
	{
		// Check if the user wants to know more about something
		if (string.startsWith("what") || string.startsWith("where") || string.startsWith("who")) return Type.DEFINITION_QUESTION;
		// Check if the user wants to verify if something is true or false
		if (string.startsWith("is") || string.startsWith("are") || string.startsWith("does") || string.startsWith("was") || string.startsWith("were")
			||string.startsWith("do"))
		{
			try
			{
				Document doc = alchemyObj.TextGetRankedKeywords(string);
				System.out.println(getStringFromDocument(doc));
			} catch (Exception e)
			{
				System.err.println(e);
			}
		}
		return Type.STATEMENT;
	}
	
	private String getKeyword()
	{
		try
		{
			Document doc = alchemyObj.TextGetRankedKeywords(string);
			String[] line = doc.getElementsByTagName("keyword").item(0).getTextContent().trim().split("\n");
			return line[0];
		} catch (Exception e) 
		{
			return "";
		}
	}
    // utility method
    private static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }	

}
