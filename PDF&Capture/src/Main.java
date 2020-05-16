import java.awt.MouseInfo;
import java.awt.PointerInfo;

public class Main {
	public static void main(String[] args) {
		//GetHTML ht = new GetHTML("https://eclass3.cau.ac.kr/courses/34799/external_tools/2");
		//ht.GetSize();
		CaptureView captureview = new CaptureView();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Capture capture = new Capture(captureview.drawpanel.xpos,captureview.drawpanel.ypos,captureview.drawpanel.width,captureview.drawpanel.height);
		capture.run();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SaveIMG imgsa = new SaveIMG();
		imgsa.run();
		
		


	}
}
