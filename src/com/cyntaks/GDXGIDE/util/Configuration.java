package com.cyntaks.GDXGIDE.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration extends HashMap<String, String>{

	private static final long serialVersionUID = 1L;
	public static final int KEY = 0;
	public static final int VALUE = 1; 
	public static final String EMPTY_CHAR = ".";
	
	
	public Configuration() {
		
	}
	
	public void parse(boolean reverseKeyAndValue, boolean commaSplit, String text) {
		String[] temp = text.split(System.getProperty("line.separator"));
		ArrayList<String> lines = new ArrayList<String>();
		for(String line : temp)
			lines.add(line);
		parse(reverseKeyAndValue, commaSplit, lines);
	}
	
	public void parse(boolean reverseKeyAndValue, boolean commaSplit, ArrayList<String> lines) {
		super.clear();
		for(String line : lines) {
			if(!line.isEmpty()) {
				String[] tokens = line.split("=");

				if(commaSplit) {
					int i = 0;
					try {
						String[] values = tokens[VALUE].split(",");
						for (; i < values.length; i++) {
							if(reverseKeyAndValue)
								super.put(values[i], tokens[KEY]);
							else
								super.put(tokens[KEY], values[i]);
						}
					} catch (Exception ex) {
						System.err.println("Error in configuration file at: " + tokens[i]);
						ex.printStackTrace();
					}
				} else {
					if(reverseKeyAndValue)
						super.put(tokens[VALUE], tokens[KEY]);
					else
						super.put(tokens[KEY], tokens[VALUE]);
				}
			}
		}
	}
	
	public String getString(String key) {
		String string = super.get(key);
		if(string == null || string.equals(EMPTY_CHAR))
			return null;
		else
			return string;
	}
	
	public int getInt(String key) {
		String string = super.get(key);
		if(string == null || string.equals(EMPTY_CHAR))
			return 0;
		else
			return Integer.parseInt(string);
	}
	
	public float getFloat(String key) {
		String string = super.get(key);
		if(string == null || string.equals(EMPTY_CHAR))
			return 0;
		else
			return Float.parseFloat(string);
	}
	
	public boolean getBoolean(String key) {
		String string = super.get(key);
		if(string == null || string.equals(EMPTY_CHAR))
			return false;
		else
			return Boolean.parseBoolean(string);
	}
}
