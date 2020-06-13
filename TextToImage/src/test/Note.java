package test;

import java.awt.image.BufferedImage;

//Class Note
//1. args are BufferredImage note, int startIndex, int endIndex
public class Note {
	BufferedImage note;
	int startIndex;
	int endIndex;
	
	public Note(BufferedImage note, int startIndex, int endIndex) {
		this.note = note;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		
		
	}
}
