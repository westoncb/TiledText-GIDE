package com.cyntaks.GDXGIDE.input;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.GIDEApp;

/**
 * This class allows the possibility of converting fundamentally distinct input schemes into the same
 * format, i.e. resulting in the same set of events which are handled by the rest of the program in just
 * a single manner. The situation that motivated this was considering that I'll need to support multi-touch
 * input (touch screens) as well as video game controllers. Cycling through cells on a game controller is
 * direct: the cycling velocity is just a function of how much the main joystick is off-center; but with touch
 * screens there is a level of indirection: velocity of finger-motion should only translate to cycling velocity
 * if the finger is in a certain region of the screen. Additionally, there are no actual buttons, but a subclass
 * of this class would allow a scheme to be implemented where button-presses are emulated, completely disguising
 * this fact from the rest of the code.
 * 
 * @author weston
 *
 */

public abstract class AbstractInputTranslator {
	private static ArrayList<String> activeSymbols = new ArrayList<String>();
	
	public final void auxSymbolActivated(String symbol) {
		if(!activeSymbols.contains(symbol))
			activeSymbols.add(symbol);
		DefaultInputHandler active = findActiveInputHandler();
		if(active != null)
			symbolActivated(active, symbol);
	}
	
	public abstract void symbolActivated(DefaultInputHandler handler, String symbol);
	
	public final void auxSymbolDeactivated(String symbol) {
		activeSymbols.remove(symbol);
		DefaultInputHandler active = findActiveInputHandler();
		if(active != null)
			symbolDeactivated(active, symbol);
	}
	
	public abstract void symbolDeactivated(DefaultInputHandler handler, String symbol);
	
	public final void auxPointerMoved(int id, int x, int y) {
		DefaultInputHandler active = findActiveInputHandler();
		if(active != null)
			pointerMoved(active, id, x, y);
	}
	
	public abstract void pointerMoved(DefaultInputHandler handler, int id, int x, int y);
	
	public final void auxPointerDidNotMove(int id, int x, int y) {
		DefaultInputHandler active = findActiveInputHandler();
		if(active != null)
			pointerDidNotMove(active, id, x, y);
	}
	
	public abstract void pointerDidNotMove(DefaultInputHandler handler, int id, int x, int y);
	
	public final void auxJoystickOffCenter(int id, float x, float y, float z) {
		DefaultInputHandler active = findActiveInputHandler();
		if(active != null)
			joystickOffCenter(active, id, x, y, z);
	}
	
	public abstract void joystickOffCenter(DefaultInputHandler handler, int id, float x, float y, float z);
	
	public static boolean isSymbolActivated(String symbol) {
		return activeSymbols.contains(symbol);
	}
	
	public static DefaultInputHandler findActiveInputHandler() {
		DefaultInputHandler handler = null;

		if(!GIDEApp.getPrograms().isEmpty() &&
		   GIDEApp.getPrograms().peek().getActiveGrid().isInitalized())
			handler = GIDEApp.getPrograms().peek().getActiveGrid().getInputHandler();
		
		return handler;
	}
}
