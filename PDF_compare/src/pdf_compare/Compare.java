package src;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Compare implements Runnable {
	
	private ArrayList<BufferedImage> originImgArray;
	private Capturing capturing;
	private LaController controller;
	
	private BufferedImage capTemp1, capTemp2;
	private BufferedImage startBufImg, endBufImg;
	
	private static boolean exit;
	private boolean getNextPage;
	private boolean capNoteStart, capNoteFinish;
	
	private final double SAME_CAP=0.00001;
//	private final double SAME_PAGE=0.05;
	private final double DIF_PAGE=0.03;
	
	
	private int maxTime=3600;
//	private int startPage=0;
	private int sameImg;
	private int captureCount;
	private int capNoteStartIndex;
//	private int pdfPage;
	private final int NO_WRITE_SECONDS=3;
	
	public Compare(
			ArrayList<BufferedImage> originPDFArray
			, Capturing cap, LaController LAC
			) {
		originImgArray=originPDFArray;
		capturing=cap;
		controller=LAC;
		
		exit=false;
	}
	
	public void run(){
		//initialize
		captureCount=1;
		sameImg=0;
//		pdfPage=startPage;
		getNextPage=false;
		capNoteStart=false;
		capNoteFinish=false;
		
		//debugging log
		System.out.println("compare start");
		checkException();
		
		
		while(!exit&&captureCount<maxTime) { //main part
			
			
			//real case
			if(!capturing.endPos(captureCount+1)) { //ready to captured img
				Thread.yield();
				continue;
			}
			
			
			System.out.println("index "+captureCount+" compare, getNextPage: "+getNextPage+", capNoteStart, capNoteFinish: "+capNoteStart+", "+capNoteFinish);
			try {
				if(getNextPage) {//get next page
					System.out.println("page change");
					pageChange();
				}
				else {//compare captured image
					compareCapturedImage();
				}
			}
			catch(Exception e) {
				System.out.println(e);
				Thread.yield();
			}
			captureCount++;
		}
		
		
		System.out.println("compare finish");
		return;
	}
	
	private void checkException() { //check exception
		if(
				originImgArray==null
				||originImgArray.get(0)==null
				||capturing==null
				) {
				System.out.println("ERROR: NULL POINTER");
				throw new NullPointerException();
			}
	}
	
	
	//return value: recent pdfPage
	private void pageChange() throws Exception {
		
		controller.ADD_CompletePDF(capTemp1, captureCount+1);
		getNextPage=false;
		return;
		/* not yet complete
		int count=0;
		double difLevel;
		BufferedImage pdfTemp;
		
		while(originImgArray.size()>pdfPage+count) {
			pdfTemp=originImgArray.get(pdfPage+count);
			System.out.println("pdf page: "+(pdfPage+count));
			difLevel=PDFCompare.getDifRatio(pdfTemp, capTemp2);
			if(difLevel<SAME_PAGE) { //last same page save
				controller.ADD_CompletePDF(capTemp1, captureCount+1);
				pdfPage=pdfPage+count;
				System.out.println("pdf page save!");
				getNextPage=false;
				return;
			}
			count++;
		}
		throw new Exception("Not same page!");
		*/
	}
	
	private void compareCapturedImage() {
		double difLevel;
		
		
		//compare and check how amount different
		capTemp1=capturing.getCaptureImg(captureCount);
		capTemp2=capturing.getCaptureImg(captureCount+1);
		
		
		difLevel=BorderedImage.getDifRatio(capTemp1, capTemp2);
		
		if(difLevel>DIF_PAGE) {//different case
			System.out.println("compare Capture img: different case");
			if(capNoteStart) {//if any write is there
				saveWriting();
			}
			getNextPage=true;
			captureCount--;
			sameImg=0;
		}
		else if (difLevel>SAME_CAP) {//write case
			System.out.println("compare Capture img: write case");
			if(!capNoteStart) {//save first point after change page
				capNoteStart=true;
				startBufImg=capTemp1;
				capNoteStartIndex=captureCount;
			}
			capNoteFinish=false;
			sameImg=0;
		}
		else {//nothing case
			if(capNoteStart&&!capNoteFinish) {//save last different point if start point is save
				capNoteFinish=true;
				endBufImg=capTemp1;
			}
			sameImg++;
			
			System.out.println("compare Capture img: same case, sameImg: "+sameImg);
			
			if(sameImg>=NO_WRITE_SECONDS) {//save write
				System.out.println("save writing enter");
				saveWriting();
				capNoteStartIndex=captureCount+1;
			}
		}
		return;
	}
	
	private void saveWriting() {
		
		if(!capNoteStart) {
			return;
		}
		
		
		try { //import note part
			BorderedImage.getDifRatio(startBufImg, endBufImg);
			BorderedImage.setBufferedImage();
			int subPos[]=BorderedImage.extractBufferedImage();//already compare each other, so it use least compare data
			
			//saving
		    if(capNoteStart) {//when write is exist
		    	  
		          System.out.println("ADD!");
		          controller.ADD_Note(endBufImg.getSubimage(subPos[0], subPos[1], subPos[2], subPos[3]), capNoteStartIndex, captureCount);
		       
		    }
			
		}
		catch(Exception e) { //excetion find
			System.out.println("ERROR OCCURED IN saveWriting");
			System.out.println(e);
			return;
		}
		
		
		System.out.println("save!");

		capNoteStart=false;
		capNoteFinish=false;
		return;
	}
/*	
	public void setStartPage(int num) {
		if(num<0){
			throw new IllegalArgumentException(
					"input value must be pasitive number, not "+num+" that nagative number or zero");
		}
		startPage=num;
		return;
	}
*/
	public void setMaxTime(int time) {
		if(time<0){
			throw new IllegalArgumentException(
					"input value must be pasitive number, not "+time+" that nagative number or zero");
		}
		maxTime=time;
		return;
	}
	
	public boolean exit() {
		exit=true;
		return capturing.endPos(captureCount);
	}
}
