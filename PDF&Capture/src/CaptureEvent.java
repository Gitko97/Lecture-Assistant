import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CaptureEvent implements MouseListener, MouseMotionListener {
	DrawPanel drawpanel;
	int beforeX;
	int beforeY;
	int curX;
	int curY;
	int clickedShape=0;
	CaptureView captureView;

	public CaptureEvent(CaptureView captureView) {
		this.captureView = captureView;
	}

	@Override
	public void mouseDragged(MouseEvent e) {	// 마우스 드레그 이벤트
		curX = e.getX();
		curY = e.getY();
		if(clickedShape == 0) captureView.MovePos(curX-beforeX, curY-beforeY);	//검은색 네모 눌렀으면 드래그시 이동
		else captureView.Resize(curX-beforeX, curY-beforeY, clickedShape);		// 초록색 네모 눌렀으면 드래그시 resize
		beforeX = curX;
		beforeY = curY;
	}

	@Override
	public void mousePressed(MouseEvent e) {	// 마우스 누를때 이벤트
		beforeX = e.getX();
		beforeY = e.getY();	
		this.clickedShape = captureView.Clicked(beforeX,beforeY);	// 무엇을 눌렀는지 int로 저장
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
