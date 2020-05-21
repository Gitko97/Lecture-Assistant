package pdf_compare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.*;

import javax.imageio.ImageIO;


public class ImgCompare {
	//default noise value
	//private static int allowNoise=300; (LEGACY)
	private static boolean isDebugging=true;
	
	//get each img's RGB dif sum
	public static int getPixelDif(BufferedImage a, BufferedImage b)
	{
		int result=0;
		int row, cor;
		//int aPixel[], bPixel[];
		//int difValue; (LEGACY) LEGACY
		double aHSL[], bHSL[];
		boolean isDif;
		
		//for debugging
		BufferedImage difPartA = new BufferedImage(a.getWidth(), a.getHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage difPartB = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
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
				isDif=false;
				/*
				 * LEGACY
				//get Pixel HEX value that each point
				aPixel=RGBtoArray(a.getRGB(cor, row));
				bPixel=RGBtoArray(b.getRGB(cor, row));

				//get dif value
				difValue=getRGBdifSum(aPixel, bPixel);
				
				
				//check how much dif rgb
				if(difValue>allowNoise)
				{
					isDif=true;
				}
				*/
				
				//get HSL value
				aHSL=getHSLfromRGB(a.getRGB(cor, row));
				bHSL=getHSLfromRGB(b.getRGB(cor, row));
				
				//check how much dif hue
				if(Math.abs(aHSL[0]-bHSL[0])>0.1&&Math.abs(aHSL[0]-bHSL[0])<0.5)
				{
					if((aHSL[1]+bHSL[1])/2>0.6&&(aHSL[2]+bHSL[2])/2>0.6)
					{
						isDif=true;
					}
				}
				
				//check how much dif saturation
				if(Math.abs(aHSL[1]-bHSL[1])>0.95)
				{
					if(Math.abs(aHSL[2]-bHSL[2])>0.95)
					{
						isDif=true;
					}
				}
				
				//check how much dif luminace
				if(Math.abs(aHSL[2]-bHSL[2])>0.90)
				{
					isDif=true;
				}
				
				if(isDif)
				{
					result++;
					if(isDebugging)
					{
						difPartA.setRGB(cor, row, a.getRGB(cor, row));
						difPartB.setRGB(cor, row, b.getRGB(cor, row));
					}
				}
			}
		}
		
		if(isDebugging)
		{
			//debugging where is different
			try
			{
				File outputfileA = new File("difPartA.png");
				ImageIO.write(difPartA, "png", outputfileA);
				File outputfileB = new File("difPartB.png");
				ImageIO.write(difPartB, "png", outputfileB);
			}
			catch(IOException e)
			{
				System.out.println("exeption: img file saving get error!");
			}
		}
		return result;
	}
	
	//convert RGB HEX value to Saturation
	//Saturation: from 0.0 to 1.0
	protected static double[] getHSLfromRGB(int rgb)
	{
		double[] result=new double[3];
		int count, maxColor=0;
		int array[]=RGBtoArray(rgb);
		double min=10, max=-10, temp=0;
		
		//first get luminace
		for(count=0;count<3;count++)
		{
			temp= ((double)array[count]/255.0);
			if(temp<min)
			{
				min=temp;
			}
			if(temp>max)
			{
				max=temp;
				maxColor=count;
			}
		}
		result[2]=(min+max)/2;
		
		//second get saturation
		if(result[2]<0.5)
		{
			if(max+min!=0)
			{
				result[1]=(max-min)/(max+min);
			}
			else
			{
				result[1]=0;
			}
		}
		else
		{
			result[1]=(max-min)/(2.0-(max+min));
		}
		
		//finally get Hue
		if((max-min)!=0)
		{
			switch(maxColor)
			{
			//red
			case 0:
				temp=((double)(array[1]-array[2])/255);
				break;
			//green
			case 1:
				temp=((double)(array[2]-array[0])/255);
				break;
			//blue
			case 2:
				temp=((double)(array[0]-array[1])/255);
				break;
			}	
			result[0]=temp/((max-min)*6);
		}
		else
		{
			result[0]=0;
		}
		return result;
	}
	
	//convert RGB HEX value to array value
	protected static int[] RGBtoArray(int rgb)
	{
		int result[]=new int[3];
		rgb=-rgb;
		
		rgb-=256*256;
		result[0]=(rgb/(256*256));
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
			result+= Math.abs(arrayA[count]-arrayB[count]);
		}
		
		return result;
	}
	
	/*
	 * LEGACY
	//set Noise value that user can allow(default=150)
	public static int setNoise(int val)
	{
		if(val<0)
		{
			throw new IllegalArgumentException();
		}
		allowNoise=val;
		return allowNoise;
	}
	*/
}
