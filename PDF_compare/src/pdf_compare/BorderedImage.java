package src;

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
		BorderedImage.imageA = a;
		BorderedImage.imageB = b;
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
	
	private static void setSearch() { // ���̰� �ִ� ���� ��ǥ�� ���ϱ� ���� �������� �ʱ�ȭ
		
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
	private static void getSearchStart() { //������ �̹����� ���� �� �𼭸� ��ǥ�� ã��
		int row, col;
		int tmp[] = new int[2];
		tmp[0] = imageB.getWidth()-1;
		tmp[1] = imageB.getHeight()-1;
		
		//���� �𼭸��� X��ǥ�� ����
		for(row = imageB.getHeight()-1; row >= 0; row--)
		{
			for(col =imageB.getWidth()-1; col >= 0; col--)
			{
				if(imageA.getRGB(0, 0) != imageB.getRGB(col, row))
				{
					if(row == tmp[1] && Math.abs(col-tmp[0])==1) 
					{
						countX++;			//noise�� �ν����� �ʱ� ���� �ȼ�ũ�� ����
					}
					else
					{
						countX = 1;			//X�� �߰��� ����� 1�� �ʱ�ȭ
					}
					
					if(startPos[0] > col && countX >=5)		//���̰� �߻��� �κ��� ���̰� 5px �̻��϶�  X��ǥ ����
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
		
		//���� �𼭸��� Y��ǥ�� ����
		for(col = imageB.getWidth()-1; col >= 0; col--) 
		{

			for(row = imageB.getHeight()-1; row >= 0; row--)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(col == tmp[0] && Math.abs(row-tmp[1])==1) 
					{
						countY++;		//noise�� �ν����� �ʱ� ���� �ȼ�ũ�� ����
					}
					else
					{
						countY = 1;
					}
					
					if(startPos[1] > row && countY >=5)		//���̰� �߻��� �κ��� ���̰� 5px �̻��϶�  Y��ǥ ����
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

	private static void getSearchEnd() {		//������ �̹����� ������ �Ʒ� �𼭸� ��ǥ�� ã��
		int row, col;
		int tmp[] = new int[2];
		tmp[0] = imageB.getWidth()-1;
		tmp[1] = imageB.getHeight()-1;
		countX =0;
		countY =0;
		tmp[0] = 0;
		tmp[1] = 0;
		
		//������ �𼭸��� X��ǥ�� ����
		for(row = 0; row < imageB.getHeight(); row++) 
		{

			for(col = 0; col < imageB.getWidth(); col++)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(row == tmp[1] && Math.abs(col-tmp[0])==1) 
					{
						countX++;		//noise�� �ν����� �ʱ� ���� �ȼ�ũ�� ����
					}
					else
					{
						countX = 1;
					}
					
					if(endPos[0] < col && countX >=5)		//���̰� �߻��� �κ��� ���̰� 5px �̻��϶�  X��ǥ ����
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
		
		//������ �𼭸��� Y��ǥ�� ����
		for(col = 0; col < imageB.getWidth(); col++) 
		{

			for(row = 0; row < imageB.getHeight(); row++)
			{
				if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
					
					if(col == tmp[0] && Math.abs(row-tmp[1])==1) 
					{
						countY++;		//noise�� �ν����� �ʱ� ���� �ȼ�ũ�� ����
					}
					else
					{
						countY = 1;
					}
					
					if(endPos[1] < row && countY >=5)		//���̰� �߻��� �κ��� ���̰� 5px �̻��϶�  Y��ǥ ����
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
	
		
	public static int[] extractBufferedImage() 
	{
		int[] subPos = new int[4]; 
		subPos[0] = Math.min(startPos[0], endPos[0]);
		subPos[1] = Math.min(startPos[1], endPos[1]);
		subPos[2] =  Math.abs(endPos[0]-startPos[0]);
		subPos[3] = Math.abs(endPos[1]-startPos[1]);
		return subPos;
	}
	public static int[] extractPoint()
	{
		return startPos;
	}
}