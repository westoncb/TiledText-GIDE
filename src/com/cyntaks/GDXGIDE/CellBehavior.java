package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

public abstract class CellBehavior {
	private Cell owner;
	private ArrayList<Integer> pointersInCell;
	
	public CellBehavior() {
		pointersInCell = new ArrayList<Integer>();
	}
	
	public void rootPointerEntered(int pointerID) {
		if(!pointersInCell.contains(pointerID))
			pointersInCell.add(pointerID);
		
		pointerEntered(pointerID);
	}
	public void rootPointerExited(int pointerID) {
		pointersInCell.remove(pointerID);
		
		pointerExited(pointerID);
	}
	
	public boolean cellContainsPointer(int id) {
		return pointersInCell.contains(id);
	}
	
	public Cell getOwner() {
		return owner;
	}

	public void setOwner(Cell owner) {
		this.owner = owner;
	}
	
	public abstract void update(float delta);
	public abstract void pointerMoved(int pointerID, int x, int y);
	public abstract void pointerEntered(int pointerID);
	public abstract void pointerExited(int pointerID);
	public abstract void cellSelected();
	public abstract void cellDeselected();
	public abstract void selectionFinalized();
	public abstract void placeCursor();
}
