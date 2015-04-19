package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.Component;
import com.cyntaks.GDXGIDE.gui.ContentLine;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.gui.ScrollIndicator;
import com.cyntaks.GDXGIDE.input.DefaultInputHandler;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.text.TypeFace;

/**
 * Grids are primarily data structures for managing Cells, which are the visual representation of
 * GNodes. A more complete description, however, would be that Grids manage all of the data for the
 * visual display of a document; a GridDisplay, which represents a particular layout scheme, then
 * takes that data and actually renders it. 
 * 
 * @author weston
 *
 */

public class Grid implements ResourceUser {
	private boolean loaded;
	private boolean initialized;
	private boolean drawCursor;
	private int rowsPerScreen;
	private int colsPerScreen;
	
	private ArrayList<Cell> cells;
	private ArrayList<Cell> visibleCells;

	private DefaultInputHandler inputHandler;
	private boolean hasPrimaryPointer;
	
	private Window window; //The Window containing this grid
	
	private ScrollIndicator firstScrollIndicator;
	private ScrollIndicator secondScrollIndicator;

	private HashMap<GNode, Component> components;
	private ArrayList<GNode> initialNodes;
	private boolean addingInitialNodes;

	private GridDisplay gridDisplay;
	private CellDragger dragger;
	private Cursor cursor;
	private Scroller scroller;
	private GridController controller;
	private LinkedStructureManager structure;
	private CellConfig cellConfig; //A given Grid uses a single CellConfig for all of its Cells
	
	private boolean multiSelectEnabled = false;
	private boolean deleteEnabled = true;
	private boolean draggingEnabled = true;
	
	private int orientation; //Basically whether it's going to scroll horizontally or vertically
	
	public static final int GRID_DETERMINED = 0;
	public static final int NODE_DETERMINED = 1;
	private int orientationSource = GRID_DETERMINED;
	
	private boolean selectFirstCell = true;

	private ArrayList<FloatCell> floatCells = new ArrayList<FloatCell>();
	
	private boolean showTree = false;
	private boolean showAllConnections = false;
	
	public Grid() {
		cells = new ArrayList<Cell>();
		visibleCells = new ArrayList<Cell>();
		initialNodes = new ArrayList<GNode>();
		gridDisplay = new GridDisplay();
		gridDisplay.setAnimate(true);
		cursor = new Cursor(this);
		scroller = new Scroller(this);
		controller = new GridController(this);
		dragger = new CellDragger(this);
		components = new HashMap<GNode, Component>();
	}
	
	public void load() {
		firstScrollIndicator.load();
		secondScrollIndicator.load();
		cursor.load();
		cellConfig.load();
		loaded = true;
	}
	
	public void init() {
		if(initialNodes != null) {
			addingInitialNodes = true;
			addCells(initialNodes, null);
			addingInitialNodes = false;
		}
		initialNodes = null;
		
		
		for (Cell cell : cells) {
			prepareCell(cell);
		}
		firstScrollIndicator.init();
		secondScrollIndicator.init();
		cursor.init();
		
		scroller.moveToBeginning();
		
		//if(selectFirstCell && !cells.isEmpty() && getSelector().getSelectedCells().isEmpty())
			//getSelector().addCell(cells.get(0));
		
		initialized = true;
	}
	
	/**
	 * Called when the StructureManager is swapping out the present set of cells for a new set.
	 * @param node
	 */
	public void movingToNextNode(GNode node) {
		if(cursor != null) {
			cursor.vanish();
		}
	}
	
	public void addComponent(Component component) {
		addComponent(component, false);
	}
	
	public void addComponent(Component component, boolean addToBeginning) {
		ArrayList<GNode> wrapper = new ArrayList<GNode>();
		wrapper.add(component.getNode());
		components.put(component.getNode(), component);
		Cell cell = component.createCell(this);
		cell.load();
		Cell anchorCell = null;
		GNode anchor = null;
		if(!addToBeginning) {
			if(!cells.isEmpty())
				anchorCell = cells.get(cells.size()-1);
			if(anchorCell != null)
				anchor = anchorCell.getNode();
		}
		addCells(wrapper, anchor);
	}
	
	public ArrayList<Cell> addCells(ArrayList<GNode> nodes, GNode anchor) {
		return addCells(nodes, anchor, false);
	}

