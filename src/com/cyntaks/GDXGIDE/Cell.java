package com.cyntaks.GDXGIDE;

import com.badlogic.gdx.Gdx;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.CellDisplay;
import com.cyntaks.GDXGIDE.gui.Component;
import com.cyntaks.GDXGIDE.gui.ContentLine;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.util.Util;
import com.cyntaks.sgf.event.EventListener;

public class Cell extends Corpus implements Comparable<Cell> {
	private Grid owner;
	private GNode node;
	private int row;
	private int col;
	public int eventId;
	
	private CellDisplay display;
	private CellBehavior behavior;
	private Corpus underGraphic;
	private Corpus overGraphic;
	
	private boolean selected;
	private boolean selectionDisabled;
	private boolean dragging;
	private boolean disabled;

	private CellConfig config;
	private Component component;
	
	private boolean readyForSelectionAction;
	
	public Cell(Grid owner, GNode node) {
		this.owner = owner;
		this.node = node;
		display = new CellDisplay();
		readyForSelectionAction = true;
	}
	
	public Cell() {

	}
	
	public void load() {
		if(underGraphic == null)
			setUnderGraphic(config.getUnderGraphicInstance());
		if(underGraphic != null)
			underGraphic.load();
		
		if(overGraphic == null)
			setOverGraphic(config.getOverGraphicInstance());
		if(overGraphic != null)
			overGraphic.load();
		
		display.load();
	}
	
	public void init() {
		super.init();
		if(underGraphic != null)
			underGraphic.init();
		if(overGraphic != null)
			overGraphic.init();
		
		display.init();
	}
	
	public void update(float delta) {
		display.update(delta, getAbsX(), getAbsY(), super.getWidth(), super.getHeight());
	}
	
	public void dispose() {
		if(!isDragging()) {
			//owner = null; //causes problems if we need parent info. after a cell has been lifted from a grid, e.g. -- and I don't think it's necessary
			setDisabled(true);
		}
	}
	
	public int getOrientation() {
		return getOwner().getDisplay().getOrientation(); 
	}

	public boolean pointInCellBounds(int cursorX, int cursorY, boolean withBorders) {
		assert (cursorX > -1 && cursorX < Gdx.graphics.getWidth() + 2 && cursorY > -1 && cursorY < Gdx.graphics.getHeight() + 2) : ("cursorX: " + cursorX + ", cursorY: " + cursorY);
		
		float transCursorX = Util.getWorldX(cursorX);
		float transCursorY = Util.getWorldY(cursorY);
		
		float x = getAbsX();
		float y = getAbsY();
		float width = super.getWidth();
		float height = super.getHeight();
		
		if (withBorders && owner != null) {
			x -= owner.getDisplay().getGapSize();
			y -= owner.getDisplay().getGapSize();
			width += owner.getDisplay().getGapSize() * 2;
			height += owner.getDisplay().getGapSize() * 2;
		} else if(withBorders && owner == null) {
			System.err.println("Doesn't make sense to use \"withBorders\"=true if this cell does not belong to a grid.");
		}

		if (transCursorX > x && transCursorX < x + width && transCursorY > y
				&& transCursorY < y + height)
			return true;
		else
			return false;
	}
	
	public void drawBottomLayer() {
		if(!disabled)
			display.draw(CellDisplay.LAYER_BOTTOM, super.getRed(), super.getGreen(), super.getBlue(), super.getAlpha());
	}
	
	public void drawMiddleLayer() {
		if(!disabled)
			display.draw(CellDisplay.LAYER_MIDDLE, super.getRed(), super.getGreen(), super.getBlue(), super.getAlpha());
	}
	
	public void drawTopLayer() {
		if(!disabled)
			display.draw(CellDisplay.LAYER_TOP, super.getRed(), super.getGreen(), super.getBlue(), super.getAlpha());
	}
	
	protected void select() {
		if(!isSelectionDisabled() && !isDisabled() && !isDragging()) {
			if(behavior != null && !this.isSelected()) {
				behavior.cellSelected();
				behavior.placeCursor();
			}
			
			this.selected = true;
			this.readyForSelectionAction = false; //gets reset inside of Grid
		}
	}
	
