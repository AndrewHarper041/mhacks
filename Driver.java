
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



public class Driver 
{
	public static boolean running = false;
	public static Microphone mic = new Microphone(AudioFileFormat.Type.WAVE);
	public static File audioFile = new File("comm.wav");	
	public static File testFile = new File("ENG_M.wav");
	
	/*public void run()
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
		
	}*/
	
	public static void main(String[] args)
	{	
	
		long start = System.currentTimeMillis();
		Console console = System.console();
		try{mic.captureAudioToFile(audioFile);}catch(Exception e){System.out.println(e);}
		MicrophoneAnalyzer analyze = new MicrophoneAnalyzer(AudioFileFormat.Type.WAVE);
		String input;
		Boolean run = true;
		
		while(run)
		{
			
			input = console.readLine();
						
			if(input.equals("a"))
			{
			
				mic.close();
		
				Recognizer recog = new Recognizer();
						
				try
				{
					GoogleResponse response = recog.getRecognizedDataForWave(audioFile);
					SpeechInterpreter interpreter = new SpeechInterpreter();
					String answer = interpreter.handleText(response.getResponse());
					System.out.println(answer);
					if(answer.length() > 1)
					{
						//TOTALLY LEGIT TEXT TO SPEECH IMPLEMENTAION MOVE ALONG NOTHING TO SEE HERE
					
						Runtime rt = Runtime.getRuntime();
						try {
							rt.exec(new String[]{"SayStatic.exe", answer});
						} catch (Exception e) {System.out.println(e);}
					}
					//System.out.println(response.getResponse());
					//System.out.println(response.getAllPossibleResponses());
					
				}catch(Exception e){System.out.println(e);}
				
			}
			
			if(input.equals("q"))
			{
				mic.close();
				run = false;
			}
			input = "";
			//try{mic.captureAudioToFile(audioFile);}catch(Exception e){System.out.println(e);}
			
		}
		
		
		
		mic.close();
		
		
	/*	Recognizer recog = new Recognizer();
				
		try
		{
			GoogleResponse response = recog.getRecognizedDataForWave(audioFile);
			SpeechInterpreter interpreter = new SpeechInterpreter();
			String answer = interpreter.handleText(response.getResponse());
			System.out.println(answer);
			if(answer.length() > 1)
			{
				//TOTALLY LEGIT TEXT TO SPEECH IMPLEMENTAION MOVE ALONG NOTHING TO SEE HERE
			
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec(new String[]{"SayStatic.exe", answer});
				} catch (Exception e) {System.out.println(e);}
			}
			//System.out.println(response.getResponse());
			//System.out.println(response.getAllPossibleResponses());
			
		}catch(Exception e){System.out.println(e);}
		System.out.println((System.currentTimeMillis() - start) / 1000.0); 
		*/
	}	
}