	/**
	 * A Cell is created for each GNode passed in, and then loaded, initialized, and added to the Grid.
	 * @param nodes The nodes which Cells should be created and added for.
	 * @param anchor A node corresponding to a Cell, which the new Cells will be added after.
	 * @param addBefore If this flag is set to true, the new cells will be added before
	 * 		  the anchor rather than after.
	 * @return
	 */
	public ArrayList<Cell> addCells(ArrayList<GNode> nodes, GNode anchor, boolean addBefore) {
		ArrayList<Cell> newCells = new ArrayList<Cell>();
		if(loaded) {
			for(int i = nodes.size()-1; i > -1; i--) {
				GNode t = nodes.get(i);
				Cell cell = null;
				if(components.get(t) != null)
					cell = components.get(t).getCell();
				else if(cellConfig.getCellClass().equals("com.cyntaks.GDXGIDE.gui.TextCell"))
					cell = new TextCell(this, t);
				else
					cell = new Cell(this, t);
				
				cell.setConfig(cellConfig);
				cell.changeBehavior(cellConfig.getBehaviorInstance());
				cell.load();
				if(!addingInitialNodes) //don't want to call this here when adding the first nodes
					prepareCell(cell);
				
				int index = findAddIndex(anchor);
				if(addBefore)
					index -= 1;
				cells.add(index, cell);
				newCells.add(cell);
			}
		} else { //If Cells are attempted to be added before this Grid has loaded, they will be queued up and added after loading
			for(GNode t : nodes)
				initialNodes.add(t);
		}
		
		return newCells;
	}
		
	private void prepareCell(Cell cell) {
		cell.init();
		cell.resize(calcCellWidth(), calcCellHeight());
		if(cell instanceof TextCell)
			((TextCell)cell).updateTextBounds();
	}
	
	public Component getComponentForNode(GNode node) {
		return components.get(node);
	}
	
	/**
	 * Gives the index one after the Cell corresponding to the node
	 * passed in.
	 * 
	 * @param node
	 * @return
	 */
	protected int findAddIndex(GNode node) {
		if(node == null)
			return 0;
		
		for(Cell cell : getCells()) {
			if(cell.getNode() == node) {
				return getCells().indexOf(cell) + 1;
			}
		}
		
		return 0;
	}
	
	public void removeCells(ArrayList<GNode> nodes) {
		scroller.stopFollowingCell();
		ArrayList<Cell> scratch = new ArrayList<Cell>();
		
		for(GNode node : nodes) {
			//if(!node.isMeta()) //If a meta node were a leaf, it would still be in the list. Possible if all of its children were removed in a delete operation.
				scratch.add(getCellForNode(node));
		}
		
		for(Cell cell : scratch) {
			if(cell != null) {
				inputHandler.getSelector().removeFromSelection(cell);
				cells.remove(cell);
				cell.dispose();
			} else {
				//System.out.println("Trying to remove null cell!");
			}
		}
	}
	
	public void updateDisplay() {
		getDisplay().setContents(getCells());
		getDisplay().update(0, 0, 0, window.getWidth(), window.getHeight());
	}
	
	public void configureAppearance(int orientation, int rowsPerScreen, int colsPerScreen, int gapSize) {
		this.rowsPerScreen = rowsPerScreen;
		this.colsPerScreen = colsPerScreen;
		
		int lines = colsPerScreen;
		if(orientation == GridDisplay.HORIZONTAL)
			lines = rowsPerScreen;
		gridDisplay.configureAppearance(orientation, lines, gapSize);
		setOrientation(orientation);
		setScrollIndicatorOrientation(orientation);
	}
	
	public void configureAppearance(GNode node, int lines) {
		if(node.getText() != null) {
			gridDisplay.configureAppearance(node.getOrientation(), lines, gridDisplay.getGapSize());
			setScrollIndicatorOrientation(node.getOrientation());
		}
	}
	
	public void setInputHandler(DefaultInputHandler handler) {
		this.inputHandler = handler;
	}
	
	public void dispose() {
	
	}

	public void deselectAll() {
		for(Cell cell : cells) {
			if(cell.isSelected())
				cell.deselect();
		}
	}
	
