package pdf_compare;

import java.awt.image.*;

public class BorderedImage extends PDFCompare {
	public static int x, y;
	public static BufferedImage imageA, imageB;
	public static int height, width;
	public static int startPos[] = new int[2];
	public static int endPos[] = new int[2];
	
	public static void setBufferedImage(BufferedImage a, BufferedImage b) {
		imageA = a;
		imageB = b;
		setSearch();
		searchImage();
		return;
	}
	public static void setSearch() {
		
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
	public static void getSearchStart() {
		int row, col;
		
		for(row = imageB.getHeight()-1; row >= 0; row--)
		{
			for(col =imageB.getWidth()-1; col >= 0; col--)
			{
				if(imageB.getRGB(0, 0) != imageB.getRGB(col, row))
				{
					if(startPos[0] > col) 
					{
						startPos[0] = col;
					}
					if(startPos[1] > row) 
					{
						startPos[1] = row;
					}
				}
				
			}
		}
		System.out.println("Start Position : " +"x: " + startPos[0] + " y: " + startPos[1]);
	}		

	public static void getSearchEnd() {
		int row, col;
		
		for(row = startPos[1]; row < imageB.getHeight(); row++) 
		{

			for(col = startPos[0]+1; col < imageB.getHeight(); col++)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					if(endPos[0] < col) 
					{
						endPos[0] = col;
					}
					if(endPos[1] < row) 
					{
						endPos[1] = row;
					}
				}
			}
		}
		System.out.println("End Position " + " x: " + endPos[0] + " y: " + endPos[1]);
	}
	public static BufferedImage extract() {
		return imageB.getSubimage(startPos[0], startPos[1], endPos[0]-startPos[0], endPos[1]-startPos[1]);
	}
}
