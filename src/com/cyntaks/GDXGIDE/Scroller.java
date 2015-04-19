package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.code.CodeCell;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.sgf.event.Event;
import com.cyntaks.sgf.event.EventListener;

/**
 * Used to carry out scrolling behavior in Grids. The bulk of the code is related to a necessary optimization
 * which causes Cells to be formed only out of visible nodes and a buffer above and below the Window containing
 * the Grid.
 * 
 * buginfo: There's a bug where a dragged selected at the bottom of the document won't cause surrounding cells
 * 			to clear out of the way -- they just sit! Don't have an idea of what's causing it yet, but it seems
 * 			to have shown up once I started using this class for scrolling.
 * 
 * 			Another bug is that if a Cell is selected then gets pruned from being off-screen, when it is
 * 			brought back on screen a new Cell is constructed, so the fact that it was selected before is lost.
 * 			I think a good solution for this will involve checking a node which is going to be added against
 * 			the nodes that Selector has has currently selected. Will need to remove the old Cell and stick in the
 * 			new one once it is formed. May be that Grid.add(...) and CodeGrid.add(...) are the best places for this.
 * 
 * 			I noticed that on the "line" level of abstraction when loading in that test source file with nothing
 * 			but single line variable declarations "int x;" that after a certain point it stopped placing them on
 * 			new lines, but put them all on one line instead. There seems to be an issue with curly braces not being
 * 			moved onto new lines on the "line" abstraction -- might see if the two are related
 * 
 * 
 * 			The main bug you are working on with scrolling upward is definitely more pronounced toward the top of
 * 			the document; should investigate why that is. One possibility is that lots of tiles are being added that
 * 			shouldn't be (check the printlns with timestamps). It may have something to do with competition between
 * 			the add and remove processes, or just the add process being starved but striving anyhow. The rapid regeneration
 * 			of cells in certan conditions (i.e. random colors flashing on the strings which are assigned random colors
 * 			each time they're generated) could be related also.
 * @author weston
 *
 */

public class Scroller extends ManualCorpus {

	private Grid owner;
	private int bufferHeight;
	private int cellScrollIncrement = 1; //unity is the only number that works atm 
										 //(controls how many cells are added or removed at a time in the pruning and min-size enforcing methods)
	private static ArrayList<GNode> scratch = new ArrayList<GNode>();
	private Cell followCell;

	public static final float DEFAULT_CAM_X_VOL = 10;
	public static final float DEFAULT_CAM_Y_VOL = 10;
	
	private float xTrackSpeed = DEFAULT_CAM_X_VOL;
	private float yTrackSpeed = DEFAULT_CAM_Y_VOL;
	
	private boolean teleport;
	private static final int TELEPORT_EVENT = 898989;
	
	public static final int MODE_CENTER = 0;
	public static final int MODE_KEEP_ON_SCREEN = 1;
	public static final int MODE_TEXT_EDITOR = 2;
	private int trackMode = MODE_KEEP_ON_SCREEN;
	
	public Scroller(Grid grid) {
		this.owner = grid;
	}
	
	boolean called = false;
	public void update(float delta) {
		super.update(delta);
		
		if(followCell != null) {
			trackCell(followCell, getXTrackSpeed(), getYTrackSpeed());
		}
		
		if(owner.isLoaded() && !called) {
			//ArrayList<GNode> allNodes = owner.getInputHandler().getTreeManager().getDisplayNodes();
			//owner.addCells(allNodes, null);
			
			//executeScrollingOptimization(delta);
			called = true;
		}	
	}
	
