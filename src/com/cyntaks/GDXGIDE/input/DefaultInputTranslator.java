package com.cyntaks.GDXGIDE.input;

import com.cyntaks.GDXGIDE.InputAbstractor;

/**
 * No translation necessary by default -- this class just relays all of the events directly.
 * @author weston
 *
 */

public class DefaultInputTranslator extends AbstractInputTranslator {
	

	public DefaultInputTranslator() {
		
	}
	
	public void pointerMoved(DefaultInputHandler handler, int id, int x, int y) {
		handler.pointerMoved(id, x, y);
	}
	
	public void pointerDidNotMove(DefaultInputHandler handler, int id, int x, int y) {
		//handler.pointerMoved(id, x, y);
	}
	
	public void joystickOffCenter(DefaultInputHandler handler, int id, float x, float y, float z) {
		
		handler.joystickOffCenter(id, x, y);
		
	}
	
	public void symbolActivated(DefaultInputHandler handler, String symbol) {
		if (symbol.equalsIgnoreCase(InputAbstractor.INSERT_SYMBOL)) {
			handler.insertPressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_SYMBOL)) {
			handler.selectPressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.DELETE_SYMBOL)) {
			handler.deletePressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.ACTIVATE_SYMBOL)) {
			handler.activatePressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_NEXT_SYMBOL)) {
			handler.selectNextPressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_PREVIOUS_SYMBOL)) {
			handler.selectPreviousPressed();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_UP_SYMBOL))
			handler.selectUpPressed();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_DOWN_SYMBOL))
			handler.selectDownPressed();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_LEFT_SYMBOL))
			handler.selectLeftPressed();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_RIGHT_SYMBOL))
			handler.selectRightPressed();
		else
			handler.symbolActivated(symbol);
	}
	
	public void symbolDeactivated(DefaultInputHandler handler, String symbol) {
		if (symbol.equalsIgnoreCase(InputAbstractor.INSERT_SYMBOL)) {
			handler.insertReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_SYMBOL)) {
			handler.selectReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.DELETE_SYMBOL)) {
			handler.deleteReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.ACTIVATE_SYMBOL)) {
			handler.activateReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_NEXT_SYMBOL)) {
			handler.selectNextReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_PREVIOUS_SYMBOL)) {
			handler.selectPreviousReleased();
		} else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_UP_SYMBOL))
			handler.selectUpReleased();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_DOWN_SYMBOL))
			handler.selectDownReleased();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_LEFT_SYMBOL))
			handler.selectLeftReleased();
		else if(symbol.equalsIgnoreCase(InputAbstractor.SELECT_RIGHT_SYMBOL))
			handler.selectRightReleased();
		else
			handler.symbolDeactivated(symbol);
	}
}