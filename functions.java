import java.awt.Desktop;

public class functions extends Thread 
{
	public void openChrome(String search)
	{
		if(Desktop.isDesktopSupported())
		{
			Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + search));
		}
	}
	
	
}