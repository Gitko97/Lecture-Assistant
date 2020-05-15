package pdf_compare;
import java.awt.image.BufferedImage;


public class PDFCompare {
	private long dif;
	private BufferedImage fromPDF, fromVideo;
	private int height, width;
	
	//initiate
	//get bufferedimg when initiate
	public PDFCompare(BufferedImage a, BufferedImage b)
	{
		dif=0;
		fromPDF=a;
		fromVideo=b;
		
		height=fromPDF.getHeight();
		width=fromPDF.getWidth();
	}
	//initiate without bufferedimg
	public PDFCompare()
	{
		dif=0;
		fromPDF=null;
		fromVideo=null;
	}
	
	//show dif(just debugging)
	public long show_dif()
	{
		return dif;
	}
	
	public int get_img(BufferedImage a, BufferedImage b)
	{
		if(a==null||b==null)
		{
			throw new NullPointerException();
		}
		fromPDF=a;
		fromVideo=b;
		
		height=fromPDF.getHeight();
		width=fromPDF.getWidth();
		return 0;
	}
	
	//convert RGB value to sum
	public int getRGBsum(int rgb)
	{
		int result;
		int red, grean, blue;
		
		red=rgb/(256*256);
		grean=(rgb%(256*256))/256;
		blue=rgb%256;
		
		result=red+grean+blue;
		
		return result;
	}
	
	
	//compare two bufferedimg, 
	//if same, return false. else, return true
	public boolean compare()
	{
		int row, cor;
		int pdfPixel, videoPixel;
		int temp;
		
		//exception case if buffered is null
		if(fromPDF==null||fromVideo==null)
		{
			throw new NullPointerException();
		}
		
		//check all pixel
		for(row=0;row<height;row++)
		{
			for(cor=0;cor<width;cor++)
			{
				pdfPixel=fromPDF.getRGB(cor, row);
				videoPixel=fromVideo.getRGB(cor, row);
				
				//get dif value
				temp=getRGBsum(
						pdfPixel>videoPixel ? pdfPixel-videoPixel : videoPixel-pdfPixel);
				
				//allow noise dif(maybe noise is less then 150)
				if(temp>150)
				{
					dif++;
				}
			}
		}
		
		if(dif<50)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	


}
