package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;

public class Selector {
	private ArrayList<Cell> selected;
	private Cell lastSelected;
	private SelectController controller;
	private Grid owner;
	private boolean multiSelectInProgress;
	private boolean acceptingNewSelections;

	private boolean selectModifierPressed;
	
	public Selector(Grid owner) {
		this.owner = owner;
		controller = new CyclingController();
		selected = new ArrayList<Cell>();
	}
	
	public void addCell(Cell cell) {
		if(!cell.isDragging()) {
			selected.add(cell);
			cell.select();
			activateCell(cell);
		}
	}
	
	private void activateCell(Cell cell) {
		lastSelected = cell;
		owner.getScroller().centerOnCell(cell, 7, 10);
	}
	
	public void removeCell(Cell cell) {
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
				//sortSelectedCells();
				setLastSelected(cellBefore);//selected.get(0);
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
		return selected;
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
			else if (ownerCells.size() > selected.size()) //temporary: eventually the placeIndex will refer to a sort of -1 index cell (the blank nodes that will precede the first node in all grids).
				beforeIndex = selectIndex + selected.size();
			
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
}

public class DefaultController implements SelectController {

		public boolean endMultiSelect() {
			multiSelectInProgress = false;
			acceptingNewSelections = false;
			
			clearSelection();
			
			return false;
		}
		
		public void clearSelection() {
			ArrayList<Cell> temp = new ArrayList<Cell>();
			for(Cell cell : selected)
				temp.add(cell);
			
			for(Cell cell : temp) {
				removeCell(cell);
				cell.setReadyForSelectionAction(true);
			}
		}
		
		public void pointerMoved(float x, float y) {
			for(Cell cell : owner.getCells()) {
				if(cell.isReadyForSelectionAction()) {
					if(cell.getBehavior().cellContainsPointer(AbstractInputHandler.PRIMARY_POINTER)) {
						if((selected.isEmpty() || acceptingNewSelections) && !selected.contains(cell)) {
							addCell(cell);
						} else if(selected.contains(cell) && multiSelectInProgress && acceptingNewSelections) {
							removeCell(cell);
							if(selected.isEmpty()) {
								endMultiSelect();
							}
						}
					} else if(selected.contains(cell)) { //cell is selected but does not have the primary pointer
						if(!multiSelectInProgress) {
							removeCell(cell);
						}
					}
				}
			}
		}
		
		public boolean selectButtonPressed() {
			System.out.println(". /n select button released");
			
			return true;
		}

		public boolean selectButtonReleased() {
			System.out.println("select button pressed");
			boolean passEventOn = true;
			
			if(!selectModifierPressed && multiSelectInProgress) {
				endMultiSelect();
				passEventOn = false;
			}
			return passEventOn;
		}

		public boolean selectModifierPressed() {
			System.out.println("select modifier pressed");
			selectModifierPressed = true;
			multiSelectInProgress = true;
			acceptingNewSelections = true;
			
			return true;
		}

		public boolean selectModifierReleased() {
			System.out.println("select modifier released");
			selectModifierPressed = false;
			acceptingNewSelections = false;
			for(Cell cell : selected) {
				if(cell.getBehavior() != null && cell.getBehavior().cellContainsPointer(AbstractInputHandler.PRIMARY_POINTER))
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
		
		private float pos;
		private float vol;
		private float accel;
		
		private static final int TILE_SWITCH_INTERVAL = 10;
		private static final float AUTO_ACCEL_SCALE = .009f;
		private static final int AUTO_VOL_CAP = 25;
		private static final float AUTO_HALT_THRESHOLD = .06f;
		private static final float MANUAL_VOL_SCALE = 1.5f;
		
		private long lastTime;
		
		private boolean autoMode = false;
		
		private boolean directionIsForward = true; //whether we are cycling forward or backward
		
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
		}

		public void update(float delta) {
			//switches between the modes which correspond to motion on the x or y axis
			if(Math.abs(pointerDy) > Math.abs(pointerDx) && Math.abs(pointerDy) > 2)
				autoMode = true;
			else if(Math.abs(pointerDx) > Math.abs(pointerDy) && Math.abs(pointerDx) > 0)
				autoMode = false;
			
			//INSTEAD OF THESE MODES, WHAT YOU REALLY WANT IS ONE AXIS FOR NAVIGATING AT A LEVEL OF ABSTRACTION ABOVE THE CURRENT LEVEL;
			//I.E., NEED TO BE ABLE TO NAVIGATE BY TWO LEVELS OF ABSTRACTION AT ONCE.
			//And actually, the two levels at once thing may not even be necessary, just need an extremely easy/quick way of changing levels.
			if(autoMode) {
				accel = pointerDy*AUTO_ACCEL_SCALE;
				vol += accel;
				
				//limit the velocity
				if(vol < -AUTO_VOL_CAP)
					vol = -AUTO_VOL_CAP;
				if(vol > AUTO_VOL_CAP)
					vol = AUTO_VOL_CAP;
				
				//bring the motion to a halt if we slow down enough
				if(Math.abs(vol) < AUTO_HALT_THRESHOLD)
					vol = 0;
			} else {
				if(pointerDx == 0)
					vol = 0;
				else {
					vol = (float)(Math.log10(Math.abs(pointerDx)*10));
					vol *= MANUAL_VOL_SCALE;
				}
				
				if(pointerDx > 0) //we lose this negative in the absolute value calculation above, have to re-incorporate it
					vol = -vol;
			}
			
			pos += vol*delta;
			if(pos < -TILE_SWITCH_INTERVAL) {
				selectNext();
				pos = 0;
			} else if (pos > TILE_SWITCH_INTERVAL) {
				selectPrevious();
				pos = 0;
			}
		}
		
		private void changeSelection(boolean increment) {
			directionIsForward = increment;
			
			if(isMultiSelectInProgress() && !isSelectModifierPressed() && !CellDragger.isDragging()) {
				owner.getController().pickupSelectedCells();
				
			} else {
				Cell newCell = null;
				Cell last = getLastSelected();
				if(last == null && !owner.getCells().isEmpty())
					newCell = owner.getCells().get(0);
				else if(last != null) {
					if(!selectModifierPressed) {
						clearSelection(false);
					}
					if(getLastSelected() != null)
						last = getLastSelected();
					if(increment)
						newCell = getCellAfter(last);
					else
						newCell = getCellBefore(last);
				}
				
				
				if(newCell != null) {
					if(!selected.contains(newCell))
						addCell(newCell);
					else if (!newCell.isDragging()){
						Cell toRemove = increment ? getCellBefore(newCell) : getCellAfter(newCell);
						removeCell(toRemove);
						activateCell(toRemove);
						setLastSelected(newCell);
						owner.getCursor().placeAfter(getLastSelected(), true);
					}
				}
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
			}
			for(Cell cell : scratch) {
				removeCell(cell);
			}
		}
		
		public void selectNext() {
			changeSelection(true);
		}
		
		public void selectPrevious() {
			changeSelection(false);
		}
		
		public boolean isDirectionForward() {
			return directionIsForward;
		}
		
		protected void setDirectionIsForward(boolean forward) {
			this.directionIsForward = forward;
		}
		
		public boolean selectModifierPressed() {
			super.selectModifierPressed();
			
			return true;
		}
		
		public boolean selectModifierReleased() {
			super.selectModifierReleased();
			
			
			
			return true;
		}
		
		public boolean selectButtonReleased() {
			return endMultiSelect();
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
		
		public boolean selectButtonPressed() {
			return true;
		}
	}