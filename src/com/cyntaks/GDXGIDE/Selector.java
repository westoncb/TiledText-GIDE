package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.cyntaks.GDXGIDE.gui.ContentLine;
import com.cyntaks.GDXGIDE.gui.GridDisplay;

public class Selector {
	private ArrayList<Cell> selected;
	private Cell lastSelected;
	private SelectController controller;
	private Grid owner;
	private boolean multiSelectInProgress;
	private boolean acceptingNewSelections;
	private boolean selectModifierPressed;
	private boolean selectionDisabled;
	
	public Selector(Grid owner) {
		this.owner = owner;
		controller = new CyclingController();
		selected = new ArrayList<Cell>();
	}
	
	public void addToSelection(Cell cell) {
		if(!cell.isDragging() && (selected.isEmpty() || owner.isMultiSelectEnabled()) && !selectionDisabled) {
			lastSelected = cell;
			owner.getScroller().trackCell(cell);
			if(!selected.contains(cell)) {
				if(this.selectModifierPressed)
					owner.getController().getListener().auxCellSelected(cell);
				
				selected.add(cell);
				cell.select();
			}
		}
	}
	
	public void removeFromSelection(Cell cell) {
		if(selected.contains(cell)) {
			int index = selected.indexOf(cell);
			Cell cellBefore = null;
			selected.remove(cell);
			if(!selected.isEmpty()) {
				if(index > selected.size()-1)
					index = selected.size()-1;
				cellBefore = selected.get(index);
			}
			if(owner.getScroller().getFollowCell() == cell)
				owner.getScroller().stopFollowingCell();
			
			cell.deselect();
			
			if(getLastSelected() == cell)
				setLastSelected(null);
			if(!selected.isEmpty()) {
				setLastSelected(cellBefore);
			}
		}
	}
	
	public void clearSelection() {
		controller.clearSelection();
	}
	
	/**
	 * We want the selected cells stored in the order in which they appear in the grid, not the order in which they
	 * were selected.
	 */
	public void sortSelectedCells() {
		for(int count = 0; count < selected.size(); count++) {
			Cell top = null;
			for(int i = count; i < selected.size(); i++) {
				Cell cell = selected.get(i);
				if(top == null || owner.getCells().indexOf(cell) < owner.getCells().indexOf(top)) {
					top = cell;
				}
			}
			
			int topIndex = selected.indexOf(top);
			if(topIndex > count) {
				Cell temp = selected.get(count);
				selected.set(count, top);
				selected.set(topIndex, temp);
			}
		}
	}
	
	public ArrayList<Cell> getSelectedCells() {
		orderCellsByGridPosition(selected);
		return selected;
	}
	
	private void orderCellsByGridPosition(ArrayList<Cell> cells) {
		Collections.sort(cells);
	}
	
	public Cell getLastSelected() {
		return lastSelected;
	}

	public void setController(SelectController controller) {
		this.controller = controller;
	}
	
	public SelectController getController() {
		return controller;
	}
	
	public Cell getCellBefore(Cell cell) {
		if(cell != null) {
			ArrayList<Cell> ownerCells = cell.getOwner().getCells();
			int selectIndex = ownerCells.indexOf(cell);
			int beforeIndex = 0;
			if(selectIndex > 0)
				beforeIndex = selectIndex - 1;
			else
				return null;
			
			return ownerCells.get(beforeIndex);
		} else
			return null;
	}
	
	public Cell getCellAfter(Cell cell) {
		if(cell !=  null) {
			ArrayList<Cell> ownerCells = cell.getOwner().getCells();
			int selectIndex = ownerCells.indexOf(cell);
			int afterIndex = 0;
			if(selectIndex < ownerCells.size()-1)
				afterIndex = selectIndex + 1;
			else
				return null;
			
			return ownerCells.get(afterIndex);
		} else
			return null;
	}
	
	public boolean isCellOnLeftEdge(Cell cell) {
		return cell == null || getCellLeftOf(cell) == null;
	}
	
	public boolean isCellOnRightEdge(Cell cell) {
		return cell == null || getCellRightOf(cell) == null;
	}
	
	public boolean isCellOnTopEdge(Cell cell) {
		return cell == null || getCellAbove(cell) == null;
	}
	
	public boolean isCellOnBottomEdge(Cell cell) {
		return cell == null || getCellBelow(cell) == null;
	}
	
