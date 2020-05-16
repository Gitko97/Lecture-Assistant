import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

class DrawPanel extends JPanel{
	public int xpos ;
	public int ypos ;
	public int width;
	public int height;
	
	public DrawPanel(int x,int y, int width, int height){
		this.xpos = x;
		this.ypos = y;
		this.width = width;
		this.height = height;
	}
	public void movePos(int dx, int dy) {
		this.xpos += dx;
		this.ypos += dy;
		this.removeAll();
		revalidate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		float[] dash=new float[]{5,5,5,5};
		g2.setStroke(new BasicStroke(4,0,BasicStroke.JOIN_MITER,1.0f,dash, 0));
		g2.drawRect(xpos, ypos, width, height);
		g2.fillOval(xpos+width/2-50,ypos+height/2-50,100,100);
	}
	
	
}