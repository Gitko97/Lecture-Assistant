package src;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//Class IMG_Resize
//1. args are int newWidth, int newHeight
//2. method ResizeIMG return BufferedImage : convert an image from args into new size that constructed_
public class IMG_Resize {
	int newHeight;
	int newWidth;
	
	public IMG_Resize(int newWidth,int newHeight ) {	// construct with new widht and height
		this.newHeight = newHeight;
		this.newWidth = newWidth;
	}
	public BufferedImage ResizeIMG(BufferedImage img) {	// convert an image from args into new size that constructed_

		BufferedImage changed = new BufferedImage (newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D bGr = changed.createGraphics();
	    bGr.drawImage(img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
	    bGr.dispose();
		return changed;
	}
	
} 
