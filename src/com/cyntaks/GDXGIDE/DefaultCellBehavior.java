package com.cyntaks.GDXGIDE;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.config.CellConfig;


public class DefaultCellBehavior extends CellBehavior {
	
	public DefaultCellBehavior() {
		super();
	}

	public void update(float delta) {
		
	}

	public void pointerEntered(int pointerID) {

	}

	public void pointerExited(int pointerID) {

	}

	public void pointerMoved(int pointerID, int x, int y) {
		
	}
	
	public void placeCursor() {
		Cell selected = getOwner();
		
		if(selected != null && selected.getOwner() != null) {
			selected.getOwner().getCursor().placeAfter(selected, true);
		}
	}

	public void cellSelected() {
		Cell owner = super.getOwner();
		CellConfig config = getOwner().getConfig();
		Color color = config.getSelectedColor();
		if(owner.getUnderGraphic() != null) {
			owner.getUnderGraphic().killColorEvents();
			owner.getUnderGraphic().setDestColor(color.r, color.g, color.b, color.a, config.getSelectSpeed());
		}
	}

	public void cellDeselected() {
		CellConfig config = getOwner().getConfig();
		Color color = config.getUnderGraphicColor();
		if(getOwner().getUnderGraphic() != null) {
			getOwner().getUnderGraphic().killColorEvents();
			getOwner().getUnderGraphic().setDestColor(color.r, color.g, color.b, color.a, config.getDeselectSpeed());
		}
	}
	
	public void selectionFinalized() {
		CellConfig config = getOwner().getConfig();
		Color color = config.getFinalizedSelectionColor();
		if(getOwner().getUnderGraphic() != null) {
			getOwner().getUnderGraphic().killColorEvents();
			getOwner().getUnderGraphic().setDestColor(color.r, color.g, color.b, color.a, config.getDeselectSpeed());
		}
	}
	
	public void setOwner(Cell owner) {
		super.setOwner(owner);
	}
}