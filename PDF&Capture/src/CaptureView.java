import java.awt.*;
import javax.swing.*;

public class CaptureView extends JFrame {
	DrawPanel drawpanel ;
	public CaptureView() {
	

		DrawPanel drawpanel = new DrawPanel(50,50,700,700);
		drawpanel.setBackground(new Color(255,0,0,0));
		
		CaptureEvent event = new CaptureEvent(drawpanel);
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
}