	/**
	 * The idea of the algorithm is that only Nodes which would result in visible Cells are added to the Grid, as
	 * well as some Cells/Nodes which appear in buffers above and below the Window. Every frame the above buffer
	 * is pruned by pruneBeginning(...) and the below buffer is pruned by pruneEnd(...); if there are not enough
	 * Cells present in the Grid to give it a height of Window.getHeight()+bufferHeight*2, then additional Cells
	 * are added until the height requirement is met; this is how the Grid is initially filled too.
	 * 
	 * @param delta
	 */
	private void executeScrollingOptimization(float delta) {
		Window window = owner.getWindow();
		GridDisplay display = owner.getDisplay();
		this.bufferHeight = (int)(window.getHeight()/2)*10000;
		boolean addToEnd = true;

		//pruneBeginning(delta, window, display);
		//if(pruneEnd(delta, window, display))
			//addToEnd = false;
		
		//ensureMinimumGridSize(delta, window, display, addToEnd);
		//ensureMinimumGridSize(delta, window, display, !addToEnd); //will add cells twice if you do this; need something in between the two calls
	}
	
	private void ensureMinimumGridSize(float delta, Window window, GridDisplay display, boolean addToEnd) {
		boolean allClear = true;
		
		display.setContents(owner.getCells());
		display.update(delta, 0, 0, window.getWidth(), window.getHeight());
		while((display.getGridHeight() < window.getHeight() + bufferHeight*2 ||
			   owner.getCells().size() < 2) && allClear) {
			allClear = addSomeCells(addToEnd);
			owner.updateDisplay();
		}
	}
	
	private boolean pruneBeginning(float delta, Window window, GridDisplay display) {
		boolean neededPruning = false;
		
		display.setContents(owner.getCells());
		display.update(delta, 0, 0, window.getWidth(), window.getHeight());
		int displayHeight = display.getGridHeight();
		if(getYVol() < 0 && getY() <= -bufferHeight && !owner.getCells().isEmpty()) {
			Cell topCell = owner.getCells().get(0);
			if(topCell.getAbsY() + topCell.getHeight() < window.getAbsY()) {
				float diff = 0;
				boolean cellsRemaining = true;
				do {
					cellsRemaining = removeSomeCells(false);
					display.setContents(owner.getCells());
					display.update(delta, 0, 0, window.getWidth(), window.getHeight());
					diff = displayHeight - display.getGridHeight();
				} while (diff < ((-bufferHeight) - getY()) && cellsRemaining);

				setY(getY() + diff);
				neededPruning = true;
			}
		}
		
		return neededPruning;
	}
	
	private boolean pruneEnd(float delta, Window window, GridDisplay display) {
		boolean neededPruning = false;

		display.setContents(owner.getCells());
		display.update(delta, 0, 0, window.getWidth(), window.getHeight());
		int displayHeight = display.getGridHeight();
		if(getYVol() > 0 && getY() >= 0 && !owner.getCells().isEmpty()) {
			Cell bottomCell = owner.getCells().get(owner.getCells().size() - 1);
			if(bottomCell.getAbsY() > window.getAbsY()+window.getHeight()) {
				float diff = 0;
				boolean cellsRemaining = true;
				do {
					cellsRemaining = removeSomeCells(true);
					display.setContents(owner.getCells());
					display.update(delta, 0, 0, window.getWidth(), window.getHeight());
					diff = Math.abs(displayHeight - display.getGridHeight());
				} while (diff < (getY()) && cellsRemaining);
				
				neededPruning = true;
			}
		}
		
		return neededPruning;
	}
	
	/**
	 * Used by the pruning methods to take cells out of the ends of the buffers.
	 * 
	 * @param fromEnd Whether to remove the cells from the end or beginning of the present cells
	 * @return
	 */
	private boolean removeSomeCells(boolean fromEnd) {
		boolean success = false;
		
		if(!owner.getCells().isEmpty()) {
			scratch.clear();
			int toRemoveCount = owner.getCells().size()-1 < cellScrollIncrement ? owner.getCells().size()-1 : cellScrollIncrement;
			for(int i = 0; i < toRemoveCount; i++) {
				int index = i;
				if(fromEnd)
					index = owner.getCells().size()-1 - i;
				
				scratch.add(owner.getCells().get(index).getNode());
				success = true;
			}
			if(success) {
				owner.getDisplay().skipAnimateNextFrame();
				owner.removeCells(scratch);
			}
		}
		
		return success;
	}
	
