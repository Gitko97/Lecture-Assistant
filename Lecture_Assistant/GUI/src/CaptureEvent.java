package src;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class CaptureEvent implements MouseListener, MouseMotionListener {
	DrawPanel drawpanel;
	private String pdfPath = null;
	private String keyPath = null;
	int beforeX;
	int beforeY;
	int curX;
	int curY;
	int clickedShape=0;
	boolean start;
	CaptureView captureView;
	LA_controller controller;
	public class PDFButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		   JFileChooser fc = new JFileChooser("/Users/");
		   fc.setDialogTitle("Select pdf file");
		   
		   fc.setFileFilter(new FileNameExtensionFilter("pdf", "pdf")); //.pdf 파일만 선택하도록
		   fc.setMultiSelectionEnabled(false);                          //다중 선택 불가

	       fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.
	        
	        
	        if(fc.getSelectedFile() != null) {
	        	pdfPath = fc.getSelectedFile().toString(); 
	        	try {
	        		controller.GetLecturePDF(pdfPath);
	        		captureView.setLabel1(pdfPath);
	        	} catch (IOException e) {
	        		pdfPath = null;
	        		JOptionPane.showMessageDialog(null, "This pdf file cannot be read");
	        	}
	        }
		}
		
	}
	
	public class CloseButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			captureView.exit();
		}
		
	}
	
	public class StartButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(pdfPath == null || keyPath == null) {
				JOptionPane.showMessageDialog(null, "You have to input PDF File and Key File");
			}
			else { 
				start = true; 
				try {
					controller.start();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Microphone not support");
				}
			}
		}
		
	}
	
	public class ExitButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(start)
			 if(controller.exit()) 
				 {
				 	JOptionPane.showMessageDialog(null, "Save Complete!");
				 	start = false;
				 	controller = new LA_controller(captureView);
				 }
		}
		
	}
	
	public class KeyButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			JFileChooser fc = new JFileChooser("/Users/");
			fc.setDialogTitle("Select Key file");
			fc.setFileFilter(new FileNameExtensionFilter("json", "json")); //.pdf 파일만 선택하도록
			fc.setMultiSelectionEnabled(false);                          //다중 선택 불가
	        fc.showOpenDialog(null); // showOpenDialog는 창을 띄우는데 어느 위치에 띄울건지 인자를 받고
	                                                      // 그리고 선택한 파일의 경로값을 반환한다.

	        if(fc.getSelectedFile() != null)  {
	        	String []language = {"한국어", "English"};
	        	int selected = JOptionPane.showOptionDialog(null, "Language", "The language is the lecture in",
	        	  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,language, language[0]);
	        	if(selected != JOptionPane.CLOSED_OPTION) {
	        		keyPath = fc.getSelectedFile().toString();
	        		try {
	        			controller.Authentication(keyPath,langCode(language[selected]));
	        			captureView.setLabel2(keyPath);
	        		} catch (IOException e) {
	        			keyPath = null;
	        			JOptionPane.showMessageDialog(null, "This key file does not work.");
	        		}
	        	}
	        }
		}
		
		public String langCode(String lang) {
			switch(lang) {
				case "한국어": return "ko-KR";
				case "English" : return "en-US";
			}
			return "en-US";
		}
	}
	
	public CaptureEvent(CaptureView captureView) {
		controller = new LA_controller(captureView);
		this.captureView = captureView;
		start = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {	// 마우스 드레그 이벤트
		curX = e.getX();
		curY = e.getY();
		
		if(clickedShape == 0) captureView.MovePos(curX-beforeX, curY-beforeY);	//검은색 네모 눌렀으면 드래그시 이동
		else if(clickedShape == -1) captureView.MovePanel(curX-beforeX, curY-beforeY);
		else captureView.Resize(curX-beforeX, curY-beforeY, clickedShape);		// 초록색 네모 눌렀으면 드래그시 resize
		beforeX = curX;
		beforeY = curY;
	}

	@Override
	public void mousePressed(MouseEvent e) {	// 마우스 누를때 이벤트
		beforeX = e.getX();
		beforeY = e.getY();	
		this.clickedShape = captureView.Clicked(beforeX,beforeY);	// 무엇을 눌렀는지 int로 저장
		if(clickedShape == 0) captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		else if(clickedShape == -1) captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		else  captureView.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {
		captureView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public Thread getThread() {
		return Thread.currentThread();
	}
}
