package pdf_compare;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

public class PDFCompare extends ImgCompare{
	//NOT YET
	
	//option that need to correct blank area(if main is dark, it will be hard to correct)
	private static BufferedImage deleteBlank(BufferedImage origin)
	{
		BufferedImage result;
		
		int minWidth=0, maxWidth=origin.getWidth();
		int minHeight=0, maxHeight=origin.getHeight();
		
		int[][] rowAverge=new int[maxWidth][3];
		int[][] carAverge=new int[maxHeight][3];
		int tempPixel[]=new int[3];
		
		int countH, countW, count;
		
		//calculate pixel RGB array sum value
		for(countH=0;countH<maxHeight;countH++)
		{
			for(countW=0;countW<maxWidth;countW++)
			{
				tempPixel=RGBtoArray(origin.getRGB(countW, countH));
				for(count=0;count<3;count++)
				{
					rowAverge[countH][count]+=tempPixel[count];
					carAverge[countW][count]+=tempPixel[count];
				}
			}
		}
		
		//calculate pixel RGB average
		for(countH=0;countH<maxHeight;countH++)
		{
			for(count=0;count<3;count++)
			{
				rowAverge[countH][count]/=maxWidth;
			}
		}
		for(countW=0;countW<maxWidth;countW++)
		{
			for(count=0;count<3;count++)
			{
				carAverge[countW][count]/=maxHeight;
			}
		}
		
		
		return result;
	}
	
	//resize img to compare
	private static BufferedImage rezise(BufferedImage origin, int afterWidth, int afterHeight)
	{
		BufferedImage result;
		Image temp;
		
		//resizing
		temp = origin.getScaledInstance(afterWidth, afterHeight, BufferedImage.SCALE_SMOOTH);
		
		//convert Image output to BufferedImage
		result=toBufferedImage(temp);
		
		return result;
	}
	
	//convert Image to BufferedImage
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