	/**
	 * Used by ensureMinimumGridSize(...) to trickle in cells when there is space for them in the visible
	 * area or in either of the buffers.
	 * 
	 * @param toEnd Whether to append the new cells to the end or beginning of the present cells.
	 * @return
	 */
	private boolean addSomeCells(boolean toEnd) {
		scratch.clear();
		
		boolean success = false;
		
		ArrayList<GNode> allNodes = ((AbstractionManager)owner.getStructureManager()).getDisplayNodes();
		if(toEnd) { 
			GNode endNode = owner.getCells().isEmpty() ? null : owner.getCells().get(owner.getCells().size() - 1).getNode();
			int endIndex = allNodes.indexOf(endNode);
			int availableNodes = (allNodes.size() - endIndex) - 1;
			int toAddCount = cellScrollIncrement < availableNodes ? cellScrollIncrement : availableNodes;
			
			for(int i = 0; i < toAddCount; i++) {
				scratch.add(allNodes.get(endIndex + 1 + i));
				success = true;
			}
			if(success) {
				owner.getDisplay().skipAnimateNextFrame();
				owner.addCells(scratch, endNode);
			}
			
		} else {
			GNode startNode = owner.getCells().isEmpty()? null : owner.getCells().get(0).getNode();
			int startIndex = allNodes.indexOf(startNode);
			int availableNodes = startIndex;
			int toAddCount = cellScrollIncrement < availableNodes ? cellScrollIncrement : availableNodes;
		
			if(!owner.getCells().isEmpty() && owner.getCells().get(owner.getCells().size()-1).getNode() == allNodes.get(allNodes.size()-1) &&
			   getYVol() < 0)
				toAddCount = 0;
			
			for(int i = 0; i < toAddCount; i++) {
				scratch.add(allNodes.get(startIndex - 1 - i));
				success = true;
			}
			if(success) {
				owner.getDisplay().skipAnimateNextFrame();
				owner.addCells(scratch, null);
				
				GridDisplay display = owner.getDisplay();
				display.setContents(owner.getCells());
				display.update(0, 0, 0, owner.getWindow().getWidth(), owner.getWindow().getHeight());
				float addedHeight = 0;
				
				for(int i = 0; i < toAddCount; i++) {
					addedHeight += owner.getCells().get(i).getHeight();
				}
				System.out.println(addedHeight + " " + System.currentTimeMillis());
				setY(getY() - addedHeight);
			}
		}
		
		return success;
	}
	
	private void moveToCellY(Cell cell, float speed) {
		float yDest = -cell.getY();
		if(yDest > 0)
			yDest = 0;
		
		if(teleport) {
			super.killYEvents();
			this.setY(yDest);
		}
		else if(this.getDestY() != yDest) {
			super.killYEvents();
			this.setDestY(yDest, speed, true);
		}
	}
	
	private void moveToCellX(Cell cell, float speed, float offset) {
		float xDest = -cell.getX() - offset;
		if(xDest > 0)
			xDest = 0;
		
		if(teleport) {
			super.killXEvents();
			this.setX(xDest);
		}
		else if(this.getDestX() != xDest) {
			super.killXEvents();
			this.setDestX(xDest, speed, true);
		}
	}
	
	private void moveFarEdgeToCellX(Cell oCell, float speed) {
		CodeCell cell = (CodeCell)oCell;
		float windowWidth = owner.getWindow().getWidth();
		float offset = -windowWidth + cell.getWidth() + windowWidth/5f;
		CodeCell next = (CodeCell)owner.getSelector().getCellAfter(cell);
		if(next != null && !cell.lineBetween(next))
			offset += next.getWidth();
		if(offset > 0)
			offset = 0;
		
		moveToCellX(cell, speed, offset);
	}
	
