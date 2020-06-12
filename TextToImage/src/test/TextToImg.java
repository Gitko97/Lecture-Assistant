package test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TextToImg {
	
	private ArrayList<Note> notes;
	private ArrayList<String> string;
	private ArrayList<Integer> it;
	private ArrayList<Integer> changedP;
	private int width;
	private int height;
	private int pdfPages = 1;
	private int outputCount = 1;
	private int fontSize = 20;
	private Font font;
	private String fontFamily = "바탕";
	private String fileName;
	private FileOutputStream fos;
	private Graphics2D graphics;
	private BufferedImage bImg;

	private String headLine = "";
	
	public TextToImg(ArrayList<String> sttString, ArrayList<Note> notes, ArrayList<Integer> changedPosition, int width, int height) throws Exception {
		this.string = sttString;
		this.notes = notes;
		this.changedP = changedPosition;
		this.width = width;
		this.height = height;
		
		this.font = new Font(fontFamily,Font.PLAIN,fontSize);
		
		
	}
	public void convert() throws Exception{
		fileName = Integer.toString(outputCount)+".png";
		initGraphic();
		
		int wordStart = 10;
		int lineStart = fontSize * 3;
		int noteIndex = 0;
		int cPosIndex = 0;
		int widthMargin = width;
		int heightMargin = 0;

		boolean notePrinted = false;
		
		Note note = notes.get(noteIndex++);
		BufferedImage noteImg = null; 
		System.out.println("width : " + width + " | height : " + height);
		

		for(int i = 0; i < string.size(); i++) {
			
			if (changedP.get(cPosIndex) == i) {
				if (cPosIndex < changedP.size() - 1 ) cPosIndex += 1;
				pdfPages += 1;
				imageWrite();
				initGraphic();
				lineStart = fontSize * 3;
				wordStart = 10;
				widthMargin = width;
				heightMargin = 0;
				
				
				//System.out.println("페이지 넘김");
			}
			
			// ---------------------- 노트 출력 --------------------------------
			// note의 startIndex과 string의 index가 같다면 같은 초이므로 노트의 이미지를 그릴 준비.
			if (note.startIndex == i ) {
				wordStart = 10;
				if (lineStart + fontSize * (3 * 2) > height ) {	// 하단 여백이 모자랄 경우 다음 장으로 넘기기
					imageWrite();
					initGraphic();
					lineStart = fontSize * 3;
				}
				else lineStart += fontSize * 2;
				notePrinted = true;
				noteImg = note.note;
				headLine = "<"+secondToMinute(i)+"'s note>";
				graphics.drawString(headLine, wordStart, lineStart);
				wordStart += headLine.length() * (fontSize - 3);
			}
				
				//System.out.println(noteIndex + "번째 그림과 " + i + "초의 텍스트 그리기");
			
			if (noteImg != null) {
				//System.out.println("그림");
				if (wordStart + noteImg.getWidth() + 5 > width) {	// 우측 여백이 모자랄 경우 한 줄 띄우기
					lineStart += fontSize * 2;
					wordStart = 10;
				}
				if (lineStart + noteImg.getHeight() > height ) {	// 하단 여백이 모자랄 경우 다음 장으로 넘기기
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
			// --------------------------노트 출력 끝 ---------------------------------

			String word = string.get(i);
			// i초의 word이다. 
			if (wordStart + word.length() * (fontSize - 4) + fontSize> widthMargin) {	// 가로의 남은 공간이 불충분하여 줄 띄움
				wordStart = 10;
				if (lineStart > height - fontSize * 2){	// 줄 띄울만한 여백이 존재하지 않아 다음 장으로 넘어감
					imageWrite();
					initGraphic();
					lineStart = fontSize;
				}
				lineStart += fontSize * 2;
			}
			word = " " + spaceRemover(word);
			//System.out.println("wordStart = " + wordStart + " | lineStart = " + lineStart);
			//System.out.println(word);
			graphics.drawString(word,wordStart, lineStart);
			wordStart += word.length() * (fontSize - 3);
			
		}
		imageWrite();
	}
	
	private void initGraphic() throws IOException{
		
		bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		fos = new FileOutputStream(new File(fileName));
		graphics = bImg.createGraphics();
		graphics = bImg.createGraphics();
		
		// draw canvas
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0, width, height);
		
		// draw text
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		
		// 위쪽 여백 PDf 인덱스
		graphics.drawString(Integer.toString(outputCount)+"th page",width-100, 20);
		graphics.drawString("PDF "+Integer.toString(pdfPages)+"th page",width/2, 20);

	}
	
	private void imageWrite() throws IOException {
		ImageIO.write(bImg, "PNG", fos);
		System.out.println(outputCount++ + "th note converted");
		fileName = Integer.toString(outputCount)+".png";
		
	}
	private String spaceRemover(String s) {
		if (s.charAt(0) != ' ') return s;
		else  s = spaceRemover(s.substring(1));
		return s;
	}
	
	private String secondToMinute(int s) {
		String m = Integer.toString(s / 60);
		String sc = Integer.toString(s % 60);
		return m+"min "+sc+"sec";
	}
}
