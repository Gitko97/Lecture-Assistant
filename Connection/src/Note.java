package src;

import java.awt.image.BufferedImage;

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
