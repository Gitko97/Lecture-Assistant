package pdf_compare;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Compare implements Runnable {
	
	
	private ArrayList<BufferedImage> originImgArray;
	private Capturing capturing;
	private LA_Controller controller;
	
	private BufferedImage pdfTemp, capTemp1, capTemp2, capNoteStart, capNoteFinish;
	
	private boolean exit;
	private boolean getNextPage;
	private boolean lastPage;
	
	private final double SAME_CAP=0.0003;
	private final double SAME_PAGE=0.05;
	private final double DIF_PAGE=0.1;
	
	private int startPage=0;
	private final int NO_WRITE_SECONDS=3;
	
	public Compare(
			ArrayList<BufferedImage> originPDFArray
			, Capturing cap, LA_Controller LAC
			){
		originImgArray=originPDFArray;
		capturing=cap;
		controller=LAC;
		
		exit=false;
	}
	
	public void run(){
		//initialize
		int pdfPage=startPage;
		int captureCount=0;
		int capSaveIndex;
		int sameImg=0;
		double difLevel;
		
		getNextPage=true;
		capNoteStart=null;
		capNoteFinish=null;
		lastPage=false;
		
		
		//check exception
		if(
			originImgArray==null
			||originImgArray.get(0)==null
			||capturing==null
			) {
			throw new NullPointerException();
		}
		
		//main part
		while(!exit&&!lastPage){
			try {
				if(getNextPage) {//get next page
					pdfPage=pageChange(pdfPage, captureCount);
				}
				else{//compare captured image
					
					if(capNoteStart==null) {//first compare after change page
						capNoteStart=capturing.getCaptureImg(captureCount);
					}
					
					if(capturing.endPos(captureCount+1)) {//check if next page is last page
						lastPage=true;
					}
					
					capTemp1=capturing.getCaptureImg(captureCount);
					capTemp2=capturing.getCaptureImg(captureCount+1);
					difLevel=PDFCompare.getDifRatio(capTemp1, capTemp2);
					
					if(difLevel>DIF_PAGE) {//different case
						//input how to work in different case
						getNextPage=true;
						capNoteStart=null;
					}
					else if (difLevel>SAME_CAP) {//write case
						
						//tracked same case
						sameImg=0;
					}
					else {//nothing case
						sameImg++;
						
						if(sameImg>=NO_WRITE_SECONDS) {
							//save writing
							capNoteStart=capturing.getCaptureImg(captureCount+1);
						}
					}
				}
			}
			catch(Exception e){
				Thread.yield();
			}
			captureCount++;
		}
		
		return;
	}
	
	
	
	//return value: recent pdfPage
	private int pageChange(int pdfPage, int capCount)
	{
		double difLevel;
		pdfTemp=originImgArray.get(pdfPage);
		capNoteStart=capturing.getCaptureImg(capCount);
		difLevel=PDFCompare.getDifRatio(pdfTemp, capNoteStart);
		if(difLevel<SAME_PAGE) {
			//같은 페이지 인정
		}
		else{
			pdfTemp=originImgArray.get(pdfPage+1);
			difLevel=PDFCompare.getDifRatio(pdfTemp, capNoteStart);
			if(difLevel<SAME_PAGE) {
				//다음 페이지 인정
				pdfPage++;
			}
			else {//fail to search same PDF page
				throw new IllegalArgumentException("Fail to search same PDF page");
			}
		}
		
		
		getNextPage=false;
		return pdfPage;
	}
	
	public void setStartPage(int num){
		if(num<0){
			throw new IllegalArgumentException(
					"input value must be pasitive number, not "+num+" that nagative number or zero");
		}
		startPage=num;
		return;
	}
	
	public void exit(){
		exit=true;
		return;
	}
	
	
	//legacy
	public interface Capturing{

		BufferedImage getCaptureImg(int capCount);

		boolean endPos(int index);
		
	}
	public interface LA_Controller{
		
	}
}
