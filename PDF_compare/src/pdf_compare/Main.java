//this is for testing
package pdf_compare;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args)
	{
		File a, b;
		BufferedImage ba=null, bb=null;
		
		a=new File("sampleB.bmp");
		b=new File("sampleC.bmp");
		
		try 
		{
			ba=ImageIO.read(a);
			bb=ImageIO.read(b);
		}
		catch(Exception e) {};
		
//		PDFCompare.setNoise(0.1, 0.35, 0.5);
		System.out.println(PDFCompare.getDifRatio(ba,bb));
		
		File difA, difB;
		BufferedImage dA = null, dB = null;
		
		difA = new File("difPartA.png");
		difB = new File("difPartB.png");
		
		try 
		{
			dA=ImageIO.read(difA);
			dB=ImageIO.read(difB);
		}
		catch(Exception e) {};
		BorderedImage.setBufferedImage(dA, dB);
		try
		{
			File outputImage = new File("writeDown.png");
			ImageIO.write(BorderedImage.extract(), "png", outputImage);
		}
		catch(IOException e)
		{
			System.out.println("exeption: img file saving get error!");
		}
	}
}
