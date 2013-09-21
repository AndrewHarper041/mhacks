import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class ListenThread extends Thread
{
	
	private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "myusername";
    private static final String SMTP_AUTH_PWD  = "mypwd";
	
	ListenThread
	{
		
	}
	
	public void run()
	{
		//Here be the voice recoginition 
		
		
		
		//When we get to voiceinput = String command;
		
	}
	
	//"Search/Lookup ___"
	public static void chromeSearch(String search)
	{
		try{
		if(Desktop.isDesktopSupported())
		{
			Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + search));
		}}
		catch(Exception e){System.out.println(e);}
	}
	
	//"Goto/open ___"
	public static void openSite(String site)
	{
		try{
		if(Desktop.isDesktopSupported())
		{
			Desktop.getDesktop().browse(new URI("https://www." + site + ".com"));
		}}
		catch(Exception e){System.out.println(e);}
	}
	
	//"email/send ___"
	public void test(String msg) throws Exception
	{
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        // uncomment for debugging infos to stdout
        // mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        Multipart multipart = new MimeMultipart("alternative");

        BodyPart part1 = new MimeBodyPart();
        part1.setText("This is multipart mail and u read part1......");

        BodyPart part2 = new MimeBodyPart();
        part2.setContent("<b>This is multipart mail and u read part2......</b>", "text/html");

        multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);

        message.setContent(multipart);
        message.setFrom(new InternetAddress("me@myhost.com"));
        message.setSubject("This is the subject");
        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress("someone@somewhere.com"));

        transport.connect();
        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
	
	private class SMTPAuthenticator extends javax.mail.Authenticator 
	{
        public PasswordAuthentication getPasswordAuthentication() 4
		{
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
}