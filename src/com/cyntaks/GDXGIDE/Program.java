package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.Component;
import com.cyntaks.GDXGIDE.transitions.FadeFromBlackTransition;
import com.cyntaks.sgf.event.EventListener;

public abstract class Program implements EventListener, Comparable<Program> {
	private ProgramConfig config;
	private Program parent;
	private Window rootWindow;
	private GNode input;
	private Cell hoverTarget;
	private float hoverWidth = -1;
	private float hoverHeight = -1;
	private float relativeHoverWidth = -1;
	private float relativeHoverHeight = -1;
	private float hoverXOffset;
	private float hoverYOffset;
	private int hoverOrientation;
	public static final int HOVER_CENTER = 0;
	public static final int HOVER_ABOVE = 1;
	public static final int HOVER_BELOW = 2;
	public static final int HOVER_LEFT = 3;
	public static final int HOVER_RIGHT = 4;
	private boolean noLoadScreen;
	private ArrayList<ProgramFinishedListener> finishListeners;
	
	//For embedding
	private int embedAxis;
	private int embeddedWindowIndex;
	private boolean unEmbedding;
	private boolean firstFinished;
	private static final int SIZE_CHANGE_ID_1 = 888;
	private static final int SIZE_CHANGE_ID_2 = 999;
	
	private Drawable loadScreen;
	private boolean initialized;
	private boolean loaded;
	
	public Program(ProgramConfig config, Program parent) {
		this.config = config;
		this.parent = parent;
		this.loadScreen = new DefaultLoadScreen();
		finishListeners = new ArrayList<ProgramFinishedListener>();
	}
	
	public Program(ProgramConfig config) {
		this(config, null);
	}
	
	public Program() {
		this(null, null);
		rootWindow = new Window();
	}
	
	public abstract void load();
	
	public abstract void init();
	
	public abstract void cellActivated(Cell cell, int typeID);
	
	public abstract void cellLifted(Cell cell);
	
	public abstract void cellInserted(Cell cell);
	
	public abstract void cellDeleted(Cell cell);
	
	public abstract void cellSelected(Cell cell);
	
	public abstract void cellDeselected(Cell cell);
	
	public abstract void cellSwitchedTo(Cell cell);
	
	public abstract void cellSwitchedFrom(Cell cell);
	
	public abstract void selectionSet(ArrayList<Cell> selection);
	
	public abstract void update(float delta);
	
	public abstract void dispose();
	
	public Grid getActiveGrid() {
		return getFirstWindowWithGrid().getGrid();
	}
	
	public Window getWindow(String name) {
		return rootWindow.getNamedWindow(name);
	}
	
	public Grid getGrid(String name) {
		return rootWindow.getNamedWindow(name).getGrid();
	}
	
	public Window getFirstWindowWithGrid() {
		return recursiveGetFirstWindow(rootWindow);
	}
	
	private Window recursiveGetFirstWindow(Window window) {
		if(window.getGrid() == null) {
			for (int i = 0; i < window.getChildren().size(); i++) {
				Window childWindow = recursiveGetFirstWindow(window.getChildren().get(i));
				if(childWindow != null)
					return childWindow;
			}
			
			return null;
		} else {
			return window;
		}
	}

	public GNode getInput() {
		return input;
	}

	public void setInput(GNode input) {
		this.input = input;
	}
	
	public Window getRootWindow() {
		return rootWindow;
	}
	
	protected final void auxLoad() {
		config.load();
		rootWindow = config.getRootWindow();
		rootWindow.load();
		setupControllersRecursive(rootWindow);
		if(hoverTarget != null && parent != null)
			rootWindow.setZ(parent.getRootWindow().getZ()+1);
		load();
		loaded = true;
	}
	
	protected final void auxInit() {
		Grid grid = getActiveGrid();
		grid.getStructureManager().initialize(getInput(), grid);
		rootWindow.init();

		init();
		initialized = true;
	}
	
	private void setupControllersRecursive(Window window) {
		Grid grid = window.getGrid();
		if(grid != null) {
			GridController controller = grid.getInputHandler().getGridController();
			controller.setListener(this);
		}
		for (Window subWindow : window.getChildren()) {
			setupControllersRecursive(subWindow);
		}
	}
	
