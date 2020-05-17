import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Capture extends Thread {
	int x;
	int y;
	int width;
	int height;
	Timer m_timer = new Timer();
	TimerTask m_task;
	
	static ArrayList<BufferedImage> captureIMG = new ArrayList<>();
	
	public Capture(int[] information) {
		this.x = information[0];
		this.y = information[1];
		this.width = information[2];
		this.height = information[3];
	}
	
	public void run() {
		Robot robot;
		try {
			robot = new Robot();
			m_task = new TimerTask() {

				public void run() {
					System.out.println("capture! "+Capture.captureIMG.size());
					Capture.captureIMG.add(robot.createScreenCapture(new Rectangle(x,y,width,height)));
					Thread.yield();
				}
				
			};
			m_timer.schedule(m_task, 0 ,1000);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		System.out.println("종료");
		m_timer.cancel();
		m_task.cancel();
	}
}
