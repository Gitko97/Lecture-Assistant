package pdf_compare;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

public class PDFCompare extends ImgCompare{

	private static int left=0, right=0;
	private static int top=0, down=0;
	private static int allowDif=200;
	
	private static boolean isCutMargin[]= {false, true};
	
	//main method
	//if same, return false, else, return true
	//origin and video buffered img are not changed
	public static boolean compare(BufferedImage origin, BufferedImage video)
	{
		int difPixelNum, pixelAmount;
		BufferedImage originTransform, videoTransform;
		//exception task
		if(origin==null||video==null)
		{
			throw new NullPointerException();
		}

		originTransform=origin;
		videoTransform=video;
		
		//cut blank
		videoTransform=marginCut(videoTransform);
		
		//resize each size to same
		//resize bigger img
		if(originTransform.getWidth()<videoTransform.getWidth())
		{
			originTransform=resize(originTransform, videoTransform.getWidth(), videoTransform.getHeight());
		}
		else
		{
			videoTransform=resize(videoTransform, originTransform.getWidth(), originTransform.getHeight());
		}
		//get difValue(=number of dif pixel)
		difPixelNum=getPixelDif(originTransform, videoTransform);

		pixelAmount=videoTransform.getWidth()*videoTransform.getHeight();
		
		//debugging
		System.out.println("difvalue: "+difPixelNum);
		System.out.println("allowvalue: "+(pixelAmount/10000)*allowDif);
		
		if((pixelAmount/10000)*allowDif<difPixelNum)
		{
			return true;
		}
		else
		{
			return false;
		}	
	}
	
	//cut video's margin
	private static BufferedImage marginCut(BufferedImage video)
	{
		BufferedImage result=video.getSubimage(left, top, right-left+1, down-top+1);	
		return result;
	}
	
	//setting cut margin option(default=true)
	public static void setIsCutMargin(boolean row, boolean car)
	{
		isCutMargin[0]=row;
		isCutMargin[1]=car;
		return;
	};
	
	//setting ArrowDif
	public static void setAllowDif(int num)
	{
		allowDif=num;
		return;
	}
	
	//set video's not margin area manually
	public static void setArea(int l, int r, int t, int d)
	{
		left=l;
		right=r;
		top=t;
		down=d;
	}
	
	
	//option that get video's width area and height area that are not Margin AUTO
	public static void setNoMarginArea(BufferedImage origin)
	{
		
		int width=origin.getWidth();
		int height=origin.getHeight();
		
		int rowDifMax[]= {0, 0}, carDifMax[]= {0,0};
		int rowDifMaxPoint[]=new int[2];
		int carDifMaxPoint[]=new int[2];
		
		int[][] rowAverge=new int[height][3];
		int[][] carAverge=new int[width][3];
		int[] rowDif=new int[height];
		int[] carDif=new int[width];
		int tempPixel[]=new int[3];
		
		int countH, countW, count;
		
		left=0;
		right=origin.getWidth()-1;
		top=0;
		down=origin.getHeight()-1;
		
		//calculate pixel RGB array sum value
		for(countH=0;countH<height;countH++)
		{
			for(countW=0;countW<width;countW++)
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
		if(isCutMargin[0])
		{
			for(countH=0;countH<height;countH++)
			{
				for(count=0;count<3;count++)
				{
					rowAverge[countH][count]/=width;
				}
				if(countH>0)
				{
					rowDif[countH-1]=getRGBdifSum(rowAverge[countH-1], rowAverge[countH]);
				
					if(rowDif[countH-1]>rowDifMax[0])
					{
						rowDifMax[0]=rowDif[countH-1];
						rowDifMaxPoint[0]=countH-1;
					}
					else if(rowDif[countH-1]>rowDifMax[1])
					{
						rowDifMax[1]=rowDif[countH-1];
						rowDifMaxPoint[1]=countH-1;
					}
				}
			}
			//setting area
			if(rowDifMaxPoint[0]>rowDifMaxPoint[1])
			{
				top=rowDifMaxPoint[1]+1;
				down=rowDifMaxPoint[0];
			}
			else
			{
				top=rowDifMaxPoint[0]+1;
				down=rowDifMaxPoint[1];
			}
		}
		
		if(isCutMargin[1])
		{
			for(countW=0;countW<width;countW++)
			{
				for(count=0;count<3;count++)
				{
					carAverge[countW][count]/=height;
				}
				if(countW>0)
				{
					carDif[countW-1]=getRGBdifSum(carAverge[countW-1], carAverge[countW]);
					
					if(carDif[countW-1]>carDifMax[0])
					{
						carDifMax[0]=carDif[countW-1];
						carDifMaxPoint[0]=countW-1;
					}
					else if(carDif[countW-1]>carDifMax[1])
					{
						carDifMax[1]=carDif[countW-1];
						carDifMaxPoint[1]=countW-1;
					}
				}
			}
			if(carDifMaxPoint[0]>carDifMaxPoint[1])
			{
				left=carDifMaxPoint[1]+1;
				right=carDifMaxPoint[0];
			}
			else
			{
				left=carDifMaxPoint[0]+1;
				right=carDifMaxPoint[1];
			}
		}
		
		
		return;
	}
	
	//resize img to compare
	private static BufferedImage resize(BufferedImage origin, int afterWidth, int afterHeight)
	{
		BufferedImage result;
		Image temp;
		
		//debugging
		System.out.println("resie: "+afterWidth+" "+afterHeight);
		
		//if same size
		if(origin.getWidth()==afterWidth&&origin.getHeight()==afterHeight)
		{
			return origin;
		}
		
		//resizing
		temp = origin.getScaledInstance(afterWidth, afterHeight, BufferedImage.SCALE_SMOOTH);
		
		//convert Image output to BufferedImage
		result=imageToBufferedImage(temp);
		
		return result;
	}
	
	//convert Image to BufferedImage
	private static BufferedImage imageToBufferedImage(Image img)
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
