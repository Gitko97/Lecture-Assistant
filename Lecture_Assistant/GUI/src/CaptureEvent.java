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
  public class PDFButtonEvent implements ActionListener {

    @Override
	public void actionPerformed(ActionEvent arg0) {
	  JFileChooser fc = new JFileChooser("/Users/");
	  fc.setDialogTitle("Select pdf file");
		   
	  fc.setFileFilter(new FileNameExtensionFilter("pdf", "pdf")); //.pdf �뙆�씪留� �꽑�깮�븯�룄濡�
	  fc.setMultiSelectionEnabled(false);                          //�떎以� �꽑�깮 遺덇�

	  fc.showOpenDialog(null); // showOpenDialog�뒗 李쎌쓣 �쓣�슦�뒗�뜲 �뼱�뒓 �쐞移섏뿉 �쓣�슱嫄댁� �씤�옄瑜� 諛쏄퀬
	                                                      // 洹몃━怨� �꽑�깮�븳 �뙆�씪�쓽 寃쎈줈媛믪쓣 諛섑솚�븳�떎.
	        
	        
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
	  } else { 
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
	  if(start) {
	    if(controller.exit()) {
		  JOptionPane.showMessageDialog(null, "Save Complete!");
		  start = false;
		  controller = new LaController(captureView);
		}
	  }
    }
		
  }
	
  public class KeyButtonEvent implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
	  // TODO Auto-generated method stub
      JFileChooser fc = new JFileChooser("/Users/");
	  fc.setDialogTitle("Select Key file");
	  fc.setFileFilter(new FileNameExtensionFilter("json", "json")); //.pdf �뙆�씪留� �꽑�깮�븯�룄濡�
	  fc.setMultiSelectionEnabled(false);                          //�떎以� �꽑�깮 遺덇�
	  fc.showOpenDialog(null); // showOpenDialog�뒗 李쎌쓣 �쓣�슦�뒗�뜲 �뼱�뒓 �쐞移섏뿉 �쓣�슱嫄댁� �씤�옄瑜� 諛쏄퀬
	                                                      // 洹몃━怨� �꽑�깮�븳 �뙆�씪�쓽 寃쎈줈媛믪쓣 諛섑솚�븳�떎.

	  if(fc.getSelectedFile() != null) {
	    String []language = {"�븳援��뼱", "English"};
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
    controller = new LaController(captureView);
	this.captureView = captureView;
	start = false;
  }

  @Override
  public void mouseDragged(MouseEvent e) {	// 留덉슦�뒪 �뱶�젅洹� �씠踰ㅽ듃
    curX = e.getX();
    curY = e.getY();
		
	if(clickedShape == 0) {
	  captureView.MovePos(curX-beforeX, curY-beforeY);	//寃����깋 �꽕紐� �닃���쑝硫� �뱶�옒洹몄떆 �씠�룞
	} else if(clickedShape == -1) {
	  captureView.MovePanel(curX-beforeX, curY-beforeY);
	} else {
	  captureView.Resize(curX-beforeX, curY-beforeY, clickedShape);		// 珥덈줉�깋 �꽕紐� �닃���쑝硫� �뱶�옒洹몄떆 resize
	}
	beforeX = curX;
	beforeY = curY;
  }

  @Override
  public void mousePressed(MouseEvent e) {	// 留덉슦�뒪 �늻瑜쇰븣 �씠踰ㅽ듃
    beforeX = e.getX();
	beforeY = e.getY();	
	this.clickedShape = captureView.Clicked(beforeX,beforeY);	// 臾댁뾿�쓣 �닃���뒗吏� int濡� ���옣
	if(clickedShape == 0) {
	  captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	} else if(clickedShape == -1) {
	  captureView.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	} else {
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

  public Thread getThread() {
    return Thread.currentThread();
  }
}
