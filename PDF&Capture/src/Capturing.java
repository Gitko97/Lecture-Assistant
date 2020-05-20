import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Capturing extends TimerTask {
	int x;
	int y;
	int width;
	int height;
	
	static ArrayList<BufferedImage> captureIMG = new ArrayList<>();
	
	public Capturing(int[] information) {	// 캡쳐 범위 초기화
		this.x = information[0];
		this.y = information[1];
		this.width = information[2];
		this.height = information[3];
	}
	
	public void run() {
		Robot robot;
		try {
			robot = new Robot();
			Capturing.captureIMG.add(robot.createScreenCapture(new Rectangle(x+2,y+2,width-4,height-4)));	//이 클래스의 static arrayList에 이미지 추가
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
