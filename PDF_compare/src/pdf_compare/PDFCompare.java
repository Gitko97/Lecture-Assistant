package src;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class PDFCompare extends ImgCompare{

	private static int left=0, right=0;
	private static int top=0, down=0;
	private static int pixelAmount=0;

	//meaning that cut margin or not
	//[0]: top and down, [1]:left and right
	private static boolean isCutMargin[]= {false, false};
	private static boolean isMarginCheckAuto=true;

	//main method
	//if same, return false, else, return true
	//origin and video buffered img are not changed
	public static double getDifRatio(BufferedImage origin, BufferedImage video) {
		int difPixelNum;
		difPixelNum=getPDFDifValue(origin, video);

		System.out.println(((double)difPixelNum/(double)pixelAmount));

		return ((double)difPixelNum/(double)pixelAmount);
	}

	//return int value that calculate how much different
	public static int getPDFDifValue(BufferedImage origin, BufferedImage video) {
		BufferedImage originTransform, videoTransform;

		//exception task
		if(origin==null||video==null) {
			throw new NullPointerException();
		}

		originTransform=origin;
		videoTransform=video;

		//cut blank
		if(isMarginCheckAuto) {
			setIsCutMarginAuto(originTransform, videoTransform);
		}
		setNoMarginArea(videoTransform);
		videoTransform=marginCut(videoTransform);

		//resize each size to same
		//resize bigger img
		if(originTransform.getWidth()<videoTransform.getWidth()) {
			originTransform=resize(originTransform, videoTransform.getWidth(), videoTransform.getHeight());
		}
		else {
			videoTransform=resize(videoTransform, originTransform.getWidth(), originTransform.getHeight());
		}
		//calculate total pixel amount
		pixelAmount=videoTransform.getWidth()*videoTransform.getHeight();
		//get difValue(=number of dif pixel)
		return getPixelDif(originTransform, videoTransform);
	}

	//cut img's margin in any img
	public static BufferedImage getMarginCut(BufferedImage img)
	{
		BufferedImage result;

		boolean[] tempBool=isCutMargin;

		isCutMargin[0]=true;
		isCutMargin[1]=true;

		setNoMarginArea(img);
		result=marginCut(img);

		isCutMargin=tempBool;

		return result;
	}

	//cut video's margin
	private static BufferedImage marginCut(BufferedImage video) {
		BufferedImage result=video.getSubimage(left, top, right-left+1, down-top+1);
		return result;
	}

	//set isMarginCheckAuto
	//if input is false, program is not check margin automatically
	//else, check margin automatically.
	public static void setIsMarginCheckAuto(boolean check) {
		isMarginCheckAuto=check;
		return;
	}

	//set isCutMargin auto or not
	private static void setIsCutMarginAuto(BufferedImage origin, BufferedImage video) {
		int[] originAverge=new int[3];
		int[] videoAverge=new int[3];
		int[] temp=new int[3];

		int count;


		//not special case, merge area is (top and down)OR(left and right), so only compare top and left
		//first top
		for(count=0;count<3;count++) {
			originAverge[count]=0;
			videoAverge[count]=0;
		}

		for(count=0;count<origin.getWidth();count++) {
			temp=RGBtoArray(origin.getRGB(count, 0));
			originAverge[0]+=temp[0];
			originAverge[1]+=temp[1];
			originAverge[2]+=temp[2];
		}
		for(count=0;count<video.getWidth();count++) {
			temp=RGBtoArray(video.getRGB(count, 0));
			videoAverge[0]+=temp[0];
			videoAverge[1]+=temp[1];
			videoAverge[2]+=temp[2];
		}
		for(count=0;count<3;count++) {
			originAverge[count]/=origin.getWidth();
			videoAverge[count]/=video.getWidth();
		}
		if(isHSLDifferent(getHSLfromRGB(originAverge), getHSLfromRGB(videoAverge))) {
			isCutMargin[0]=true;
		}
		else {
			isCutMargin[0]=false;
		}

		//second left
		for(count=0;count<3;count++) {
			originAverge[count]=0;
			videoAverge[count]=0;
		}

		for(count=0;count<origin.getHeight();count++) {
			temp=RGBtoArray(origin.getRGB(0, count));
			originAverge[0]+=temp[0];
			originAverge[1]+=temp[1];
			originAverge[2]+=temp[2];
		}
		for(count=0;count<video.getHeight();count++) {
			temp=RGBtoArray(video.getRGB(0, count));
			videoAverge[0]+=temp[0];
			videoAverge[1]+=temp[1];
			videoAverge[2]+=temp[2];
		}
		for(count=0;count<3;count++) {
			originAverge[count]/=origin.getHeight();
			videoAverge[count]/=video.getHeight();
		}
		if(isHSLDifferent(getHSLfromRGB(originAverge), getHSLfromRGB(videoAverge))) {
			isCutMargin[1]=true;
		}
		else {
			isCutMargin[1]=false;
		}

		return;

	}

	//setting cut margin option(default: row=false, cor=true)
	public static void setIsCutMargin(boolean row, boolean col) {
		isCutMargin[0]=row;
		isCutMargin[1]=col;
		return;
	};


	//set video's not margin area manually
	public static void setArea(int l, int r, int t, int d) {
		left=l;
		right=r;
		top=t;
		down=d;

		return;
	}


	//option that get video's width area and height area that are not Margin AUTO
	public static void setNoMarginArea(BufferedImage origin) {

		int width=origin.getWidth();
		int height=origin.getHeight();
		int rowDifMax[]= {0, 0}, colDifMax[]= {0,0};
		int rowDifMaxPoint[]=new int[2];
		int colDifMaxPoint[]=new int[2];

		int[][] rowAverge=new int[height][3];
		int[][] colAverge=new int[width][3];
		int[] rowDif=new int[height];
		int[] colDif=new int[width];
		int tempPixel[]=new int[3];

		int countH, countW, count;

		left=0;
		right=origin.getWidth()-1;
		top=0;
		down=origin.getHeight()-1;

		//calculate pixel RGB array sum value
		for(countH=0;countH<height;countH++) {
			for(countW=0;countW<width;countW++) {
				tempPixel=RGBtoArray(origin.getRGB(countW, countH));
				for(count=0;count<3;count++) {
					rowAverge[countH][count]+=tempPixel[count];
					colAverge[countW][count]+=tempPixel[count];
				}
			}
		}

		//calculate pixel RGB average and compare dif value
		if(isCutMargin[0]) {
			for(countH=0;countH<height;countH++) {
				for(count=0;count<3;count++) {
					rowAverge[countH][count]/=width;
				}
				if(countH>0) {
					rowDif[countH-1]=getRGBdifSum(rowAverge[countH-1], rowAverge[countH]);

					if(rowDif[countH-1]>rowDifMax[0]) {
						rowDifMax[0]=rowDif[countH-1];
						rowDifMaxPoint[0]=countH-1;
					}
					else if(rowDif[countH-1]>rowDifMax[1]) {
						rowDifMax[1]=rowDif[countH-1];
						rowDifMaxPoint[1]=countH-1;
					}
				}
			}
			//setting area
			if(rowDifMaxPoint[0]>rowDifMaxPoint[1]) {
				top=rowDifMaxPoint[1]+1;
				down=rowDifMaxPoint[0];
			}
			else {
				top=rowDifMaxPoint[0]+1;
				down=rowDifMaxPoint[1];
			}
		}

		if(isCutMargin[1]) {
			for(countW=0;countW<width;countW++) {
				for(count=0;count<3;count++) {
					colAverge[countW][count]/=height;
				}
				if(countW>0) {
					colDif[countW-1]=getRGBdifSum(colAverge[countW-1], colAverge[countW]);

					if(colDif[countW-1]>colDifMax[0]) {
						colDifMax[0]=colDif[countW-1];
						colDifMaxPoint[0]=countW-1;
					}
					else if(colDif[countW-1]>colDifMax[1]) {
						colDifMax[1]=colDif[countW-1];
						colDifMaxPoint[1]=countW-1;
					}
				}
			}
			if(colDifMaxPoint[0]>colDifMaxPoint[1]) {
				left=colDifMaxPoint[1]+1;
				right=colDifMaxPoint[0];
			}
			else {
				left=colDifMaxPoint[0]+1;
				right=colDifMaxPoint[1];
			}
		}


		return;
	}


	//resize img to compare
	private static BufferedImage resize(BufferedImage origin, int afterWidth, int afterHeight) {
		BufferedImage result;
		Image temp;

		//if same size
		if(origin.getWidth()==afterWidth&&origin.getHeight()==afterHeight) {
			return origin;
		}

		//resizing
		temp = origin.getScaledInstance(afterWidth, afterHeight, BufferedImage.SCALE_SMOOTH);

		//convert Image output to BufferedImage
		result=imageToBufferedImage(temp);

		return result;
	}

	//convert Image to BufferedImage
	private static BufferedImage imageToBufferedImage(Image img) {
	    if (img instanceof BufferedImage) {
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
