import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

class DrawPanel extends JPanel{
	ArrayList<Rectangle> rectangles;
	boolean captureStart;
	
	public DrawPanel(int x,int y, int width, int height){
		rectangles = new ArrayList<Rectangle>();
		rectangles.add(new Rectangle(x,y,width,height));
		rectangles.add(new Rectangle(x-2, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2+width/2, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2+width, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2, y-2+height/2, 6, 6));
		rectangles.add(new Rectangle(x-2, y-2+height, 6, 6));
		rectangles.add(new Rectangle(x+width, y-2+height/2, 6, 6));
		rectangles.add(new Rectangle(x-2+width/2, y-2+height, 6, 6));
		rectangles.add(new Rectangle(x-2+width, y-2+height, 6, 6));
		this.captureStart = false;
	}
	
	public void makeNewSizeButton() {
		for(int i = 8; i>0;i--)
			rectangles.remove(i);
		int x = rectangles.get(0).xpos;
		int y = rectangles.get(0).ypos;
		int width = rectangles.get(0).width;
		int height = rectangles.get(0).height;
		rectangles.add(new Rectangle(x-2, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2+width/2, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2+width, y-2, 6, 6));
		rectangles.add(new Rectangle(x-2, y-2+height/2, 6, 6));
		rectangles.add(new Rectangle(x-2, y-2+height, 6, 6));
		rectangles.add(new Rectangle(x+width, y-2+height/2, 6, 6));
		rectangles.add(new Rectangle(x-2+width/2, y-2+height, 6, 6));
		rectangles.add(new Rectangle(x-2+width, y-2+height, 6, 6));
	}
	
	public int Clicked(int x, int y) {
		for(int i = 1; i<rectangles.size(); i++)
			if(rectangles.get(i).clicked(x,y)) return i;
		return 0;
	}
	
	public void Resize(int dx, int dy,int clickedShape) {
		Rectangle rectangle = rectangles.get(0);
		switch(clickedShape) {
		case 1: 
			rectangle.ResizeHeight(-dy);
			rectangle.ResizeWidth(-dx);
			rectangle.ResizeX(dx);
			rectangle.ResizeY(dy);
			makeNewSizeButton();
			break;
		case 2:
			rectangle.ResizeHeight(-dy);
			rectangle.ResizeY(dy);
			makeNewSizeButton();
			break;
		case 3:
			rectangle.ResizeHeight(-dy);
			rectangle.ResizeWidth(dx);
			rectangle.ResizeY(dy);
			makeNewSizeButton();
			break;
		case 4:
			rectangle.ResizeWidth(-dx);
			rectangle.ResizeX(dx);
			makeNewSizeButton();
			break;
		case 5:
			rectangle.ResizeHeight(dy);
			rectangle.ResizeWidth(-dx);
			rectangle.ResizeX(dx);
			makeNewSizeButton();
			break;
		case 6:
			rectangle.ResizeWidth(dx);
			makeNewSizeButton();
			break;
		case 7:
			rectangle.ResizeHeight(dy);
			makeNewSizeButton();
			break;
		case 8:
			rectangle.ResizeHeight(dy);
			rectangle.ResizeWidth(dx);
			makeNewSizeButton();
			break;
		}
	}
	
	public void MovePos(int dx, int dy) {
		for(Rectangle rectangle : rectangles)
			rectangle.movePos(dx, dy);
	}
	
	public void paintComponent(Graphics g) {
		int i = 0;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		float[] dash=new float[]{5,5,5,5};
		g2.setStroke(new BasicStroke(4,0,BasicStroke.JOIN_MITER,1.0f,dash, 0));
		rectangles.get(i++).Draw(g2);
		if(!captureStart) {
			//g2.fillOval(xpos+width/2-50,ypos+height/2-50,100,100);
			g2.setColor(Color.GREEN);
			g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,0));
			for(;i<rectangles.size();i++)
				rectangles.get(i).Draw(g2);
		}
	}
}

class Rectangle{
	public int xpos ;
	public int ypos ;
	public int width;
	public int height;
	public Rectangle(int x, int y, int width, int height) {
		this.xpos = x;
		this.ypos = y;
		this.width = width;
		this.height = height;
	}
	
	public void movePos(int dx, int dy) {
		this.xpos += dx;
		this.ypos += dy;
	}
	
	public void ResizeX(int delta) {
		this.xpos += delta;
	}
	
	public void ResizeY(int delta) {
		this.ypos += delta;
	}
	
	public void ResizeWidth(int delta) {
		this.width += delta;
	}
	
	public void ResizeHeight(int delta) {
		this.height += delta;
	}
	
	public void Draw(Graphics2D g) {
		g.drawRect(xpos, ypos, width, height);
	}
	
	public boolean clicked(int x, int y) {
		if(xpos+width+2>x && x>xpos-2 && ypos-2<y && y<ypos+height+2)  return true;
		return false;
	}
}