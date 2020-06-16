ImgCompare
==========
this class has methods that compare 2 Buffered image


variable
--------

```java
 private static double hueNoise=0.1, satNoise=0.35, lumNoise=0.5
```

these variables are allow noise that different each images' pixel.

these variables are using isHSLDifferent to jedge different.

they are set 0.1, 0.35, 0.5 default, and setNoise method can change these values.

```java
 protected static BufferedImage partA, partB;
```

these variables are using extract img.

method
-------

```java
 public static int getPixelDif(BufferedImage a, BufferedImage b)
```

//return int variable that how much different pixel number.

this is method that get each BufferedImage pixel different number.

different judge is decided by "isHSLdifferent" method.

it has lagacy remark that compare to RGB value.(it don't use ANYMORE)


```java
 protected static boolean isHSLdifferent(double[] aHSL, double[] bHSL)
```

//same: return false, different: return false. 

this is method that check each HSL array value is different.

because of noise, each pixel is very hard to perfectly same.

so each Hue, Saturation, Luminace value compare part is considered that how much allow same to small different.

if anypart is not allow, this method return true, else, return false.

Hue need to considered that how much different Saturation and Luminace too.

Saturation also considered that how much different Luminace too.

```java
 protected static double[] getHSLfromRGB(int rgb)
 protected static double[] getHSLfromRGB(int[] rgbArray)
```

//return double array that has HSL value.

these are methods that get HSL array to RGB value(OR RGB array)

all array double value just in 0<value, AND 1>value.

```java
 protected static int[] RGBtoArray(int rgb)
```

//return int array that meaning rgbArray

this is method that get rgbArray from rgb value.

```java
 protected static int getRGBdifSum(int arrayA[], int arrayB[])
```

//return int value that meaning each rgbArray's different part.

this is method that get different value of each rgbArray's red, green, and blue value.

this method use to compare each pixel value to rgb different.

```java
 public static int setNoise(double h, double s, double l)
```

this is method that setting noise variables.

each parameters are matching hueNoise, satNoise, and lumNoise. 



public class PDFCompare extends ImgCompare
==========================================
this class has methods that compare 2 PDF captured image OR video captured image

methods about margin are assume that using video, which is compared PDF captured image.

variable
---------------

```java
 private static int left=0, right=0
 private static int top=0, down=0
```

these variables are meaning that set no margin area.

default values are all 0, but when need to cut margin, these variables are set each values meaning no margin area.

```java
 private static int pixelAmount=0
```

this variable is meaning that how much size of image.

default is 0, but when compare two images, each BufferedImage is converted same size.

and this variable is set width×height value.

```java
 private static boolean isCutMargin[]= {false, false}
```

this variable is meaning that cut margin or not.

isCutMargin[0] is meaning top and down margin.

isCutMargin[1] is meaning left and right margin.

```java
 private static boolean isMarginCheckAuto=true
```

this variable is meaning that set margin area automatically or not.

method
----------------

```java
 public static double getDifRatio(BufferedImage origin, BufferedImage video)
```

//return ((double)difPixelNum/(double)pixelAmount);

this method is meaning that each BufferedImage is how much same.

when it get different Pixel number to getPDFDifValue,

it calculate with pixelAmount and return double value.

```java
 public static int getPDFDifValue(BufferedImage origin, BufferedImage video)
```

//return int value that calculate getPixelDif(originTransform, videoTransform)

this method is convert original, and video BufferedImage to same size, and cut margin(if option setting allow that)

when check each parameters are not null value, then check margin and cut margin.

resizing each BufferedImage, and set pixelAmount and input each converted BufferedImage to getPixelDif method.

```java
 public static BufferedImage getMarginCut(BufferedImage img)
```

//return BufferedImage that cut margin

this method is just cut margin in any image.

you don't need to do before using this method.


```java
 private static BufferedImage marginCut(BufferedImage video)
```

//return BufferedImage thar cut margin

this method is convert BufferedImage that cut margin, and return converted BufferedImage.


```java
 public static void setIsMarginCheckAuto(boolean check)
```

this method set isMarginCheckAuto

if input is false, program is not check margin automatically, else, check margin automatically.

```java
 private static void setIsCutMarginAuto(BufferedImage origin, BufferedImage video)
```

this method is set isCutMargin automatically

this method judge margin that compare each BufferedImage.

if top, OR left side averge value is different, then each direction has margin.

```java
 public static void setIsCutMargin(boolean row, boolean car)
```

this magin set isCutMargin by input two value.

isCutMargin[0]=row;

isCutMargin[1]=car;

this method is for not automatic setting when program has mistake to search margin.

```java
 public static void setArea(int l, int r, int t, int d)
```

this method is set PDFCompare's class variables,(left, right, top, down) value.

this method is for not automatic setting when program has mistake to set where is not margin area.

```java
 public static void setNoMarginArea(BufferedImage origin)
```

this method is that get video's width area and height area that are not margin AUTOMATICALLY.

when method is started,(left, right, top, down), which is PDFCompare's class variables, is set input parameter BufferedImage's value default.

```java
(
	left=0;
	right=origin.getWidth()-1;
	top=0;
	down=origin.getHeight()-1;
)
```

when method is processed, this method reference isCutMargin, that if isCutMargin's part is false, then program understand that direction is not margin, then that direction's no margin area is not change more.

else, method calculate averge pixel line value, and compare next value.

//meaning that if direction is row, compare each column pixel line's averge value. in column case, compare each row.

in calculate, highest two different values are save with index level.

//if direction is row, highest two different values are save with each column index value.

then save each index value in (left, right), OR (top, down).

```java
 private static BufferedImage resize(BufferedImage origin, int afterWidth, int afterHeight)
```

//return BufferedImage that resize image

just resize image input afterWidth and afterHeight.

```java
 private static BufferedImage imageToBufferedImage(Image img)
```

//return BufferedImage that converted img

just convert Image variable to BufferedImage.


public class BorderedImage extends PDFCompare
==========================================

this class has methods that cut different part and extract that BufferedImage.

variable
---------------

```java
 private static BufferedImage imageA, imageB;
```

two BufferedImage that using extract different part.

```java
 private static int startPos[] = new int[2];
 private static int endPos[] = new int[2];
```

two int arrays are meaning that set location about different part.

```java
 private static int countX = 0;
 private static int countY = 0;
```

two int variables are meaning that index count.


method
---------------

```java
 public static void setBufferedImage(BufferedImage a, BufferedImage b)
 public static void setBufferedImage()
```

these method import do to ready extract different part.

if parameters are no exist, this method use partA and partB, which is static BorderedImage variables in ImgCompare class.

once set imageA and imageB, these methods call setSearch and searchImage.


```java
 private static void setSearch()
```

this method is initializing variables to obtain coordinates where there are differences.

```java
 public static void searchImage()
```

this method call getSearchStart and getSearchEnd.

```java
 private static void getSearchStart()
```

this method find upper left corner coordinates of image to extract.

```java
 private static void getSearchEnd()
```

this method find lower right corner coordinates of image to extract.

```java
 public static int[] extractBufferedImage()
```

this method return location about different part.

```java
 public static int[] extractPoint()
```

this method return startPos.

public class Compare implements Runnable
==========================================

this class is main class that compare each captured images, and save extracted different part.

remark is meaning that don't use now, but will use.

variable
---------------

```java
 private ArrayList<BufferedImage> originImgArray;
 private Capturing capturing;
 private LA_controller controller;
```

these variables are objects that need to get captured video images, PDF images, and need to save all data on *.pdf

originImgArray has captured PDF images information.

capturing has captured video images information.

controller is critical part that need to save data.

```java
 private BufferedImage capTemp1, capTemp2;
 private BufferedImage startBufImg, endBufImg;
```

these variables are BufferedImages that save each captured image's index.

capTemp1, capTemp2 are BufferedImages that compared captureCount and captureCount+1 images.

startBufImg, endBufImg are BufferedImages that start writing and end writing.

```java
 private static boolean exit;
 private boolean getNextPage;
 private boolean capNoteStart, capNoteFinish;
```

these variables are boolean variables that meaning each flag about now state.

exit is flag that finish captured, so if this flag is ture, compare is finished.

getNextPage is flag that do getNextPage or not.

capNoteStart, capNoteFinish are flags that meaning writing is start, and finish.

```java
   private final double SAME_CAP=0.00001;
// private final double SAME_PAGE=0.05;
   private final double DIF_PAGE=0.03;
```

these final variables are constant value that compare different value.

these variables are standards that meaning PDFCompare class's getDifRatio's return value.

if getDifRatio's return's value is smaller than SAME_CAP, each BorderedImages are same(about video captured images).

if getDifRatio's return's value is smaller than SAME_PAGE, each BorderedImages are same(about PDF captured images VS video captured images).

if getDifRatio's return's value is bigger than DIF_PAGE, each BorderedImages are different.

```java
   private int maxTime=3600;
// private int startPage=0;
   private int sameImg;
   private int captureCount;
   private int capNoteStartIndex;
// private int pdfPage;
   private final int NO_WRITE_SECONDS=3;
```

these variables are indexes or related index.

maxTime is value that how much long time to compare image.

startPage is value that what is start number of original PDF page.

sameImg is count that how many video capturing images that each are same.

captureCount is index that about call captured image from Capturing class.

capNoteStartIndex is index that writing start time.

pdfPage is index that meaning now original PDF page.

NO_WRITE_SECONDS is constant value that how long time to accept writing state that no write.(if value is 3, than 3 seconds is limit that remain state of writing when new write is not detect)

method
----------------

```java
	public Compare(
			ArrayList<BufferedImage> originPDFArray
			, Capturing cap, LaController LAC
			) {
		originImgArray=originPDFArray;
		capturing=cap;
		controller=LAC;
		
		exit=false;
	}
```

just constructor.

set each objects parameters and exit flag to false.

```java
 public void run()
```

this is main method.

when this method is start, initialize these variables.

```java
		captureCount=1;
		sameImg=0;
//		pdfPage=startPage;
		getNextPage=false;
		capNoteStart=false;
		capNoteFinish=false;
```

and, check exception, if exit flag is not true AND captureCount<maxTime, while part is not finish.

these fallow content is about while part.

when this program get new captured image, then this method is running.

if getNextPage is true, enter the pageChange method.

else, enter the compareCapturedImage method.

after get out these two methods, captureCount++.

and do while part again.

when while part is finish, this method return.

```java
 private void checkException()
```

this method is just check objects variables, originImgArray, capturing is null or not.

if one of these is null, throw NullPointerException.

```java
 private void pageChange()
```

when enter this method, call controller.ADD_CompletePDF(capTemp1, captureCount+1).

and, set getNextPage to false.

```java
 private void compareCapturedImage()
```

this method compare capturing images that (captureCount) and (captureCount+1).

capTemp1 set capturing image which index number is captureCount.

capTemp2 set capturing image which index number is captureCount+1.

once compare each images, double difLevel, which is this method's local variable get return value about BorderedImage.getDifRatio(capTemp1, capTemp2)

and compare difLevel and DIF_PAGE, SAME_CAP.

all processe is like this:
```java
		if(difLevel>DIF_PAGE) {//different case
			System.out.println("compare Capture img: different case");
			if(capNoteStart) {//if any write is there
				capNoteFinish=true;
				endBufImg=capTemp1;
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
```
//different case

in this case, if last state is writing state, call saveWriting();

and set getNextPage true, and captureCount-- to go to pageChange()

//write case

in this case, set startBufImg to front image(=capTemp1) and capNoteStartIndex to captureCount.

and set capNoteFinish to false, so writing continue.

//nothing case

in this case, program judge that captured images are same images.

so set capNoteFinish to ture, and endBufImg to capTemp1.

and sameImg++, and if sameImg>=NO_WRITE_SECONDS, call saveWriting().

```java
 private void saveWriting()
```

this method is save writing note.

if capNoteStart is false, program don't save writing, so directly return this method.

else, do this processe.

```java
 		BorderedImage.getDifRatio(startBufImg, endBufImg);
		BorderedImage.setBufferedImage();
		int subPos[]=BorderedImage.extractBufferedImage();//already compare each other, so it use least compare data
		
		//saving
	    if(capNoteStart) {//when write is exist
	    	  
	          System.out.println("ADD!");
	          controller.ADD_Note(endBufImg.getSubimage(subPos[0], subPos[1], subPos[2], subPos[3]), capNoteStartIndex, captureCount);
	       
	    }
```

compare startBufImg and endBufImg. and get BorderedImage which is extract different part in two each images.

if anything is not wrong, call ADD_Note to save this different part.

finished this part, this method set capNoteStart and capNoteFinish to false, and return this method.

```java
// public void setStartPage(int num)
```
this method is set startPage to parameter "num".

if num<=0, throw IllegalArgumentException.

```java
 public void setMaxTime(int time)
```

this method is set maxTime to parameter "time".

if time<=0, throw IllegalArgumentException.

```java
 public boolean exit()
```

set exit to true. and return capturing.endPos(captureCount);

once call this method, while part in run method is finish forced.