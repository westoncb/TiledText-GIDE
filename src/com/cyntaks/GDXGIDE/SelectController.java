package com.cyntaks.GDXGIDE;

public interface SelectController {
	public void pointerMoved(float x, float y);
	public boolean selectButtonPressed();
	public boolean selectButtonReleased();
	public void update(float delta);
	public boolean endMultiSelect();
	public void clearSelection();
}