	public void update(float delta) {
		if(initialized) {

			inputHandler.getSelector().getController().update(delta);
			inputHandler.getGridController().update(delta);
			dragger.update(delta);
			scroller.update(delta);
			updateCellSizes();
			gridDisplay.setContents(cells);
			gridDisplay.update(delta, 0, 0, window.getWidth(), window.getHeight());
			visibleCells.clear();
			for(Cell cell : cells) {
				if(isCellVisible(cell) || showTree)
					visibleCells.add(cell);
			}
			
			for (int i = 0; i < visibleCells.size(); i++) {
				(visibleCells.get(i)).update(delta);
			}

			firstScrollIndicator.gridPositionUpdate(scroller.getX(), window.getAbsX(), scroller.getY(), window.getAbsY(), gridDisplay.getGridWidth(), gridDisplay.getGridHeight(), window.getWidth(), window.getHeight());
			secondScrollIndicator.gridPositionUpdate(scroller.getX(), window.getAbsX(), scroller.getY(), window.getAbsY(), gridDisplay.getGridWidth(), gridDisplay.getGridHeight(), window.getWidth(), window.getHeight());
			
			updateCellCursorContainment(0, Gdx.input.getX(0), Gdx.input.getY(0));
		
			cursor.update(delta);
		}
	}
	
	public void draw() {
		if(initialized) {
			for(Cell cell : visibleCells) {
				cell.drawBottomLayer();
			}
			for(Cell cell : visibleCells) {
				cell.drawMiddleLayer();
			}
			for(Cell cell : visibleCells) {
				cell.drawTopLayer();
			}
			
			if(showTree) {
				setUpFloatCells();
				drawNodeConnections();
			}
			
			//if(scroller.getXDelta() == 0 && scroller.getYDelta() == 0 && hasPrimaryPointer)
			if(drawCursor)	
				cursor.draw();
				
			dragger.draw();
			inputHandler.getGridController().draw();
			//drawScrollIndicators();
		}
	}
	
	private void setUpFloatCells() {
		floatCells.clear();
		float height = 0;
		int cellCount = 0;
		float rightEdgeAccum = 0;
		for(Cell cell : visibleCells) {
			GNode node = cell.getNode();
			GNode parent = node.getParent();
			FloatCell floatC = getFloatCellForNode(parent);
			if(floatC == null) {
				floatC = new FloatCell(parent);
				floatC.yMin = cell.getY();
				floatC.yMax = cell.getY() + cell.getHeight();
				floatCells.add(floatC);
			} else {
				if(cell.getY() < floatC.yMin)
					floatC.yMin = cell.getY();
				if(cell.getY() + cell.getHeight() > floatC.yMax)
					floatC.yMax = cell.getY() + cell.getHeight();
			}
			
			if(cell.getY() + cell.getHeight() > height)
				height = cell.getY() + cell.getHeight();
			
			if(cell.getX() + cell.getWidth() > rightEdgeAccum)
			rightEdgeAccum = cell.getX() + cell.getWidth();
			cellCount++;
		}
		
		float middle = height/2;
		float right = rightEdgeAccum;
		for (FloatCell fCell : floatCells) {
			fCell.calcBounds(middle, right);
			fCell.draw();
		}
	}
	
