package pdf_compare;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

public class PDFCompare extends ImgCompare{

	static int left=0, right=0;
	static int top=0, down=0;
	//NOT YET
	
	//set video's not blank area manually
	public static void setArea(int l, int r, int t, int d)
	{
		left=l;
		right=r;
		top=t;
		down=d;
	}
	
	//option that get video's width area and height area that are not blank AUTO
	public static void setArea(BufferedImage origin)
	{
		
		int minWidth=0, maxWidth=origin.getWidth();
		int minHeight=0, maxHeight=origin.getHeight();
		
		int rowMax[]= {0, 0}, carMax[]= {0,0};
		int rowMaxPoint[]=new int[2];
		int carMaxPoint[]=new int[2];
		
		int[][] rowAverge=new int[maxWidth][3];
		int[][] carAverge=new int[maxHeight][3];
		int[] rowDif=new int[maxWidth];
		int[] carDif=new int[maxHeight];
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
		
		//calculate pixel RGB average and compare dif value
		for(countH=0;countH<maxHeight;countH++)
		{
			for(count=0;count<3;count++)
			{
				rowAverge[countH][count]/=maxWidth;
			}
			if(countH>0)
			{
				rowDif[countH-1]=getRGBdifSum(rowAverge[countH-1], rowAverge[countH]);
				
				if(rowDif[countH-1]>rowMax[0])
				{
					rowMax[0]=rowDif[countH-1];
					rowMaxPoint[0]=countH-1;
				}
				else if(rowDif[countH-1]>rowMax[1])
				{
					rowMax[1]=rowDif[countH-1];
					rowMaxPoint[1]=countH-1;
				}
			}
		}
		for(countW=0;countW<maxWidth;countW++)
		{
			for(count=0;count<3;count++)
			{
				carAverge[countW][count]/=maxHeight;
			}
			if(countW>0)
			{
				carDif[countW-1]=getRGBdifSum(carAverge[countW-1], carAverge[countW]);
				
				if(carDif[countW-1]>carMax[0])
				{
					carMax[0]=carDif[countW-1];
					carMaxPoint[0]=countW-1;
				}
				else if(carDif[countW-1]>carMax[1])
				{
					carMax[1]=carDif[countW-1];
					carMaxPoint[1]=countW-1;
				}
			}
		}
		//setting area
		if(rowMaxPoint[0]>rowMaxPoint[1])
		{
			top=rowMaxPoint[1]+1;
			down=rowMaxPoint[0];
		}
		else
		{
			top=rowMaxPoint[0]+1;
			down=rowMaxPoint[1];
		}
		
		if(carMaxPoint[0]>carMaxPoint[1])
		{
			left=carMaxPoint[1]+1;
			right=carMaxPoint[0];
		}
		else
		{
			left=carMaxPoint[0]+1;
			right=carMaxPoint[1];
		}
		
		return;
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
