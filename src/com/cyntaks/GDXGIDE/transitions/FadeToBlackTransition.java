package com.cyntaks.GDXGIDE.transitions;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.AbstractWindowTransition;
import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.Window;
import com.cyntaks.sgf.event.Event;
import com.cyntaks.sgf.event.EventListener;

public class FadeToBlackTransition extends AbstractWindowTransition implements EventListener{

	private static final int A_ID = 0;
	
	public FadeToBlackTransition() {
		super();
		super.setIsInTransition(false);
	}

	@Override
	public void customBegin() {
		Window owner = super.getOwner();
		
		new Event(A_ID, A_ID, 1, 0, 2.2f, .1f, false, this);
		super.getOwner().setDestAlpha(0, 2.5f, false);
		
		if(owner.getGrid() != null) {
			ArrayList<Cell> cells = owner.getGrid().getCells();
			for (Cell cell : cells) {
				//cells.get(i).get(j).setDrawText(false);
			}
		}
	}
	
	public void update(int id, int type, float value) {
		if(super.getOwner().getGrid() != null) {
			ArrayList<Cell> cells = super.getOwner().getGrid().getCells();
			for (Cell cell : cells) {
				cell.setColor(cell.getRed(), cell.getGreen(), cell.getBlue(), value);
			}
		}
	}
	
	public void finish(int id, int type) {
			super.finish();
	}
}
