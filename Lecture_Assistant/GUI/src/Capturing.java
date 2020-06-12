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
	
  ArrayList<BufferedImage> captureIMG = new ArrayList<>(); //This variable save Captured Images
	
  public Capturing(int[] information) {	
    this.x = information[0];
	this.y = information[1];
	this.width = information[2];
	this.height = information[3];
  }
	
  public Capturing() {
		this.x = 50;
		this.y = 50;
		this.width = 900;
		this.height = 900;
	}
  
  public void run() {
    Robot robot;
	try {
	  robot = new Robot();
	  captureIMG.add(robot.createScreenCapture(new Rectangle(x+2, y+2, width-4, height-4))); //Capture image of capture area and add to captureIMG variable
	} catch (AWTException e) {
	  e.printStackTrace();
	}
  }

  //Move Capture Area
  public void syncPosition(int[] information) {
    this.x = information[0];
	this.y = information[1];
	this.width = information[2];
	this.height = information[3];
  }
  
  //Return image in index
  public BufferedImage getCaptureImg(int index) throws ArrayIndexOutOfBoundsException{
	return captureIMG.get(index);
  }

  //Judge index is final index of capture image
  public boolean endPos(int index) {
    if(index == captureIMG.size()-1) {
      return true;
    } else {
      return false;
    }
  }
}
