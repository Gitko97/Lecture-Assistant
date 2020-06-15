package test.src;

import java.io.IOException;

import SpeechToText.src.SpeechToText;

public class Main {
	public static void main(String args[]) {
	SpeechToText stt = new SpeechToText();
	try {
		stt.Authentiation(".//key.json", "en-US");
	} catch (IOException e) {
		e.printStackTrace();
	}
	stt.init(null);
	Thread sttThread = new Thread(stt);
	sttThread.start();
	try {
		Thread.sleep(120000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	stt.exit();
	System.out.println(stt.getString());
	}
}
