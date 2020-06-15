package test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File; //debug for fileIO
import java.io.FileOutputStream; //debug for fileIO
import javax.imageio.ImageIO; // debug for fileIO

// Class TextToImg
// 1. args are ArrayList<String> STTString, ArrayList<Note> notes, ArrayLise<Integer> changedPosition, int width, int height
// 2. method convert return void : Run a conversion of whole STTString and notes into an image. External calls.
// 3. method initGraphic return void : Initialize a imageBuffer to draw
// 4. method imageWrite return void : Convert the graphic buffer that method 'convert' drew into images
// 5. method spaceRemover return String : Remove space
// 6. method secondToMinute return String: Convert seconds into minutes.
public class TextToImg {
	
	private ArrayList<Note> notes;
	private ArrayList<String> string;
	private ArrayList<Integer> changedP;
	private ArrayList<BufferedImage> result = new ArrayList<>();
	private int width;
	private int height;
	private int pdfPages = 1;
	private int outputCount = 1;
	private int fontSize = 20;
	private Font font;
	private String fontFamily = "바탕";
	private String fileName; // debug for fileIO
	private FileOutputStream fos; // debug for fileIO
	private Graphics2D graphics;
	private BufferedImage bImg;

	private String headLine = "";
	
	// TextToImg Constructor. 
	public TextToImg(ArrayList<String> sttString, ArrayList<Note> notes, ArrayList<Integer> changedPosition, int width, int height) throws Exception {
		this.string = sttString;
		this.notes = notes;
		this.changedP = changedPosition;
		this.width = width;
		this.height = height;
		this.font = new Font(fontFamily,Font.PLAIN,fontSize);	
	}
	
	// method convert
	public ArrayList<BufferedImage> convert() throws Exception{
		fileName = Integer.toString(outputCount)+".png"; // debug for fileIO
		initGraphic();
		
		int wordStart = 10;
		int lineStart = fontSize * 3;
		int noteIndex = 0;
		int cPosIndex = 0;
		int widthMargin = width;
		int heightMargin = 0;
		Note note = null;
		boolean notePrinted = false;
		
		if (notes != null) note = notes.get(noteIndex++);
		BufferedImage noteImg = null; 
		System.out.println("width : " + width + " | height : " + height);
		

		for(int i = 0; i < string.size(); i++) {
			

			if (changedP != null && changedP.get(cPosIndex) == i) {
				if (cPosIndex < changedP.size() - 1 ) cPosIndex += 1;
				pdfPages += 1;
				imageWrite();
				initGraphic();
				lineStart = fontSize * 3;
				wordStart = 10;
				widthMargin = width;
				heightMargin = 0;
				
				
				//System.out.println("페이지 넘김"); // debugging
			}
			
			// ---------------------- print notations from Note class --------------------------------
			// When note's startIndex and STTString's index i is same, ready to draw an notation from Note
			if (note != null && note.startIndex == i ) {
				wordStart = 10;
				if (lineStart + fontSize * 6 > height ) {	// In the case of lack of bottom margins
					imageWrite();
					initGraphic();
					lineStart = fontSize * 3;
				}
				else lineStart += fontSize * 2;
				notePrinted = true;
				noteImg = note.note;
				headLine = "<"+secondToMinute(i)+"'s note>";	// print a note's index to identify
				graphics.drawString(headLine, wordStart, lineStart);
				wordStart += headLine.length() * (fontSize - 3);
			}
				
				//System.out.println(noteIndex + "th image and " + i + "'s text"); // debugging
			
			if (noteImg != null) {
				noteImg = sizeCheck(noteImg);
				//System.out.println("그림");
				if (wordStart + noteImg.getWidth() + 5 > width) {	// line spacing due to lack of right margins
					lineStart += fontSize * 2;
					wordStart = 10;
				}
				if (lineStart + noteImg.getHeight() > height ) {	// init new page when there is no margins to draw notation
					imageWrite();
					initGraphic();
					wordStart = 10;
					lineStart = fontSize * 3;
				}
				
				graphics.drawImage(noteImg, width - noteImg.getWidth() - 5, lineStart, null);
				widthMargin = width - noteImg.getWidth() - 5;
				heightMargin = lineStart + noteImg.getHeight();
				
				noteImg = null;
			}
			
			if (notePrinted) {
				if (note.endIndex == i) {
					
					notePrinted = false;
					lineStart = heightMargin + fontSize * 3;
					wordStart = 10;
					widthMargin = width;
					if (noteIndex < notes.size()) {
						note = notes.get(noteIndex++);
					}
				}
			}
			// --------------------------print end ---------------------------------

			String word = string.get(i);
			// i초의 word이다. 

			word = " " + spaceRemover(word);
			if (wordStart + word.length() * (fontSize - 4) + fontSize> widthMargin) {	// line spacing due to lack of right margins
				wordStart = 10;
				if (lineStart > height - fontSize * 2){	// init new page when there is no margins to space a line
					imageWrite();
					initGraphic();
					lineStart = fontSize;
				}
				lineStart += fontSize * 2;
			}
			//System.out.println("wordStart = " + wordStart + " | lineStart = " + lineStart); // debugging
			//System.out.println(word); // debugging
			graphics.drawString(word,wordStart, lineStart);
			wordStart += wordSpace(word);
			
		}
		imageWrite();
		return result;
		
	}
	
	private void initGraphic() throws IOException{
		
		bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		fos = new FileOutputStream(new File(fileName)); // debug for fileIO
		graphics = bImg.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0, width, height);
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		
		// print lecture note's index and whole pdf's index at head area 
		graphics.drawString(Integer.toString(outputCount)+"th page",width-100, 20);
		graphics.drawString("PDF "+Integer.toString(pdfPages)+"th page",width/2, 20);

	}
	
	private void imageWrite() throws IOException {
		result.add(bImg);
		//----- debug for file IO--------
		ImageIO.write(bImg, "PNG", fos);
		System.out.println(outputCount++ + "th note converted"); //debugging
		fileName = Integer.toString(outputCount)+".png";
		//-------------------------------
	}
	private String spaceRemover(String s) {
		
		if (s.length() == 0 || s.charAt(0) != ' ') return s;
		else  s = spaceRemover(s.substring(1));
		return s;
	}
	
	private String secondToMinute(int s) {
		String m = Integer.toString(s / 60);
		String sc = Integer.toString(s % 60);
		return m+"min "+sc+"sec";
	}
	private int wordSpace(String word){
		if (word.length() == 1) return 0; 
		int tempLength = 0;
		for (char c : word.toCharArray()){
			if (c == ' ') tempLength += (fontSize - 13); // case of space
			else if (c > 122) tempLength += (fontSize);	// case of korean
			else tempLength += (fontSize - 8); // case of english
		}
		return tempLength;
	}
	private BufferedImage sizeCheck(BufferedImage img){
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		while(imgWidth > width/2 || imgHeight > height/2){
			System.out.println("호출됨");
			imgWidth = imgWidth * 3 / 4;
			imgHeight = imgHeight * 3 / 4;
		}
		IMG_Resize resizer = new IMG_Resize(imgWidth, imgHeight);
		img = resizer.ResizeIMG(img);
		return img;
	}
	
}
