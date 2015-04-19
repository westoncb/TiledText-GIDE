package com.cyntaks.GDXGIDE.gui;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.Corpus;

public class GridDisplay extends Corpus {
	private ContentLine[] contents;
	private int orientation;
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	private int gapSize;
	private int gridWidth;
	private int gridHeight;

	private boolean animate;
	private boolean animateThisFrame; //having everything animated automatically can be nice, but sometimes you need something to jump somewhere immediately;
										  //i.e. dropping some tiles into a new spot an not having them come shooting from offscreen first. Use the eponymous method.
	private float animateSpeed = 20;
	private boolean contentsNotSet = true;
	
	public void configureAppearance(int orientation, int lines, int gapSize) {
		contents = new ContentLine[lines];
		for (int i = 0; i < lines; i++) {
			contents[i] = new ContentLine();
		}
		this.orientation = orientation;
		this.gapSize = gapSize;
	}
	
	public ContentLine getContentLine(int line) {
		return contents[line];
	}
	
	public int indexOf(ContentLine line) {
		for (int i = 0; i < contents.length; i++) {
			if(contents[i] == line)
				return i;
		}
		
		return -1;
	}
	
	public ContentLine getLineWithCorpus(Corpus target) {
		for (int i = 0; i < contents.length; i++) {
			if(contents[i].contains(target))
				return contents[i];
		}
		
		return null;
	}
	
	public ContentLine getLineBefore(ContentLine line) {
		for (int i = 0; i < contents.length; i++) {
			if(contents[i] == line) {
				if(i > 0)
					return contents[i-1];
				else
					return null;
			}
		}
		
		return null;
	}
	
	public ContentLine getLineAfter(ContentLine line) {
		for (int i = 0; i < contents.length; i++) {
			if(contents[i] == line) {
				if(i < contents.length-1)
					return contents[i+1];
				else
					return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets called by a Grid every frame.
	 * 
	 * The ArrayList here should be <Corpus>, but has no explicit type since sometimes we would like to pass in an ArrayList of a subtype
	 * of Corpus, e.g. Cell, but Java does not allow this.
	 *   
	 * @param cells
	 */
	public void setContents(ArrayList newContents) {
		for (int i = 0; i < contents.length; i++) {
			contents[i].clear();
		}
		
		int count = 0;
		for(Object o : newContents) {
			Corpus c = (Corpus)o;
			int index = count % contents.length;
			if(c.getBeforeBlock() != null) {
				contents[index].add(c.getBeforeBlock());
				c.setBeforeBlock(null); 
			}
			contents[index].add(c); //(count % contents.length) is a way of cycling through the content lines. If contents.length == 3 and cells.size() == 6,
			if(c.getAfterBlock() != null) {			  //then a 3x2 grid is built; each of the three content lines will have two cells in it.
				contents[index].add(c.getAfterBlock()); //The afterBlock is a transitory, dynamic spacer that can be placed between cells.
				c.setAfterBlock(null); //The afterBlock for the cell is nulled here; next frame the afterBlock will not be added to the layout again, unless something else
									   //generates a new afterBlock for the cell.
			}
			count++;
		}
		
		contentsNotSet = false;
	}
	
	public void update(float delta, float x, float y, float width, float height) {
		this.gridWidth = 0;
		this.gridHeight = 0;
		
		x += getX();
		y += getY();
		
		if(orientation == HORIZONTAL) {
			float nextY = y;
			for (int i = 0; i < contents.length; i++) {
				ContentLine line = contents[i];
				nextY = layoutHorizontalLine(line, x, nextY, width, height);
			}
			this.gridHeight = (int)Math.floor(nextY - gapSize);
		}
		else {
			float nextX = x;
			for (int i = 0; i < contents.length; i++) {
				ContentLine line = contents[i];
				nextX = layoutVerticalLine(line, nextX, y, width, height);
			}
			this.gridWidth = (int)Math.floor(nextX - gapSize);
		}
		
		if(animate)
			animateThisFrame = true;
	}
	
	private float layoutHorizontalLine(ContentLine line, float x, float y, float width, float height) {
		float insertX = x;
		float tallest = 0;
		for(Corpus c : line) {
			if(animateThisFrame) {
				if(Math.abs(insertX - c.getDestX()) > 1)
					c.setDestX(insertX, animateSpeed, true);
			} else
				c.setX(insertX);
			c.setY(y);
			insertX += c.getWidth() + gapSize;
			if(c.getHeight() > tallest)
				tallest = c.getHeight();
		}
		
		float totalWidth = insertX - x - gapSize;
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
		
		if(totalWidth > this.gridWidth)
			this.gridWidth = (int)Math.floor(totalWidth);
		
		return y + tallest + gapSize;
	}
	
	private float layoutVerticalLine(ContentLine line, float x, float y, float width, float height) {
		float insertY = y;
		float widest = 0;
		for(Corpus c : line) {
			c.setX(x);
			if(animateThisFrame) {
				if(Math.abs(insertY - c.getDestY()) > 0)
					c.setDestY(insertY, animateSpeed, true);
			} else
				c.setY(insertY);
			insertY += c.getHeight() + gapSize;
			if(c.getWidth() > widest)
				widest = c.getWidth();
		}
		
		float totalHeight = insertY - y - gapSize;
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
		
		if(totalHeight > this.gridHeight)
			this.gridHeight = (int)Math.floor(totalHeight);
		
		return x + widest + gapSize;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public int getGridHeight() {
		return gridHeight;
	}
	
	public int getGapSize() {
		return gapSize;
	}
	
	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
	
	public boolean isAnimate() {
		return animate;
	}
	
	public void setAnimateSpeed(float speed) {
		this.animateSpeed = speed;
	}
	
	public float getAnimateSpeed() {
		return animateSpeed;
	}
	
	public void skipAnimateNextFrame() {
		animateThisFrame = false;
	}
	
	public boolean isContentsSet() {
		return !contentsNotSet;
	}
}