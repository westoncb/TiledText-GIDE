package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.config.Settings;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.input.DefaultInputHandler;
import com.cyntaks.GDXGIDE.transitions.FadeFromBlackTransition;
import com.cyntaks.GDXGIDE.transitions.FadeToBlackTransition;
import com.cyntaks.GDXGIDE.util.GLImage;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

public class ProgramConfig {
	private static final int DEFAULT_INT_VALUE = 0;
	private static final int DEFAULT_FLOAT_VALUE = 0;
	private static final String DEFAULT_STRING_VALUE = "";
	
	private Window rootWindow;
	private static final ArrayList<Node> visited = new ArrayList<Node>(); 
	private String xmlPath;
	
	public ProgramConfig(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	
	protected void load() {
		rootWindow = loadFromXML(xmlPath);
	}
	
	private Window loadFromXML(String xmlPath) {
		ResourceManager.loadXML(xmlPath, xmlPath);
		Document doc = ResourceManager.getXML(xmlPath);
		
		Element rootElement = doc.getDocumentElement();
		NodeList windows = rootElement.getChildNodes();
		
		visited.clear();
		Window rootWindow = buildWindowTree(windows.item(1), null); //using "1" this way is inappropriate
		rootWindow.setWidth(Gdx.graphics.getWidth() * rootWindow.getWidth());
		rootWindow.setHeight(Gdx.graphics.getHeight() * rootWindow.getHeight());
		
		return rootWindow;
	}
	
	private Window buildWindowTree(Node node, Window parent) {
		NamedNodeMap attribs = node.getAttributes();
		Window window = buildWindow(attribs, parent);
		int childIndex = 0;
		int subWindowCount = 0;
		while(nextUnvisitedChild(node.getChildNodes(), childIndex) != null) {
			Node cur = nextUnvisitedChild(node.getChildNodes(), childIndex);
			if(cur.getNodeName().equalsIgnoreCase("window")) {
				Window child = buildWindowTree(cur, window);
				window.addChild(child, child.getWeight(), subWindowCount);
				subWindowCount++;
			} else if(cur.getNodeName().equalsIgnoreCase("grid")) {
				Grid grid = buildGrid(cur.getAttributes());
				grid.setInputHandler(new DefaultInputHandler(grid.getController()));
				window.setGrid(grid);
			}
			childIndex++;
		}
		
		visited.add(node);
		return window;
	}
	
	private Window buildWindow(NamedNodeMap map, Window parent) {
		Window window = new Window();
		window.setWidth(getFloatFromMap(map, "width"));
		window.setHeight(getFloatFromMap(map, "height"));
		window.setName(getStringFromMap(map, "name"));
		
		if(parent != null) {
			if(parent.getOrientation() == Window.HORIZONTAL) {
				window.setWeight(window.getWidth());
			} else {
				window.setWeight(window.getHeight());
			}
		}
		int weight = getIntFromMap(map, "weight");
		if(weight != DEFAULT_INT_VALUE) {
			window.setWeight(weight);
		}
		
		String inTransitionClassName = getStringFromMap(map, "in-transition");
		AbstractWindowTransition inTrans = getTransition(false, inTransitionClassName, window);
		window.setInTransition(inTrans);

		String outTransitionClassName = getStringFromMap(map, "out-transition");
		AbstractWindowTransition outTrans = getTransition(true, outTransitionClassName, window);
		window.setOutTransition(outTrans);
			
		String orientation = getStringFromMap(map, "orientation");
		if(!orientation.equals(DEFAULT_STRING_VALUE)) {
			if(orientation.equalsIgnoreCase("horizontal")) {
				window.setOrientation(Window.HORIZONTAL);
			}
			else if(orientation.equalsIgnoreCase("vertical")) {
				window.setOrientation(Window.VERTICAL);
			}
		}
		
		int borderSize = getIntFromMap(map, "bordersize");
		window.setInsets(borderSize, borderSize, borderSize, borderSize);
		
		String underPath = getStringFromMap(map, "underimage");
		String overPath = getStringFromMap(map, "overimage");
		
		if(!underPath.equals(DEFAULT_STRING_VALUE)) {
			if(VariableSizeBox.datFileExistsForImage(underPath))
				window.setUnderGraphic(new VariableSizeBox(underPath));
			else
				window.setUnderGraphic(new GLImage(underPath));
		}
		if(!overPath.equals(DEFAULT_STRING_VALUE))
			window.setOverGraphic(new VariableSizeBox(overPath));
		
		return window;
	}
	
	private AbstractWindowTransition getTransition(boolean isOutTransition, String className, Window window) {
		AbstractWindowTransition transition = null;
		if(className != null) {
			try {
				transition = (AbstractWindowTransition)Class.forName(className).newInstance();
				transition.setOwner(window);
				transition.setIsInTransition(!isOutTransition);
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex ) {
				ex.printStackTrace();
			} catch (InstantiationException ex) {
				ex.printStackTrace();
			}
			window.setOutTransition(transition);
		}
		else {
			transition = isOutTransition ? new FadeToBlackTransition() : new FadeFromBlackTransition();
			transition.setOwner(window);
			transition.setIsInTransition(!isOutTransition);
		}
		
		return transition;
	}
	
	private String getStringFromMap(NamedNodeMap map, String name) {
		Node node = map.getNamedItem(name);
		if(node != null)
			return node.getNodeValue();
		else
			return DEFAULT_STRING_VALUE;
	}
	
	private int getIntFromMap(NamedNodeMap map, String name) {
		Node node = map.getNamedItem(name);
		if(node != null)
			return Integer.parseInt(node.getNodeValue());
		else
			return DEFAULT_INT_VALUE;
	}
	
	private float getFloatFromMap(NamedNodeMap map, String name) {
		Node node = map.getNamedItem(name);
		if(node != null)
			return Float.parseFloat(node.getNodeValue());
		else
			return DEFAULT_FLOAT_VALUE;
	}
	
	private Grid buildGrid(NamedNodeMap map) {
		int gapSize = getIntFromMap(map, "gapsize");
		int rowsPerScreen = getIntFromMap(map, "rowsperscreen");
		int colsPerScreen = getIntFromMap(map, "colsperscreen");
		String orientationString = getStringFromMap(map, "orientation");
		int orientation = GridDisplay.VERTICAL;
		if(orientationString.equalsIgnoreCase("horizontal"))
			orientation = GridDisplay.HORIZONTAL;
		String cellConfigPath = getStringFromMap(map, "cellconfig");
		CellConfig cellConfig = Settings.getCellConfig(cellConfigPath); 
		String type = getStringFromMap(map, "type");
		String structureManagerType = getStringFromMap(map, "structuremanager");
		LinkedStructureManager sManager = null;
		Grid grid = null;
		
		try {
			grid = (Grid)Class.forName(type).newInstance();
			sManager = (LinkedStructureManager)Class.forName(structureManagerType).newInstance();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex ) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		}
		
		grid.configureAppearance(orientation, rowsPerScreen, colsPerScreen, gapSize);
		grid.setCellConfig(cellConfig);
		grid.setStructureManager(sManager);
		
		
		return grid;
	}
	
	private Node nextUnvisitedChild(NodeList children, int startIndex) {
		for (int i = startIndex; i < children.getLength(); i++) {
			if(!visited.contains(children.item(i)))
				return children.item(i);
		}
		
		return null;
	}
	
	public Window getRootWindow() {
		return rootWindow;
	}
}