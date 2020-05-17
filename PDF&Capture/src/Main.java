import java.awt.MouseInfo;
import java.awt.PointerInfo;

public class Main {
	public static void main(String[] args) {
		//CaptureView captureview = new CaptureView();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//Capture capture = new Capture(captureview.getInfo());
	//capture.setDaemon(true);
	//capture.run();
	
		PDFtoIMG pdfimg = new PDFtoIMG("C:\\Users\\xcvds\\Downloads\\[Lecture07] Git for collaboration.pdf");
		pdfimg.change();
		
		SaveIMG imgsa = new SaveIMG();
		imgsa.savePDF(pdfimg.getIMG());
		//imgsa.setDaemon(true);
		//imgsa.run();
	}
}
