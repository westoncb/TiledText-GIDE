package com.cyntaks.GDXGIDE.code;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.AbstractionManager;
import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.Grid;
import com.cyntaks.GDXGIDE.Selector;

/**
 * So far this class primarily exists as a way of carrying out a necessary optimization algorithm which
 * only locally replaces more abstract things with less abstract things (e.g. a function with lines). The
 * much simpler alternative would be to replace all of the more abstract constructs in a document with their
 * less abstract counterparts when the abstraction level is decreased; but lets say we want to deal in the 
 * abstraction of individual characters: it is far too resource intensive to represent a large source file with 
 * Cells for ever character in it. Instead, as the Selector moves around a document, Cells surrounding it are
 * broken into less abstract cells according to the current abstraction level set in the AbstractionManager.
 * 
 * @author weston
 *
 */

public class CodeSelector extends Selector{
	private boolean cycling;
	
	public CodeSelector(Grid owner) {
		super(owner);
		super.setController(new CodeCyclingController());
	}
	
	public void addToSelection(Cell oCell) {
		addtoSelection(oCell, true);
	}
	
	public void addtoSelection(Cell oCell, boolean adjustAbstraction) {
		CodeCell cell = (CodeCell)oCell;
		super.addToSelection(cell);
		
	}
	
	/**
	 * Determines whether the current selection includes all of and only the members of the parent node.
	 * @return
	 */
	public boolean selectionComprisesParent() {
		ArrayList<Cell> selection = super.getSelectedCells();
		if(selection.isEmpty() || selection.size() != selection.get(0).getNode().getParent().getChildCount() || selection.get(0).getNode().getParent().isMeta())
			return false;
		else {
			GNode parent = selection.get(0).getNode().getParent();
			for (Cell cell : selection) {
				if(cell.getNode().getParent() != parent)
					return false;
			}
			
			return true;
		}
	}
	
	public void attemptLocalAbstractionDecrease(Cell cell) {
		attemptLocalAbstractionDecrease(cell, false);
	}
	
	/**
	 * Takes a cell and replaces it with its less abstract children. If the abstractionLevel of 
	 * the CodeNode belonging to the Cell passed in is not matched with one pass, the method will
	 * call itself. However, if the initial call is made with oneLevelOnly set to true, the method
	 * will not recurse. 
	 * @param cell
	 * @param oneLevelOnly
	 * 
	 * @return endingAbstractionIndex
	 */
	public int attemptLocalAbstractionDecrease(Cell cell, boolean oneLevelOnly) {
		assert !cell.getNode().isMeta();
		
		CodeGrid owner = (CodeGrid)getOwner();
		AbstractionManager manager = (AbstractionManager)owner.getStructureManager();
		CyclingController controller = (CyclingController)getController();
		int abstractionIndex = ((CodeNode)cell.getNode()).getAbstraction() + 1; //try for one level of abstraction below where the cell to be selected is.
																	 //This behavior, incrementing levels rather than going directly to the current
																	 //abstraction, is what allows the a method which is selected to break into lines only
																	 //if we are at the word abstraction level, and only have the current line broken into words.
		if(abstractionIndex > manager.getAbstractionIndex() && !oneLevelOnly)
			abstractionIndex = manager.getAbstractionIndex();
		
		ArrayList<GNode> abstraction = manager.getAbstractionForNode((CodeNode)cell.getNode(), abstractionIndex);
		if(abstraction.get(0) == cell.getNode()) { //at the correct abstraction (size of abstraction should always be one in this case)
			owner.updateDisplay();
			return abstractionIndex;
		} else {
			Cell anchor = getCellBefore(cell);
			GNode anchorNode = null;
			if(anchor != null)
				anchorNode = anchor.getNode();
			ArrayList<GNode> scratch = new ArrayList<GNode>();
			scratch.add(cell.getNode());
			
			if(owner.getCells().contains(cell))
				owner.removeCells(scratch);

			System.out.println("abs size: " + abstraction.size());
			for (GNode gNode : abstraction) {
				System.out.println("ADDING: " + gNode);
			}
			
			owner.addCells(abstraction, anchorNode, true, true, false);
			owner.updateDisplay();
			
			int nextIndex = 0;
			if(!controller.isDirectionForward())
				nextIndex = abstraction.size()-1;
			
			Cell aLessAbstractCell = owner.getCellForNode(abstraction.get(nextIndex));
			addToSelection(aLessAbstractCell);
			if(!oneLevelOnly)
				return attemptLocalAbstractionDecrease(aLessAbstractCell, false);
			else
				return abstractionIndex;
		}
	}
	
	/**
	 * Finds the parent node for the cell passed in and replaces all of its children cells with a
	 * cell for itself; parent nodes are more a more abstract representation of their children, so
	 * this swapping of cells introduces a greater degree of abstraction locally.
	 * @param cell
	 * @return
	 */
	public CodeCell attemptLocalAbstractionIncrease(Cell cell) {
		if(!cell.getNode().isMeta()) {
			CyclingController controller = (CyclingController)getController();
			CodeNode node = (CodeNode)cell.getNode();
			
			//If the parent is a meta node, then the condition (see below) about the current node being the last
			//(or first, depending on controller direction) is inappropriate for deciding to to switch
			//abstractions in the first place and there is nothing to do here.
			GNode parent = node.getParent();
			if(parent == null || parent.isMeta())
				return null;
			
			//index of the last cell in a group which should be replaced by the cell for a parent node when we pass it
			int boundaryIndex = 0;
			if(controller.isDirectionForward()) {
				boundaryIndex = parent.getChildCount()-1;
			}
			if(node.getChildIndex() == boundaryIndex) {
				CodeCell replacement = replaceLessAbstractCells(parent);
				if(replacement != null) { //This last conditional is not fully understood; was used as a stupid bug fix for a nullpointer that would arise when cycling through certain elements...
					setLastSelected(replacement);
					return attemptLocalAbstractionIncrease(replacement);
				} else
					return (CodeCell)getLastSelected();
			}
		}
		
		return (CodeCell)cell;
	}
	
