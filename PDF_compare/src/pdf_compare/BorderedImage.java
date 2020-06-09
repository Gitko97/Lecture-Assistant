package pdf_compare;

import java.awt.image.*;

public class BorderedImage extends PDFCompare {		
	private static BufferedImage imageA, imageB;
	
	public static int startPos[] = new int[2];
	public static int endPos[] = new int[2];
	
	protected static int tmpX[] = new int[10];
	protected static int tmpY[] = new int[10];
	protected static int countX = 0;
	protected static int countY = 0;
	
	public static void setBufferedImage(BufferedImage a, BufferedImage b) {	//import difPartA and difPartB
		imageA = a;
		imageB = b;
		setSearch();		
		searchImage();		
		return;
	}
	public static void setBufferedImage() {	//already extracted image import
		imageA = partA;
		imageB = partB;
		setSearch();		
		searchImage();		
		return;
	}
	
	private static void setSearch() { // 차이가 있는 곳의 좌표를 구하기 위한 변수들을 초기화
		
		startPos[0] = imageB.getWidth()-1;
		startPos[1] = imageB.getHeight()-1;
		for(int i = 0; i < 2; i++)
		{
			endPos[i] = 0;
		}
	}
	
	public static void searchImage() {
		
		getSearchStart();	
		getSearchEnd();
		
	}
	private static void getSearchStart() {		//추출할 이미지의 왼쪽 위 모서리 좌표를 찾음
		int row, col;
		int tmp[] = new int[2];
		tmp[0] = imageB.getWidth()-1;
		tmp[1] = imageB.getHeight()-1;
		
		//왼쪽 모서리의 X좌표를 구함
		for(row = imageB.getHeight()-1; row >= 0; row--)
		{
			for(col =imageB.getWidth()-1; col >= 0; col--)
			{
				if(imageA.getRGB(0, 0) != imageB.getRGB(col, row))
				{
					if(row == tmp[1] && Math.abs(col-tmp[0])==1) 
					{
						countX++;			//noise를 인식하지 않기 위해 픽셀크기 측정
					}
					else
					{
						countX = 1;			//X가 중간에 끊기면 1로 초기화
					}
					
					if(startPos[0] > col && countX >=5)		//차이가 발생한 부분의 길이가 5px 이상일때  X좌표 저장
					{
						startPos[0] = col;
					}
					tmp[0] = col;
					tmp[1] = row;
				}
			}	
		}
		tmp[0] = imageB.getWidth()-1;
		tmp[1] = imageB.getHeight()-1;
		
		//왼쪽 모서리에 Y좌표를 구함
		for(col = imageB.getWidth()-1; col >= 0; col--) 
		{

			for(row = imageB.getHeight()-1; row >= 0; row--)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(col == tmp[0] && Math.abs(row-tmp[1])==1) 
					{
						countY++;		//noise를 인식하지 않기 위해 픽셀크기 측정
					}
					else
					{
						countY = 1;
					}
					
					if(startPos[1] > row && countY >=5)		//차이가 발생한 부분의 길이가 5px 이상일때  Y좌표 저장
					{
						startPos[1] = row;
					}
					tmp[0] = col;
					tmp[1] = row;
				}
			}
		}
		System.out.println("Start Position : " +"x: " + startPos[0] + " y: " + startPos[1]);
	}		

	private static void getSearchEnd() {		//추출할 이미지의 오른쪽 아래 모서리 좌표를 찾음
		int row, col;
		int tmp[] = new int[2];
		tmp[0] = imageB.getWidth()-1;
		tmp[1] = imageB.getHeight()-1;
		countX =0;
		countY =0;
		tmp[0] = 0;
		tmp[1] = 0;
		
		//오른쪽 모서리의 X좌표를 구함
		for(row = 0; row < imageB.getHeight(); row++) 
		{

			for(col = 0; col < imageB.getWidth(); col++)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(row == tmp[1] && Math.abs(col-tmp[0])==1) 
					{
						countX++;		//noise를 인식하지 않기 위해 픽셀크기 측정
					}
					else
					{
						countX = 1;
					}
					
					if(endPos[0] < col && countX >=5)		//차이가 발생한 부분의 길이가 5px 이상일때  X좌표 저장
					{
						endPos[0] = col;
					}
					tmp[0] = col;
					tmp[1] = row;
				}
			}
		}
		tmp[0] = 0;
		tmp[1] = 0;
		
		//오른쪽 모서리의 Y좌표를 구함
		for(col = 0; col < imageB.getWidth(); col++) 
		{

			for(row = 0; row < imageB.getHeight(); row++)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(col == tmp[0] && Math.abs(row-tmp[1])==1) 
					{
						countY++;		//noise를 인식하지 않기 위해 픽셀크기 측정
					}
					else
					{
						countY = 1;
					}
					
					if(endPos[1] < row && countY >=5)		//차이가 발생한 부분의 길이가 5px 이상일때  Y좌표 저장
					{
						endPos[1] = row;
					}
					tmp[0] = col;
					tmp[1] = row;
				}
			}
		}
		System.out.println("End Position " + " x: " + endPos[0] + " y: " + endPos[1]);
	}
	
		
	public static BufferedImage extractBufferedImage() 
	{
		return partB.getSubimage(startPos[0], startPos[1], endPos[0]-startPos[0], endPos[1]-startPos[1]);
	//	return PDFCompare.extractImage.getSubimage(0, 0, PDFCompare.extractImage.getWidth(), PDFCompare.extractImage.getHeight());
	}
	public static int[] extractPoint()
	{
		return startPos;
	}
}
