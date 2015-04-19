package com.cyntaks.GDXGIDE.config;

import java.util.HashMap;

import com.cyntaks.GDXGIDE.util.ConfigFile;
import com.cyntaks.GDXGIDE.util.NodeConfig;

public class Settings {
	private static HashMap<String, CellConfig> cellConfigs = new HashMap<String, CellConfig>();
	private static HashMap<String, NodeConfig> nodeConfigs = new HashMap<String, NodeConfig>();
	
	public static CellConfig getCellConfig(String name) {
		CellConfig config = cellConfigs.get(name);
		if(config == null) {
			ConfigFile configFile = new ConfigFile("config/cell/" + name + ".txt");
			configFile.load(false, false);
			config = new CellConfig(configFile);
			cellConfigs.put(name, config);
		}
		
		return config;
	}
	
	public static NodeConfig getNodeConfig(String name) {
		NodeConfig config = nodeConfigs.get(name);
		if(config == null) {
			config = new NodeConfig("config/node/" + name + ".txt");
			config.load();
			nodeConfigs.put(name, config);
		}
		
		return config;
	}
}
