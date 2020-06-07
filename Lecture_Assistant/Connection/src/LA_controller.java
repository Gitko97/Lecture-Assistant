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
	//Compare compare;
	SpeechToText speechToText;
	PDFandIMG pdfAndimg;
	//TextToImg textToimg;
	ArrayList<BufferedImage> complete_PDF;
	ArrayList<Note> notes;
	ArrayList<Integer> change_pos;
	String savePos;
	int PDFwidth;
	int PDFheight;
	Thread compare_thread;
	Thread stt_thread;
	Timer capturing_Timer;
	
	public LA_controller(CaptureView captureView){
		pdfAndimg = new PDFandIMG();
		this.captureView =  captureView;
		notes = new ArrayList<Note>();
		complete_PDF = new ArrayList<BufferedImage>();
		change_pos = new ArrayList<Integer>();

		capturing_Timer = new Timer();
	}
	
	public void GetLecturePDF(String filePath) throws IOException { // pdf 가져오기 버튼 누르고 실행 
		//compare = new Compare(pdfAndimg.PDFtoIMG(filePath), capturing, this);
		//PDFwidth = pdfAndimg.get(0).getWidth();
		//PDFhidth = pdfAndimg.get(0).getHeight();
		this.savePos = filePath;
		//compare_thread = new Thread(compare);
	}
	
	public void Authentication(String filePath) throws IOException {
			speechToText = new SpeechToText();
			speechToText.Authentiation(filePath);
			speechToText.init(null);
			stt_thread = new Thread(speechToText);
	}
	
	public void start() {
		Thread.currentThread().setPriority(10);
		captureView.captureStart();
		capturing = new Capturing(captureView.getInfo());
		
		
		stt_thread.setPriority(9);
		//compare_thread.setPriority(5);
		capturing_Timer.schedule(capturing, 0, 1000);
		stt_thread.start();
		//compare_thread.run();
		
		Thread.currentThread().setPriority(1);
	}
	
	public void pause() {
		
	}
	
	public boolean exit() {
		captureView.captureStop();
		speechToText.exit();
		capturing_Timer.cancel();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//if(!compare.exit()) return false;
		System.out.println(speechToText.getString());
		//textToimg = new TextToImg(speechToText.getString(),notes,change_pos, PDFwidth, PDFheight);
		//pdfAndimg.IMGtoPDF(textToimg.change(), this.savePos);
		//pdfAndimg.IMGtoPDF(complete_PDF, this.savePos);
		return true;
	}
	
	public void ADD_Note(BufferedImage note, int start, int end) {
		notes.add(new Note(note, start, end));
	}
	
	public void ADD_CompletePDF(BufferedImage completePDF, int changePos) {
		complete_PDF.add(completePDF);
		change_pos.add(changePos);
	}
}
