package com.darkprograms.speech.microphone;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat;
import com.darkprograms.speech.utility.*;
import java.io.*;
import java.util.Arrays;


/***************************************************************************
 * Microphone class that contains methods to capture audio from microphone
 *
 * @author Luke Kuza, Aaron Gokaslan
 ***************************************************************************/
public class Microphone {
	
    /**
     * TargetDataLine variable to receive data from microphone
     */
    private TargetDataLine targetDataLine;

    /**
     * Enum for current Microphone state
     */
    public enum CaptureState {
        PROCESSING_AUDIO, STARTING_CAPTURE, CLOSED
    }

    /**
     * Variable for enum
     */
    CaptureState state;

    /**
     * Variable for the audios saved file type
     */
    private AudioFileFormat.Type fileType;

    /**
     * Variable that holds the saved audio file
     */
    private File audioFile;

    /**
     * Gets the current state of Microphone
     *
     * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
     *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
     *         CLOSED is returned if the Thread is not doing anything/not capturing audio
     */
    public CaptureState getState() {
        return state;
    }

    /**
     * Sets the current state of Microphone
     *
     * @param state State from enum
     */
    private void setState(CaptureState state) {
        this.state = state;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public AudioFileFormat.Type getFileType() {
        return fileType;
    }

    public void setFileType(AudioFileFormat.Type fileType) {
        this.fileType = fileType;
    }

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void setTargetDataLine(TargetDataLine targetDataLine) {
        this.targetDataLine = targetDataLine;
    }
    
    
    /**
     * Constructor
     *
     * @param fileType File type to save the audio in<br>
     *                 Example, to save as WAVE use AudioFileFormat.Type.WAVE
     */
    public Microphone(AudioFileFormat.Type fileType) {
        setState(CaptureState.CLOSED);
        setFileType(fileType);
        initTargetDataLine();
    }

    /**
     * Initializes the target data line.
     */
    private void initTargetDataLine(){
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        try {
			setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

    }


    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The File to save the audio to
     * @throws Exception Throws an exception if something went wrong
     */
    public void captureAudioToFile(File audioFile) throws Exception {
        setState(CaptureState.STARTING_CAPTURE);
        setAudioFile(audioFile);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));


        //Get Audio
        new Thread(new CaptureThread()).start();


    }

    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The fully path (String) to a file you want to save the audio in
     * @throws Exception Throws an exception if something went wrong
     */
    public void captureAudioToFile(String audioFile) throws Exception {
        setState(CaptureState.STARTING_CAPTURE);
        File file = new File(audioFile);
        setAudioFile(file);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));


        //Get Audio
        new Thread(new CaptureThread()).start();


    }

	/**
     * Gets the volume of the microphone input
     * Note: Do not update more than every 250ms
     * unless you specify a smaller numOfBytes
     * @return The volume of the microphone input will return -1 if data-line is not available
     */
    public int getAudioVolume(){
    	return getAudioVolume(100);
    }
    
    /**
     * Gets the volume of microphone input
     * @param numOfBytes The number of bytes you want for volume interpretation
     * @return The volume over the specified number of bytes or -1 if mic is unavailable.
     */
    public int getAudioVolume(int numOfBytes){
    	if(getTargetDataLine()!=null){
    		byte[] data = new byte[numOfBytes];
    		this.getTargetDataLine().read(data, 0, numOfBytes);
    		return calculateRMSLevel(data);
    	}
		else{
			return -1;
		}
    }
    
    /**
     * Calculates the volume of AudioData which may be buffered data from a dataline
     * @param audioData The byte[] you want to determine the volume of
     * @return the calculated volume of audioData
     */
	private int calculateRMSLevel(byte[] audioData){
		long lSum = 0;
		for(int i=0; i<audioData.length; i++)
			lSum = lSum + audioData[i];

		double dAvg = lSum / audioData.length;

		double sumMeanSquare = 0d;
		for(int j=0; j<audioData.length; j++)
			sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

		double averageMeanSquare = sumMeanSquare / audioData.length;
		return (int)(Math.pow(averageMeanSquare,0.5d) + 0.5);
	}
	
    /**
     * The audio format to save in
     *
     * @return Returns AudioFormat to be used later when capturing audio from microphone
     */
    public AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    /**
     * Opens the microphone, starting the targetDataLine.
     * If it's already open, it does nothing.
     */
    public void open(){
        if(getTargetDataLine()==null){
        	initTargetDataLine();
        }
    	if(!getTargetDataLine().isOpen()){
        	try {
                setState(CaptureState.PROCESSING_AUDIO);
        		getTargetDataLine().open(getAudioFormat());
            	getTargetDataLine().start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
        }

    }

    /**
     * Close the microphone capture, saving all processed audio to the specified file.<br>
     * If already closed, this does nothing
     */
    public void close() {
        if (getState() == CaptureState.CLOSED) {
        } else {
			//System.out.println("PLS SEE");
            getTargetDataLine().stop();
            getTargetDataLine().close();
            setState(CaptureState.CLOSED);
        }
    }
	
	//BEHOLD FOR THIS IS WHERE I MAKE EVERYTHING WORK
	public int getFrequency()
	{
		try {
			return getFrequency(1024);
		} catch (Exception e) {
			//If this occurs then reality is broken.
			return -666;
		}
	}
	
	public int getFrequency(int numOfBytes) throws Exception
	{
		if(getTargetDataLine()!=null){
    		byte[] data = new byte[numOfBytes+1];
    		this.getTargetDataLine().read(data, 0, numOfBytes);
			//for(int i =0; i < data.length; i++)
				//System.out.println(data[i]);
    		return getFrequency(data);
    		//return -1;
    	}
		else{
			return -1;
		}
		
	}
	
	private int getFrequency(byte[] bytes)
	{//This method requires an AudioFormat and cannot be static.
		double[] audioData = this.bytesToDoubleArray(bytes);
		Complex[] complex = new Complex[audioData.length];
		for(int i = 0; i<complex.length; i++){
			complex[i] = new Complex(audioData[i], 0);
		}
		Complex[] fftTransformed = FFT.fft(complex);
		return calculateFundamentalFrequency(fftTransformed);
	}
	
	private int calculateFundamentalFrequency(Complex[] fftData)
	{
		//System.out.println(fftData);
		int index = -1;
		double max = Double.MIN_VALUE;
		for(int i = 0; i<fftData.length/2; i++){
			Complex complex = fftData[i];
			double tmp = complex.getMagnitude();
			if(tmp>max && !isHarmonic(i,index))
			{
				max = tmp;
				index = i;
			}
		}
		return index*getFFTBinSize(fftData.length);
	}
	
	private int getFFTBinSize(int fftDataLength)
	{
		return (int)(getAudioFormat().getSampleRate()/fftDataLength+.5);
	}
	
	private boolean isHarmonic(int currentIndex, int proposedIndex)
	{
		return (currentIndex>2 && proposedIndex>2 && currentIndex%proposedIndex==0);
	}
	
	private double[] bytesToDoubleArray(byte[] bufferData)
	{
	    final int bytesRecorded = bufferData.length;
		final int bytesPerSample = getAudioFormat().getSampleSizeInBits()/8; 
	    final double amplification = 100.0; // choose a number as you like
	    double[] micBufferData = new double[bytesRecorded - bytesPerSample +1];
	    for (int index = 0, floatIndex = 0; index < bytesRecorded - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
	        double sample = 0;
	        for (int b = 0; b < bytesPerSample; b++) {
	            int v = bufferData[index + b];
	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
	                v &= 0xFF;
	            }
	            sample += v << (b * 8);
	        }
	        double sample32 = amplification * (sample / 32768.0);
	        micBufferData[floatIndex] = sample32;
	        
	    }
	    return micBufferData;
	}
	
	public void filter()
	{
		try{
		File audioFile = getAudioFile();
		long len = audioFile.length();
		//System.out.println(length);
		byte[] audio = read(audioFile);
		int[] fftNums = new int[Math.round(len/1024)];
		
		int count = 0;
		byte[] toFFT;
		for(int i = 0; i< len; i+=1024)
		{
			//System.out.println(i);
			toFFT = Arrays.copyOfRange(audio, i, i+1024);
			fftNums[count] = getFrequency(toFFT);
			count++;
		}
		
		//for(int j=0; j<fftNums.length; j++)
			//System.out.println(fftNums[j]);
		
		}catch(Exception e){System.out.println(e);}
	}
	
	public byte[] read(File file) throws IOException
	{


		byte []buffer = new byte[(int) file.length()];
		InputStream ios = null;
		try 
		{
			ios = new FileInputStream(file);
			if ( ios.read(buffer) == -1 ) 
			{
				throw new IOException("EOF reached while trying to read the whole file");
			}        
		} 
		finally 
		{ 
			try 
			{
				 if ( ios != null ) 
					  ios.close();
			} catch ( IOException e) {
			}
		}

		return buffer;
	}
	
	
	
	
	
	
	
	
	
	
	
	

    /**
     * Thread to capture the audio from the microphone and save it to a file
     */
    private class CaptureThread implements Runnable {

        /**
         * Run method for thread
         */
        public void run() {
            try {
                AudioFileFormat.Type fileType = getFileType();
                File audioFile = getAudioFile();
                open();
				int freq = getFrequency();
				//System.out.println(freq);
				//while(freq >= 100 && freq <= 270)
				
				AudioSystem.write(new AudioInputStream(getTargetDataLine()), fileType, audioFile);
				
                //Will write to File until it's closed.
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
