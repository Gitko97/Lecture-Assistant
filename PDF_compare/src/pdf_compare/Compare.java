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
	
	private final double SAME_CAP=0.0005;
	private final double SAME_PAGE=0.05;
	private final double DIF_PAGE=0.1;
	
	private int startPage=0;
	private int sameImg;
	private int captureCount;
	private int capNoteStartIndex;
	private int capPageStartIndex;
	private int pdfPage;
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
		captureCount=0;
		sameImg=0;
		capPageStartIndex=0;
		pdfPage=startPage;
		getNextPage=false;
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
		while(!exit&&!lastPage) {
			try {
				if(getNextPage) {//get next page
					pageChange();
				}
				else {//compare captured image
					compareCapturedImage();
				}
			}
			catch(Exception e) {
				Thread.yield();
			}
			captureCount++;
		}
		
		return;
	}
	
	//return value: recent pdfPage
	private void pageChange() {
		double difLevel;
		BufferedImage temp;
		
		pdfTemp=originImgArray.get(pdfPage);
		temp=capturing.getCaptureImg(captureCount);
		
		difLevel=PDFCompare.getDifRatio(pdfTemp, temp);
		if(difLevel<SAME_PAGE) {
			controller.ADD_Note(temp, capPageStartIndex, captureCount);
		}
		else{
			/* NOT YET CONSTRUCT
			pdfTemp=originImgArray.get(++pdfPage);
			difLevel=PDFCompare.getDifRatio(pdfTemp, temp);
			if(difLevel<SAME_PAGE) {
				//compare next page
				//NOT YET
			 
			}
			
			else {//fail to search same PDF page
				throw new IllegalArgumentException("Fail to search same PDF page");
			}
			*/
			
			throw new IllegalArgumentException("Fail to search same PDF page");
		}
		capPageStartIndex=captureCount+1;
		pdfPage++;
		
		getNextPage=false;
		return;
	}
	
	private void compareCapturedImage() {
		double difLevel;
		
		if(capturing.endPos(captureCount+1)) {//check if next page is last page
			lastPage=true;
		}
		
		//compare and check how amount different
		capTemp1=capturing.getCaptureImg(captureCount);
		capTemp2=capturing.getCaptureImg(captureCount+1);
		difLevel=BorderedImage.getDifRatio(capTemp1, capTemp2);
		
		if(difLevel>DIF_PAGE) {//different case
			saveWriting();
			getNextPage=true;
			captureCount--;
		}
		else if (difLevel>SAME_CAP) {//write case
			if(capNoteStart==null) {//save first point after change page
				capNoteStart=capturing.getCaptureImg(captureCount);
				capNoteStartIndex=captureCount;
			}
			capNoteFinish=null;
			sameImg=0;
		}
		else {//nothing case
			if(capNoteStart!=null&&capNoteFinish==null) {//save last different point if start point is save
				capNoteFinish=capturing.getCaptureImg(captureCount+1);
			}
			sameImg++;
			
			if(sameImg>=NO_WRITE_SECONDS) {
				saveWriting();
				capNoteStart=capturing.getCaptureImg(captureCount+1);
				capNoteStartIndex=captureCount+1;
			}
		}
		return;
	}
	
	private void saveWriting() {
		BufferedImage temp;
		//import note part
		BorderedImage.setBufferedImage();//already compare each other, so it use least compare data
		temp=BorderedImage.extractBufferedImage();
		
		//saving
		controller.ADD_Note(temp, capNoteStartIndex, captureCount);
		capNoteStart=null;
		capNoteFinish=null;
		return;
	}
	
	public void setStartPage(int num) {
		if(num<0){
			throw new IllegalArgumentException(
					"input value must be pasitive number, not "+num+" that nagative number or zero");
		}
		startPage=num;
		return;
	}
	
	public void exit() {
		exit=true;
		return;
	}
}
