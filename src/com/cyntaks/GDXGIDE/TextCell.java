package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.CellDisplay;
import com.cyntaks.GDXGIDE.gui.ContentLine;
import com.cyntaks.GDXGIDE.text.GLString;

public class TextCell extends Cell{
	private GLString text;
	private int hAlign;
	private int vAlign;
	private boolean adjustToContents;
	
	public TextCell(Grid owner, GNode node) {
		this(owner, node, GLString.CENTER, GLString.CENTER);
	}
	
	public TextCell(Grid owner, GNode node, int hAlign, int vAlign) {
		super(owner, node);
		this.hAlign = hAlign;
		this.vAlign = vAlign;
		setText(node.getText());
	}
	
	public void setText(String newText) {
		ContentLine line = super.getDisplay().getContentLine(CellDisplay.LAYER_TOP, CellDisplay.LINE_X_CENTER);

		clearText();
		
		line.setAttachLocation(ContentLine.ATTACH_MIDDLE);
		CellConfig config = getOwner().getCellConfig();
		text = new GLString(newText, config.getTitleTypeFace(), config.getFontStyle(), config.getFontSize(), config.getFontColor(), false);
		text.setHorizontalAlignment(hAlign);
		text.setVerticalAlignment(vAlign);
		text.setHyphenateSplitAtoms(true);
		text.setAllowTruncation(true);
		text.setLockFontSize(true);
		line.add(text);
	}
	
	public void clearText() {
		ContentLine line = super.getDisplay().getContentLine(CellDisplay.LAYER_TOP, CellDisplay.LINE_X_CENTER);
		if(!line.isEmpty()) {
			ArrayList<Corpus> toRemove = new ArrayList<Corpus>();
			for (int i = 0; i < line.size(); i++) {
				if(line.get(i) instanceof GLString)
					toRemove.add(line.get(i));
			}
			for (Corpus corpus : toRemove) {
				line.remove(corpus);
			}
		}
	}
	
	public void fitToSize() {
		float textWidth = text.getTextWidth();
		float textHeight = text.getTextHeight();
		text.setSpatialConstraints(textWidth, textHeight);

		resize(textWidth, textHeight);
		text.setPosition(getAbsX(), getAbsY());
	}
	
	public void update(float delta) {
		super.update(delta);
		if(adjustToContents)
			fitToSize();
	}
	
	public void load() {
		super.load();
		text.load();
	}
	
	public void init() {
		super.init();
		
		text.enforceSpatialConstraints();
	}
	
	public void setWidth(float width) {
		super.setWidth(width);
	}
	
	public void setHeight(float height) {
		super.setHeight(height);
	}
	
	public void reAlignText(int hAlign, int vAlign) {
		text.setHorizontalAlignment(hAlign);
		text.setVerticalAlignment(vAlign);
		text.layoutText();
	}
	
	public void updateTextBounds() {
		text.setSpatialConstraints(getWidth(), getHeight());
	}
	
	public GLString getText() {
		return text;
	}

	public boolean isAdjustToContents() {
		return adjustToContents;
	}

	public void setAdjustToContents(boolean adjustToContents) {
		this.adjustToContents = adjustToContents;
	}
}