	public Cell getCellLeftOf(Cell cell) {
		GridDisplay display = cell.getOwner().getDisplay();
		ContentLine line = display.getLineWithCorpus(cell);
		if(line != null) { //If the first frame hasn't rendered yet, no cells will have been loaded into content lines and this will be null
			if(display.getOrientation() == GridDisplay.HORIZONTAL) {
				return line.getCellBefore(cell);
			} else {
				int yIndex = line.getCellIndex(cell); //have to use a special method because spacer cells may be in the ContentLine
				ContentLine lineLeft = display.getLineBefore(line);
				if(lineLeft != null && lineLeft.size() > yIndex) {
					return lineLeft.getNthCell(yIndex);
				}
			}
		}
		
		return null;
	}
	
	public Cell getCellBelow(Cell cell) {
		GridDisplay display = cell.getOwner().getDisplay();
		ContentLine line = display.getLineWithCorpus(cell);
		if(line != null) { //If the first frame hasn't rendered yet, no cells will have been loaded into content lines and this will be null
			if(display.getOrientation() == GridDisplay.VERTICAL) {
				return line.getCellAfter(cell);
			} else {
				int xIndex = line.getCellIndex(cell); //have to use a special method because spacer cells may be in the ContentLine
				ContentLine lineBelow = display.getLineAfter(line);
				if(lineBelow != null && lineBelow.size() > xIndex)
					return lineBelow.getNthCell(xIndex);
			}
		}
		
		return null;
	}
	
	public Cell getCellAbove(Cell cell) {
		GridDisplay display = cell.getOwner().getDisplay();
		ContentLine line = display.getLineWithCorpus(cell);
		if(line != null) { //If the first frame hasn't rendered yet, no cells will have been loaded into content lines and this will be null
			if(display.getOrientation() == GridDisplay.VERTICAL) {
				return line.getCellBefore(cell);
			} else {
				int xIndex = line.getCellIndex(cell); //have to use a special method because spacer cells may be in the ContentLine
				ContentLine lineAbove = display.getLineBefore(line);
				if(lineAbove != null && lineAbove.size() > xIndex)
					return lineAbove.getNthCell(xIndex);
			}
		}
		
		return null;
	}
	
	public Cell getCellRightOf(Cell cell) {
		GridDisplay display = cell.getOwner().getDisplay();
		ContentLine line = display.getLineWithCorpus(cell);
		if(line != null) { //If the first frame hasn't rendered yet, no cells will have been loaded into content lines and this will be null
			if(display.getOrientation() == GridDisplay.HORIZONTAL) {
				return line.getCellAfter(cell);
			} else {
				int yIndex = line.getCellIndex(cell); //have to use a special method because spacer cells may be in the ContentLine
				ContentLine lineRight = display.getLineAfter(line);
				if(lineRight != null && lineRight.size() > yIndex)
					return lineRight.getNthCell(yIndex);
			}
		}
		
		return null;
	}
	
	public Cell getCellBeforeSelected() {
		return getCellBefore(getLastSelected());
	}
	
	public Cell getCellAfterSelected() {
		return getCellAfter(getLastSelected());
	}
	
	protected void initializeMultiSelect() {
		multiSelectInProgress = true;
		acceptingNewSelections = true;
	}
	
	public boolean isMultiSelectInProgress() {
		return multiSelectInProgress;
	}
	
	public boolean isAcceptingNewSelections() {
		return acceptingNewSelections;
	}
	
	protected Grid getOwner() {
		return owner;
	}
	
	protected void setLastSelected(Cell last) {
		this.lastSelected = last;
	}
	
	public boolean isSelectModifierPressed() {
		return selectModifierPressed;
	}
	
	public boolean isSelectionDisabled() {
		return selectionDisabled;
	}

	public void setSelectionDisabled(boolean selectionDisabled) {
		this.selectionDisabled = selectionDisabled;
	}

	public class DefaultController implements SelectController {

		public boolean endMultiSelect() {
			multiSelectInProgress = false;
			acceptingNewSelections = false;
			
			//clearSelection();
			
			return false;
		}
		
		public void clearSelection() {
			ArrayList<Cell> temp = new ArrayList<Cell>();
			for(Cell cell : selected)
				temp.add(cell);
			
			for(Cell cell : temp) {
				removeFromSelection(cell);
				cell.setReadyForSelectionAction(true);
			}
		}
		
