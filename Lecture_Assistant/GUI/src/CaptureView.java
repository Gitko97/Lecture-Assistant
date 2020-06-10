package src;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class CaptureView extends JFrame {
  DrawPanel drawpanel ;
  JPanel panel;
  Point panelPos;
  Point panelSize;
  JLabel lblNewLabel;
  JLabel lblNewLabel1 ;
  JButton btnNewButton2;
  JButton btnNewButton3;
  CaptureEvent event;
  
  public ArrayList<Thread> getThread() {
    ArrayList<Thread> threads = new ArrayList<Thread>();
	threads.add(Thread.currentThread());
	threads.add(event.getThread());
	threads.add(drawpanel.getThread());
	return threads;
  }
  public CaptureView(int width, int height) {
		
    panelPos = new Point(1200, 200);
	panelSize = new Point(313, 304);
	panel = new JPanel();
	panel.setLayout(null);
	panel.setBounds(panelPos.x, panelPos.y, panelSize.x, panelSize.y);
	panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 10), "Lecture Assistant"));

	event = new CaptureEvent(this);
		
	lblNewLabel = new JLabel("your_pdf_file_path");
	lblNewLabel.setFont(new Font("Dialog", Font.ITALIC, 12));
	lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
	lblNewLabel.setBounds(20, 40, 270, 15);
	lblNewLabel.setOpaque(true);
	lblNewLabel.setBackground(Color.lightGray);
	panel.add(lblNewLabel);
		
	lblNewLabel1 = new JLabel("your_key_file_path");
	lblNewLabel1.setFont(new Font("Dialog", Font.ITALIC, 12));
	lblNewLabel1.setHorizontalAlignment(JLabel.CENTER);
	lblNewLabel1.setBounds(20, 155, 270, 15);
	lblNewLabel1.setOpaque(true);
	lblNewLabel1.setBackground(Color.lightGray);
	panel.add(lblNewLabel1);
		
	JButton btnNewButton = new JButton("Lecture_PDF");
	btnNewButton.setBounds(90, 70, 127, 23);
	btnNewButton.addActionListener(event.new PDFButtonEvent());
	panel.add(btnNewButton);
		
	JButton btnNewButton_1 = new JButton("STT KEY");
	btnNewButton_1.setBounds(110, 185, 97, 23);
	btnNewButton_1.addActionListener(event.new KeyButtonEvent());
	panel.add(btnNewButton_1);
		
	btnNewButton2 = new JButton("Start");
	btnNewButton2.setBounds(50, 254, 97, 23);
	btnNewButton2.addActionListener(event.new StartButtonEvent());
	panel.add(btnNewButton2);
		
	btnNewButton3 = new JButton("Exit");
	btnNewButton3.setBounds(50, 254, 97, 23);
	btnNewButton3.addActionListener(event.new ExitButtonEvent());
	btnNewButton3.setVisible(false);
	panel.add(btnNewButton3);
		
	JButton btnNewButton4 = new JButton("Close");
	btnNewButton4.setBounds(170, 256, 97, 23);
	btnNewButton4.addActionListener(event.new CloseButtonEvent());
	panel.add(btnNewButton4);
	///	�뙣�꼸 �깮�꽦	//
	drawpanel = new DrawPanel(50, 50, width, height);
	drawpanel.setOpaque(false);
	// �뙣�꼸 �씠踰ㅽ듃 異붽� //
		
	drawpanel.addMouseListener(event);				// drawPanel �뿉 �씠踰ㅽ듃 異붽�
	drawpanel.addMouseMotionListener(event);		// drawPanel �뿉 �씠踰ㅽ듃 異붽�
		
	// �쟾泥� �봽�젅�엫�뿉 drawPanel�쓣 異붽�
		
	getContentPane().add(panel);
	getContentPane().add(drawpanel);	
		
	///	�봽�젅�엫 �꽕�젙	//
		
	this.setUndecorated(true);						
	this.setBackground(new Color(255, 0, 0, 0));	// �봽�젅�엫�쓣 �닾紐낇븯寃�
	this.setAlwaysOnTop(true);						// �봽�젅�엫 �쐞移섎�� �빆�긽 �쐞濡�
	this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// �봽�젅�엫�쓣 紐⑤땲�꽣 理쒕� �겕湲곕줈
	// this.pack();
	this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	this.setVisible(true);
		
	try { 
	  UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch(Exception e) { 
	  System.out.println("Error setting Java LAF: " + e);
	}
  }
	
  public void setLabel1(String content) {
    lblNewLabel.setText(content);
  }
	
  public void setLabel2(String content) {
    lblNewLabel1.setText(content);
  }
	
	
  public void exit() {
    this.dispose();
	System.exit(1);
  }
  public void MovePos(int dx, int dy) {	// �꽕紐� 諛뺤뒪 ��吏곸씠湲� Event�뿉�꽌 �샇異�
    this.remove(drawpanel);	// �뙣�꼸�쓣 吏��슦怨�
	this.revalidate();		
	this.repaint();			// 洹몃━湲�
	drawpanel.MovePos(dx, dy);	// �뙣�꼸�쓽 諛뺤뒪 �쐞移� �삷湲곌린
	getContentPane().add(drawpanel);
	this.revalidate();
	this.repaint();
		
  }
	
  public void Resize(int dx, int dy, int clickedShape) {	// �꽕紐� 諛뺤뒪 �궗�씠利� 諛붽씀湲�
    this.remove(drawpanel);
	this.revalidate();
	this.repaint();
	drawpanel.Resize(dx, dy, clickedShape);
	getContentPane().add(drawpanel);
	this.revalidate();
	this.repaint();
  }
	
  public int Clicked(int x, int y) {	// 諛뺤뒪, 珥덈줉�깋 resize 踰꾪듉 臾댁뾿�쓣 �겢由��뻽�뒗吏� return, 0�� 寃����깋 �굹癒몄��뒗 珥덈줉�깋
    if(panelPos.x<x && panelPos.y<y && panelPos.x+panelSize.x>x && panelPos.y+panelSize.y>y) {
      return -1;
    }
	  return drawpanel.Clicked(x, y);
	}

  public void captureStart() {
    drawpanel.captureStart();
	this.btnNewButton2.setVisible(false);
	this.btnNewButton3.setVisible(true);
	repaint();
  }
	
  public void captureStop() {
    drawpanel.captureStop();
	this.btnNewButton2.setVisible(true);
	this.btnNewButton3.setVisible(false);
	repaint();
  }
	
  public void StartMove() {
    drawpanel.captureStart = false;
	repaint();
  }
	
  public int[] getInfo() {	// �쁽�옱 �쐞移� 醫뚰몴 return  
    int[] information = new int[4];
	information[0] = drawpanel.rectangles.get(0).xpos;
	information[1] = drawpanel.rectangles.get(0).ypos;
	information[2] = drawpanel.rectangles.get(0).width;
	information[3] = drawpanel.rectangles.get(0).height;
				
	return information;
  }
  public void cursorDefault(Cursor curCursor) {
    setCursor(curCursor);
  }
  public void MovePanel(int i, int j) {
    // TODO Auto-generated method stub
	panelPos.x += i; 
	panelPos.y += j; 
	panel.setBounds(panelPos.x, panelPos.y, panelSize.x, panelSize.y);
  }	
	
}

