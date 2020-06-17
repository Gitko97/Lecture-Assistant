package src;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

//This Class Need Apache's PDFBox library
public class PdfAndImg{

  //Save the image received as parameter to filepath as pdf file
  public boolean IMGtoPDF(ArrayList<BufferedImage> imgs, String filePath) {	
    PDDocument document = new PDDocument(); //Make New PDF document
	try {
	  for(BufferedImage img : imgs) {
	    PDPage page = new PDPage(new PDRectangle(img.getWidth(), img.getHeight())); //Make Page with image
	    //Add page to document
		document.addPage(page); 
		PDImageXObject pdImage = JPEGFactory.createFromImage(document, img);
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		contentStream.drawImage(pdImage, 0, 0, img.getWidth(), img.getHeight());
		contentStream.close();
	  }
	  document.save(filePath); //document save
	  return true;
	} catch (IOException e) {
	  e.printStackTrace();
	}
	return false;
  }

  //Read PDF and return PDF's pages to images
  public ArrayList<BufferedImage> PdfToImg(String filePath) throws IOException {
    ArrayList<BufferedImage> imgs = new ArrayList<>();
	File readFile = new File(filePath);
	PDDocument document = PDDocument.load(readFile);
	int pageCount = document.getNumberOfPages();
	PDFRenderer pdfRenderer = new PDFRenderer(document);
	
	for(int i=0; i<pageCount; i++) {
	  imgs.add(pdfRenderer.renderImageWithDPI(i, 100, ImageType.RGB));
	}
	document.close();
	return imgs;
  }
}
