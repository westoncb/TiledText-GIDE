package com.cyntaks.GDXGIDE.input;

import java.util.HashMap;
import java.util.Set;

public class GameControllerConfig {
	private static HashMap<String, GameControllerConfig> configurations = new HashMap<String, GameControllerConfig>();
	private static boolean controllersDefined = false;
	
	private HashMap<Integer, String> map;
	
	public GameControllerConfig() {
		map = new HashMap<Integer, String>();
	}
	
	private static void defineControllers() {
		defineXbox360();
		
		controllersDefined = true;
	}
	
	public static GameControllerConfig getConfigForController(String name) {
		if(!controllersDefined)
			defineControllers();
		
		name = name.toLowerCase();
		
		Set<String> names = configurations.keySet();
		for (String string : names) {
			string = string.toLowerCase();
			if(name.indexOf(string) > 0)
				return configurations.get(string);
		}
		
		return null;
	}
	
	private static void defineXbox360() {
		GameControllerConfig the360 = new GameControllerConfig();
		the360.map.put(GameController.BUTTON_0, "0");
		the360.map.put(GameController.BUTTON_1, "1");
		the360.map.put(GameController.BUTTON_2, "2");
		the360.map.put(GameController.BUTTON_3, "3");
		the360.map.put(GameController.BUTTON_4, "4");
		the360.map.put(GameController.BUTTON_5, "5");
		the360.map.put(GameController.BUTTON_6, "6");
		the360.map.put(GameController.BUTTON_7, "7");
		the360.map.put(GameController.BUTTON_8, "8");
		the360.map.put(GameController.BUTTON_9, "9");
		
		the360.map.put(GameController.X0, "x");
		the360.map.put(GameController.X0_LEFT, "x");
		the360.map.put(GameController.X0_RIGHT, "x");
		
		the360.map.put(GameController.X1, "rx");
		the360.map.put(GameController.X1_LEFT, "rx");
		the360.map.put(GameController.X1_RIGHT, "rx");
		
		the360.map.put(GameController.Y0, "y");
		the360.map.put(GameController.Y0_UP, "y");
		the360.map.put(GameController.Y0_DOWN, "y");
		
		the360.map.put(GameController.Y1, "ry");
		the360.map.put(GameController.Y1_UP, "ry");
		the360.map.put(GameController.Y1_DOWN, "ry");
		
		the360.map.put(GameController.Z0, "z");
		the360.map.put(GameController.Z0_BACK, "z");
		the360.map.put(GameController.Z0_FORWARD, "z");
		
		the360.map.put(GameController.POV0, "pov");
		the360.map.put(GameController.POV0_UP, "pov");
		the360.map.put(GameController.POV0_DOWN, "pov");
		the360.map.put(GameController.POV0_LEFT, "pov");
		the360.map.put(GameController.POV0_RIGHT, "pov");
		
		configurations.put("xbox 360", the360);
	}
	
	public String getControllerSpecificIdentifier(int genericID) {
		return map.get(genericID);
	}
}
