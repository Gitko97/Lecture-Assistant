package src;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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

public class PdfAndImg{
	
  public boolean IMGtoPDF(ArrayList<BufferedImage> imgs, String filePath) {	// 留ㅺ컻蹂��닔濡� 諛쏆� imgs�뱾�쓣 filePath�뿉 ���옣
    PDDocument document = new PDDocument();
	try {
	  for(BufferedImage img : imgs) {
	    PDPage page = new PDPage(new PDRectangle(img.getWidth(), img.getHeight()));
		document.addPage(page);
		PDImageXObject pdImage = JPEGFactory.createFromImage(document, img);
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		contentStream.drawImage(pdImage, 0, 0, img.getWidth(), img.getHeight());
		contentStream.close();
	  }
	  document.save(filePath);
	  return true;
	} catch (IOException e) {
	  e.printStackTrace();
	}
	return false;
  }
	
  public ArrayList<BufferedImage> PdfToImg(String filePath) throws IOException { //留ㅺ컻蹂��닔濡� 諛쏆� filePath�쓽 PDF瑜� IMG濡� 蹂��솚�썑 ArrayList濡� 諛섑솚
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
