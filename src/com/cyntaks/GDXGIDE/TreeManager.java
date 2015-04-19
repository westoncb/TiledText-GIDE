package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.Stack;

public class TreeManager implements LinkedStructureManager{
	private GNode active;
	private Grid grid;
	private Stack<GNode> lastViewedNodes;
	private ArrayList<GNode> addList;
	private ArrayList<GNode> removeList;
	private ArrayList<GNode> displayList;
	private boolean pushUpdates;
	
	public TreeManager() {
		addList = new ArrayList<GNode>();
		removeList = new ArrayList<GNode>();
		displayList = new ArrayList<GNode>();
		lastViewedNodes = new Stack<GNode>();
	}
	
	public void  initialize(GNode tree, Grid grid) {
		boolean goToRoot = true; //this used to be another parameter but this signature needs to match subclass signatures for constructing through Reflection
		this.grid = grid;
		
		pushUpdates = true;
		
		if(goToRoot && tree != null)
			goToAux(tree, false);
	}
	
	public ArrayList<GNode> getDisplayNodes() {
		return displayList;
	}
	
	public void updateGrid(GNode addAnchor) {
		grid.movingToNextNode(active);
		grid.removeCells(removeList);
		removeList.clear();
		grid.addCells(addList, addAnchor);
		addList.clear();
	}
	
	public void updateGrid() {
		updateGrid(null);
	}

	public void goTo(GNode node) {
		goToAux(node, true);
		System.gc();
	}
	
	private void goToAux(GNode newNode, boolean viewingThisNode) {
		if(newNode.isLeaf())
			return;
		
		if(this.active != null) {
			active.setBeingViewed(false);
			lastViewedNodes.push(active);
		}
		
		this.active = newNode;
		
		if(viewingThisNode) {
			active.setBeingViewed(true);
			
			//if(active.getFontSize() > 0)
				//Settings.getCellConfig("code").setFontSize(active.getFontSize());
		}
		
		ArrayList<Cell> cells = grid.getCells();
		for(Cell cell : cells)
			removeList.add(cell.getNode());
		
		if(grid.getOrientationSource() == Grid.GRID_DETERMINED)
			active.setOrientation(grid.getOrientation());
		
		fillAddList(newNode, pushUpdates ? addList : displayList);
		
		if(pushUpdates)
			updateGrid();
		
		if(!lastViewedNodes.isEmpty()) {
			Cell cell = grid.getCellForNode(lastViewedNodes.pop());
			if(cell != null)
				grid.getSelector().addToSelection(cell);
		}
	}
	
	protected void fillAddList(GNode newNode, ArrayList<GNode> addList) {
		if(newNode != null) {
			addList.clear();
			for (int i = 0; i < newNode.getChildCount(); i++) {
				addList.add(newNode.getChild(i));
			}
		}
	}
	
	public GNode getCurrentNode() {
		return active;
	}

	@Override
	public void add(ArrayList<GNode> nodes, GNode anchor) {
		int offset = 1;
		for(GNode node : nodes) {
			if(anchor != null) {
				anchor.getParent().addChild(anchor.getChildIndex() + offset, node);
				offset++;
			}
			
			addList.add(node);
		}

		if(isPushUpdates()) {
			updateGrid(anchor);
		} else {
			fillAddList(active, getDisplayNodes());
		}
	}
	
	@Override
	public void remove(ArrayList<GNode> nodes) {
		removeAux(nodes, true);
	}
	
	public void removeAux(ArrayList<GNode> nodes, boolean top) {
		//The goTo functionality is currently disabled -- always null
		GNode goTo = null; //the node we should navigate too after deleting, if any
		
		for(GNode node : nodes) {
			GNode parent = node.getParent();
			if(parent != null) {
				parent.deleteChild(node.getChildIndex());
				if(parent.isLeaf()) {
					ArrayList<GNode> list = new ArrayList<GNode>();
					list.add(parent);
					removeAux(list, false);
					
					//if(top && node.getParent() == active) //if we deleted all the nodes on the current screen, move up 
						//goTo = node.getParent().getParent();
				}
				
			}
			removeList.add(node);
		}
		
		grid.removeCells(nodes);
		removeList.clear();
		
		if(goTo != null)
			goTo(goTo);
		else if(!isPushUpdates()) {
			fillAddList(active, getDisplayNodes());
		}
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}
	
	protected ArrayList<GNode> getRemoveList() {
		return removeList;
	}
	
	protected ArrayList<GNode> getAddList() {
		return addList;
	}
	
	protected Grid getGrid() {
		return grid;
	}
	
	public boolean isPushUpdates() {
		return pushUpdates;
	}
	
	public void setPushUpdates(boolean push) {
		this.pushUpdates = push;
	}
	
	public GNode getActiveNode() {
		return active;
	}
}