package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.code.CodeCell;
import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.util.CorporealBox;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

public class DragBox extends CorporealBox {
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	
	private float destWidth;
	private float destHeight;
	private boolean truncated;
	
	public DragBox(ArrayList<Cell> cells) {
		super(new VariableSizeBox("images/newtest.png"));
		load();
		init();
		super.setAlpha(.25f);
		super.setRed(.3f);
		super.setGreen(1f);
		super.setBlue(.6f);
		this.cells = new ArrayList<Cell>(cells);

		int index = 0;
		for(Cell cell : this.cells) {
			cell.prepareForDrag();

			if(index == 0 && cell instanceof CodeCell)
				((CodeCell)cell).trimWhitespaceFromBeginning();
			else if(index == cells.size()-1 && cell instanceof CodeCell)
				((CodeCell)cell).trimWhitespaceFromEnd();
			index++;
		}
	}
	
	private boolean spanMultipleLines(ArrayList<Cell> cells) {
		boolean multiLine = false;
		for (Cell oCell : cells) {
			CodeCell cell = (CodeCell)oCell;
			ArrayList<CodeNode> codeList = cell.getCodeList();
			for (CodeNode codeNode : codeList) {
				if(codeNode.getType() == GNode.NEWLINE) {
					multiLine = true;
					break;
				}
			}
			
			if(multiLine)
				break;
		}
		
		return multiLine;
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public void setWidth(float width, boolean animate) {
		if(super.getWidth() != width && animate) {
			if(!super.isWidthChanging())
				super.setDestWidth(width, (Math.abs(super.getWidth()-width))*5f, false);
		}
		else
			super.setWidth(width);
	}
	
	public void setHeight(float height, boolean animate) {
		if(super.getHeight() != height && animate) {
			if(!super.isHeightChanging())
				super.setDestHeight(height, (Math.abs(super.getHeight()-height))*5f, false);
		}
		else
			super.setHeight(height);
	}
	
	public void draw(int orientation, float x, float y) {
		float width = 0;
		float height = 0;
		int offset = 0;
		float maxWidth = 0;
		float maxHeight = 0;
		
		truncated = false;
		
		if(orientation == GridDisplay.VERTICAL) {
			maxHeight = Gdx.graphics.getHeight()*.1f;
			maxWidth = Integer.MAX_VALUE;
		} else {
			maxHeight = Gdx.graphics.getHeight()*.1f;
			maxWidth = Gdx.graphics.getWidth()*.33f;
		}
		
		for(Cell cell : cells) {
			if(orientation == GridDisplay.VERTICAL) {
				cell.setX(x - cell.getOwner().getAbsX());
				cell.setY(y + offset - cell.getOwner().getAbsY());
				offset += cell.getHeight();
				
				width = cell.getWidth() > width ? cell.getWidth() : width;
				height += cell.getHeight();
			} else {
				cell.setX(x + offset - cell.getOwner().getAbsX());
				cell.setY(y - cell.getOwner().getAbsY());
				offset += cell.getWidth();
				
				height = cell.getHeight() > height ? cell.getHeight() : height;
				width += cell.getWidth();
			}
			
			if(width > maxWidth) {
				width = maxWidth;
				truncated = true;
			}
			if(height > maxHeight) {
				height = maxHeight;
				truncated = true;
			}

			cell.update(0);
		}
		
		if(width != destWidth) {
			destWidth = width;
			setWidth(destWidth, false);
		}
		
		if(height != destHeight) {
			destHeight = height;
			setHeight(destHeight, false);
		}
		
		if(true) {
			//GIDEApp.SPRITE_BATCH.flush();
			//Gdx.gl10.glScissor((int)x, Gdx.graphics.getHeight() - (int)(y + height), (int)width,
				//	(int)height);
			//Gdx.gl10.glEnable(GL10.GL_SCISSOR_TEST);
		}
	
		if(!truncated) {
			for(Cell cell : cells) {
				cell.drawBottomLayer();
				cell.drawMiddleLayer();
				cell.drawTopLayer();
			}
		}
		
		super.draw(x, y);
		
		if(truncated) {
			
			Cell cell = cells.get(0);
			CellConfig config = cell.getConfig();
			String text = "" + cells.size() + " cell" + (cells.size() > 1 ? "s" : "");
			GLString glString = new GLString(text, config.getTypeFace(),
					config.getFontStyle(), config.getFontSize(),
					new Color(0, 1, 0, 1), false);
			
			int textX = (int)(x + width/2 - glString.getTextWidth()/2);
			int textY = (int)(y + height/2 - glString.getTextHeight()/2);
			
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(.3f, .3f, .3f, 1f);
			debugBox.draw(textX, textY, glString.getTextWidth(), glString.getTextHeight());
			GIDEApp.SPRITE_BATCH.setColor(old);
			glString.draw(textX, textY);
		}
		
		if (true) {
			//GIDEApp.SPRITE_BATCH.flush();
			//Gdx.gl10.glDisable(GL10.GL_SCISSOR_TEST);
		}
	}
}