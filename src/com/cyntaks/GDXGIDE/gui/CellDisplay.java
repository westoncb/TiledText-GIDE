package com.cyntaks.GDXGIDE.gui;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.Corpus;
import com.cyntaks.GDXGIDE.GIDEApp;

public class CellDisplay {
	private ContentLine[][] contents;
	public static final int LAYER_TOP = 0;
	public static final int LAYER_MIDDLE = 1;
	public static final int LAYER_BOTTOM = 2;
	public static final int LINE_TOP = 0;
	public static final int LINE_Y_CENTER = 1;
	public static final int LINE_BOTTOM = 2;
	public static final int LINE_LEFT = 3;
	public static final int LINE_X_CENTER = 4;
	public static final int LINE_RIGHT = 5;
	
	public CellDisplay() {
		contents = new ContentLine[3][6];
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents[i].length; j++) {
				contents[i][j] = new ContentLine();
			}
		}
	}
	
	public ContentLine getContentLine(int layer, int line) {
		return contents[layer][line];
	}
	
	public void load() {
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents[i].length; j++) {
				ContentLine line = contents[i][j];
				for (Corpus corpus : line) {
					corpus.load();
				}
			}
		}
	}
	
	public void init() {
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents[i].length; j++) {
				ContentLine line = contents[i][j];
				for (Corpus corpus : line) {
					if(corpus != null)
						corpus.init();
				}
			}
		}
	}
	
	public void update(float delta, float x, float y, float width, float height) {
		
		for(int j = 0; j < contents.length; j++) {
			ContentLine[] cLayer = contents[j];
			
			for (int i = 0; i < cLayer.length; i++) {
				ContentLine line = cLayer[i];
				switch (i) {
				case LINE_TOP:
					layoutHorizontalLine(i, line, x, y, width, height);
					break;
				case LINE_Y_CENTER:
					layoutHorizontalLine(i, line, x, y, width, height);
					break;
				case LINE_BOTTOM:
					layoutHorizontalLine(i, line, x, y, width, height);
					break;
				case LINE_LEFT:
					layoutVerticalLine(i, line, x, y, width, height);
					break;
				case LINE_X_CENTER:
					layoutVerticalLine(i, line, x, y, width, height);
					break;
				case LINE_RIGHT:
					layoutVerticalLine(i, line, x, y, width, height);
					break;
				}
				
				for(Corpus c : line)
					c.update(delta);
			}
		}
	}
	
	public void draw(int layer, float r, float g, float b, float a) {
		Color old = GIDEApp.SPRITE_BATCH.getColor();
		GIDEApp.SPRITE_BATCH.setColor(r, g, b, a);
		ContentLine[] cLayer = contents[layer];
		
		
		for (int i = 0; i < cLayer.length; i++) {
			ContentLine line = cLayer[i];	
			for (Corpus c : line)
				c.draw();
		}
		
		GIDEApp.SPRITE_BATCH.setColor(old);
	}
	
	private void layoutHorizontalLine(int lineIndex, ContentLine line, float x, float y, float width, float height) {
		float insertX = x;
		for(Corpus c : line) {
			if(c == null)
				System.out.println("test");
			c.setX(insertX);
			c.setY(y);
			insertX += c.getWidth();
		}
		
		float totalWidth = insertX - x;
		float transAmount = 0;

		switch (line.getAttachLocation()) {
		case ContentLine.ATTACH_MIDDLE:
			transAmount = (width - totalWidth)/2;
			break;
		case ContentLine.ATTACH_END:
			transAmount = (width - totalWidth);
			break;
		}
		for(Corpus c : line)
			c.setX(c.getX() + transAmount);
		
		transAmount = 0;
		switch (lineIndex) {
		case LINE_Y_CENTER:
			for(Corpus c : line) {
				transAmount = (height - c.getHeight())/2;
				c.setY(c.getY() + transAmount);
			}
			break;
		case LINE_BOTTOM:
			for(Corpus c : line) {
				transAmount = (height - c.getHeight());
				c.setY(c.getY() + transAmount);
			}
			break;
		}
	}
	
	private void layoutVerticalLine(int lineIndex, ContentLine line, float x, float y, float width, float height) {
		float insertY = y;
		for(Corpus c : line) {
			c.setX(x);
			c.setY(insertY);
			insertY += c.getHeight();
		}
		
		float totalHeight = insertY - y;
		float transAmount = 0;

		switch (line.getAttachLocation()) {
		case ContentLine.ATTACH_MIDDLE:
			transAmount = (height - totalHeight)/2;
			break;
		case ContentLine.ATTACH_END:
			transAmount = (height - totalHeight);
			break;
		}
		for(Corpus c : line)
			c.setY(c.getY() + transAmount);
		
		transAmount = 0;
		switch (lineIndex) {
		case LINE_X_CENTER:
			for(Corpus c : line) {
				transAmount = (width - c.getWidth())/2;
				c.setX(c.getX() + transAmount);
			}
			break;
		case LINE_RIGHT:
			for(Corpus c : line) {
				transAmount = (width - c.getWidth());
				c.setX(c.getX() + transAmount);
			}
			break;
		}
	}
	
	public ContentLine[][] getContents() {
		return contents;
	}
}