//this is for testing
package pdf_compare;

import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;

public class Main {
	public static void main(String[] args)
	{
		File a, b;
		BufferedImage ba=null, bb=null;
		a=new File("sampleA.bmp");
		b=new File("sampleB.bmp");
		
		try {
		ba=ImageIO.read(a);
		bb=ImageIO.read(b);
		}
		catch(Exception e) {};
		
		PDFCompare p=new PDFCompare(ba, bb);
		p.compare();
		System.out.println(p.show_dif());
		return;
	}
}
