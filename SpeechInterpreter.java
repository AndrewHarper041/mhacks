import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
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

import com.alchemyapi.api.*;

public class SpeechInterpreter {
	
	private static final String API_KEY = "f883996a525267c2bffcb1073f63d7474e42a827"; // API Key for AlchemyAPI
	
	public static AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(API_KEY);
	
	
	public SpeechInterpreter() 
	{
		
	}
	
	public void interpretText(File textFile)
	{
		BufferedReader reader = null;
		try {
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile)));
		} catch (Exception e) {
			System.err.println(e);
		}
		Document doc = null;
		try {
	        // Categorize some text
			 doc = alchemyObj.TextGetCategory(reader.readLine());
		} catch (Exception e) {
			System.err.println(e);
		}
		if (doc != null) System.out.println(getStringFromDocument(doc));
	}
	
	public String respondToText(String text)
	{
		System.out.println("You: " + text);
		Phrase phrase = new Phrase(text);
		String response = "Umm...";
		switch (phrase.type)
		{
		case NONSENSE:
			response = "Huh?";
			break;
		case STATEMENT:
			response = "Okay.";
			break;
		case YES_NO_QUESTION:
			response = "Yes.";
			break;
		case DEFINITION_QUESTION:
			response = respondToDefinitionQuestion(phrase);
			break;
		}
		return "Me: " + response;
	}
	
	private String respondToDefinitionQuestion(Phrase phrase)
	{	
		String definition = "I'm not sure.";
		try {
			// Format keyword for Wikipedia
			String[] keywords = phrase.keyword.split("  *");
			StringBuilder formattedKeyword = new StringBuilder();
			for (int i = 0; i < keywords.length; i++ )
			{
				formattedKeyword.append(keywords[i]);
				if (i < keywords.length - 1) formattedKeyword.append("_"); 
			}
			System.out.println("en.wikipedia.org/wiki/" + formattedKeyword.toString());
			Document doc = alchemyObj.URLGetText("en.wikipedia.org/wiki/" + formattedKeyword.toString());
			String[] sentences = doc.getElementsByTagName("text").item(0).getTextContent().split("\\.");
			definition = sentences[0];

		} catch (Exception e) {
			System.out.println(e);
			return definition;
		}
		return definition;
	}
	
	private String respondToYesNoQuestion(Phrase phrase)
	{
		String response = "I'm not sure.";
		return response; 
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
