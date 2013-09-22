
public class SpeechTest {

	public static void main(String[] args) {
		SpeechInterpreter interpreter = new SpeechInterpreter();
		System.out.println("Watson: " + interpreter.handleText("everyone is a moron"));
	}

}
