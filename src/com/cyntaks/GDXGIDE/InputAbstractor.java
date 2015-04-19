package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.cyntaks.GDXGIDE.input.AbstractInputTranslator;
import com.cyntaks.GDXGIDE.input.GameController;
import com.cyntaks.GDXGIDE.input.GameControllerConfig;
import com.cyntaks.GDXGIDE.util.ConfigFile;

/**
 * InputAbstractor is used for abstracting the sources of input and allowing the program to instead deal with "symbols" which
 * either are activated or not. For instance, rather than asking if the left control button is pressed, the program may ask
 * whether the modifier1 symbol is activated; any key (or mouse button, etc.) might have been mapped to modifier1 so that it
 * becomes activated or not depending on the state of the key(s)/button(s) mapped to it. This mapping is done via an external
 * file, config/cell/input_bindings.txt.
 * 
 * In order to add a new input symbol the input_bindings.txt file must be edited to contain an entry for that symbol.
 * Additionally, a key/button etc. which is to be available for mapping to symbols must be added to the 
 * recognizedKeys/recognizedMouseButtons collection (or a new collection if appropriate). See initialize() below. I am 
 * storing constants for defined symbols in this class.
 * 
 * @author Weston Beecroft
 *
 */

public class InputAbstractor {
	public static final String SELECT_SYMBOL = "select";
	public static final String INSERT_SYMBOL = "insert";
	public static final String ACTIVATE_SYMBOL = "activate";
	public static final String EXIT_SYMBOL = "exit";
	public static final String DELETE_SYMBOL = "delete";
	public static final String SELECT_NEXT_SYMBOL = "select_next";
	public static final String SELECT_PREVIOUS_SYMBOL = "select_previous";
	public static final String SELECT_UP_SYMBOL = "select_up";
	public static final String SELECT_DOWN_SYMBOL = "select_down";
	public static final String SELECT_LEFT_SYMBOL = "select_left";
	public static final String SELECT_RIGHT_SYMBOL = "select_right";
	public static final String GENERIC_SYMBOL_1 = "generic_1";
	public static final String GENERIC_SYMBOL_2 = "generic_2";
	public static final String GENERIC_SYMBOL_3 = "generic_3";
	public static final String GENERIC_SYMBOL_4 = "generic_4";
	public static final String GENERIC_SYMBOL_5 = "generic_5";
	public static final String GENERIC_SYMBOL_6 = "generic_6";
	public static final String GENERIC_SYMBOL_7 = "generic_7";
	public static final String GENERIC_SYMBOL_8 = "generic_8";
	public static final String GENERIC_SYMBOL_9 = "generic_9";
	public static final String GENERIC_SYMBOL_10 = "generic_10";
	
	private static int MAX_POINTERS = 3; //max number of pointers we could possibly be interested in (e.g. number of fingers touching a screen).
	private static ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedControllerButtons = new ArrayList<Integer>();
	
	private static HashMap<Integer, String> recognizedKeys = new HashMap<Integer, String>();
	private static HashMap<Integer, String> recognizedMouseButtons = new HashMap<Integer, String>();
	private static HashMap<Integer, String> recognizedControllerButtons = new HashMap<Integer, String>();
	private static ConfigFile bindings;
	private static AbstractInputTranslator translator;
	
	public static final int PRIMARY_POINTER = 0;
	
	public static GameController controller;
	
	public static void update(float delta) {
		if(controller != null)
			controller.poll();
		
		updateKeyboard();
		updateMouse();
		updatePointers();
		updateController();
	}
	
	public static void initialize(AbstractInputTranslator translator) {
		InputAbstractor.translator = translator;
		
		loadBindings();
		recognizedKeys.put(Input.Keys.ESCAPE, "key_escape");
		recognizedKeys.put(Input.Keys.CONTROL_LEFT, "key_leftControl");
		recognizedKeys.put(Input.Keys.CONTROL_RIGHT, "key_rightControl");
		recognizedKeys.put(Input.Keys.K, "key_k");
		recognizedKeys.put(Input.Keys.I, "key_i");
		recognizedKeys.put(Input.Keys.D, "key_d");
		recognizedKeys.put(Input.Keys.U, "key_u");
		recognizedKeys.put(Input.Keys.R, "key_r");
		recognizedKeys.put(Input.Keys.DEL, "key_delete");
		recognizedKeys.put(Input.Keys.SPACE, "key_space");
		recognizedKeys.put(Input.Keys.SHIFT_LEFT, "key_shift_left");

		
		recognizedMouseButtons.put(Input.Buttons.LEFT, "mouse_leftButton");
		recognizedMouseButtons.put(Input.Buttons.RIGHT, "mouse_rightButton");
		
		recognizedControllerButtons.put(GameController.BUTTON_0, "controller_b0");
		recognizedControllerButtons.put(GameController.BUTTON_1, "controller_b1");
		recognizedControllerButtons.put(GameController.BUTTON_2, "controller_b2");
		recognizedControllerButtons.put(GameController.BUTTON_3, "controller_b3");
		recognizedControllerButtons.put(GameController.BUTTON_4, "controller_b4");
		recognizedControllerButtons.put(GameController.BUTTON_5, "controller_b5");
		recognizedControllerButtons.put(GameController.BUTTON_6, "controller_b6");
		recognizedControllerButtons.put(GameController.BUTTON_7, "controller_b7");
		recognizedControllerButtons.put(GameController.BUTTON_8, "controller_b8");
		recognizedControllerButtons.put(GameController.BUTTON_9, "controller_b9");
		recognizedControllerButtons.put(GameController.X0_LEFT, "controller_x0_left");
		recognizedControllerButtons.put(GameController.X0_RIGHT, "controller_x0_right");
		recognizedControllerButtons.put(GameController.X1_LEFT, "controller_x1_left");
		recognizedControllerButtons.put(GameController.X1_RIGHT, "controller_x1_right");
		recognizedControllerButtons.put(GameController.Y0_UP, "controller_y0_up");
		recognizedControllerButtons.put(GameController.Y0_DOWN, "controller_y0_down");
		recognizedControllerButtons.put(GameController.Y1_UP, "controller_y1_up");
		recognizedControllerButtons.put(GameController.Y1_DOWN, "controller_y1_down");
		recognizedControllerButtons.put(GameController.Z0_BACK, "controller_z0_back");
		recognizedControllerButtons.put(GameController.Z0_FORWARD, "controller_z0_forward");
		recognizedControllerButtons.put(GameController.POV0_LEFT, "controller_pov0_left");
		recognizedControllerButtons.put(GameController.POV0_RIGHT, "controller_pov0_right");
		recognizedControllerButtons.put(GameController.POV0_UP, "controller_pov0_up");
		recognizedControllerButtons.put(GameController.POV0_DOWN, "controller_pov0_down");
		
//		findController();
	}
	
