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
	JLabel lblNewLabel_1 ;
	JButton btnNewButton_2;
	JButton btnNewButton_3;
	CaptureEvent event;
	public ArrayList<Thread> getThread(){
		ArrayList<Thread> threads = new ArrayList<Thread>();
		threads.add(Thread.currentThread());
		threads.add(event.getThread());
		threads.add(drawpanel.getThread());
		return threads;
	}
	public CaptureView(int width, int height) {
		
		panelPos = new Point(1200,200);
		panelSize = new Point(313, 304);
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(panelPos.x,panelPos.y, panelSize.x, panelSize.y);
		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK,10),"Lecture Assistant"));

		event = new CaptureEvent(this);
		
		lblNewLabel = new JLabel("your_pdf_file_path");
		lblNewLabel.setFont(new Font("Dialog",Font.ITALIC, 12));
		lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
		lblNewLabel.setBounds(20, 40, 270, 15);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.lightGray);
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("your_key_file_path");
		lblNewLabel_1.setFont(new Font("Dialog",Font.ITALIC, 12));
		lblNewLabel_1.setHorizontalAlignment(JLabel.CENTER);
		lblNewLabel_1.setBounds(20, 155, 270, 15);
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.lightGray);
		panel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Lecture_PDF");
		btnNewButton.setBounds(90, 70, 127, 23);
		btnNewButton.addActionListener(event.new PDFButtonEvent());
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("STT KEY");
		btnNewButton_1.setBounds(110, 185, 97, 23);
		btnNewButton_1.addActionListener(event.new KeyButtonEvent());
		panel.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Start");
		btnNewButton_2.setBounds(50, 254, 97, 23);
		btnNewButton_2.addActionListener(event.new StartButtonEvent());
		panel.add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("Exit");
		btnNewButton_3.setBounds(50, 254, 97, 23);
		btnNewButton_3.addActionListener(event.new ExitButtonEvent());
		btnNewButton_3.setVisible(false);
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Close");
		btnNewButton_4.setBounds(170, 256, 97, 23);
		btnNewButton_4.addActionListener(event.new CloseButtonEvent());
		panel.add(btnNewButton_4);
		///	패널 생성	//
		drawpanel = new DrawPanel(50,50,width,height);
		drawpanel.setOpaque(false);
		// 패널 이벤트 추가 //
		
		drawpanel.addMouseListener(event);				//drawPanel 에 이벤트 추가
		drawpanel.addMouseMotionListener(event);		//drawPanel 에 이벤트 추가
		
						// 전체 프레임에 drawPanel을 추가
		
		getContentPane().add(panel);
		getContentPane().add(drawpanel);	
		
		///	프레임 설정	//
		
		this.setUndecorated(true);						
		this.setBackground(new Color(255, 0, 0, 0));	// 프레임을 투명하게
		this.setAlwaysOnTop(true);						// 프레임 위치를 항상 위로
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// 프레임을 모니터 최대 크기로
		this.pack();
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		this.setVisible(true);
		
		 try { 
	        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	        }catch(Exception e) { 
	        		System.out.println("Error setting Java LAF: " + e);
	        }
	}
	
	public void setLabel1(String content) {
		lblNewLabel.setText(content);
	}
	
	public void setLabel2(String content) {
		lblNewLabel_1.setText(content);
	}
	
	
	public void exit() {
		this.dispose();
		System.exit(1);
	}
	public void MovePos(int dx, int dy) {	//네모 박스 움직이기 Event에서 호출
		this.remove(drawpanel);	//패널을 지우고
		this.revalidate();		
		this.repaint();			// 그리기
		drawpanel.MovePos(dx, dy);	//패널의 박스 위치 옮기기
		getContentPane().add(drawpanel);
		this.revalidate();
		this.repaint();
		
	}
	
	public void Resize(int dx,int dy, int clickedShape) {	//네모 박스 사이즈 바꾸기
		this.remove(drawpanel);
		this.revalidate();
		this.repaint();
		drawpanel.Resize(dx, dy, clickedShape);
		getContentPane().add(drawpanel);
		this.revalidate();
		this.repaint();
	}
	
	public int Clicked(int x, int y) {	// 박스, 초록색 resize 버튼 무엇을 클릭했는지 return, 0은 검은색 나머지는 초록색
		if(panelPos.x<x && panelPos.y<y && panelPos.x+panelSize.x>x && panelPos.y+panelSize.y>y) return -1;
		return drawpanel.Clicked(x, y);
	}

	public void captureStart() {
		drawpanel.captureStart();
		this.btnNewButton_2.setVisible(false);
		this.btnNewButton_3.setVisible(true);
		repaint();
	}
	
	public void captureStop() {
		drawpanel.captureStop();
		this.btnNewButton_2.setVisible(true);
		this.btnNewButton_3.setVisible(false);
		repaint();
	}
	
	public void StartMove() {
		drawpanel.captureStart = false;
		repaint();
	}
	
	public int[] getInfo() {	//현재 위치 좌표 return  
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

