ImgCompare
==========
this class has methodes that compare 2 Buffered image


variable
--------
* private static boolean isDebugging

this is boolean variable that just can access debugging part.
when it is true, we can get 2 png image file that different part.



method
-------
* public static int getPixelDif(BufferedImage a, BufferedImage b)

//return int variable that how much different pixel number.

this is method that get each BufferedImage pixel different number.

different judge is decided by "isHSLdifferent" method.

it has lagacy remark that compare to RGB value.(it don't use ANYMORE)

* protected static boolean isHSLdifferent(double[] aHSL, double[] bHSL)

//same: return false, different: return false. 

this is method that check each HSL array value is different.

because of noise, each pixel is very hard to perfectly same.

so each Hue, Saturation, Luminace value compare part is considered that how much allow same to small different.

if anypart is not allow, this method return true, else, return false.

Hue need to considered that how much different Saturation and Luminace too.

Saturation also considered that how much different Luminace too.

* protected static double[] getHSLfromRGB(int rgb)
* protected static double[] getHSLfromRGB(int[] rgbArray)

//return double array that has HSL value.

these are methodes that get HSL array to RGB value(OR RGB array)

all array double value just in 0<value, AND 1>value.

* protected static int[] RGBtoArray(int rgb)

//return int array that meaning rgbArray

this is method that get rgbArray from rgb value.

* protected static int getRGBdifSum(int arrayA[], int arrayB[])

//return int value that meaning each rgbArray's different part.

this is method that get different value of each rgbArray's red, green, and blue value.

this method use to compare each pixel value to rgb different.

* public static int setNoise()

!THIS METHOD IS NOT CONSTRUCT YET, NEED TO HELP!



public class PDFCompare extends ImgCompare
==========================================
this class has methodes that compare 2 PDF captured image

variable
---------------
* private static int left=0, right=0
* private static int top=0, down=0

these variables are meaning that set no margin area.

default values are all 0, but when need to cut margin, these variables are set each values meaning no margin area.

* private static int allowDif=200

this variable is meaning that how much allow different pixel

default is 200.

* private static int pixelAmount=0

this variable is meaning that how much size of image.

default is 0, but when compare two images, it is set images width×height value.

* private static boolean isCutMargin[]= {false, true}

this variable is meaning that cut margin or not.


isCutMargin[0] is meaning top and down margin.

isCutMargin[1] is meaning left and right margin.

* private static boolean isMarginCheckAuto=true

this variable is meaning that set margin area automatically or not.

method
----------------
* public static boolean compare(BufferedImage origin, BufferedImage video)

//return different:true, same:false.

this method is meaning that each BufferedImage is same or not.

when it get different Pixel number to getPDFDifValue,

it calculate with pixelAmount and allowDif that it can set same judge or not.

////////CONTINUE TO WRITE//////////