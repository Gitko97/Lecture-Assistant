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
		a=new File("sampleA.bmp");
		b=new File("sampleC.bmp");
		
		try {
		ba=ImageIO.read(a);
		bb=ImageIO.read(b);
		}
		catch(Exception e) {};
		
		
		ImgCompare.setNoise(200);
		PDFCompare.setMarginCut(true);
		PDFCompare.setNoMarginArea(bb);
		if(PDFCompare.compare(ba,bb))
		{
			System.out.println("Different!");
		}

		return;
	}
}