	protected final void auxDraw() {
		if(initialized) {
			if(rootWindow.getParent() == null)
				rootWindow.draw();
			draw();
		}
	}
	
	public void draw() {}
	public void drawBackground() {}
	public void drawOverlay() {}
	
	protected final void auxUpdate(float delta) {
		rootWindow.update(delta);
		update(delta);
		orientToHoverTarget();
	}
	
	protected final void auxDispose() {
		if(loaded) {
			rootWindow.dispose();
			dispose();
			notifyFinishListeners();
		}
	}
	
	
	protected final void auxCellInserted(Cell cell) {
		ResourceManager.getSound("insert").play();
		CellConfig config = cell.getConfig();
		
		float r = config.getUnderGraphicColor().r;
		float g = config.getUnderGraphicColor().g;
		float b = config.getUnderGraphicColor().b;
		float a = config.getUnderGraphicColor().a;
		
		Color sColor = config.getSelectedColor();
		cell.getUnderGraphic().setColor(sColor.r, sColor.g, sColor.b, sColor.a);
		cell.getUnderGraphic().setDestColor(r, g, b, a, 1);
		cellInserted(cell);
	}
	
	protected final void auxCellLifted(Cell cell) {
		ResourceManager.getSound("insert").play();
		cellLifted(cell);
	}
	
	protected final void auxCellDeleted(Cell cell) {
		ResourceManager.getSound("insert").play();
		cellDeleted(cell);
	}
	
	protected final void auxCellSwitchedTo(Cell cell) {
		ResourceManager.getSound("rollover").play(0.05f);
		cellSwitchedTo(cell);
	}
	
	protected final void auxCellSwitchedFrom(Cell cell) {
		cellSwitchedFrom(cell);
	}
	
	protected final void auxCellSelected(Cell cell) {
		cellSelected(cell);
	}
	
	protected final void auxCellDeselected(Cell cell) {
		cellDeselected(cell);
	}
	
	public final void auxCellActivated(Cell cell) {
		int id = -1;
		
		Component comp = cell.getOwner().getComponentForNode(cell.getNode());
		if(comp != null)
			id = comp.getEventType();
		
		cellActivated(cell, id);
	}
	
	public final void auxSelectionSet(ArrayList<Cell> selection) {
		for (Cell cell : selection) {
			cell.getBehavior().selectionFinalized();
		}
		selectionSet(selection);
	}
	
	public void setInput(String[] messages) {
		GNode root = new GNode("WCOMPILATIONUNIT");
		for (int i = 0; i < messages.length; i++) {
			GNode node = new GNode(messages[i]);
			root.addChild(node);
		}
		setInput(root);
	}
	
	public void orientToHoverTarget() {
		if(hoverTarget != null) {
			if(hoverWidth != -1)
				rootWindow.setWidth(hoverWidth);
			if(hoverHeight != -1)
				rootWindow.setHeight(hoverHeight);
			if(relativeHoverWidth != -1)
				rootWindow.setWidth(relativeHoverWidth*getParent().getRootWindow().getInteriorWidth());
			if(relativeHoverHeight != -1)
				rootWindow.setHeight(relativeHoverHeight*getParent().getRootWindow().getInteriorHeight());
			
			if(hoverOrientation == HOVER_CENTER) {
				rootWindow.setX(hoverTarget.getAbsX() + hoverTarget.getWidth()/2 - rootWindow.getWidth()/2 + hoverXOffset);
				rootWindow.setY(hoverTarget.getAbsY() + hoverTarget.getHeight()/2 - rootWindow.getHeight()/2 + hoverYOffset);
			} else if(hoverOrientation == HOVER_ABOVE) {
				rootWindow.setX(hoverTarget.getAbsX() + hoverTarget.getWidth()/2 - rootWindow.getWidth()/2 + hoverXOffset);
				rootWindow.setY(hoverTarget.getAbsY() - rootWindow.getHeight() + hoverYOffset);
			} else if(hoverOrientation == HOVER_BELOW) {
				rootWindow.setX(hoverTarget.getAbsX() + hoverTarget.getWidth()/2 - rootWindow.getWidth()/2 + hoverXOffset);
				rootWindow.setY(hoverTarget.getAbsY() + hoverTarget.getHeight() + hoverYOffset);
			} else if(hoverOrientation == HOVER_LEFT) {
				rootWindow.setX(hoverTarget.getAbsX() - rootWindow.getWidth() + hoverXOffset);
				rootWindow.setY(hoverTarget.getAbsY() + hoverTarget.getHeight()/2 - rootWindow.getHeight()/2 + hoverYOffset);
			} else if(hoverOrientation == HOVER_RIGHT) {
				rootWindow.setX(hoverTarget.getAbsX() + hoverTarget.getWidth() + hoverXOffset);
				rootWindow.setY(hoverTarget.getAbsY() + hoverTarget.getHeight()/2 - rootWindow.getHeight()/2 + hoverYOffset);
			}
		}
	}
	