	private void updateOffset(Cell cell, float xSpeed, float ySpeed) {
		float windowWidth = owner.getWindow().getInteriorWidth();
		float windowHeight = owner.getWindow().getInteriorHeight();
		float xDest = 0;
		float yDest = 0;
		boolean lockLeftEdge = false;
		boolean lockRightEdge = false;
		boolean lockTopEdge = false;
		boolean lockBottomEdge = false;
		if(trackMode == MODE_CENTER) {
			xDest = getCenterOnCellXDest(cell);
			yDest = getCenterOnCellYDest(cell);
		} else if(trackMode == MODE_KEEP_ON_SCREEN) {
			xDest = getCenterOnCellXDest(cell);
			yDest = getCenterOnCellYDest(cell);
			lockLeftEdge = true;
			lockRightEdge = true;
			lockTopEdge = true;
			lockBottomEdge = true;
		} else if(trackMode == MODE_TEXT_EDITOR) {
			xDest = getCenterOnCellXDest(cell);
			yDest = getCenterOnCellYDest(cell);
			
			xDest = getLargeCellAdjustedX(cell, xDest);
			yDest = getLargeCellAdjustedY(cell, yDest);
			lockLeftEdge = true;
			lockTopEdge = true;
		}
		
		if(lockLeftEdge) {
			if(xDest > 0 && cell.getX() >= 0)
				xDest = 0;
		}
		if(lockRightEdge) {
			if(xDest < 0 && cell.getX()+cell.getWidth() <= windowWidth)
				xDest = 0;
			else if(xDest < 0 && cell.getX()+cell.getWidth() > windowWidth)
				xDest = -(cell.getX()+cell.getWidth() - windowWidth);
		}
		
		if(lockTopEdge) {
			if(yDest > 0 && cell.getY() >= 0)
				yDest = 0;
		}
		if(lockBottomEdge) {
			if(yDest < 0 && cell.getY()+cell.getHeight() <= windowHeight)
				yDest = 0;
			else if(yDest < 0 && cell.getY()+cell.getHeight() > windowHeight)
				yDest = -(cell.getY()+cell.getHeight() - windowHeight);
		}
		
		if(teleport) {
			super.killXEvents();
			this.setX(xDest);
			super.killYEvents();
			this.setY(yDest);
		}
		else {
			if(this.getDestX() != xDest) {
				super.killXEvents();
				this.setDestX(xDest, xSpeed, true);
			}
			if(this.getDestY() != yDest) {
				super.killYEvents();
				this.setDestY(yDest, ySpeed, true);
			}
		}
	}
	
	private float getCenterOnCellYDest(Cell cell) {
		float windowHeight = owner.getWindow().getInteriorHeight();
		return -cell.getY() + windowHeight/2 -cell.getHeight()/2;
	}
	
	private float getLargeCellAdjustedY(Cell cell, float currentYDest) {
		float cellHeightAdjustment = 0;
		float windowHeight = owner.getWindow().getInteriorHeight();
		boolean adjusting = false;
		if(cell.getHeight() > windowHeight/2) {
			cellHeightAdjustment = windowHeight/2 - cell.getHeight();
			adjusting = true;
		}
		if(adjusting && cellHeightAdjustment < -windowHeight/2)
			cellHeightAdjustment = -windowHeight/2;
		
		return currentYDest + cell.getHeight()/2 + cellHeightAdjustment;
	}
	
	private float getLargeCellAdjustedX(Cell cell, float currentXDest) {
		float cellWidthAdjustment = 0;
		float windowWidth = owner.getWindow().getInteriorWidth();
		
		boolean adjusting = false;
		if(cell.getWidth() > windowWidth/2) {
			cellWidthAdjustment = windowWidth/2 - cell.getWidth();
			adjusting = true;
		}
		if(adjusting && cellWidthAdjustment < -windowWidth/2)
			cellWidthAdjustment = -windowWidth/2;
		
		return currentXDest + cell.getWidth()/2 + cellWidthAdjustment; 
	}
	
	private float getCenterOnCellXDest(Cell cell) {
		float windowWidth = owner.getWindow().getInteriorWidth();
		return  -cell.getX() + windowWidth/2 -cell.getWidth()/2;
	}
	
