package com.cyntaks.GDXGIDE.input;

import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.GridController;
import com.cyntaks.GDXGIDE.InputAbstractor;
import com.cyntaks.GDXGIDE.Selector;
import com.cyntaks.GDXGIDE.Selector.CyclingController;

public class DefaultInputHandler {
	private Selector selector;
	private GridController controller;
	private int primaryPointerID = 0;
	
	public DefaultInputHandler(GridController controller) {
		this.controller = controller;
		
		selector = new Selector(controller.getGrid());
	}
	
	public void pointerMoved(int id, float x, float y) {
		if(id == primaryPointerID)
			selector.getController().pointerMoved(x, y);
	}
	
	public void joystickOffCenter(int id, float x, float y) {
		((CyclingController)selector.getController()).setVelocitiesFromNormalizedValues(x, y);
	}
	
	public void selectPressed() {
		selector.getController().selectButtonPressed();
	}
	
	public void selectReleased() {
		selector.getController().selectButtonReleased();
	}
	
	public void activatePressed() {
		
	}
	
	public void activateReleased() {
		if(selector.getLastSelected() != null)
			controller.getListener().auxCellActivated(selector.getLastSelected());
	}
	
	public void insertPressed() {
		controller.insertDraggedCells();
	}
	
	public void deletePressed() {
		
	}
	
	public void deleteReleased() {
		controller.explicitDelete();
	}
	
	public void insertReleased() {
		
	}
	
	public void selectNextPressed() {
		
	}
	
	public void selectNextReleased() {
		
	}
	
	public void selectPreviousPressed() {

	}
	
	public void selectPreviousReleased() {
		
	}
	
	public void selectUpPressed() {
		((CyclingController)selector.getController()).selectAbove();
	}
	
	public void selectDownPressed() {
		((CyclingController)selector.getController()).selectBelow();
	}
	
	public void selectLeftPressed() {
		((CyclingController)selector.getController()).selectLeft();
	}
	
	public void selectRightPressed() {
		((CyclingController)selector.getController()).selectRight();
	}
	
	public void selectUpReleased() {
	}
	
	public void selectDownReleased() {
	}
	
	public void selectLeftReleased() {
	}
	
	public void selectRightReleased() {
	}
	
	public void symbolActivated(String symbol) {
		if(symbol.equalsIgnoreCase(InputAbstractor.EXIT_SYMBOL))
			GIDEApp.shutDown();
		 else if(symbol.equalsIgnoreCase(InputAbstractor.GENERIC_SYMBOL_2)) {
			System.out.println("undo");
			getGridController().undo();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.GENERIC_SYMBOL_3)) {
			System.out.println("redo");
			getGridController().redo();
		}
	}
	
	public void symbolDeactivated(String symbol) {
		
	}
	
	public Selector getSelector() {
		return selector;
	}
	
	public void setSelector(Selector selector) {
		this.selector = selector;
	}
	
	public GridController getGridController() {
		return controller;
	}
	
	public int getPrimaryPointerID() {
		return primaryPointerID;
	}

	public void setPrimaryPointerID(int primaryPointerID) {
		this.primaryPointerID = primaryPointerID;
	}
}
