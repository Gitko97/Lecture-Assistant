package src;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SaveIMG{ // 확인, 디버깅용 클래스
	
	public void save(ArrayList<BufferedImage> imgs,String filePath, String filename) {
		int i = 0;
		for(BufferedImage img : imgs ) {
			
			try {
				ImageIO.write(img, "png", new File(filePath+filename+i+".png"));
				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void save(BufferedImage img,String filePath, String filename) {
	
			try {
				ImageIO.write(img, "png", new File(filePath+filename+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
		
	}
	
}