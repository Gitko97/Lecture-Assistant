import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SaveIMG implements Runnable{ // 확인, 디버깅용 클래스
	
	public void run() {
		int i = 0;
		while(true) {
			
			if(Capturing.captureIMG.size()>i && Capturing.captureIMG.size()!=0) {
				System.out.println("save! "+i);
				try {
					ImageIO.write(Capturing.captureIMG.get(i), "png", new File("C:\\Users\\xcvds\\eclipse-workspace\\JavaCapture\\"+i+".png"));
					i++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Thread.yield();
			}
		}
	}
	
	public void savePDF(ArrayList<BufferedImage> imgs) {
		int i=0;
		for(BufferedImage img : imgs) {
			try {
				ImageIO.write(img, "png", new File("C:\\Users\\xcvds\\eclipse-workspace\\JavaCapture\\"+(i++)+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
