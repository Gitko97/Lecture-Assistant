import java.awt.*;
import javax.swing.*;

public class CaptureView extends JFrame {
	DrawPanel drawpanel ;
	public CaptureView() {
	

		drawpanel = new DrawPanel(50,50,900,900);
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
	
	public void MovePos(int dx, int dy) {
		this.remove(drawpanel);
		this.revalidate();
		this.repaint();
		drawpanel.MovePos(dx, dy);
		getContentPane().add(drawpanel);
		this.revalidate();
		this.repaint();
		
	}
	
	public void Resize(int dx,int dy, int clickedShape) {
		this.remove(drawpanel);
		this.revalidate();
		this.repaint();
		drawpanel.Resize(dx, dy, clickedShape);
		getContentPane().add(drawpanel);
		this.revalidate();
		this.repaint();
	}
	
	public int Clicked(int x, int y) {
		return drawpanel.Clicked(x, y);
	}
	public void StartCapture() {
		drawpanel.captureStart = true;
		repaint();
	}

	public void StartMove() {
		drawpanel.captureStart = false;
		repaint();
	}
	
	public int[] getInfo() {
		int[] information = new int[4];
		information[0] = drawpanel.rectangles.get(0).xpos;
		information[1] = drawpanel.rectangles.get(0).ypos;
		information[2] = drawpanel.rectangles.get(0).width;
		information[3] = drawpanel.rectangles.get(0).height;
				
		return information;
	}
}

