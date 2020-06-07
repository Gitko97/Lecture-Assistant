package test;
import java.awt.image.BufferedImage;

public class IMG_Resize {
	int newHeight;
	int newWidth;
	
	public IMG_Resize(int newWidth,int newHeight ) {	// 새로운 높이, 폭을 지정하며 생성
		this.newHeight = newHeight;
		this.newWidth = newWidth;
	}
	public BufferedImage ResizeIMG(BufferedImage img) {	//매개변수 img를 새로운 높이 폭으로 바꾼뒤 return 
		BufferedImage changed = (BufferedImage) img.getScaledInstance(newWidth, newHeight,BufferedImage.SCALE_SMOOTH);
		return changed;
	}
}