		public void pointerMoved(float x, float y) {
			for(Cell cell : owner.getCells()) {
				if(cell.isReadyForSelectionAction()) {
					if(cell.getBehavior().cellContainsPointer(InputAbstractor.PRIMARY_POINTER)) {
						if((selected.isEmpty() || acceptingNewSelections) && !selected.contains(cell)) {
							addToSelection(cell);
						} else if(selected.contains(cell) && multiSelectInProgress && acceptingNewSelections) {
							removeFromSelection(cell);
							owner.getController().getListener().auxCellDeselected(cell);
							if(selected.isEmpty()) {
								endMultiSelect();
							}
						}
					} else if(selected.contains(cell)) { //cell is selected but does not have the primary pointer
						if(!multiSelectInProgress) {
							removeFromSelection(cell);
							owner.getController().getListener().auxCellDeselected(cell);
						}
					}
				}
			}
		}
		
		public boolean selectButtonPressed() {
			if(!selectModifierPressed && multiSelectInProgress) {
				ArrayList<Cell> temp = new ArrayList<Cell>();
				for(Cell cell : selected)
					temp.add(cell);
				
				endMultiSelect();
				
				for (Cell cell : temp) {
					owner.getController().getListener().auxCellDeselected(cell);
				}
				
				return false;
			}
			
			selectModifierPressed = true;
			
			multiSelectInProgress = true;
			acceptingNewSelections = true;
			
			if(getLastSelected() != null)
				owner.getController().getListener().auxCellSelected(getLastSelected());
			
			return true;
		}

		public boolean selectButtonReleased() {
			if(acceptingNewSelections) //don't want this to happen if the rease is from a selection-cancelling action
				owner.getController().getListener().auxSelectionSet(new ArrayList<Cell>(selected));
			
			selectModifierPressed = false;
			acceptingNewSelections = false;
			for(Cell cell : selected) {
				if(cell.getBehavior() != null && cell.getBehavior().cellContainsPointer(InputAbstractor.PRIMARY_POINTER))
					cell.setReadyForSelectionAction(true);
			}
			
			return true;
		}

		public void update(float delta) {
			
			
		}
		
	}
	
	public class CyclingController extends DefaultController {
		private float lastX;
		private float lastY;
		private float pointerDx;
		private float pointerDy;
		
		//private static final float MANUAL_VOL_SCALE = 1.5f;
		
		private static final int LEFT = 0;
		private static final int RIGHT = 1;
		private static final int UP = 2;
		private static final int DOWN = 3;
		private static final int NEXT = 4;
		private static final int PREVIOUS = 5;
		
		private long lastTime;
		
		private boolean directionIsForward = true; //whether we are cycling forward or backward
		
		private boolean atFirstSlot;
		private float customVolScale = 1f;
		private CycleMotionTracker xTracker = new CycleMotionTracker(CycleMotionTracker.X_AXIS);
		private CycleMotionTracker yTracker = new CycleMotionTracker(CycleMotionTracker.Y_AXIS);
		private CycleMotionTracker sequenceTracker = new CycleMotionTracker(CycleMotionTracker.SEQUENCE);
		private boolean trackX = true;
		private boolean trackY = true;
		private boolean trackSequence = false;
		private boolean pointersUpdated;
		
		public void pointerMoved(float x, float y) {
			//If the cursor is abruptly moved by something (e.g. another program takes focus), we get rid of the gap that would
			//result between x and lastX (and y and lastY)
			
			if(x - lastX > owner.getWindow().getWidth()/3)
				lastX = x;
			if(y - lastY > owner.getWindow().getHeight()/3)
				lastY = y;
			
			//for getting the cursor to cycle around the screen, unable to leave the window
			//WARNING!!!! -- This code is not good enough for production; need something else if you release a version with mouse control.
			if(x >= owner.getWindow().getWidth() - Math.abs(pointerDx)*2 ||
			   x <= Math.abs(pointerDx*2)) {
				x = owner.getWindow().getWidth()/2;
				lastX = x;
				Gdx.input.setCursorPosition((int)x, (int)y);
			}
			if(y >= owner.getWindow().getHeight() - Math.abs(pointerDy)*2 ||
				y <= Math.abs(pointerDy*2)) {
				y = owner.getWindow().getHeight()/2;
				lastY = y;
				Gdx.input.setCursorPosition((int)x, (int)y);
			}
			
			//for the first run through
			if(lastTime == 0)
				lastTime = System.currentTimeMillis();
			
			//calculate the velocities
			pointerDx = x - lastX;
			pointerDy = -(y - lastY);

			lastX = x;
			lastY = y;
			pointersUpdated = true;
		}
		
