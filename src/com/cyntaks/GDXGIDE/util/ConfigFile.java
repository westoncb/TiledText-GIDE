package com.cyntaks.GDXGIDE.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;

public class ConfigFile extends Configuration {

	private static final long serialVersionUID = 1L;
	private String path;
	
	
	public ConfigFile(String path) {
		this.path = path;
	}
	
	public void load(boolean reverseKeyAndValue, boolean commaSplit) {
		Scanner scanner = new Scanner(Gdx.files.internal(path).read());
		ArrayList<String> lines = new ArrayList<String>();
		while(scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}
		scanner.close();
		super.parse(reverseKeyAndValue, commaSplit, lines);
	}
	
	public String getString(String key) {
		String string = super.get(key);
		if(string.equals(EMPTY_CHAR))
			return null;
		else
			return string;
	}
	
	public int getInt(String key) {
		String string = super.get(key);
		if(string.equals(EMPTY_CHAR))
			return 0;
		else
			return Integer.parseInt(string);
	}
	
	public float getFloat(String key) {
		String string = super.get(key);
		if(string.equals(EMPTY_CHAR))
			return 0;
		else
			return Float.parseFloat(string);
	}
	
	public boolean getBoolean(String key) {
		String string = super.get(key);
		if(string.equals(EMPTY_CHAR))
			return false;
		else
			return Boolean.parseBoolean(string);
	}
	
	public String getPath() {
		return path;
	}
}