	private void drawNodeConnections() {
		ShapeRenderer renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(GIDEApp.camera.combined);

		Color old = GIDEApp.SPRITE_BATCH.getColor();
		Color startColor = new Color(1, 0, 0, .75f);
		Color endColor = new Color(0, 0, 1, .75f);
		
		Gdx.gl.glLineWidth(2);
		for(Cell cell : visibleCells) {
			if(cell.isSelected() || showAllConnections) {
				GNode node = cell.getNode();
				FloatCell parent = getParentFloatCell(node);
				if(parent != null) {
					Random rand = new Random((int)parent.node.getText().length());
					rand.nextFloat();
					float yAdjust = rand.nextFloat();
					float x1 = cell.getAbsX() + cell.getWidth()/2 - 15;
					float y1 = cell.getAbsY() + cell.getHeight()/2;
					float x2 = parent.x + parent.width/2 + 15;
					float y2 = parent.y + parent.height/2 - 25 + 25*yAdjust;
					renderer.begin(ShapeType.Line);
					renderer.setColor(1, 1, 1, 1);
					renderer.line(x1, y1, x2, y2);
					renderer.end();
					GIDEApp.SPRITE_BATCH.setColor(startColor);
					Corpus.debugBox.draw(x1 - 5, y1 - 5, 10, 10);
					GIDEApp.SPRITE_BATCH.setColor(endColor);
					Corpus.debugBox.draw(x2 - 5, y2 - 5, 10, 10);
					GIDEApp.SPRITE_BATCH.flush();
				} else {
					//System.out.println("cell was null: " + parent);
				}
			}
		}
		for (FloatCell fCell : floatCells) {
			FloatCell parent = getParentFloatCell(fCell.node);
			if(parent != null) {
				Random rand = new Random((int)fCell.x);
				rand.nextFloat();
				float yAdjust = rand.nextFloat();
				renderer.begin(ShapeType.Line);
				renderer.setColor(1, 1, 1, 1);
				float x1 = fCell.x + fCell.width/2 - 15;
				float y1 = fCell.y + fCell.height/2;
				float x2 = parent.x + parent.width/2 + 15;
				float y2 = parent.y + parent.height/2 - 25 + 25*yAdjust;
				renderer.line(x1, y1, x2, y2);
				renderer.end();
				GIDEApp.SPRITE_BATCH.setColor(startColor);
				Corpus.debugBox.draw(x1 - 5, y1 - 5, 10, 10);
				GIDEApp.SPRITE_BATCH.setColor(endColor);
				Corpus.debugBox.draw(x2 - 5, y2 - 5, 10, 10);
				GIDEApp.SPRITE_BATCH.flush();
			} else {
				//System.out.println("cell was null: " + parent);
			}
		}
		
		GIDEApp.SPRITE_BATCH.setColor(old);
	}
	
	private FloatCell getParentFloatCell(GNode node) {
		for (FloatCell fCell : floatCells) {
			if(fCell.node == node.getParent())
				return fCell;
		}
		return null;
	}
	
	/**
	 * This function will return true for any cell atm -- needs a real implementation.
	 * @param cell
	 * @return
	 */
	private boolean isCellVisible(Cell cell) {
		boolean yIn = cell.getAbsY() + cell.getHeight() > window.getAbsY() && cell.getAbsY() < window.getAbsY() + window.getHeight() ||
					  window.getAbsY() + window.getHeight() > cell.getAbsY() && window.getAbsY() < cell.getAbsY() + cell.getHeight();
		boolean xIn = cell.getAbsX() + cell.getWidth() > window.getAbsX() && cell.getAbsX() < window.getAbsX() + window.getWidth() ||
					  window.getAbsX() + window.getWidth() > cell.getAbsX() && window.getAbsX() < cell.getAbsX() + cell.getWidth();
		
		return yIn && xIn;
	}
	
	private void drawScrollIndicators() {
			firstScrollIndicator.draw(window.getAbsX(), window.getAbsY(), window.getWidth(), window.getHeight());
			secondScrollIndicator.draw(window.getAbsX(), window.getAbsY(), window.getWidth(), window.getHeight());
	}
	
	public void updateCellCursorContainment(int pointerID, int cursorX, int cursorY) {
		for (int i = 0; i < visibleCells.size(); i++) {
			Cell cell = visibleCells.get(i);
			CellBehavior behavior = cell.getBehavior();
			if(!cell.pointInCellBounds(cursorX, cursorY, false)) {
				cell.setReadyForSelectionAction(true);
				if (behavior != null && behavior.cellContainsPointer(pointerID)) {
					behavior.rootPointerExited(pointerID);
				}
			}
		}

		for (int i = 0; i < visibleCells.size(); i++) {
			Cell cell = visibleCells.get(i);
			CellBehavior behavior = cell.getBehavior();
			if (behavior != null && cell.pointInCellBounds(cursorX, cursorY, false)
				&& !behavior.cellContainsPointer(pointerID)) {
				behavior.rootPointerEntered(pointerID);
				i = cells.size();
				break;
			}
		}
	}
	
	public boolean someCellSelected() {
		boolean selected = false;
		
		for(Cell cell : cells) {
			if (cell.isSelected()) {
				if(getVisibleCells().contains(cell)) {
					selected = true;
					break;
				} else
					cell.deselect();
			}
		}
		
		return selected;
	}
	
