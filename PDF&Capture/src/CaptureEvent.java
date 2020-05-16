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
	boolean ready = false;
	CaptureView captureView;

	public CaptureEvent(CaptureView captureView) {
		this.captureView = captureView;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		curX = e.getX();
		curY = e.getY();
		captureView.madeNew(curX-beforeX, curY-beforeY);
		beforeX = curX;
		beforeY = curY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		beforeX = e.getX();
		beforeY = e.getY();	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	
}
