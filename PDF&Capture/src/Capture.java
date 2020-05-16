import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Capture {
	int x;
	int y;
	int width;
	int height;
	Timer m_timer = new Timer();
	TimerTask m_take;
	
	static ArrayList<BufferedImage> captureIMG = new ArrayList<>();
	
	public Capture(int x, int y, int width, int height) {
		this.x = x;
		this.y =y;
		this.width = width;
		this.height = height;
	}
	
	public void run() {
		Robot robot;
		try {
			robot = new Robot();
			m_take = new TimerTask() {

				public void run() {
					System.out.println("capture! "+Capture.captureIMG.size());
					Capture.captureIMG.add(robot.createScreenCapture(new Rectangle(x,y,width,height)));
				}
				
			};
			m_timer.schedule(m_take,1000 ,1000);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}
}