	public ArrayList<Cell> getVisibleCells() {
		return visibleCells;
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public Cell getCellAt(int row, int col) {
		if(!gridDisplay.isContentsSet())
			gridDisplay.setContents(cells);
		
		if(gridDisplay.getOrientation() == GridDisplay.HORIZONTAL) {
			ContentLine line = gridDisplay.getContentLine(col);
			return line.getNthCell(row);
		} else {
			ContentLine line = gridDisplay.getContentLine(row);
			return line.getNthCell(col);
		}
	}
	
	public int getCellRow(Cell cell) {
		if(!gridDisplay.isContentsSet())
			gridDisplay.setContents(cells);
		
		ContentLine line = gridDisplay.getLineWithCorpus(cell);
		if(gridDisplay.getOrientation() == GridDisplay.HORIZONTAL) {
			return line.getCellIndex(cell);
		} else {
			return gridDisplay.indexOf(line);
		}
	}
	
	public int getCellCol(Cell cell) {
		if(!gridDisplay.isContentsSet())
			gridDisplay.setContents(cells);
		
		ContentLine line = gridDisplay.getLineWithCorpus(cell);
		if(gridDisplay.getOrientation() == GridDisplay.VERTICAL) {
			return line.getCellIndex(cell);
		} else {
			return gridDisplay.indexOf(line);
		}
	}
	
	public boolean isInitalized() {
		return initialized;
	}
	
	public float getAbsX() {
		return window.getAbsInteriorX() + scroller.getX();
	}
	
	public float getAbsY() {
		return window.getAbsInteriorY() + scroller.getY();
	}
	
	public DefaultInputHandler getInputHandler() {
		return inputHandler;
	}
	
	private float calcCellWidth() {
		float width = (window.getInteriorWidth() - (colsPerScreen - 1)*gridDisplay.getGapSize()) / (float)colsPerScreen;
		if(width < 0)
			width = 0;
		return width;
	}
	
	private float calcCellHeight() {
		float height = (window.getInteriorHeight() - (rowsPerScreen - 1)*gridDisplay.getGapSize()) /(float)rowsPerScreen;
		if(height < 0)
			height = 0;
		return height;
	}
	
	public Window getWindow() {
		return window;
	}
	
	public void setWindow(Window window) {
		this.window = window;
	}
	
	protected void setRowsPerScreen(int rowsPerScreen) {
		this.rowsPerScreen = rowsPerScreen;
	}
	
	protected void setColsPerScreen(int colsPerScreen) {
		this.colsPerScreen = colsPerScreen;
	}
	
	public boolean isHasPrimaryPointer() {
		return hasPrimaryPointer;
	}

	public void setHasPrimaryPointer(boolean hasPrimaryPointer) {
		if(this.hasPrimaryPointer && !hasPrimaryPointer) {
			this.deselectAll();
		}
		
		this.hasPrimaryPointer = hasPrimaryPointer;
	}
	
	private void setScrollIndicatorOrientation(int orientation) {
		
		if(orientation == GridDisplay.HORIZONTAL) {
			firstScrollIndicator = new ScrollIndicator(ScrollIndicator.LEFT);
			secondScrollIndicator = new ScrollIndicator(ScrollIndicator.RIGHT);
		} else {
			firstScrollIndicator = new ScrollIndicator(ScrollIndicator.UP);
			secondScrollIndicator = new ScrollIndicator(ScrollIndicator.DOWN);
		}
	}
	
	public void updateCellSizes() {
		for(Cell cell : cells) {
			cell.resize(calcCellWidth(), calcCellHeight());
		}
	}

	public CellConfig getCellConfig() {
		return cellConfig;
	}

	public void setCellConfig(CellConfig cellConfig) {
		this.cellConfig = cellConfig; //probably need to recreate all the cells when this happens (see if they are using a different cellconfig)
	}
	
	public GridDisplay getDisplay() {
		return gridDisplay;
	}
	
	public int getRowsPerScreen() {
		return rowsPerScreen;
	}
	
	public int getColsPerScreen() {
		return colsPerScreen;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	protected ArrayList<GNode> getInitialNodes() {
		return initialNodes;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public Scroller getScroller() {
		return scroller;
	}
	
	public Cell getCellForNode(GNode node) {
		Cell cell = null;
		for(Cell cCell : cells) {
			//System.out.println("cell: " + cCell.getNode());
			if(cCell.getNode() == node) {
				cell = cCell;
				break;
			}
		}
		
		if(cell == null) {
			/*System.out.println("node: \"" + node + "\" has no corresponding cell. Children: ");
			for (int i = 0; i < node.getChildCount(); i++) {
				System.out.print(" " + node.getChild(i));
			}
			System.out.println();
			*/
		}
		
		return cell;
	}

	public Selector getSelector() {
		return inputHandler.getSelector();
	}

	public LinkedStructureManager getStructureManager() {
		return structure;
	}

	public void setStructureManager(LinkedStructureManager structure) {
		this.structure = structure;
	}
	
	public void setGridDisplay(GridDisplay display) {
		this.gridDisplay = display;
	}

	public GridController getController() {
		return controller;
	}

	protected void setController(GridController controller) {
		this.controller = controller;
	}

	public CellDragger getDragger() {
		return dragger;
	}

	public void setDragger(CellDragger dragger) {
		this.dragger = dragger;
	}

	public boolean isMultiSelectEnabled() {
		return multiSelectEnabled;
	}

	public void setMultiSelectEnabled(boolean multiSelectEnabled) {
		this.multiSelectEnabled = multiSelectEnabled;
	}

	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}

	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	public boolean isDraggingEnabled() {
		return draggingEnabled;
	}

	public void setDraggingEnabled(boolean draggingEnabled) {
		this.draggingEnabled = draggingEnabled;
	}
	
	public Cell getSelectedCell() {
		return getSelector().getLastSelected();
	}

	public int getOrientationSource() {
		return orientationSource;
	}

	public void setOrientationSource(int orientationSource) {
		this.orientationSource = orientationSource;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
		gridDisplay.setOrientation(orientation);
	}
	
	public boolean isSelectionEnabled() {
		return !getSelector().isSelectionDisabled();
	}
	
	public void setSelectionEnabled(boolean enabled) {
		getSelector().setSelectionDisabled(!enabled);
	}

	public boolean isAddingInitialNodes() {
		return addingInitialNodes;
	}
	
	public boolean isSelectFirstCell() {
		return selectFirstCell;
	}
	
	public void setSelectFirstCell(boolean selectFirst) {
		this.selectFirstCell = selectFirst;
	}

	public boolean isDrawCursor() {
		return drawCursor;
	}

	public void setDrawCursor(boolean drawCursor) {
		this.drawCursor = drawCursor;
	}
	
	private FloatCell getFloatCellForNode(GNode node) {
		for (FloatCell floatC : floatCells) {
			if(floatC.node == node)
				return floatC;
		}
		
		return null;
	}
	
	private class FloatCell {
		GNode node;
		float x;
		float y;
		float yMin;
		float yMax;
		float width;
		float height;
		private GLString string;
		
		private FloatCell(GNode node) {
			this.node = node;
			string = new GLString(node.getText(), ResourceManager.getTypeFace("droid"),
					TypeFace.STYLE_PLAIN, 14,
					new Color(0, 1f, .2f, 1), false);
		}
		
		private void draw() {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			ArrayList<Cell> selected = getSelector().getSelectedCells();
			boolean brighten = false;
			for (Cell cell : selected) {
				if(cell.getNode().getParent() == node)
					brighten = true;
			}
			float amount = .25f;
			if(brighten)
				amount = .4f;
			GIDEApp.SPRITE_BATCH.setColor(amount, amount, amount, 0.8f);
			Corpus.debugBox.draw(x, y, width, height);
			GIDEApp.SPRITE_BATCH.setColor(old);

			Random rand = new Random((int)x);
			int count = (int)Math.ceil(height/string.getTextHeight()/2);
			if(height < string.getTextHeight() || count > 100)
				count = 1;
			for (int i = 0; i < count; i++) {
				rand.nextFloat();
				string.draw(x + width/2 - string.getTextWidth()/2, y + rand.nextFloat()*(height-string.getTextHeight()));
			}
		}
		
		private void calcBounds(float docCenter, float docRightEdge) {
			height = yMax - yMin;
			y = yMin + window.getAbsInteriorY();
			width = string.getTextWidth() + 10;
			
			float ratio = (Math.abs((y+height) - (docCenter))/(docCenter));
			//System.out.println("ratio: " + ratio);
			x = docRightEdge + (Gdx.graphics.getWidth() - docRightEdge)*ratio - width;
			if(Gdx.graphics.getWidth() - x < width)
				x = Gdx.graphics.getWidth() - width;
			//System.out.println("x: " + x + ", y: " + ", width: " + width + ", height: " + height);
			y += scroller.getY();
		}
	}
}