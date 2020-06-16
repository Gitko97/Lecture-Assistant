package src;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class CaptureView extends JFrame {
  DrawPanel drawpanel ;
  JPanel panel;
  Point panelPos;
  Point panelSize;
  JLabel lblNewLabel;
  JLabel lblNewLabel1;
  JButton btnNewButton2;
  JButton btnNewButton3;
  CaptureEvent event;
  
  public CaptureView(int width, int height) {
	
	//Init Button Panel
    panelPos = new Point(1200, 200);
	panelSize = new Point(313, 304);
	panel = new JPanel();
	panel.setLayout(null);
	panel.setBounds(panelPos.x, panelPos.y, panelSize.x, panelSize.y);
	panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK,10), "Lecture Assistant"));
	
  
	event = new CaptureEvent(this);
	
	//Label showing the filepath of user-selected pdf file
	lblNewLabel = new JLabel("your_pdf_file_path");
	lblNewLabel.setFont(new Font("Dialog",Font.ITALIC, 12));
	lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
	lblNewLabel.setBounds(20, 40, 270, 15);
	lblNewLabel.setOpaque(true);
	lblNewLabel.setBackground(Color.lightGray);
	panel.add(lblNewLabel);
	
	//Label showing the filepath of user-selected key file
	lblNewLabel1 = new JLabel("your_key_file_path");
	lblNewLabel1.setFont(new Font("Dialog",Font.ITALIC, 12));
	lblNewLabel1.setHorizontalAlignment(JLabel.CENTER);
	lblNewLabel1.setBounds(20, 155, 270, 15);
	lblNewLabel1.setOpaque(true);
	lblNewLabel1.setBackground(Color.lightGray);
	panel.add(lblNewLabel1);
	
	
	//Lecture choose Button
	JButton btnNewButton = new JButton("Lecture_PDF");
	btnNewButton.setBounds(90, 70, 127, 23);
	btnNewButton.addActionListener(event.new PDFButtonEvent());
	panel.add(btnNewButton);
	
	
	//Google Speech Key File Choose Button
	JButton btnNewButton_1 = new JButton("STT KEY");
	btnNewButton_1.setBounds(110, 185, 97, 23);
	btnNewButton_1.addActionListener(event.new KeyButtonEvent());
	panel.add(btnNewButton_1);
	
	//Start Button
	btnNewButton2 = new JButton("Start");
	btnNewButton2.setBounds(50, 254, 97, 23);
	btnNewButton2.addActionListener(event.new StartButtonEvent());
	panel.add(btnNewButton2);
	
	//Exit Button
	btnNewButton3 = new JButton("Exit");
	btnNewButton3.setBounds(50, 254, 97, 23);
	btnNewButton3.addActionListener(event.new ExitButtonEvent());
	btnNewButton3.setVisible(false);
	panel.add(btnNewButton3);
	
	//Close Button
	JButton btnNewButton4 = new JButton("Close");
	btnNewButton4.setBounds(170, 256, 97, 23);
	btnNewButton4.addActionListener(event.new CloseButtonEvent());
	panel.add(btnNewButton4);
	
	//Make DrawPanel this DrawPanel work for Capture Area
	drawpanel = new DrawPanel(50,50,width,height);
	drawpanel.setOpaque(false);  //Make DrawPanel Transparent
	drawpanel.addMouseListener(event);				
	drawpanel.addMouseMotionListener(event);	
		
	//Add panel to whole Frame
	getContentPane().add(panel);
	getContentPane().add(drawpanel);	
	
	//Make Frame Transparent
	this.setUndecorated(true);						
	this.setBackground(new Color(255, 0, 0, 0));	
	this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	this.setAlwaysOnTop(true);						
	this.setExtendedState(JFrame.MAXIMIZED_BOTH);	
	
	//Start GUI
	this.setVisible(true);
		
	try { 
	  UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch(Exception e) { 
	  System.out.println("Error setting Java LAF: " + e);
	}
  }

  //This method change Label with content (PDF file path)
  public void setLabel1(String content) {
	lblNewLabel.setText(content);
  }

  //This method change Label with content (Key file path)
  public void setLabel2(String content) {
	lblNewLabel1.setText(content);
  }
	
  //Exit this program
  public void exit() {
    this.dispose();
	System.exit(1);
  }
  
  //When Capture Area Moving
  public void MovePos(int dx, int dy) {
	//DrawPanel have to be remake 
    this.remove(drawpanel);	
	this.revalidate();		
	this.repaint();			
	
	drawpanel.MovePos(dx, dy);	//Move DrawPanel
	
	//DrawPanel Readded to frame
	getContentPane().add(drawpanel);
	this.revalidate();
	this.repaint();
		
  }
  
  //When Capture Area Resize
  public void Resize(int dx,int dy, int clickedShape) {	
	//DrawPanel have to be remake 
	this.remove(drawpanel);
	this.revalidate();
	this.repaint();
	
	drawpanel.Resize(dx, dy, clickedShape);  //Resize DrawPanel
	
	//DrawPanel Readded to frame
	getContentPane().add(drawpanel);
	this.revalidate();
	this.repaint();
  }

  //Return clicked element based on clicked position
  public int Clicked(int x, int y) {	
	if(panelPos.x<x && panelPos.y<y && panelPos.x+panelSize.x>x && panelPos.y+panelSize.y>y) { //When press Button Panel
	  return -1;
	}
	return drawpanel.Clicked(x, y); //When Press capture Area 0 will be Green Area
  }

  //This Method make capture area Red
  public void captureStart() {
    drawpanel.captureStart();
	this.btnNewButton2.setVisible(false);
	this.btnNewButton3.setVisible(true);
	repaint();
  }

  //This Method make capture area green
  public void captureStop() {
	drawpanel.captureStop();
	this.btnNewButton2.setVisible(true);
	this.btnNewButton3.setVisible(false);
	repaint();
  }

  //Return Capture Area's position
  public int[] getInfo() {
	int[] information = new int[4];
	information[0] = drawpanel.rectangles.get(0).xpos;
	information[1] = drawpanel.rectangles.get(0).ypos;
	information[2] = drawpanel.rectangles.get(0).width;
	information[3] = drawpanel.rectangles.get(0).height;
				
	return information;
  }
  
  //Make Mouse cursor with parameter
  public void cursorDefault(Cursor curCursor) {
    setCursor(curCursor);
  }
  
  //Move Button Panel
  public void MovePanel(int i, int j) {
	panelPos.x += i; 
	panelPos.y += j; 
	panel.setBounds(panelPos.x, panelPos.y, panelSize.x, panelSize.y);
  }	
	
}

