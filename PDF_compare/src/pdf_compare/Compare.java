package src;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Compare implements Runnable {
	
	private ArrayList<BufferedImage> originImgArray;
	private Capturing capturing;
	private LA_controller controller;
	
	private BufferedImage pdfTemp, capTemp1, capTemp2;
	
	private static boolean exit;
	private boolean getNextPage;
	private boolean capNoteStart, capNoteFinish;
	
	private final double SAME_CAP=0.0005;
	private final double SAME_PAGE=0.02;
	private final double DIF_PAGE=0.05;
	
	private int startPage=0;
	private int sameImg;
	private int captureCount;
	private int capNoteStartIndex;
	private int pdfPage;
	private final int NO_WRITE_SECONDS=3;
	
	public Compare(
			ArrayList<BufferedImage> originPDFArray
			, Capturing cap, LA_controller LAC
			) {
		originImgArray=originPDFArray;
		capturing=cap;
		controller=LAC;
		
		exit=false;
	}
	
	public void run(){
		//initialize
		captureCount=0;
		sameImg=0;
		pdfPage=startPage;
		getNextPage=false;
		capNoteStart=false;
		capNoteFinish=false;
		
		//debugging log
		System.out.println("compare start");
		checkException();
		
		
		while(!exit&&captureCount<1000) { //main part
			
			//real case
			/*
			if(!capturing.endPos(captureCount+1)) { //ready to captured img
				Thread.yield();
				continue;
			}
			*/
			
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
	private void pageChange() {
		double difLevel;
		BufferedImage temp;
		
		pdfTemp=originImgArray.get(pdfPage);
		temp=PDFCompare.marginCut(capturing.getCaptureImg(captureCount));
		
		difLevel=PDFCompare.getDifRatio(pdfTemp, temp);
		if(difLevel<SAME_PAGE) { //last same page save
			controller.ADD_CompletePDF(temp, captureCount+1);
		}
		else{
			pdfTemp=originImgArray.get(++pdfPage);
			difLevel=PDFCompare.getDifRatio(pdfTemp, temp);
			
			if(difLevel<SAME_PAGE) {
				controller.ADD_CompletePDF(temp, captureCount+1);
			}
			/*
			else {//fail to search same PDF page
				throw new IllegalArgumentException("Fail to search same PDF page");
			}
			*/
			
			throw new IllegalArgumentException("Fail to search same PDF page");
		}
		pdfPage++;
		getNextPage=false;
		return;
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
				capNoteStartIndex=captureCount;
			}
			capNoteFinish=false;
			sameImg=0;
		}
		else {//nothing case
			if(capNoteStart&&!capNoteFinish) {//save last different point if start point is save
				capNoteFinish=true;
			}
			sameImg++;
			
			System.out.println("compare Capture img: same case, sameImg: "+sameImg);
			
			if(sameImg>=NO_WRITE_SECONDS) {//save write
				System.out.println("save writing enter");
				saveWriting();
				capNoteStart=true;
				capNoteStartIndex=captureCount+1;
			}
		}
		return;
	}
	
	private void saveWriting() {
		BufferedImage temp;
		
		
		try { //import note part
		BorderedImage.setBufferedImage();//already compare each other, so it use least compare data
		temp=BorderedImage.extractBufferedImage();
		}
		catch(Exception e) { //excetion find
			System.out.println("ERROR OCCURED IN saveWriting");
			System.out.println(e);
			return;
		}
		System.out.println("save writing");
		
		//saving
		if(capNoteStart) {//when write is exist
			controller.ADD_Note(temp, capNoteStartIndex, captureCount);
		}

		capNoteStart=false;
		capNoteFinish=false;
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
