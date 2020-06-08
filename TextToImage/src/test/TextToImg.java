package test;

import java.awt.*;
import java.awt.image.BufferedImage;
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
	
	private int outputCount = 1;
	private int fontSize = 20;
	private Font font;
	private String fontFamily = "바탕";
	private String fileName;
	private FileOutputStream fos;
	private Graphics2D graphics;
	private BufferedImage bImg;

	
	public TextToImg(ArrayList<String> sttString, ArrayList<Note> notes, ArrayList<Integer> changedPosition, int width, int height) throws Exception {
		this.string = sttString;
		this.notes = notes;
		this.changedP = changedPosition;
		this.width = width;
		this.height = height;
		
		this.font = new Font(fontFamily,Font.PLAIN,fontSize);
		
		convert();
		
	}
	public void convert() throws Exception{
		fileName = Integer.toString(outputCount)+".png";
		initGraphic();
		
		int wordStart = 10;
		int lineStart = fontSize * 3;
		int noteIndex = 0;
		Note note = notes.get(noteIndex);
		BufferedImage noteImg = null; 
		
		for(int i = 0; i < string.size(); i++) {
			
			// note의 startIndex과 string의 index가 같다면 같은 초이므로 노트의 이미지를 그릴 준비.
			if (note.startIndex == i ) {
				System.out.println(noteIndex + "번째 그림과 " + i + "초의 텍스트 그리기");
				noteImg = note.note;
			}
			if (noteImg != null) { // 그려야할 이미지가 있다. 여백을 고려하여 출력 혹은 띄우기나 새 장 그리기 필요
				// 그리기 필요
			}
			// if (cPos로 강의에서 pdf화면이 넘어간걸 인식했을 때, 기존 이미지 내보내고 그래픽 초기화)
			
			String word = string.get(i);
			// i초의 word이다. 
			if (wordStart + word.length() * (fontSize - 3) + 40 > width) {	// 가로의 남은 공간이 불충분하여 줄 띄움
				wordStart = 10;
				if (lineStart > height - fontSize * 2){	// 줄 띄울만한 여백이 존재하지 않아 다음 장으로 넘어감
					imageWrite();
					initGraphic();
					lineStart = fontSize;
				}
				lineStart += fontSize * 2;
			}
			System.out.println("wordStart = " + wordStart + " | lineStart = " + lineStart);
			graphics.drawString(word, wordStart, lineStart);
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
		graphics.drawString(Integer.toString(outputCount),width/2, 20);
	}
	
	private void imageWrite() throws IOException {
		ImageIO.write(bImg, "PNG", fos);
		System.out.println(outputCount++ + "th note converted");
		fileName = Integer.toString(outputCount)+".png";
		
		
		
	}
}
