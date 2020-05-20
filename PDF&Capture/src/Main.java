import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.Timer;

public class Main extends Thread {
	public static void main(String[] args) {
		Thread.currentThread().setPriority(10);
		CaptureView captureview = new CaptureView();
		captureview.run();
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Capturing capture = new Capturing(captureview.getInfo());
		Timer captureTimer = new Timer();
		
		SaveIMG save = new SaveIMG();
		Thread saveThread = new Thread(save);
		saveThread.setPriority(1);

		
		captureTimer.schedule(capture, 1000, 1000);
		saveThread.start();
		try {
			System.out.println("dd");
			Thread.currentThread().sleep(100000);
			captureTimer.cancel();
			System.out.println("dd");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
