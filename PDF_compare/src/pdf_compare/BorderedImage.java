package src;

import java.awt.image.BufferedImage;

public class BorderedImage extends PdfCompare {
  private static BufferedImage imageA;
  private static BufferedImage imageB;
  private static int startPos[] = new int[2];
  private static int endPos[] = new int[2];
  private static int countX = 0;
  private static int countY = 0;

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

  private static void setSearch() { // Initializing variables to obtain coordinates where there are differences
    startPos[0] = imageB.getWidth() - 1;
	startPos[1] = imageB.getHeight() - 1;
	for(int i = 0; i < 2; i++) {
	  endPos[i] = 0;
	}
  }

  public static void searchImage() {
    getSearchStart();
	getSearchEnd();
  }

  private static void getSearchStart() { // Found upper left corner coordinates of image to extract
    int row;
    int col;
	int tmp[] = new int[2];
	
	tmp[0] = imageB.getWidth() - 1;
	tmp[1] = imageB.getHeight() - 1;

	// Obtain X coordinate in left corner
	for(row = imageB.getHeight()-1; row >= 0; row--) {
	  for(col =imageB.getWidth()-1; col >= 0; col--) {
		if(imageA.getRGB(0, 0) != imageB.getRGB(col, row)) {
		  if(row == tmp[1] && Math.abs(col - tmp[0]) == 1) {
			countX++;			// Measure pixel size to avoid noise recognition
		  } else {
			countX = 1;			// Initialize to 1 if X breaks halfway
		  }
        if(startPos[0] > col && countX >= 5) {		// Store the X coordinate when the length of the difference is more than 5 px
		  startPos[0] = col;
		}
		tmp[0] = col;
		tmp[1] = row;
		}
	  }
	}
	tmp[0] = imageB.getWidth() - 1;
	tmp[1] = imageB.getHeight() - 1;

	// Obtain Y coordinate in left corner
	for(col = imageB.getWidth()-1; col >= 0; col--) {
      for(row = imageB.getHeight()-1; row >= 0; row--) {
	    if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
          if(col == tmp[0] && Math.abs(row-tmp[1]) == 1) {
		    countY++;		// Measure pixel size to avoid noise recognition
		  } else {
			countY = 1;
		  }
        if(startPos[1] > row && countY >= 5) {		// Store Y coordinates when the length of the difference is more than 5 px
		  startPos[1] = row;
		}
		tmp[0] = col;
		tmp[1] = row;
	    }
	  }
    }
	System.out.println("Start Position : " +"x: " + startPos[0] + " y: " + startPos[1]);
  }

  private static void getSearchEnd() {		// Found lower right corner coordinates of image to extract
    int row;
    int col;
    int tmp[] = new int[2];
	countX = 0;
	countY = 0;
	tmp[0] = 0;
	tmp[1] = 0;

	// Obtain X coordinate in right corner
	for(row = 0; row < imageB.getHeight(); row++) {
      for(col = 0; col < imageB.getWidth(); col++) {
	    if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
          if(row == tmp[1] && Math.abs(col - tmp[0]) == 1) {
		    countX++;		// Measure pixel size to avoid noise recognition
		  } else {
		    countX = 1;
		  }
        if(endPos[0] < col && countX >=5) { // Store the X coordinate when the length of the difference is more than 5 px
		  endPos[0] = col;
		}
		tmp[0] = col;
		tmp[1] = row;
	    }
	  }
	}
	tmp[0] = 0;
	tmp[1] = 0;

	// Obtained Y coordinate in right corner
	for(col = 0; col < imageB.getWidth(); col++) {
      for(row = 0; row < imageB.getHeight(); row++) {
	    if(imageA.getRGB(0,0) != imageB.getRGB(col, row)) {
          if(col == tmp[0] && Math.abs(row - tmp[1]) == 1) {
		    countY++;  // Measure pixel size to avoid noise recognition
		  } else {
		    countY = 1;
		  }
          if(endPos[1] < row && countY >= 5) { // Store Y coordinates when the length of the difference is more than 5 px
		    endPos[1] = row;
		  }
		  tmp[0] = col;
		  tmp[1] = row;
		}
	  }
    }
	System.out.println("End Position " + " x: " + endPos[0] + " y: " + endPos[1]);
  }

  public static int[] extractBufferedImage() {
    int[] subPos = new int[4];
	subPos[0] = Math.min(startPos[0], endPos[0]);
	subPos[1] = Math.min(startPos[1], endPos[1]);
	subPos[2] =  Math.abs(endPos[0] - startPos[0]);
	subPos[3] = Math.abs(endPos[1] - startPos[1]);
	return subPos;
  }
/*
	public static BufferedImage extractBufferedImage()
	{
		int startX = Math.min(startPos[0], endPos[0]);
		int startY = Math.min(startPos[1], endPos[1]);
		return partB.getSubimage(startX, startY, Math.abs(endPos[0]-startPos[0]), Math.abs(endPos[1]-startPos[1]));
	//	return PDFCompare.extractImage.getSubimage(0, 0, PDFCompare.extractImage.getWidth(), PDFCompare.extractImage.getHeight());
	}
	*/
  public static int[] extractPoint() {
    return startPos;
  }
}
