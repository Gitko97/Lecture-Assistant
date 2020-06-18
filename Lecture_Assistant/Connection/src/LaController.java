package src;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class LaController {
  CaptureView captureView;
  Capturing capturing;
  Compare compare;
  SpeechToText speechToText;
  PdfAndImg pdfAndimg;
  TextToImg textToimg;
  ArrayList<BufferedImage> completePdf;
  ArrayList<Note> notes;
  ArrayList<Integer> changePos;
  ArrayList<BufferedImage> origin_PDF;
  String savePos;
  int pdfWidth;
  int pdfHeight;
  Thread compareThread;
  Thread sttThread;
  Timer capturingTimer;
  static boolean sttStart = false;
  SaveIMG save = new SaveIMG();

  public static int u=0;
  public LaController(CaptureView captureView){
    pdfAndimg = new PdfAndImg();
    origin_PDF = new ArrayList<>();
	this.captureView =  captureView;
	notes = new ArrayList<Note>();
	completePdf = new ArrayList<BufferedImage>();
	changePos = new ArrayList<Integer>();
	capturing = new Capturing();

  }
  
  //When Press PDF Button
  public void GetLecturePDF(String filePath) throws IOException { 
		origin_PDF = pdfAndimg.PdfToImg(filePath);
		this.pdfWidth = origin_PDF.get(0).getWidth();
		this.pdfHeight = origin_PDF.get(0).getHeight();
		compare = new Compare(origin_PDF, capturing, this);
		this.savePos = filePath;
  }

  //When Press STT Key Button
  public void Authentication(String filePath,String langCode) throws IOException {
    speechToText = new SpeechToText();
	speechToText.Authentiation(filePath, langCode);
	speechToText.init(null);
  }

  //When Press Start Button
  public void start() throws InterruptedException {
	Thread.currentThread().setPriority(8);
	
	captureView.captureStart();
	capturingTimer = new Timer();
	capturing.syncPosition(captureView.getInfo());
   
	sttThread = new Thread(speechToText);
	sttThread.setPriority(10);
	sttThread.start();
	while(!sttStart) {
		System.out.println("Wait");
	}
	compareThread = new Thread(compare);
	compareThread.setPriority(1);
	
	
	capturingTimer.schedule(capturing, 0, 1000);
	Thread.sleep(1000);
	compareThread.start();
	
	Thread.currentThread().setPriority(1);
  }
	
  //When Press Exit Button
  public boolean exit() {
    captureView.captureStop();
	speechToText.exit();
	capturingTimer.cancel();
	
	while(!compare.exit()) {
		try {
			  Thread.sleep(1000);
			} catch (InterruptedException e) {
			  e.printStackTrace();
			}
	}
	pdfAndimg.IMGtoPDF(completePdf, this.savePos+"_complete.pdf");
	System.out.println(speechToText.getString().toString());
	try {
		textToimg = new TextToImg(speechToText.getString(),notes, changePos, pdfWidth, pdfHeight);
		pdfAndimg.IMGtoPDF(textToimg.convert(), this.savePos+"_script.pdf");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return true;
  }
	
  //Compare Class call this method to add note
  public void ADD_Note(BufferedImage note, int start, int end) {
	notes.add(new Note(note, start, end));
	SaveIMG save = new SaveIMG();
	save.save(note, this.savePos, ""+(u++));
	
  }
	
  //Compare Class call this method to add finished page
  public void ADD_CompletePDF(BufferedImage completePdf2, int changePos2) {
	completePdf.add(completePdf2);
	changePos.add(changePos2);
  }
}