	private static void loadBindings() {
		 bindings = new ConfigFile("config/input_bindings.txt");
		 bindings.load(true, true);
	}
	
	private static void findController() {
		Controller[] allControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for (Controller aController : allControllers) {
			GameControllerConfig config = GameControllerConfig.getConfigForController(aController.getName());
			if(config != null) {
				System.out.println("Found Controller: " + aController);
				controller = new GameController(aController, config);
				return;
			}
		}
	}
	
	private static void updateKeyboard() {
		Iterator<Integer> keys = recognizedKeys.keySet().iterator();
		while(keys.hasNext()) {
			int code = keys.next();
			if(Gdx.input.isKeyPressed(code) && !pressedKeys.contains(code)) {
				pressedKeys.add(code);
				notifyHandlersOfSymbolChange(bindings.get(recognizedKeys.get(code)), true);
			}
		}
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (Integer code : pressedKeys) {
			if(!Gdx.input.isKeyPressed(code)) {
				toRemove.add(code);
				notifyHandlersOfSymbolChange(bindings.get(recognizedKeys.get(code)), false);
			}
		}
		for (Integer code : toRemove) {
			pressedKeys.remove(code);
		}
	}
	
	private static void updateMouse() {
		Iterator<Integer> buttons = recognizedMouseButtons.keySet().iterator();
		while(buttons.hasNext()) {
			int code = buttons.next();
			if(Gdx.input.isButtonPressed(code) && !pressedMouseButtons.contains(code)) {
				pressedMouseButtons.add(code);
				notifyHandlersOfSymbolChange(bindings.get(recognizedMouseButtons.get(code)), true);
			}
		}
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (Integer code : pressedMouseButtons) {
			if(!Gdx.input.isButtonPressed(code)) {
				toRemove.add(code);
				notifyHandlersOfSymbolChange(bindings.get(recognizedMouseButtons.get(code)), false);
			}
		}
		for (Integer code : toRemove) {
			pressedMouseButtons.remove(code);
		}
	}
	
	private static void notifyHandlersOfSymbolChange(String symbol, boolean activated) {
		if(symbol == null) {
			System.err.println("input recognized but did not map to input symbol correctly.");
			return;
		}
		
		if(activated)
			notifyHandlersOfSymbolActivated(symbol);
		else
			notifyHandlersOfSymbolDeactivated(symbol);
	}
	
	private static void notifyHandlersOfSymbolActivated(String symbol) {
		translator.auxSymbolActivated(symbol);
	}
	
	private static void notifyHandlersOfSymbolDeactivated(String symbol) {
		translator.auxSymbolDeactivated(symbol);
	}
	
	private static void updatePointers() {
		for (int i = 0; i < MAX_POINTERS; i++) { 
			if((Gdx.input.getDeltaX(i) != 0 || Gdx.input.getDeltaY(i) != 0)) { //will probably need to include input.isTouched(i) for touch devices
				translator.auxPointerMoved(i, Gdx.input.getX(i), Gdx.input.getY(i));
			} else {
				translator.auxPointerDidNotMove(i, Gdx.input.getX(i), Gdx.input.getY(i));
			}
		}
	}
	
	private static void updateController() {
		if(controller != null) {
			Iterator<Integer> buttons = recognizedControllerButtons.keySet().iterator();
			while(buttons.hasNext()) {
				int nextCode = buttons.next();
				if(controller.isButtonPressed(nextCode) && !pressedControllerButtons.contains(nextCode)) {
					pressedControllerButtons.add(nextCode);
					String symbol = bindings.get(recognizedControllerButtons.get(nextCode));
					if(symbol != null)
						notifyHandlersOfSymbolChange(symbol, true);
				}
			
				ArrayList<Integer> toRemove = new ArrayList<Integer>();
				for (Integer code : pressedControllerButtons) {
					if(!controller.isButtonPressed(code)) {
						toRemove.add(code);
						String symbol = bindings.get(recognizedControllerButtons.get(code));
						if(symbol != null)
							notifyHandlersOfSymbolChange(symbol, false);
					}
				}
				for (Integer code : toRemove) {
					pressedControllerButtons.remove(code);
				}
			}
			
			updateJoysticks();
		}
	}
	
	private static void updateJoysticks() {
		for (int i = 0; i < GameController.MAX_JOYSTICKS; i++) {
			if(controller.getX(i) != 0 || controller.getY(i) != 0 || controller.getZ(i) != 0 ) {
				translator.auxJoystickOffCenter(i, controller.getX(i), controller.getY(i), controller.getZ(i));
			}
		}
	}
}
