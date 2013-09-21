import java.net.URI;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.*;
import com.darkprograms.speech.microphone.*;
import com.darkprograms.speech.recognizer.*;



public class Driver extends Thread implements Runnable  
{
	public static boolean running = false;
	public static Microphone mic = new Microphone(AudioFileFormat.Type.WAVE);
	public static File audioFile = new File("comm.wav");	
	
	public void run()
	{
		//Here be the voice recoginition 
		try{
		MicrophoneAnalyzer analyze = new MicrophoneAnalyzer(AudioFileFormat.Type.WAVE);
		System.out.println(analyze.getAudioVolume());	
		if(running = false)
		{
			mic.captureAudioToFile(audioFile);
			running = true;
		}
		if(analyze.getAudioVolume() < 100)
		{
			mic.close();
			running = false;
		}
		}catch(Exception e){System.out.println(e);}
		
		//When we get to voiceinput = String command;
		
	}
	
	public static void main(String[] args)
	{	
		long start = System.currentTimeMillis();
		try{mic.captureAudioToFile(audioFile);}catch(Exception e){System.out.println(e);}
		while(((System.currentTimeMillis() - start) / 1000.0) < 5)
		{
			/*try{ //mic.captureAudioToFile(audioFile);}catch(Exception e){System.out.println(e);}
			MicrophoneAnalyzer analyze = new MicrophoneAnalyzer(AudioFileFormat.Type.WAVE);
			if(analyze.getAudioVolume() != 0)
				System.out.println(analyze.getAudioVolume());	
			if(running = false)
			{
				mic.captureAudioToFile(audioFile);
				System.out.println("now running");
				running = true;
			}
			if(analyze.getAudioVolume() < 100)
			{
				mic.close();
				running = false;
			}
			}catch(Exception e){System.out.println(e);}
			
			//(new Thread(new Driver())).start();*/	
		}
		mic.close();
		Recognizer recog = new Recognizer();
		
		try{GoogleResponse response = recog.getRecognizedDataForWave(audioFile);
		System.out.println(response.getResponse());
		System.out.println(response.getAllPossibleResponses());
		}catch(Exception e){}
		System.out.println((System.currentTimeMillis() - start) / 1000.0);
	}	
}
