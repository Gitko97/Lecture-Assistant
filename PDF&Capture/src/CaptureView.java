import java.awt.*;
import javax.swing.*;

public class CaptureView extends JFrame {
	DrawPanel drawpanel ;
	public CaptureView() {
	

		drawpanel = new DrawPanel(50,50,700,700);
		drawpanel.setBackground(new Color(255,0,0,0));
		
		CaptureEvent event = new CaptureEvent(this);
		drawpanel.addMouseListener(event);
		drawpanel.addMouseMotionListener(event);
		getContentPane().add(drawpanel);
		
		
		
		this.setUndecorated(true);
		this.setBackground(new Color(255, 0, 0, 0));
		this.setAlwaysOnTop(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void madeNew(int dx, int dy) {
		
		this.remove(drawpanel);
		this.revalidate();
		this.repaint();
		drawpanel.movePos(dx, dy);
		getContentPane().add(drawpanel);
		this.revalidate();
		this.repaint();
		
	}
}

