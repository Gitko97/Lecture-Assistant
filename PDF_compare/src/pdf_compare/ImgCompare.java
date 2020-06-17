package src;

import java.awt.image.BufferedImage;

public class ImgCompare {
  //default noise value
  private static double hueNoise=0.1;
  private static double satNoise=0.35;
  private static double lumNoise=0.5;
	//this variable need to control semaphore
  protected static BufferedImage partA;
  protected static BufferedImage partB;

  //get each img's dif sum
  public static int getPixelDif(BufferedImage a, BufferedImage b) {
    int result = 0;
	int row;
	int cor;
	double aHSL[];
	double bHSL[];

    //for debugging
	BufferedImage difPartA = new BufferedImage(a.getWidth(), a.getHeight(), BufferedImage.TYPE_INT_ARGB);
	BufferedImage difPartB = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);

	//exception case if buffered is null
	if(a == null || b == null) {
	  throw new NullPointerException("Buffered Image pointer is null!");
	}

	//check all pixel
	for(row = 0; row < a.getHeight(); row++) {
	  for(cor = 0; cor < a.getWidth(); cor++) {
	    //get HSL value
	    aHSL = getHSLfromRGB(a.getRGB(cor, row));
	    bHSL = getHSLfromRGB(b.getRGB(cor, row));
      
	    if(isHSLDifferent(aHSL, bHSL)) {
	      result++;
	  	  difPartA.setRGB(cor, row, a.getRGB(cor, row));
		  difPartB.setRGB(cor, row, b.getRGB(cor, row));
	    }
	  }
	}
	partA = difPartA;
	partB = difPartB;
	return result;
  }

  protected static boolean isHSLDifferent(double[] aHSL, double[] bHSL) {
    double aLumVar;
    double bLumVar;
	aLumVar = 1.0 - 2.0 * Math.abs(0.5 - aHSL[2]);
	bLumVar = 1.0 - 2.0 * Math.abs(0.5 - bHSL[2]);
    //check how much dif hue
	//default hueNoise=0.1
	if(Math.abs(aHSL[0] - bHSL[0]) > hueNoise && Math.abs(aHSL[0] - bHSL[0]) < 0.5) {
	  if((aHSL[1] * aLumVar) > 0.1 || (bHSL[1] * bLumVar) > 0.1) {
	    return true;
	  }
	}
    //check how much dif saturation(it must consider lum)
	//default satNoise=0.35
	if(aHSL[1] * aLumVar - bHSL[1] * bLumVar > satNoise) {
	  return true;
	}
    //check how much dif luminace
	//default lumNoise=0.5
	if(Math.abs(aHSL[2] - bHSL[2]) > lumNoise) {
	  return true;
	}
    return false;
  }
 
  //convert RGB HEX value to HSL
  //rgb hex code input
  protected static double[] getHSLfromRGB(int rgb) {
    int array[] = RGBtoArray(rgb);
    return getHSLfromRGB(array);
  }
	
  //rgb array input
  protected static double[] getHSLfromRGB(int[] rgbArray) {
	double[] result = new double[3];
	int count;
	int maxColor=0;
	double min=10;
	double max= -10;
	double temp=0;
  
	//first get luminace
	for(count = 0; count < 3; count++) {
	  temp= ((double)rgbArray[count] / 255.0);
      if(temp < min) {
	    min = temp;
	  }
	  if(temp > max) {
		max = temp;
		maxColor = count;
	  }
	}
	result[2] = (min + max) / 2;
    //second get saturation
	if(result[2] < 0.5) {
	  if(max + min != 0) {
	    result[1]=(max - min) / (max + min);
	  } else {
		result[1] = 0;
	  }
	} else {
	  result[1] = (max - min) / (2.0 - (max + min));
	}
    //finally get Hue
	if((max - min) != 0) {
	  switch(maxColor) {
	  //red
	  case 0:
	    temp = ((double)(rgbArray[1] - rgbArray[2]) / 255);
		break;
	  //green
	  case 1:
		temp=((double)(rgbArray[2] - rgbArray[0]) / 255);
		break;
	  //blue
	  case 2:
	    temp = ((double)(rgbArray[0] - rgbArray[1]) / 255);
		break;
	  }
	  result[0]=temp/((max-min)*6);
	} else {
	  result[0] = 0;
	}
    return result;
  }

  //convert RGB HEX value to array value
  protected static int[] RGBtoArray(int rgb) {
    int result[] = new int[3];
	rgb += 256 * 256 * 256;
	result[0] = (rgb / (256 * 256));
	result[1]=(rgb % (256 * 256)) / 256;
	result[2] = rgb % 256;
	return result;
  }
  
  //convert array to RGB sum
  protected static int getRGBdifSum(int arrayA[], int arrayB[]) {
	int result = 0;
	int count;
	for(count = 0; count < 3;count++) {
	  result += Math.abs(arrayA[count] - arrayB[count]);
	}
    return result;
  }

  //set Noise value that user can allow
  public static int setNoise(double h, double s, double l) {
    if(h < 0.0 || h > 0.5) {
	  throw new IllegalArgumentException("Hue is over 0, under 0.5, it can't be " + h);
	}
	if(s < 0.0 || s > 1.0) {
	  throw new IllegalArgumentException("Saturation is over 0, under 0.5, it can't be " + s);
	}
	if(l < 0.0 || l > 1.0) {
	  throw new IllegalArgumentException("Luminace is over 0, under 0.5, it can't be " + l);
	}
	hueNoise = h;
	satNoise = s;
	lumNoise = l;
	
	return 0;
  }
}