	public void trackCell(Cell cell) {
		trackCell(cell, xTrackSpeed, yTrackSpeed);
	}
	
	public void trackCell(Cell cell, float xSpeed, float ySpeed) {
		if(cell != null) {
			updateOffset(cell, xSpeed, ySpeed);
		}
		followCell = cell;
	}
	
	public void moveToBeginning() {
		float x = 0;
		float y = 0;
		
		if(owner.getDisplay().getOrientation() == GridDisplay.VERTICAL)
			y = getTopLimit();
		if(owner.getDisplay().getOrientation() == GridDisplay.HORIZONTAL)
			x = getLeftLimit();
		
		//updateGridPosition(x, y, false);
	}
	
	public void moveToEnd() {
		float x = 0;
		float y = 0;
		
		if(owner.getDisplay().getOrientation() == GridDisplay.VERTICAL)
			y = getBottomLimit();
		if(owner.getDisplay().getOrientation() == GridDisplay.HORIZONTAL)
			x = getRightLimit();
		
		//updateGridPosition(x, y, false);
	}
	
	public float getRightLimit() {
		return -(owner.getDisplay().getGridWidth() - owner.getWindow().getWidth());
	}
	
	public  float getLeftLimit() {
		return 0;
	}
	
	public float getBottomLimit() {
		return -(owner.getDisplay().getGridHeight() - owner.getWindow().getHeight());
	}
	
	public float getTopLimit() {
		return 0;
	}
	
	public boolean isScrolling() {
		return getXVol() != 0 || getYVol() != 0;
	}
	
	/*public boolean updateGridPosition(float x, float y, boolean enforceLimits) {
		boolean atEdge = false;
		
		
		if(enforceLimits) { //enforceLimits is hack due to the fact that the limits are in flux when a grid is first loading and there's no
							//simple way to tell when the fluxuation period is over. So if you want to do something like set a scroll position
							//as you're loading in a new grid, chances are it will be rejected by some wonky bounds calculation. Look at the call
							//to this function inside of update(delta)...
			float leftLimit = getLeftLimit();
			float rightLimit = getRightLimit();
			
			if(x < rightLimit) {
				x = rightLimit;
				atEdge = true;
			}
			if(x > leftLimit) {
				x = leftLimit;
				atEdge = true;
			}
			
			float bottomLimit = getBottomLimit();
			float topLimit = getTopLimit();
			if(y < bottomLimit) {
				y = bottomLimit;
				atEdge = true;
			}
			if(y > topLimit) {
				y = topLimit;
				atEdge = true;
			}
		}

		gridXDelta = x - lastScrollX;
		gridYDelta = y - lastScrollY;
		scrollX = x;
		scrollY = y;

		lastScrollX = scrollX;
		lastScrollY = scrollY;
		
		//this must be done after lastScrollX/Y are set, otherwise the disparity will cause issues elsewhere
		scrollX = (int)scrollX;
		scrollY = (int)scrollY;
		
		return atEdge;
	}*/
	
	public Cell getFollowCell() {
		return followCell;
	}
	
	public void stopFollowingCell() {
		followCell = null;
	}

	public float getXTrackSpeed() {
		return xTrackSpeed;
	}

	public void setXTrackSpeed(float camXVol) {
		this.xTrackSpeed = camXVol;
	}

	public float getYTrackSpeed() {
		return yTrackSpeed;
	}

	public void setYTrackSpeed(float camYVol) {
		this.yTrackSpeed = camYVol;
	}
	
	public void teleportNextTime() {
		teleport = true;
		
		new Event(TELEPORT_EVENT, 0, 100, 100, false, new EventListener() {

			@Override
			public void update(int id, int type, float value) {
			}

			@Override
			public void finish(int id, int type) {
				teleport = false;
			}
			
		});
	}
	
	public void setTrackMode(int mode) {
		this.trackMode = mode;
	}
	
	public int getTrackMode() {
		return this.trackMode;
	}
}
