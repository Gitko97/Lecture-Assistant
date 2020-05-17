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
	public void mouseDragged(MouseEvent e) {
		curX = e.getX();
		curY = e.getY();
		if(clickedShape == 0) captureView.MovePos(curX-beforeX, curY-beforeY);
		else captureView.Resize(curX-beforeX, curY-beforeY, clickedShape);
		beforeX = curX;
		beforeY = curY;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		beforeX = e.getX();
		beforeY = e.getY();	
		this.clickedShape = captureView.Clicked(beforeX,beforeY);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