	public int compareTo(Program prog) {
		return this.getRootWindow().getZ() - prog.getRootWindow().getZ();
	}
	
	//////////////
	// Embedding stuff below
	/////////////////////////////
	
	public void embed(Window target, int axis, float expandRate, boolean first) {
		firstFinished = false;
		this.embedAxis = axis;
		Window newWindow = new Window(target.getAbsX(), target.getAbsY(), target.getWidth(), target.getHeight());
		
		int thisID = SIZE_CHANGE_ID_1;
		int targetID = SIZE_CHANGE_ID_2;
		embeddedWindowIndex = 0;
		if(!first) {
			thisID = SIZE_CHANGE_ID_2;
			targetID = SIZE_CHANGE_ID_1;
			embeddedWindowIndex = 1;
		}
		
		float originalWidth = rootWindow.getWidth();
		float originalHeight = rootWindow.getHeight();
		if(axis == Window.VERTICAL) {
			rootWindow.setHeight(1);
			rootWindow.setDestHeight(originalHeight, expandRate, true, thisID);
			rootWindow.getChangeHeightEvent().addEventListener(this);
			target.setDestHeight(target.getHeight()-originalHeight, expandRate, true, targetID);
			target.getChangeHeightEvent().addEventListener(this);
		} else {
			rootWindow.setWidth(1);
			rootWindow.setDestWidth(originalWidth, expandRate, true, thisID);
			rootWindow.getChangeWidthEvent().addEventListener(this);
			target.setDestWidth(target.getWidth()-originalWidth, expandRate, true, targetID);
			target.getChangeWidthEvent().addEventListener(this);
		}
		
		FadeFromBlackTransition trans = new FadeFromBlackTransition();
		newWindow.setInTransition(trans);
		newWindow.setOrientation(axis);
		
		if(first) {
			newWindow.addChild(rootWindow, 0);
			newWindow.addChild(target, 1);
		} else {
			newWindow.addChild(target, 0);
			newWindow.addChild(rootWindow, 1);
		}
		
		newWindow.load();
		rootWindow = newWindow;
	}
	
	public void unEmbed(float rate) {
		Window original = rootWindow.getChildren().get(embeddedWindowIndex);
		Window target = rootWindow.getChildren().get(1 - embeddedWindowIndex); //getting the complement (index is 1 or zero)
		original.killResizeEvents();
		target.killResizeEvents();
		
		unEmbedding = true;
		firstFinished = false;
		
		int thisID = SIZE_CHANGE_ID_1;
		int targetID = SIZE_CHANGE_ID_2;
		if(embeddedWindowIndex == 1) {
			thisID = SIZE_CHANGE_ID_2;
			targetID = SIZE_CHANGE_ID_1;
		}
		
		if(embedAxis == Window.VERTICAL) {
			original.setDestHeight(1, rate, false, thisID);
			original.getChangeHeightEvent().addEventListener(this);
			target.setDestHeight(target.getHeight()+original.getHeight(), rate, false, targetID);
			target.getChangeHeightEvent().addEventListener(this);
		} else {
			original.setDestWidth(1, rate, false, thisID);
			original.getChangeWidthEvent().addEventListener(this);
			target.setDestWidth(target.getWidth()+original.getWidth(), rate, false, targetID);
			target.getChangeWidthEvent().addEventListener(this);
		}
	}
	