		public void setVelocitiesFromNormalizedValues(float x, float y) {
			pointerDx = x*10;
			pointerDy = y*10;
			pointersUpdated = true;
		}
		
		public void update(float delta) {
			if(!pointersUpdated) {
				xTracker.pos = 0;
				yTracker.pos = 0;
				sequenceTracker.pos = 0;
			}
			
			if(trackX)
				xTracker.update(delta);
			if(trackY)
				yTracker.update(delta);
			if(trackSequence)
				sequenceTracker.update(delta);
			
			pointerDx = 0;
			pointerDy = 0;
			pointersUpdated = false;
		}
		
		private class CycleMotionTracker {
			public float pos;
			private float vol;
			private static final int TILE_SWITCH_INTERVAL = 10;
			public static final int X_AXIS = 0;
			public static final int Y_AXIS = 1;
			public static final int SEQUENCE = 2;
			private int style;
			
			public CycleMotionTracker(int style) {
				this.style = style;
			}
			
			public void update(float delta) {

				float pointerVol = 0;
				if(style == X_AXIS)
					pointerVol = pointerDx;
				else if(style == Y_AXIS)
					pointerVol = pointerDy;
				else if(style == SEQUENCE)
					pointerVol = (Math.abs(pointerDx) > Math.abs(pointerDy)) ? pointerDx : pointerDy;
				if(pointerVol == 0) {
					vol = 0;
					pos = 0;
				} else {
					vol = Math.abs(pointerVol)/3.5f*customVolScale;
					//vol = (float)(Math.log10(Math.abs(pointerDx)*10));
				}
				
				if(pointerVol > 0) //we lose this negative in the absolute value calculation above, have to re-incorporate it
					vol = -vol;
				
				pos += vol*delta;
				if(pos < -TILE_SWITCH_INTERVAL) {
					if(style == X_AXIS)
						selectRight();
					else if(style == Y_AXIS)
						selectBelow();
					else
						requestSelectNext();
				} else if (pos > TILE_SWITCH_INTERVAL) {
					if(style == X_AXIS)
						selectLeft();
					else if(style == Y_AXIS)
						selectAbove();
					else
						requestSelectPrevious();
				}
				
				if(selected.isEmpty() && !owner.getCells().isEmpty())
					addToSelection(owner.getCells().get(0));
			}
		}
		
		private void changeSelection(int direction) {
			if(direction == NEXT)
				directionIsForward = true;
			if(direction == PREVIOUS)
				directionIsForward = false;
			
			if(isMultiSelectInProgress() && !isSelectModifierPressed() && owner.isDraggingEnabled() && !owner.getDragger().isDragging()) {
				owner.getController().pickUpSelectedCells();
				
			} else {
				Cell newCell = null;
				Cell last = getLastSelected();
				
				owner.getController().getListener().auxCellSwitchedFrom(last);
				
				if(!selectModifierPressed) {
					clearSelection(false);
					multiSelectInProgress = false;
				}
				
				if(direction == LEFT) {
					newCell = getCellLeftOf(last);
				} else if(direction == RIGHT) {
					newCell = getCellRightOf(last);
				} else if(direction == UP) {
					newCell = getCellAbove(last);
				} else if(direction == DOWN) {
					newCell = getCellBelow(last);
				} else if(direction == NEXT) {
					newCell = getCellAfter(last);
				} else if(direction == PREVIOUS) {
					newCell = getCellBefore(last);
				}
				if(newCell == null)
					return;
				if(newCell.getNode().isMeta()) { //skip the cell if it's meta
					setLastSelected(newCell);
					changeSelection(direction);
					return;
				}
				
				if(atFirstSlot) {
					newCell = last;
					setLastSelected(last);
					owner.getCursor().placeAfter(getLastSelected(), true);
				}
				
				//If the cell we've just moved onto was already selected, de-select it
				if(newCell != null) {
					if(!selected.contains(newCell)) {
						addToSelection(newCell);
					} else if (!newCell.isDragging()){
						Cell toRemove = null;
						if(direction == LEFT) {
							toRemove = getCellRightOf(newCell);
						} else if(direction == RIGHT) {
							toRemove = getCellLeftOf(newCell);
						} else if(direction == UP) {
							toRemove = getCellBelow(newCell);
						} else if(direction == DOWN){
							toRemove = getCellAbove(newCell);
						} else if(direction == NEXT) {
							toRemove = getCellBefore(newCell);
						} else if(direction == PREVIOUS) {
							toRemove = getCellAfter(newCell);
						}
						removeFromSelection(toRemove);
						owner.getController().getListener().auxCellDeselected(toRemove);
						setLastSelected(newCell);
						owner.getCursor().placeAfter(getLastSelected(), true);
					}
					
					owner.getController().getListener().auxCellSwitchedTo(newCell);
				}
			}
		}
		
