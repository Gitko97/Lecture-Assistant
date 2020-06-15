package src;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class LaController {
  CaptureView captureView;
  Capturing capturing;
  PdfAndImg pdfAndimg;
  ArrayList<BufferedImage> completePdf;
  ArrayList<Integer> changePos;
  ArrayList<BufferedImage> origin_PDF;
  String savePos;
  Timer capturingTimer;
	
  public LaController(CaptureView captureView){
    pdfAndimg = new PdfAndImg();
    origin_PDF = new ArrayList<>();
	this.captureView =  captureView;
	changePos = new ArrayList<Integer>();
	capturing = new Capturing();

  }
  
  //When Press PDF Button
  public void GetLecturePDF(String filePath) throws IOException { 
		origin_PDF = pdfAndimg.PdfToImg(filePath);
	    System.out.println(filePath+" selected");
		this.savePos = filePath;
  }

  //When Press STT Key Button
  public void Authentication(String filePath,String langCode) throws IOException {
			System.out.println(filePath+" And "+langCode+" selected");
  }

  //When Press Start Button
  public void start() throws InterruptedException {
	Thread.currentThread().setPriority(10);
	
	captureView.captureStart();
	capturingTimer = new Timer();
	capturing.syncPosition(captureView.getInfo());
	capturingTimer.schedule(capturing, 0, 1000);
	
	Thread.currentThread().setPriority(1);
  }
	
  //When Press Exit Button
  public boolean exit() {
    captureView.captureStop();
	capturingTimer.cancel();
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
	pdfAndimg.IMGtoPDF(capturing.getAll(), this.savePos+"_captured.pdf");
	return true;
  }
}
