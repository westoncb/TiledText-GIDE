package com.cyntaks.GDXGIDE.transitions;


import java.util.ArrayList;

import com.cyntaks.GDXGIDE.AbstractWindowTransition;
import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.Window;
import com.cyntaks.sgf.event.Event;
import com.cyntaks.sgf.event.EventListener;

public class FadeFromBlackTransition extends AbstractWindowTransition implements EventListener{

	private static final int A_ID = 1;
	
	public FadeFromBlackTransition() {
		super();
		super.setIsInTransition(true);
	}

	@Override
	public void customBegin() {
		Window owner = super.getOwner();
		owner.setAlpha(0);
		
		if(owner.getGrid() != null) {
			ArrayList<Cell> cells = owner.getGrid().getCells();
			for (Cell cell : cells) {
				//cell.setColor(0, 0, 0, cell.getAlpha());
			}
		}
		
		//super.getOwner().activate();
		
		if(owner.getGrid() != null)
			super.getOwner().getGrid().getScroller().moveToBeginning();
		
		//new Event(A_ID, A_ID, 0, 1, 3f, 0.01f, false, this);
		super.getOwner().setDestAlpha(0.85f, 2.5f, false);
	}

	public void update(int id, int type, float value) {
		if(super.getOwner().getGrid() != null) {
			ArrayList<Cell> cells = super.getOwner().getGrid().getCells();
			for (Cell cell : cells) {
				//cell.setColor(cell.getRed(), cell.getGreen(), cell.getBlue(), value);
			}
		}
	}

	public void finish(int id, int type) {
		if(super.getOwner().getGrid() != null) {
			ArrayList<Cell> cells = super.getOwner().getGrid().getCells();
			for (Cell cell : cells) {
				//cell.setDrawText(true);
			}
		}
		super.finish();
	}
}
