package com.cyntaks.GDXGIDE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.cyntaks.GDXGIDE.Selector.CyclingController;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.util.TextureRegion;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

/**
 * It should be noted that although it seems like a Cursor would belong to whatever Grid it is hovering within, it doesn't ever actually belong to an embedded Grid;
 * instead the Grid which the parent Cell belongs to has the Cursor.
 * @author weston
 *
 */
public class Cursor extends Corpus {
	private static Drawable graphic;
	private static final int SIZE = 6; //also the draw operation is commented out
	private static final float MOVE_SPEED = 25f;

	private static final boolean debug = false;
	
	private int insertIndex;
	private GNode insertNode;
	private Cell insertCell;
	
	private Grid owner;
	
	public Cursor(Grid owner) {
		this.owner = owner;
	}
	
	public void load() {
		if(graphic == null) {
			graphic = new VariableSizeBox("images/newtest.png");
			graphic.load();
			super.setDrawable(graphic);
		}
	}
	
	public void init() {
		super.init();
	}
		
	public void placeAfter(Cell cell, boolean animate) {
		if(cell != null && cell.getOwner() != null && !cell.isDisabled() && !cell.isDragging()) {
			int orientation = cell.getOrientation();
			float x = 0;
			float y = 0;
			float width = 0;
			float height = 0;
			if(orientation == GridDisplay.VERTICAL) {
				x = cell.getAbsX();
				y = cell.getAbsY() + cell.getHeight();
				if(((CyclingController)owner.getSelector().getController()).isAtFirstSlot())
					y -= cell.getHeight();
				width = cell.getWidth();
				height = SIZE;
			} else {
				x = cell.getAbsX() + cell.getWidth();
				if(((CyclingController)owner.getSelector().getController()).isAtFirstSlot())
					x -= cell.getWidth();
				y = cell.getAbsY();
				width = SIZE;
				height = cell.getHeight();
			}
			
			if(animate) {
				setDestX(x, MOVE_SPEED, true);
				setDestY(y, MOVE_SPEED, true);
				setDestWidth(width, MOVE_SPEED, true);
				setDestHeight(height, MOVE_SPEED, true);
			} else {
				setX(x);
				setY(y);
				setWidth(width);
				setHeight(height);
			}
			
			super.setColor(1, 1, 1, 1f);
			insertNode = cell.getNode().getParent();
			insertIndex = cell.getNode().getChildIndex() + 1;
			insertCell = cell;
			
			owner.getScroller().trackCell(cell);
			
			if(owner.getDragger().isDragging())
				owner.getDragger().place(this, insertCell.getOrientation(), insertNode, insertIndex, insertCell);
		}
	}
	
	public void update(float delta) {
		super.update(delta);
		
		if(insertCell != null && insertCell.getOwner() != null) {
			super.killMotionEvents();
			placeAfter(insertCell, true);
		}
	}
	
	public void draw() {
	if(!owner.getDragger().isDragging() || true) {
		super.draw();
		if(insertCell != null && debug) {
			GLString string = new GLString("x: " + this.getX(), insertCell.getConfig().getTypeFace(), insertCell.getConfig().getFontStyle(), 20, new Color(0, 1, 0, 1), " ");
			string.draw(getX(), getY());
			GLString string2 = new GLString("y: " + this.getY(), insertCell.getConfig().getTypeFace(), insertCell.getConfig().getFontStyle(), 20, new Color(0, 1, 0, 1), " ");
			string2.draw(getX(), getY() + string.getTextHeight());
		}
	}
	}
	
	public void abstractionChanging() {
		//insertCell = null;
	}
	
	public boolean atCell(Cell cell) {
		return cell == insertCell;
	}
	
	public void vanish() {
		insertCell = null;
		super.setColor(0, 0, 0, 0);
	}

	public int getInsertIndex() {
		return insertIndex;
	}

	public GNode getInsertNode() {
		return insertNode;
	}

	public Grid getOwner() {
		return owner;
	}
	
	public Cell getInsertCell() {
		return insertCell;
	}
}