package src;
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
	rectangles.add(new Rectangle(x, y, width, height));	//Init Capture Rectangle, 0 index will be Capture Area Rectangle
		
	//Init Resize Buttons
	rectangles.add(new Rectangle(x+5, y+5, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y+5, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y+5, 10, 10));
	rectangles.add(new Rectangle(x+5, y+height/2, 10, 10));
	rectangles.add(new Rectangle(x+5, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height/2, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height, 10, 10));
	this.captureStart = false;
  }

  //Make GUI Red
  public void captureStart() {
    this.captureStart = true;
  }

  //Make GUI Green
  public void captureStop() {
    this.captureStart = false;
  }
  
  //This method will be called when user Resize Capture Area
  public void makeNewSizeButton() {
    for(int i = 8; i>0;i--) {
			rectangles.remove(i);
    }
	int x = rectangles.get(0).xpos;
	int y = rectangles.get(0).ypos;
	int width = rectangles.get(0).width;
	int height = rectangles.get(0).height;
	
	//make new resize Buttons
	rectangles.add(new Rectangle(x+5, y+5, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y+5, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y+5, 10, 10));
	rectangles.add(new Rectangle(x+5, y+height/2, 10, 10));
	rectangles.add(new Rectangle(x+5, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height/2, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height, 10, 10));
  }

  //Return Resize Buttons index or 0 (Capture Rectangle)
  public int Clicked(int x, int y) {
	for(int i = 1; i<rectangles.size(); i++) {
			if(rectangles.get(i).clicked(x,y)) {
			  return i;
			}
	}
		return 0;
  }

  //Resize Panel
  public void Resize(int dx, int dy,int clickedShape) {	
    Rectangle rectangle = rectangles.get(0);
    
    //Each case means the position of the pressed button.
	switch(clickedShape) {
	  case 1:  //Left Top (NW)
	    rectangle.ResizeHeight(-dy);
		rectangle.ResizeWidth(-dx);
		rectangle.ResizeX(dx);
		rectangle.ResizeY(dy);
		makeNewSizeButton();
		break;
	  case 2: //Middle Top (N)
		rectangle.ResizeHeight(-dy);
		rectangle.ResizeY(dy);
		makeNewSizeButton();
		break;
	  case 3: //Right Top (NE)
		rectangle.ResizeHeight(-dy);
		rectangle.ResizeWidth(dx);
		rectangle.ResizeY(dy);
		makeNewSizeButton();
		break;
	  case 4: //Left Middle (W)
		rectangle.ResizeWidth(-dx);
		rectangle.ResizeX(dx);
		makeNewSizeButton();
		break;
	  case 5: //Left Bottom (SW)
		rectangle.ResizeHeight(dy);
		rectangle.ResizeWidth(-dx);
		rectangle.ResizeX(dx);
		makeNewSizeButton();
		break;
	  case 6: //Right Middle (E)
		rectangle.ResizeWidth(dx);
		makeNewSizeButton();
		break;
	  case 7: //Middle Bottom (S)
		rectangle.ResizeHeight(dy);
		makeNewSizeButton();
		break;
	  case 8: //Right Bottom (SE)
		rectangle.ResizeHeight(dy);
		rectangle.ResizeWidth(dx);
		makeNewSizeButton();
		break;
	}
  }

  //Move All Rectangle
  public void MovePos(int dx, int dy) {	
	for(Rectangle rectangle : rectangles) {
			rectangle.movePos(dx, dy);
	}
  }

  //Drawing Method
  public void paintComponent(Graphics g) {	
    int i = 1;
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	if(!captureStart) {
		g2.setColor(Color.GREEN);
	} else {
		g2.setColor(Color.RED);
	}
	g2.setStroke(new BasicStroke(4,0,BasicStroke.JOIN_MITER,1.0f,null, 0.0f)); 
	g.drawRect(rectangles.get(0).xpos, rectangles.get(0).ypos, rectangles.get(0).width, rectangles.get(0).height);  //Draw Capture Area Rectangle
	if(!captureStart) {
	  g2.setColor(Color.GREEN);
	  g2.fillOval(rectangles.get(0).xpos+rectangles.get(0).width/2-25,rectangles.get(0).ypos+rectangles.get(0).height/2-25,50,50);
	  g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,0));
	  for(;i<rectangles.size();i++) {
				rectangles.get(i).Draw(g2); //Draw Resize Rectangle
	  }
	}
  }

}

//Class that save Rectangle information
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
	g.fillRect(xpos, ypos, width, height);
  }
	
  public boolean clicked(int x, int y) {
    if(xpos+width+2>x && x>xpos-2 && ypos-2<y && y<ypos+height+2) {
      return true;
    }
	return false;
  }
}