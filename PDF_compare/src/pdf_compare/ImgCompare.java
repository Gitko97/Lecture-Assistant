package pdf_compare;

import java.awt.image.BufferedImage;


public class ImgCompare {
	//default noise value
	private static int allowNoise=200;
	
	//get each img's RGB dif sum
	public static int getRGBdif(BufferedImage a, BufferedImage b)
	{
		int result=0;
		int row, cor;
		int aPixel[], bPixel[];
		int difValue;
		
		//exception case if buffered is null
		if(a==null||b==null)
		{
			throw new NullPointerException();
		}
		
		//check all pixel
		for(row=0;row<a.getHeight();row++)
		{
			for(cor=0;cor<a.getWidth();cor++)
			{
				//get Pixel HEX value that each point
				aPixel=RGBtoArray(a.getRGB(cor, row));
				bPixel=RGBtoArray(b.getRGB(cor, row));
				
				//get dif value
				difValue=getRGBdifSum(aPixel, bPixel);
				
				//allow noise dif(maybe noise is less then 150)
				if(difValue>allowNoise)
				{
					result++;
				}
			}
		}
		return result;
	}
	
	//convert RGB HEX value to array value
	protected static int[] RGBtoArray(int rgb)
	{
		int result[]=new int[3];
		
		result[0]=rgb/(256*256);
		result[1]=(rgb%(256*256))/256;
		result[2]=rgb%256;
		
		return result;
	}
	
	//convert array to RGB sum
	protected static int getRGBdifSum(int arrayA[], int arrayB[])
	{
		int result=0;
		int count;
		for(count=0;count<3;count++)
		{
			result+= arrayA[count]>arrayB[count] ? 
					arrayA[count]-arrayB[count] : arrayB[count]-arrayA[count];
		}
		
		return result;
	}
	
	//set Noise value that user can allow(default=150)
	public static int setNoise(int val)
	{
		if(val<0||val>255*3)
		{
			throw new IllegalArgumentException();
		}
		allowNoise=val;
		return allowNoise;
	}
}
