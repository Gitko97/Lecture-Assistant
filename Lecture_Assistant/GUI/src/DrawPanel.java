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
	
  public DrawPanel(int x,int y, int width, int height){ // �뙣�꼸�쓽 �슂�냼 珥덇린�솕
    rectangles = new ArrayList<Rectangle>();
	rectangles.add(new Rectangle(x, y, width, height));	//寃����깋 諛뺤뒪
		
	//珥덈줉�깋 諛뺤뒪//
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
	
  public void captureStart() {
    this.captureStart = true;
  }
	
  public void captureStop() {
    this.captureStart = false;
  }
  
  public void makeNewSizeButton() {		// ��吏곸��쓣�븣 �씠�쟾 �슂�냼�뱾�쓣 吏��슦怨� �깉濡� �깮�꽦
    for(int i = 8; i>0;i--) {
			rectangles.remove(i);
    }
	int x = rectangles.get(0).xpos;
	int y = rectangles.get(0).ypos;
	int width = rectangles.get(0).width;
	int height = rectangles.get(0).height;
	
	rectangles.add(new Rectangle(x+5, y+5, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y+5, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y+5, 10, 10));
	rectangles.add(new Rectangle(x+5, y+height/2, 10, 10));
	rectangles.add(new Rectangle(x+5, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height/2, 10, 10));
	rectangles.add(new Rectangle(x+width/2, y-15+height, 10, 10));
	rectangles.add(new Rectangle(x-15+width, y-15+height, 10, 10));
  }
	
  public int Clicked(int x, int y) {	// 臾댁뾿�씠 �겢由��릺�뿀�뒗吏� �뙋�떒 �썑 return 0�� 寃��� �꽕紐� �굹癒몄��뒗 珥덈줉�깋 resize �꽕紐�
	for(int i = 1; i<rectangles.size(); i++) {
			if(rectangles.get(i).clicked(x,y)) {
			  return i;
			}
	}
		return 0;
  }
	
  public void Resize(int dx, int dy,int clickedShape) {	//resize�븯湲�
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
	
  public void MovePos(int dx, int dy) {	// ��吏곸씠湲�
	for(Rectangle rectangle : rectangles) {
			rectangle.movePos(dx, dy);
	}
  }
	
  public void paintComponent(Graphics g) {	// drawpanel�쓣 洹몃━�뒗 遺�遺�
    int i = 1;
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	float[] dash=new float[]{5,5,5,5};
	if(!captureStart) {
		g2.setColor(Color.GREEN);
	} else {
		g2.setColor(Color.RED);
	}
	g2.setStroke(new BasicStroke(4,0,BasicStroke.JOIN_MITER,1.0f,null, 0.0f));
	g.drawRect(rectangles.get(0).xpos, rectangles.get(0).ypos, rectangles.get(0).width, rectangles.get(0).height);
	if(!captureStart) {
	  g2.setColor(Color.GREEN);
	  g2.fillOval(rectangles.get(0).xpos+rectangles.get(0).width/2-25,rectangles.get(0).ypos+rectangles.get(0).height/2-25,50,50);
	  g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,0));
	  for(;i<rectangles.size();i++) {
				rectangles.get(i).Draw(g2);
	  }
	}
  }

  public Thread getThread() {
    return Thread.currentThread();
  }
}

class Rectangle{	// 紐⑤뱺 �꽕紐� shape�쓽 �젙蹂댁� �븿�닔瑜� �떞怨좎엳�쓬
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