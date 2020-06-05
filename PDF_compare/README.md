ImgCompare
==========
this class has methods that compare 2 Buffered image


variable
--------

```java
 private static boolean isDebugging
```

this is boolean variable that just can access debugging part.

when it is true, we can get 2 png image file that different part.

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
//these variables need to be controled by semaphore!

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
 protected static void extractDifferentPart() 
```

//need to be controled by semaphore!

this is method that export image files that different part.

file name is difPartA, and difPartB.

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



public class PDFCompare extends ImgCompare
==========================================
this class has methods that compare 2 PDF captured image

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

//return int value that calculate getPixelDif(originTransform, videoTransform),

this method is convert origin, and video BufferedImage to same size, and cut margin(if option setting allow that)

when check each parameters are not null value, then check margin and cut margin.

resizing each BufferedImage, and set pixelAmount and input each converted BufferedImage to getPixelDif method.

```java
 public static void setIsMarginCheckAuto(boolean check)
```

this method set isMarginCheckAuto

if input is false, program is not check margin automatically, else, check margin automatically.

```java
 private static BufferedImage marginCut(BufferedImage video)
```

//return BufferedImage thar cut margin

this method is convert BufferedImage that cut margin, and return converted BufferedImage.

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