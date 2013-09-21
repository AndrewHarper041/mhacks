import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.alchemyapi.api.*;

public class SpeechInterpreter {
	
	private static final String API_KEY = "f883996a525267c2bffcb1073f63d7474e42a827"; // API Key for AlchemyAPI
	
	private AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString(API_KEY);
	
	private static enum PhraseType 
	{
		STATEMENT, YES_NO_QUESTION, DEFINITION_QUESTION
	}
	
	
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
	
	public void interpretText()
	{
		Document doc = null;
		try {
	        // Categorize some text
			 doc = alchemyObj.TextGetRelations("The man in the black suit is white.");
		} catch (Exception e) {
			System.err.println(e);
		}
		if (doc != null) System.out.println(getStringFromDocument(doc));
		
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
