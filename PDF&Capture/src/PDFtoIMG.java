import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFtoIMG {
	ArrayList<BufferedImage> imgs = new ArrayList<>();
	File file;
	PDDocument document;
	int pageCount;
	public PDFtoIMG(String filePath) {
		file = new File(filePath);
		try {
			document = PDDocument.load(file);
			pageCount = document.getNumberOfPages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BufferedImage> getIMG(){
		return this.imgs;
	}
	
	public void change() {
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		try {
			for(int i=0;i<pageCount;i++) {
				imgs.add(pdfRenderer.renderImageWithDPI(i, 100, ImageType.RGB));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