	/**
	 * Too many cases exist where a Cell corresponding to a Node is requested, and it's a valid request
	 * except that no Cell is present because the local abstraction is different. This method attempts to ignore
	 * the local abstraction by shifting it incrementally to its highest and lowest extents to see if the Cell is
	 * present at any level.
	 * 
	 * @param node
	 * @return
	 */
	public CodeCell getCorrespondingCell(CodeNode node) {
		if(node == null)
			return null;
		
		CodeCell cell  = (CodeCell)getOwner().getCellForNode(node);
		
		//First see if the local cells are not abstract enough, replacing with more abstract
		if(cell == null) {
			CodeNode child = node;
			ArrayList<CodeNode> children = new ArrayList<CodeNode>();
			while(!child.isLeaf() && cell == null) { //see if some child has a Cell in the Grid
				children.add(0, child);
				child = child.getChild(0);
				cell = (CodeCell)getOwner().getCellForNode(child);
			}
			if(cell != null) {
				for (CodeNode codeNode : children) { //increase local abstraction until our node has a cell
					cell = (CodeCell)replaceLessAbstractCells(codeNode);
				}
			}
		}
		
		//if that wasn't the problem, try the other direction
		if(cell == null) {
			CodeNode parent = node;
			ArrayList<CodeNode> parents = new ArrayList<CodeNode>();
			do { //see if some parent has a Cell in the Grid
				if(!parent.isMeta())
					parents.add(0, parent);
				parent = parent.getParent();
				cell = (CodeCell)getOwner().getCellForNode(parent);
			} while (parent.getParent() != null && cell == null);
			
			if(cell != null) {
				for (CodeNode codeNode : parents) { //decrease local abstraction until our node has a cell
					attemptLocalAbstractionDecrease(cell, true);
					cell = (CodeCell)getOwner().getCellForNode(codeNode);
				}
			}
		}
		
		return cell;
	}
	
	/**
	 * Replaces the cells which correspond to the children of the passed in node with a new
	 * cell for the passed in node. 
	 * @parwam parent The node which a new Cell should be formed for, to replace its children
	 * 
	 * @return The Cell which replaced the less abstract Cells
	 */
	public CodeCell replaceLessAbstractCells(GNode oParent) {
		CodeNode parent = (CodeNode)oParent;
		CodeGrid owner = (CodeGrid)getOwner();
		
		int firstIndex = 0;
		CodeCell firstChild = null;
		do { //if some Cell(s) as the first children of this parent, they may not have Cells yet even though the nodes are present
			firstChild = getCorrespondingCell(parent.getChild(firstIndex));
			firstIndex++;
			
			if(firstChild == null && firstIndex == parent.getChildCount())
				return null;
		} while (firstChild == null);

		Cell beforeCell = getCellBefore(firstChild);
		GNode anchor = null;
		if(beforeCell != null) {
			while(parent.isAncestorOf(beforeCell.getNode())) { //We want a cell not beneath the area we are about to abstract
				beforeCell = getCellBefore(beforeCell);
			}
				
			anchor = beforeCell.getNode();
		}
		
		ArrayList<GNode> scratch = parent.getChildrenRecursive(); 
		owner.removeCells(scratch);
		if(parent.getParent() != null) { //don't want to add the root
			scratch.clear();
			scratch.add(parent);
			owner.addCells(scratch, anchor, true, false, false);
			owner.updateDisplay();
		}
		
		return (CodeCell)owner.getCellForNode(parent);
	}
	
	public CodeCyclingController getController() {
		return (CodeCyclingController)super.getController();
	}
	
	public class CodeCyclingController extends CyclingController {
		public void selectNext() {
			if(isMultiSelectInProgress() && !isSelectModifierPressed() && getOwner().isDraggingEnabled() && !getOwner().getDragger().isDragging()) {
				getOwner().getController().pickUpSelectedCells();
				
			} else {
				cycling = true;
				super.setDirectionIsForward(true);
				
				CodeCell replacement = attemptLocalAbstractionIncrease(getLastSelected());
				if(replacement != null && replacement.hasCollapsedNode())
					getOwner().getScroller().teleportNextTime();
				
				super.selectNext();
				
				CodeGrid grid = (CodeGrid)getOwner();
				grid.formatAroundCursor();
				
				if(!super.isAtFirstSlot())
					attemptLocalAbstractionDecrease(getLastSelected());
				
				super.checkForAtFirstSlot();
			}
			cycling = false;
		}
		
		public void selectPrevious() {
			if(isMultiSelectInProgress() && !isSelectModifierPressed() && getOwner().isDraggingEnabled() && !getOwner().getDragger().isDragging()) {
				getOwner().getController().pickUpSelectedCells();
				
			} else {
				cycling = true;
				super.setDirectionIsForward(false);
				super.checkForAtFirstSlot();
	
				if(!super.isAtFirstSlot()) {
					CodeCell replacement = attemptLocalAbstractionIncrease(getLastSelected());
					if(replacement != null && replacement.hasCollapsedNode())
						getOwner().getScroller().teleportNextTime();
				}
				
				super.selectPrevious();
				
				if(!super.isAtFirstSlot())
					attemptLocalAbstractionDecrease(getLastSelected());
			}
			cycling = false;
		}
	}
}
