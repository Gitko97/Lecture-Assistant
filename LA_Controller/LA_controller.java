import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class LA_controller {
	CaptureView captureView;
	Capturing capturing;
	Compare compare;
	Speech_To_Text speechToText;
	PDFandIMG pdfAndimg;
	TextToImg textToimg;
	ArrayList<BufferedImage> complete_PDF;
	ArrayList<BufferedImage> notes;
	ArrayList<Point> note_pos;
	ArrayList<Integer> change_pos;
	String savePos;
	
	Thread compare_thread;
	Thread stt_thread;
	Timer capturing_Timer;
	
	public LA_controller(){
		captureView = new CaptureView(900,900);
		capturing = new Capturing(captureView.getInfo());
		pdfAndimg = new PDFandIMG();
		
		notes = new ArrayList<BufferedImage>();
		note_pos = new ArrayList<Point>();
		complete_PDF = new ArrayList<BufferedImage>();
		change_pos = new ArrayList<Integer>();
		
		capturing_Timer = new Timer();
	}
	
	public void GetLecturePDF(String filePath) throws IOException { // pdf 가져오기 버튼 누르고 실행 
		compare = new Compare(pdfAndimg.PDFtoIMG(filePath), capturing, this);
		this.savePos = filePath;
		compare_thread = new Thread();
	}
	
	public void Authentication(String filePath) throws IOException {
			speechToText = new Speech_To_Text(filePath);
			stt_thread = new Thread();
	}
	
	public void start() {
		Thread.currentThread().setPriority(10);
		captureView.captureStart();
		capturing.syncPosition(captureView.getInfo());
		
		
		//TODO : this thread part have to be recoded to scheduling thread
		stt_thread.setPriority(9);
		compare_thread.setPriority(8);
		capturing_Timer.schedule(capturing, 0, 1000);
		stt_thread.run();
		compare_thread.run();
		
		Thread.currentThread().setPriority(1);
	}
	
	public void pause() {
		
	}
	
	public boolean exit() {
		if(!compare.exit()) return false;
		capturing_Timer.cancel();
		stt_thread.exit();
		captureView.exit();
		
		textToimg = new TextToImg(speechToText.getString(),notes,note_pos,change_pos);
		pdfAndimg.IMGtoPDF(textToimg.change(), this.savePos);
		return true;
	}
	
	public void ADD_Note(BufferedImage note, int start, int end) {
		notes.add(note);
		note_pos.add(new Point(start, end));
	}
	
	public void ADD_CompletePDF(BufferedImage completePDF, int changePos) {
		complete_PDF.add(completePDF);
		change_pos.add(changePos);
	}
}
