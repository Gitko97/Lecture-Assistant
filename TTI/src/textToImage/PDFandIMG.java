package textToImage;
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

//Class PDFandIMG
//1. no constructor
//2. method IMGtoPDF return boolean : save imgs from args at filePath
// 3. method PDFtoIMG return ArrayList<BufferedImage> : Convert PDF at filePath from args into ArrayList<imgs>
public class PDFandIMG{
  public boolean IMGtoPDF(ArrayList<BufferedImage> imgs, String filePath) {	// save imgs from args at filePath
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
	
  public ArrayList<BufferedImage> PDFtoIMG(String filePath) throws IOException { // Convert PDF at filePath from args into ArrayList<imgs>
    ArrayList<BufferedImage> imgs = new ArrayList<>();
	File readFile = new File(filePath);
	PDDocument document = PDDocument.load(readFile);
	int pageCount = document.getNumberOfPages();
	PDFRenderer pdfRenderer = new PDFRenderer(document);
	for(int i = 0; i < pageCount; i++) {
	  imgs.add(pdfRenderer.renderImageWithDPI(i, 100, ImageType.RGB));
	}
	document.close();
	return imgs;
  }
}
