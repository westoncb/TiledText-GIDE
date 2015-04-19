package com.cyntaks.GDXGIDE.util;

import java.util.HashMap;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class NodeConfig {

	private HashMap<String, Configuration> blocks;
	private String path;
	public static final String NAME = "name";
	
	public NodeConfig(String path) {
		this.path = path;
		blocks = new HashMap<String, Configuration>();
	}
	
	public void load() {
		FileHandle handle = Gdx.files.internal(path);
		if(handle.exists()) {
			Scanner scanner = new Scanner(handle.read());
			String text = "";
			while(scanner.hasNextLine())
				text += scanner.nextLine() + System.getProperty("line.separator");
			String[] tempBlocks = text.split("END");
			for(String block : tempBlocks) {
				Configuration config = new Configuration();
				config.parse(false, false, block);
				if(!config.isEmpty())
					blocks.put(config.get(NAME), config);
			}
		}
	}
	
	public Configuration getBlock(String name) {
		return blocks.get(name);
	}
}