	@Override
	public void update(int id, int type, float value) {
		Window window = null;
		if(id == SIZE_CHANGE_ID_1)
			window = rootWindow.getChildren().get(0);
		else if(id == SIZE_CHANGE_ID_2)
			window = rootWindow.getChildren().get(1);
		
		if(window != null) {
			if(type == Corpus.CHANGE_HEIGHT_TYPE)
				window.setWeightSilently(window.getHeight()/rootWindow.getHeight());
			else if(type == Corpus.CHANGE_WIDTH_TYPE)
				window.setWeightSilently(window.getWidth()/rootWindow.getWidth());
		}
	}

	
	@Override
	public void finish(int id, int type) {
		if(unEmbedding && firstFinished) {
			dislodge();
		} else if(!unEmbedding && firstFinished) {
			embedCompleted();
		}
		
		if(id == SIZE_CHANGE_ID_1 || id == SIZE_CHANGE_ID_2)
			firstFinished = true;
	}
	
	private void dislodge() {
		Window original = rootWindow.getChildren().get(embeddedWindowIndex);
		rootWindow.removeChild(0);
		rootWindow.removeChild(0);
		rootWindow = original;
		unEmbedding = false;
		unEmbedCompleted();
	}
	
	public void unEmbedCompleted() {
		
	}
	
	public void embedCompleted() {
		
	}

	public Drawable getLoadScreen() {
		return loadScreen;
	}

	public void setLoadScreen(Drawable loadScreen) {
		this.loadScreen = loadScreen;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public Program getParent() {
		return parent;
	}

	public void setParent(Program parent) {
		this.parent = parent;
	}

	public Cell getHoverTarget() {
		return hoverTarget;
	}

	public void setHoverTarget(Cell hoverTarget) {
		this.hoverTarget = hoverTarget;
	}

	public float getHoverWidth() {
		return hoverWidth;
	}

	public void setHoverWidth(float hoverWidth) {
		this.hoverWidth = hoverWidth;
	}

	public float getHoverHeight() {
		return hoverHeight;
	}

	public void setHoverHeight(float hoverHeight) {
		this.hoverHeight = hoverHeight;
	}

	public float getRelativeHoverWidth() {
		return relativeHoverWidth;
	}

	public void setRelativeHoverWidth(float relativeHoverWidth) {
		this.relativeHoverWidth = relativeHoverWidth;
	}

	public float getRelativeHoverHeight() {
		return relativeHoverHeight;
	}

	public void setRelativeHoverHeight(float relativeHoverHeight) {
		this.relativeHoverHeight = relativeHoverHeight;
	}

	public float getHoverXOffset() {
		return hoverXOffset;
	}

	public void setHoverXOffset(float hoverXOffset) {
		this.hoverXOffset = hoverXOffset;
	}

	public float getHoverYOffset() {
		return hoverYOffset;
	}

	public void setHoverYOffset(float hoverYOffset) {
		this.hoverYOffset = hoverYOffset;
	}

	public int getHoverOrientation() {
		return hoverOrientation;
	}

	public void setHoverOrientation(int hoverOrientation) {
		this.hoverOrientation = hoverOrientation;
	}

	public boolean isNoLoadScreen() {
		return noLoadScreen;
	}

	public void setNoLoadScreen(boolean noLoadScreen) {
		this.noLoadScreen = noLoadScreen;
	}
	
	public void addFinishListener(ProgramFinishedListener listener) {
		if(!finishListeners.contains(listener))
			finishListeners.add(listener);
	}
	
	public void removeFinishListener(ProgramFinishedListener listener) {
		finishListeners.remove(listener);
	}
	
	private void notifyFinishListeners() {
		for (ProgramFinishedListener listener : finishListeners) {
			listener.programFinished(this);
		}
	}
}