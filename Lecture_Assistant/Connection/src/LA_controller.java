package src;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import src.CaptureView;
import src.Capturing;
import src.PDFandIMG;
import src.SpeechToText;

public class LA_controller {
  CaptureView captureView;
  Capturing capturing;
  // Compare compare;
  SpeechToText speechToText;
  PDFandIMG pdfAndimg;
  // TextToImg textToimg;
  ArrayList<BufferedImage> completePdf;
  ArrayList<Note> notes;
  ArrayList<Integer> changePos;
  String savePos;
  int pdfWidth;
  int pdfHeight;
  Thread compareThread;
  Thread sttThread;
  Timer capturingTimer;
	
  public LA_controller(CaptureView captureView){
    pdfAndimg = new PDFandIMG();
	this.captureView =  captureView;
	notes = new ArrayList<Note>();
	completePdf = new ArrayList<BufferedImage>();
	changePos = new ArrayList<Integer>();

	capturingTimer = new Timer();
  }
	
  public void GetLecturePDF(String filePath) throws IOException { // pdf 媛��졇�삤湲� 踰꾪듉 �늻瑜닿퀬 �떎�뻾 
    //compare = new Compare(pdfAndimg.PDFtoIMG(filePath), capturing, this);
	//PDFwidth = pdfAndimg.get(0).getWidth();
	//PDFhidth = pdfAndimg.get(0).getHeight();
	this.savePos = filePath;
  }
	
  public void Authentication(String filePath,String langCode) throws IOException {
    speechToText = new SpeechToText();
	speechToText.Authentiation(filePath, langCode);
	speechToText.init(null);
			
  }
	
  public void start() throws InterruptedException {
    Thread.currentThread().setPriority(10);
	sttThread = new Thread(speechToText);
	//compare_thread = new Thread(compare);
	captureView.captureStart();
	capturing = new Capturing(captureView.getInfo());
		
	sttThread.setPriority(9);
	//compare_thread.setPriority(5);
	capturingTimer.schedule(capturing, 0, 1000);
	sttThread.start();
	//compare_thread.run();
		
	Thread.currentThread().setPriority(1);
  }
	
  public void pause() {
		
  }
	
  public boolean exit() {
    captureView.captureStop();
	speechToText.exit();
	capturingTimer.cancel();
	try {
	  Thread.sleep(1000);
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
	//if(!compare.exit()) return false;
	System.out.println(speechToText.getString());
	//textToimg = new TextToImg(speechToText.getString(),notes,change_pos, PDFwidth, PDFheight);
	//pdfAndimg.IMGtoPDF(textToimg.change(), this.savePos+"_script");
	//pdfAndimg.IMGtoPDF(complete_PDF, this.savePos+"_complete");
	return true;
  }
	
  public void ADD_Note(BufferedImage note, int start, int end) {
	notes.add(new Note(note, start, end));
  }
	
  public void ADD_CompletePDF(BufferedImage completePDF, int changePos2) {
	completePdf.add(completePDF);
	changePos.add(changePos2);
  }
}