	protected void deselect() {
		this.selected = false;
		if(behavior != null)
			behavior.cellDeselected();
		this.readyForSelectionAction = false;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelectionDisabled(boolean disabled) {
		this.selectionDisabled = disabled;
	}
	
	public boolean isSelectionDisabled() {
		return selectionDisabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if(disabled) {
			super.setX(Integer.MAX_VALUE);
			super.setY(Integer.MAX_VALUE);
			//setColor(0, 0, 0, 0);
		}
	}
	
	public void setColor(float r, float g, float b, float a) {
		super.setColor(r, g, b, a);
		
		ContentLine[][] contents = getDisplay().getContents();
		for(int i = 0; i < contents.length; i++) {
			for(int j = 0; j < contents[i].length; j++) {
				for(int k = 0; k < contents[i][j].size(); k++) {
					contents[i][j].get(k).setColor(r, g, b, a);
				}
			}
		}
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public Grid getOwner() {
		return owner;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setIndex(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void setX(float x) {
		if(!disabled)
			super.setX(x);
	}
	
	public void setY(float y) {
		if(!disabled)
			super.setY(y);
	}

	public boolean isDragging() {
		return dragging;
	}
	
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
		//if (!dragging)
			//deselect();
	}
	
	public void resize(float width, float height) {
		if(width != this.getWidth())
			setWidth(width);
		if(height != this.getHeight())
			setHeight(height);
	}
	
	public float getAbsX() {
		return getX() + (owner != null ? owner.getAbsX() : 0);
	}
	
	public float getAbsY() {
		return getY() + (owner != null ? owner.getAbsY() : 0);
	}
	
	public CellDisplay getDisplay() {
		return display;
	}

	public Corpus getUnderGraphic() {
		return underGraphic;
	}

	public void setUnderGraphic(Corpus underGraphic) {
		if(underGraphic != null) {
			this.underGraphic = underGraphic;
			underGraphic.setWidth(super.getWidth());
			underGraphic.setHeight(super.getHeight());
			ContentLine line = getDisplay().getContentLine(CellDisplay.LAYER_BOTTOM, CellDisplay.LINE_Y_CENTER);
			line.clear();
			line.add(underGraphic);
		}
	}
	
	public Corpus getOverGraphic() {
		return overGraphic;
	}

	public void setOverGraphic(Corpus overGraphic) {
		if(overGraphic != null) {
			this.overGraphic = overGraphic;
			overGraphic.setWidth(super.getWidth());
			overGraphic.setHeight(super.getHeight());
			ContentLine line = getDisplay().getContentLine(CellDisplay.LAYER_TOP, CellDisplay.LINE_LEFT);
			line.clear();
			line.add(overGraphic);
		}
	}
	
	public void setWidth(float width) {
		if(component != null) {
			if(width < component.getMinWidth())
				width = component.getMinWidth();
			else if(width > component.getMaxWidth())
				width = component.getMaxWidth();
			
			component.cellWidthChanged(width);
		}
		super.setWidth(width);
		if(underGraphic != null)
			underGraphic.setWidth(width);
		if(overGraphic != null)
			overGraphic.setWidth(width);
	}
	
	public void setHeight(float height) {
		if(component != null) {
			if(height < component.getMinHeight())
				height = component.getMinHeight();
			else if(height > component.getMaxHeight())
				height = component.getMaxHeight();
			component.cellHeightChanged(height);
		}
		super.setHeight(height);
		if(underGraphic != null)
			underGraphic.setHeight(height);
		if(overGraphic != null)
			overGraphic.setHeight(height);
	}
	
	public CellBehavior getBehavior() {
		return behavior;
	}
	
	public void changeBehavior(CellBehavior behavior) {
		this.behavior = behavior;
		behavior.setOwner(this);
	}

	public CellConfig getConfig() {
		return config;
	}

	public void setConfig(CellConfig config) {
		this.config = config;
	}

	public GNode getNode() {
		return node;
	}
	
	public void setEventId(int id) {
		this.eventId = id;
	}
	
	public void prepareForDrag() {
		setDragging(true);
		deselect();
	}
	
	public float getTopWindowWidth() {
		return getOwner().getWindow().getWidth();
	}
	
	public float getTopWindowHeight() {
		return getOwner().getWindow().getHeight();
	}
	
	public void shrinkAlongMainAxis(EventListener listener, int id) {
		float speed = 12;
		float destHeight = dragging ? getHeight() : 0;
		float destWidth = dragging ? getWidth() : 0;
		if(getOrientation() == GridDisplay.VERTICAL)
			setDestHeight(destHeight, speed*this.getHeight(), false, id, listener);
		else
			setDestWidth(destWidth, speed*this.getWidth(), false, id, listener);
		
	}
	
	public int getEventId() {
		return eventId;
	}

	public boolean isReadyForSelectionAction() {
		return readyForSelectionAction;
	}
	
	public void setReadyForSelectionAction(boolean ready) {
		this.readyForSelectionAction = ready;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	@Override
	public int compareTo(Cell o) {
		return owner.getCells().indexOf(this) - owner.getCells().indexOf(o);
	}
}