		public void checkForAtFirstSlot() {
			Cell last = getLastSelected();
			if(last == null)
				return;
			
			if(atFirstSlot) {
				atFirstSlot = false;
			} else if(owner.getDragger().isDragging() && last.getNode().getChildIndex() == 0) {
				atFirstSlot = true;
			}
		}
		
		public void clearSelection() {
			clearSelection(false);
		}
		
		public void clearSelection(boolean skipLast) {
			ArrayList<Cell> scratch = new ArrayList<Cell>();
			for (Cell cell : selected) {
				if(!(skipLast && cell == getLastSelected()))
					scratch.add(cell);
				else if(cell == getLastSelected()){
					cell.deselect();
					cell.setReadyForSelectionAction(true);
					cell.select();
				}
			}
			for(Cell cell : scratch) {
				removeFromSelection(cell);
			}
		}
		
		protected void selectNext() {
			changeSelection(NEXT);
		}
		
		protected void selectPrevious() {
			changeSelection(PREVIOUS);
		}
		
		public void requestSelectNext() {
			if(ensureSelectionExists() && owner.getCells().indexOf(getLastSelected()) < owner.getCells().size()-1)
				selectNext();
			sequenceTracker.pos = 0;
		}
		
		public void requestSelectPrevious() {
			if(ensureSelectionExists() && owner.getCells().indexOf(getLastSelected()) > 0)
				selectPrevious();
			sequenceTracker.pos = 0;
		}
		
		private boolean ensureSelectionExists() {
			if(getLastSelected() == null) {
				if(!owner.getCells().isEmpty()) {
					addToSelection(owner.getCells().get(0));
					return true;
				} else
					return false;
			} else
				return true;
		}
		
		public void selectRight() {
			if(ensureSelectionExists() && !isCellOnRightEdge(getLastSelected()))
				changeSelection(RIGHT);
			xTracker.pos = 0;
			
			/* we clear the position on the other axis too because slight deviations of 
			 * the joystick (applies to other devices too) result in accumulated position
			 * changes; so even if we've mostly just been pressing left/right occasionally
			 * we'll get a jump up or down. 
			 */
			yTracker.pos = 0;
		}
		
		public void selectLeft() {
			if(ensureSelectionExists() && !isCellOnLeftEdge(getLastSelected()))
				changeSelection(LEFT);
			xTracker.pos = 0;
			yTracker.pos = 0;
		}
		
		public void selectAbove() {
			if(ensureSelectionExists() && !isCellOnTopEdge(getLastSelected()))
				changeSelection(UP);
			yTracker.pos = 0;
			xTracker.pos = 0;
		}
		
		public void selectBelow() {
			if(ensureSelectionExists() && !isCellOnBottomEdge(getLastSelected()))
				changeSelection(DOWN);
			yTracker.pos = 0;
			xTracker.pos = 0;
		}
		
		public boolean isDirectionForward() {
			return directionIsForward;
		}
		
		protected void setDirectionIsForward(boolean forward) {
			this.directionIsForward = forward;
		}
		
		public boolean selectButtonPressed() {
			super.selectButtonPressed();
			
			return true;
		}
		
		public boolean selectButtonReleased() {
			super.selectButtonReleased();
			
			
			
			return true;
		}
		
		public boolean endMultiSelect() {
			boolean passOn = true;
			if(multiSelectInProgress && !selectModifierPressed) {
				clearSelection(true);
				multiSelectInProgress = false;
				passOn = false;
			}
			
			return passOn;
		}
		
		public boolean isAtFirstSlot() {
			return atFirstSlot;
		}

		public float getCustomVolScale() {
			return customVolScale;
		}

		public void setCustomVolScale(float customVolScale) {
			this.customVolScale = customVolScale;
		}

		public boolean isTrackX() {
			return trackX;
		}

		public void setTrackX(boolean trackX) {
			this.trackX = trackX;
		}

		public boolean isTrackY() {
			return trackY;
		}

		public void setTrackY(boolean trackY) {
			this.trackY = trackY;
		}

		public boolean isTrackSequence() {
			return trackSequence;
		}

		public void setTrackSequence(boolean trackSequence) {
			this.trackSequence = trackSequence;
		}
	}
}
