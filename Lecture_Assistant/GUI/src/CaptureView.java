import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class CaptureView extends JFrame {
	DrawPanel drawpanel ;
	CaptureEvent event;
	
	public ArrayList<Thread> getThread(){
		ArrayList<Thread> threads = new ArrayList<Thread>();
		threads.add(Thread.currentThread());
		threads.add(event.getThread());
		threads.add(drawpanel.getThread());
		return threads;
	}
	public CaptureView(int width, int height) {
	
		///	패널 생성	//
		drawpanel = new DrawPanel(50,50,width,height);
		drawpanel.setBackground(new Color(255,0,0,0));
		
		// 패널 이벤트 추가 //
		event = new CaptureEvent(this);
		drawpanel.addMouseListener(event);				//drawPanel 에 이벤트 추가
		drawpanel.addMouseMotionListener(event);		//drawPanel 에 이벤트 추가
		getContentPane().add(drawpanel);				// 전체 프레임에 drawPanel을 추가
		
		
		///	프레임 설정	//
		
		this.setUndecorated(true);						
		this.setBackground(new Color(255, 0, 0, 0));	// 프레임을 투명하게
		
		this.setAlwaysOnTop(true);						// 프레임 위치를 항상 위로
		
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// 프레임을 모니터 최대 크기로
		this.pack();
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void exit() {
		this.exit();
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
		return drawpanel.Clicked(x, y);
	}
	public void StartCapture() {
		drawpanel.captureStart = true;
		repaint();
	}

	public void captureStart() {
		drawpanel.captureStart();
		repaint();
	}
	
	public void captureStop() {
		drawpanel.captureStop();
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
	
}

