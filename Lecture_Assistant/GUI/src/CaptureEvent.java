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
  LaController controller;
  
  
  
  // This Sub-Class is for events in PDF Select button.
  public class PDFButtonEvent implements ActionListener {

    @Override
	public void actionPerformed(ActionEvent arg0) {
	  JFileChooser fc = new JFileChooser("/Users/");
	  fc.setDialogTitle("Select pdf file");
	  
	  // File filter that allows users to choose only .pdf
	  fc.setFileFilter(new FileNameExtensionFilter("pdf", "pdf")); 
	  fc.setMultiSelectionEnabled(false);                          

	  fc.showOpenDialog(null); 
	        
	        
	  if(fc.getSelectedFile() != null) {
		//file path that user choose 
	    pdfPath = fc.getSelectedFile().toString(); 
	    try {
	      controller.GetLecturePDF(pdfPath);
	      captureView.setLabel1(pdfPath);	//set Label to pdf's Path
	      } catch (IOException e) {
	    	//If pdf file cannot be read to BufferedImages
	        pdfPath = null;
	        JOptionPane.showMessageDialog(null, "This pdf file cannot be read");
	      }
	    }
	  }
		
  }
  
  //This Sub-Class is for events Close button.
  public class CloseButtonEvent implements ActionListener {

    @Override
	public void actionPerformed(ActionEvent arg0) {
	  captureView.exit(); // this method will exit the program
	}
		
  }
  
  //This Sub-Class is for events Start button.
  public class StartButtonEvent implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      
      //Program cannot be start without select pdf and key file
	  if(pdfPath == null || keyPath == null) {
	    JOptionPane.showMessageDialog(null, "You have to input PDF File and Key File");
	  } else { 
		start = true; 
		try {
		  controller.start();
		  } catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Microphone not support");
		  }
	  }
	}
		
  }
  
  //This Sub-Class is for events Exit button.
  public class ExitButtonEvent implements ActionListener {

    @Override
	public void actionPerformed(ActionEvent arg0) {
    	
	  if(start) {
	    if(controller.exit()) {
		  JOptionPane.showMessageDialog(null, "Save Complete!");
		  start = false;
		}
	  }
    }
		
  }

  //This Sub-Class is for events Exit button.
  public class KeyButtonEvent implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      JFileChooser fc = new JFileChooser("/Users/");
	  fc.setDialogTitle("Select Key file");
	  
	  // File filter that allows users to choose only .json
	  fc.setFileFilter(new FileNameExtensionFilter("json", "json"));
	  fc.setMultiSelectionEnabled(false);                          
	  fc.showOpenDialog(null); 

	  if(fc.getSelectedFile() != null) {
	    String []language = {"한국어", "English"}; //languages that user can choose
	    
	    //Language Selection View and save it in int variable
	    int selected = JOptionPane.showOptionDialog(null, "Language", "The language is the lecture in",
	     JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,language, language[0]);
	    
	    
	    if(selected != JOptionPane.CLOSED_OPTION) { //IF User Choose option
	      keyPath = fc.getSelectedFile().toString();
	      try {
	        controller.Authentication(keyPath,langCode(language[selected])); //langCode() returns the code for language of user-selected options.
	        captureView.setLabel2(keyPath);
	      } catch (IOException e) {
	    	//When key file cannot be certified by google speech api
	        keyPath = null;
	        JOptionPane.showMessageDialog(null, "This key file does not work.");
	      }
	    }
	  }
    }
	
    //Give Language Code based on Language Code Table
	public String langCode(String lang) {
	  switch(lang) {
	    case "한국어": return "ko-KR";
		case "English" : return "en-US";
	  }
	  return "en-US";
	}
  }
	
  public CaptureEvent(CaptureView captureView) {
    controller = new LaController(captureView);
	this.captureView = captureView;
	start = false;
  }

  @Override
  //Mouse Dragged Event
  //This method is responsible for Resize, Move of GUI element.
  public void mouseDragged(MouseEvent e) {	
    curX = e.getX();
    curY = e.getY();
		
	if(clickedShape == 0) {  //When user try to move Capture Area
	  captureView.MovePos(curX-beforeX, curY-beforeY);	
	} else if(clickedShape == -1) {  //When user try to move Button Panel
	  captureView.MovePanel(curX-beforeX, curY-beforeY);
	} else {  //When user try to resize Capture Area
	  captureView.Resize(curX-beforeX, curY-beforeY, clickedShape);		
	}
	beforeX = curX;
	beforeY = curY;
  }

  @Override
  //Mouse Pressed Event
  //This method determines which GUI elements the user pressed.
  public void mousePressed(MouseEvent e) {	
    beforeX = e.getX();
	beforeY = e.getY();	
	this.clickedShape = captureView.Clicked(beforeX,beforeY);  //Determine which element user pressed
	
	if(clickedShape == 0) {  //When Press Capture Area
	  captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	} else if(clickedShape == -1) {  //When Press Button Panel
	  captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	} else {  //When Press Resize Rectangle
	  captureView.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
  }
	
  @Override
  public void mouseMoved(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {
    captureView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

}
