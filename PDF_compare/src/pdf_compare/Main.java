//this is for testing
package pdf_compare;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args)
	{
		File a, b;
		BufferedImage ba=null, bb=null;
		
		a=new File("sampleC.bmp");
		b=new File("sampleD.bmp");
		
		try 
		{
			ba=ImageIO.read(a);
			bb=ImageIO.read(b);
		}
		catch(Exception e) {};
		
		PDFCompare.setAllowDif(200);
		PDFCompare.setNoise(0.1, 0.35, 0.5);
		PDFCompare.setIsMarginCheckAuto(false);
		if(PDFCompare.compare(ba,bb))
		{
			System.out.println("Different!");
		}

		return;
	}
}
