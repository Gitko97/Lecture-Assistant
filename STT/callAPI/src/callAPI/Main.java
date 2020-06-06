package callAPI;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		InfiniteStreamRecognize stt = new InfiniteStreamRecognize();
		try {
			stt.Authentiation("C:\\Users\\xcvds\\eclipse-workspace\\SoundRecord\\key\\key.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stt.init("");
		Thread thread = new Thread(stt);
		thread.start();
		try {
			Thread.sleep(20000);
			System.out.println(stt.getString().toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
