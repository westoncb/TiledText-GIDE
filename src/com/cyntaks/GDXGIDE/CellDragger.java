package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.Stack;

import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.Selector.CyclingController;
import com.cyntaks.sgf.event.EventListener;

public class CellDragger implements EventListener{
	
	private Stack<DragBox> dragStack = new Stack<DragBox>(); 
	private ArrayList<Spacer> spacers = new ArrayList<Spacer>();
	private Cursor cursor;
	private int orientation;
	private Cell insertCell;
	private final float SPACER_SPEED = 20f;
	private ArrayList<Spacer> toRemove = new ArrayList<Spacer>();
	private static boolean debug = false;
	private boolean dragging = false;
	private Grid owner;
	
	public CellDragger(Grid owner) {
		this.owner = owner;
	}
	
	public void place(Cursor activeCursor, int gridOrientation, GNode node, int index, Cell cell) {
		cursor = activeCursor;
		orientation = gridOrientation;
		insertCell = cell;
		
		if(!dragStack.isEmpty()) {
			boolean createNewSpacer = true;
			for(Spacer spacer : spacers) {
				if(spacer.cell == cell) {
					createNewSpacer = false;
					break;
				}
			}
			
			if(createNewSpacer && !owner.getScroller().isScrolling()) {
				Spacer spacer = new Spacer();
				spacer.setDebug(debug);
				spacer.cell = cell;
				spacer.id = Spacer.idCount++;
				spacers.add(spacer);
			}
		}
	}
	
	public void draw() {
		if(cursor != null) {
			for(DragBox box : dragStack) {
				int xMod = 0;
				int yMod = 0;

				if(((CyclingController)owner.getSelector().getController()).isAtFirstSlot()) {
					if(orientation == GridDisplay.HORIZONTAL)
						xMod -= box.getWidth() + cursor.getWidth();
					else
						yMod -= box.getHeight() + cursor.getHeight();
				}
				
				if(orientation == GridDisplay.VERTICAL) {
					//xMod -= cursor.getX();
				}
				
				box.draw(orientation, cursor.getX() + (orientation == GridDisplay.HORIZONTAL ? cursor.getWidth() : 0) + xMod,
						 cursor.getY() + (orientation == GridDisplay.VERTICAL ? cursor.getHeight() : 0) + yMod);
			}
			
			for(Spacer spacer : spacers) {
				float xAdd = orientation == GridDisplay.VERTICAL ? 0 : spacer.cell.getWidth();
				float yAdd = orientation == GridDisplay.HORIZONTAL ? 0 : spacer.cell.getHeight();
				spacer.draw(spacer.cell.getAbsX() + xAdd, spacer.cell.getAbsY() + yAdd);
			}
		}
	}
	
	private void setExpandingSpacerSizes() {
		float width = 0;
		float height = 0;
		
		for(DragBox box : dragStack) {
			if(orientation == GridDisplay.VERTICAL) {
				width = width > insertCell.getWidth() ? insertCell.getWidth() : width;
				height += box.getHeight();
			}
			else {
				height = height > insertCell.getHeight() ? insertCell.getHeight() : height;
				width += box.getWidth();
			}
		}
		
		if(orientation == GridDisplay.VERTICAL)
			height += cursor.getHeight();
		else
			width += cursor.getWidth();
		
		for(Spacer spacer : spacers) {
			if(spacer.cell == insertCell) {
				if(Math.abs(spacer.getWidth() - width) > 1) {
					if(orientation == GridDisplay.HORIZONTAL) {
						spacer.setDestWidth(width, SPACER_SPEED, true);
					}
				} else
					spacer.setWidth(width);
				if(Math.abs(spacer.getHeight() - height) > 1) {
					if(orientation == GridDisplay.VERTICAL) {
						spacer.setDestHeight(height, SPACER_SPEED, true);
					}
				}
				else
					spacer.setHeight(height);
			}
		}
	}
	
	private void setShrinkingSpacerSizes() {
		float totalHeight = 0;
		float totalWidth = 0;
		Spacer insertSpacer = null;
		
		for(Spacer spacer : spacers) {
			if(spacer.cell != insertCell) {
				totalHeight += spacer.getHeight();
				totalWidth += spacer.getWidth();
			} else
				insertSpacer = spacer;
		}
		
		if(insertSpacer != null) {
			for(Spacer spacer : spacers) {
				if(spacer.cell != insertCell) {
					spacer.killResizeEvents();
					
					if(orientation == GridDisplay.HORIZONTAL) {
						float newWidth = ((insertSpacer.getDestWidth() - insertSpacer.getWidth())*(spacer.getWidth()/totalWidth));
						if(newWidth < 0)
							newWidth = 0;
						if(newWidth < spacer.getWidth())
							spacer.setWidth(newWidth);
						else if(spacer.getWidth() < 1)
							toRemove.add(spacer);
					} else {
						//the subtle shaking effect could be due to the fact that the proportions of free space allotted to each spacer may be
						//changing every frame, since spacer.getHeight()/totalHeight changes every frame; could cache it.
						//(This has been remedied by having anything changing position in a GridDisplay animate; but if that doesn't work out,
						//this issue could return.)
						float newHeight = ((insertSpacer.getDestHeight() - insertSpacer.getHeight())*(spacer.getHeight()/totalHeight));
						if(newHeight < 0)
							newHeight = 0;
						if(newHeight < spacer.getHeight())
							spacer.setHeight(newHeight);
						else if(spacer.getHeight() < 1)
							toRemove.add(spacer);
					}
				}
			}
		} else {
			spacers.clear();
		}

		//have dropped the dragged load and insertSpacer is the only spacer left in the collection
		if(spacers.size() == 1 && insertSpacer != null && !isDragging()) {
			insertSpacer.setDestWidth(0, SPACER_SPEED, true);
			insertSpacer.setDestHeight(0, SPACER_SPEED, true);
		}
		
		for(Spacer spacer : toRemove)
			spacers.remove(spacer);
		
		toRemove.clear();
	}
	
	public void update(float delta) {
		if(cursor != null && insertCell != null) {
			setExpandingSpacerSizes();
		}
		
		setShrinkingSpacerSizes();
		
		for(Spacer spacer : spacers) {
			if(spacer.cell == insertCell && ((CyclingController)owner.getSelector().getController()).isAtFirstSlot())
				spacer.cell.setBeforeBlock(spacer);
			else
				spacer.cell.setAfterBlock(spacer);
		}
	}
	
	public void addCells(ArrayList<Cell> cells) {
		this.setDragging(true);
		dragStack.push(new DragBox(cells));
	}
	
	public DragBox getTop() {
		return dragStack.get(dragStack.size()-1);
	}
	
	public DragBox dropTop() {
		DragBox box = dragStack.pop();
		//box.reverseCellOrder();
		if(dragStack.isEmpty())
			this.setDragging(false);
		return box;
	}
	
	static class Spacer extends Corpus {
		private Cell cell;
		private int id;
		private static int idCount;
	}

	@Override
	public void update(int id, int type, float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish(int id, int type) {
		for(Spacer spacer : spacers) {
			if(spacer.id == id && spacer.cell != insertCell)
				toRemove.add(spacer);
		}
		
		for(Spacer spacer : toRemove) {
			if(spacer.id == id)
				spacers.remove(spacer);
		}
		
		toRemove.clear();
	}
	
	private void setDragging(boolean isDragging) {
		dragging = isDragging;
	}
	
	public boolean isDragging() {
		return dragging;
	}
}