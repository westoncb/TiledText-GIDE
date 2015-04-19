package com.cyntaks.GDXGIDE.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class GameController {
	public static final int BUTTON_0 = 0;
	public static final int BUTTON_1 = 1;
	public static final int BUTTON_2 = 2;
	public static final int BUTTON_3 = 3;
	public static final int BUTTON_4 = 4;
	public static final int BUTTON_5 = 5;
	public static final int BUTTON_6 = 6;
	public static final int BUTTON_7 = 7;
	public static final int BUTTON_8 = 8;
	public static final int BUTTON_9 = 9;
	public static final int BUTTON_10 = 10;
	public static final int BUTTON_11 = 11;
	public static final int BUTTON_12 = 12;
	public static final int BUTTON_13 = 13;
	public static final int BUTTON_14 = 14;
	public static final int BUTTON_15 = 15;
	public static final int BUTTON_16 = 16;
	public static final int BUTTON_17 = 17;
	public static final int BUTTON_18 = 18;
	public static final int BUTTON_19 = 19;
	
	public static final int X0 = 1000;
	public static final int X0_LEFT = 101;
	public static final int X0_RIGHT = 102;
	public static final int X1 = 1001;
	public static final int X1_LEFT = 105;
	public static final int X1_RIGHT = 104;
	public static final int X2 = 1002;
	public static final int X2_LEFT = 107;
	public static final int X2_RIGHT = 108;
	public static final int X3 = 1003;
	public static final int X3_LEFT = 111;
	public static final int X3_RIGHT = 110;
	
	public static final int Y0 = 2000;
	public static final int Y0_UP = 113;
	public static final int Y0_DOWN = 114;
	public static final int Y1 = 2001;
	public static final int Y1_UP = 117;
	public static final int Y1_DOWN = 116;
	public static final int Y2 = 2002;
	public static final int Y2_UP = 119;
	public static final int Y2_DOWN = 120;
	public static final int Y3 = 2003;
	public static final int Y3_UP = 123;
	public static final int Y3_DOWN = 122;
	
	public static final int Z0 = 3000;
	public static final int Z0_BACK = 125;
	public static final int Z0_FORWARD = 126;
	public static final int Z1 = 3001;
	public static final int Z1_BACK = 129;
	public static final int Z1_FORWARD = 128;
	public static final int Z2 = 3002;
	public static final int Z2_BACK = 133;
	public static final int Z2_FORWARD = 132;
	public static final int Z3 = 3003;
	public static final int Z3_BACK = 135;
	public static final int Z3_FORWARD = 134;
	
	public static final int POV0 = 204;
	public static final int POV0_LEFT = 200;
	public static final int POV0_RIGHT = 201;
	public static final int POV0_UP = 202;
	public static final int POV0_DOWN = 203;
	
	public static final int POV1 = 304;
	public static final int POV1_LEFT = 300;
	public static final int POV1_RIGHT = 301;
	public static final int POV1_UP = 302;
	public static final int POV1_DOWN = 303;
	
	public static final int MAX_JOYSTICKS = 3;
	
	private Controller controller;
	private GameControllerConfig config;
	
	public GameController(Controller controller, GameControllerConfig config) {
		this.controller = controller;
		this.config = config;
	}
	
	public float getX(int joystickID) {
		int id = X0 + joystickID;
		Component comp = getComponent(id);
		if(comp == null)
			return 0;
		else
			return noiseFilter(comp.getPollData());
	}
	
	public float getY(int joystickID) {
		int id = Y0 + joystickID;
		Component comp = getComponent(id);
		if(comp == null)
			return 0;
		else
			return noiseFilter(comp.getPollData());
	}
	
	public float getZ(int joystickID) {
		int id = Z0 + joystickID;
		Component comp = getComponent(id);
		if(comp == null)
			return 0;
		else
			return noiseFilter(comp.getPollData());
	}
	
	public boolean isButtonPressed(int id) {
		Component comp = getComponent(id);
		
		if(id < 100) { //dealing with regular buttons
			if(comp.getPollData() == 1.0f)
				return true;
			else
				return false;
		} else if (id < 200) { //dealing with axes
			if(id%2 == 0) { //dealing with left, up, or forward
				if(noiseFilter(comp.getPollData()) > 0)
					return true;
				else
					return false;
			} else { //dealing with right, down, or back 
				if(noiseFilter(comp.getPollData()) < 0)
					return true;
				else
					return false;
			}
		} else { //dealing with a POV ("digital" direction pad)
			int modId = id % 100;
			if(modId == 0) {
				if(comp.getPollData() == 1.0) //left
					return true;
				else
					return false;
			} else if(modId == 1) {
				if(comp.getPollData() == 0.5) //right
					return true;
				else
					return false;
			} else if(modId == 2) {
				if(comp.getPollData() == 0.25) //up
					return true;
				else
					return false;
			} else if(modId == 3) {
				if(comp.getPollData() == 0.75) //down
					return true;
				else
					return false;
			} else {
				return false;
			}
		}
	}
	
	private float noiseFilter(float signal) {
		if(Math.abs(signal) < .3)
			return 0;
		else
			return signal;
	}
	
	public boolean poll() {
		return controller.poll();
	}
	
	private Component getComponent(int id) {
		String identifier = config.getControllerSpecificIdentifier(id);
		Component[] components = controller.getComponents();
		for (Component component : components) {
			if(component.getIdentifier().toString().equalsIgnoreCase(identifier)) {
				return component;
			}
		}
		
		return null;
 	}
}
