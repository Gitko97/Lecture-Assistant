package src;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class Capturing extends TimerTask {
  int x;
  int y;
  int width;
  int height;
	
  ArrayList<BufferedImage> captureIMG = new ArrayList<>();
	
  public Capturing(int[] information) {	// 罹≪퀜 踰붿쐞 珥덇린�솕
    this.x = information[0];
	this.y = information[1];
	this.width = information[2];
	this.height = information[3];
  }
	
  public void run() {
    Robot robot;
	try {
	  robot = new Robot();
	  captureIMG.add(robot.createScreenCapture(new Rectangle(x+2,y+2,width-4,height-4)));	//�씠 �겢�옒�뒪�쓽 static arrayList�뿉 �씠誘몄� 異붽�
	} catch (AWTException e) {
	  e.printStackTrace();
	}
  }
	
  public void syncPosition(int[] information) { // 罹≪퀜 踰붿쐞 �룞湲고솕
    this.x = information[0];
	this.y = information[1];
	this.width = information[2];
	this.height = information[3];
  }
	
  public BufferedImage getCaptureImg(int index) throws ArrayIndexOutOfBoundsException{
	return captureIMG.get(index);
  }
	
  public boolean endPos(int index) {
	if(index == captureIMG.size()-1) {
	  return true;
	} else {
	  return false;
	}
  }
}
