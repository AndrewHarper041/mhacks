import java.net.URI;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JTextField;


public class Driver 
{
	public boolean running = false;
	public static Microphone mic = new Microphone(AudioFileFormat.Type.WAVE);
	public static File audioFile = new File("command.wav");	
	
	public static void main(String[] args)
	{	
		//Component c; // this is the component you want to add a listener to 

		/*c.addKeyListener(new KeyListener() { 

		public void keyPressed(KeyEvent e) 
		{ 
			if(e.getKeyChar() == e.VK_ENTER) 
			{ 
				try{mic.captureAudioToFile(audioFile);}
				catch(Exception x){System.out.println(x);}
			} 
		} 

			// unused abstract methods 
			public void keyTyped(KeyEvent e) {} 

			public void keyReleased(KeyEvent e) {} 
		});*/
		
		mic.getAudioVolumn();
	}	
}
	

