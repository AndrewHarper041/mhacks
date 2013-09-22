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
	//public final double sentiment;
	
	
	
	private static final String API_KEY = "f883996a525267c2bffcb1073f63d7474e42a827"; // API Key for AlchemyAPI
	private static AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(API_KEY);;
	
	public static enum Type 
	{
		NONSENSE, STATEMENT, YES_NO_QUESTION, DEFINITION_QUESTION
	}

	
	public Phrase(String string)
	{
		this.string = string;
		this.keyword = getKeyword();
		this.type = getPhraseType();
	}
	
	/* THIS IS NOT DONE!
	private double getSentiment()
	{
		try {getStringFromDocument(alchemyObj.TextGetTextSentiment(string));} catch (Exception e) {};
	}
	*/
	
	private Type getPhraseType()  
	{
		// Check if the user wants to know more about something
		if (string.startsWith("what") || string.startsWith("where") || string.startsWith("who") || string.startsWith("why") || string.startsWith("how") ) return Type.DEFINITION_QUESTION;
		// Check if the user wants to verify if something is true or false
		if (string.startsWith("is") || string.startsWith("are") || string.startsWith("does") || string.startsWith("was") || string.startsWith("were")
			||string.startsWith("do") || string.startsWith("am"))
		{
			try
			{
				return Type.YES_NO_QUESTION;
			} catch (Exception e)
			{
				System.err.println(e);
				return Type.NONSENSE;
			}
		}
		// A statement is a non-question that contains at least one keyword
		if (!keyword.isEmpty()) return Type.STATEMENT;
		// If there are no keywords, the phrase is nonsense
		return Type.NONSENSE;
	}
	
	private String getKeyword()
	{
		try
		{
			Document doc = alchemyObj.TextGetRankedKeywords(string);
			String[] words = doc.getElementsByTagName("keyword").item(0).getTextContent().trim().split("\n");
			//System.out.println(getStringFromDocument(doc));
			if (words[0].isEmpty()) return "";
			return words[0];
		} catch (Exception e) 
		{
			System.out.println("No keywords found.");
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
