package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.List;

import com.cyntaks.GDXGIDE.code.CodeCell;
import com.cyntaks.GDXGIDE.code.CodeGrid;
import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.code.CodeSelector;
import com.cyntaks.GDXGIDE.code.CodeSelector.CodeCyclingController;
import com.cyntaks.GDXGIDE.programs.CodeEditor;

public class AbstractionManager extends TreeManager{
	private CodeNode root;
	private int abstractionIndex = 0;
	private ArrayList<GNode> topLevelNodes = new ArrayList<GNode>();
	private ArrayList<GNode> localNodes = new ArrayList<GNode>();
	
	public AbstractionManager() {
		
	}
	
	public void initialize(GNode tree, Grid grid) {
		super.initialize(tree, grid);
		super.setPushUpdates(true); //setting it to true only temporarily
		
		goTo(tree);
	}
	
	/**
	 * AbstractionManager doesn't use the goTo functionality; you are always in one place with it.
	 */
	public void goTo(GNode node) {
		root = (CodeNode)node.getRoot();
	}

	public void changeAbstractionIndex(boolean increase) {
		CodeGrid grid = (CodeGrid)super.getGrid();
		CodeSelector selector = (CodeSelector)grid.getSelector();
		CodeCyclingController controller = (CodeCyclingController)selector.getController();
		Cell lastSelected = selector.getLastSelected();

		if(increase && abstractionIndex < CodeNode.getNumAbstractionLevels()) {
			abstractionIndex++;
			
			if(lastSelected != null) {
				controller.setDirectionIsForward(true); //always want the first child cell selected when manually decreasing abstraction, regardless
														//of the direction we were moving in previously.
				selector.attemptLocalAbstractionDecrease(lastSelected, true);
				if(lastSelected == selector.getLastSelected())
					changeAbstractionIndex(true);
			}
		}
		else if(!increase && abstractionIndex > 0) {
			abstractionIndex--;
			if(lastSelected != null) {
				CodeNode parent = (CodeNode)lastSelected.getNode().getConcreteParent();
				Cell parentCell = selector.replaceLessAbstractCells(parent);
				selector.addtoSelection(parentCell, false);
				abstractionIndex = ((CodeNode)parentCell.getNode()).getAbstraction();
				grid.getScroller().teleportNextTime();
				grid.getCursor().placeAfter(parentCell, true);
			}
		}

		if(CodeEditor.UNIFORM_ABSTRACTION_LEVEL) {
			for (Cell cell : grid.getCells()) {
				((CodeCell)cell).buildCodeList((CodeNode)cell.getNode());
			}
			grid.formatNewCells(grid.getCells(), true);
			grid.getScroller().teleportNextTime();
		}
		
		grid.getCursor().abstractionChanging();
	}
	
	/**
	 * LOOOK!: This is a strange and inappropriate method -- the number of "lines" to use
	 * with the GridDisplay on CodeGrids is not being set correctly; ordinarily it's determined by
	 * rowsperscreen or colsperscreen (depending on orientation) in the xml config, but if I manually
	 * set it to 1 there, the layout is broken. I leave this as a puzzle for another day...
	 */
	protected void fillAddList(GNode newNode, ArrayList<GNode> addList) {
		System.out.println("called");
		super.getGrid().configureAppearance(super.getActiveNode(), 1);
		super.fillAddList(newNode, addList);
	}
	
	public void updateGrid(GNode anchor) {
		int oldAbstraction = -1;
		
		if(anchor != null) {
			CodeNode codeAnchor = (CodeNode)anchor;
			oldAbstraction = getAbstractionIndex();
			setAbstractionToIndex(codeAnchor.getAbstraction());
		}
		
		super.updateGrid(anchor);
		
		if(oldAbstraction != -1) {
			setAbstractionToIndex(oldAbstraction);
		}
	}
	
	public void exposeCell(GNode node) {
		CodeGrid grid = (CodeGrid)super.getGrid();
		CodeSelector selector = (CodeSelector)grid.getSelector();
		
		this.setAbstractionToIndex(0);
		grid.getScroller().teleportNextTime();
		ArrayList<Integer> address = node.getTreeAddress();
		node = node.getRoot();
		
		for (int i = 0; i < address.size(); i++) {
			int index = address.get(i);
			node = node.getChild(index);

			Cell cell = grid.getCellForNode(node);
			if(!node.isMeta() && i < address.size()-1) {
				System.out.println("The Node: " + node + ", The Cell: " + cell);
				this.abstractionIndex = selector.attemptLocalAbstractionDecrease(cell, true);
				grid.getScroller().teleportNextTime();
			}
		}
	}
	
	public void setAbstractionToIndex(int index) {
		while(index > getAbstractionIndex()) {
			changeAbstractionIndex(true);
		}
		while(index < getAbstractionIndex())
			changeAbstractionIndex(false);
	}
	
	protected void getAllNodesForCurrentAbstraction(ArrayList<GNode> addList) {
		getAbstractionForNode(root, addList, abstractionIndex);
	}
	
	public ArrayList<GNode> getAbstractionForNode(CodeNode start, int abstraction) {
		localNodes.clear();
		getAbstractionForNode(start, localNodes, abstraction);
		return localNodes;
	}
	
	public void getAbstractionForNode(CodeNode start, ArrayList<GNode> addList, int abstraction) {
		//add all the nodes at this depth or any leaves that show up before this depth
		addList.clear();
		auxGetAbstractionForNode(start, addList, abstraction);
	}
	
	private void auxGetAbstractionForNode(CodeNode node, ArrayList<GNode> addList, int abstraction) {
		if((node.getAbstraction() >= abstraction || node.isLeaf()) &&
			!(node.isMeta() && node.isLeaf())) {
			
			List ancestors = node.getAncestors();
			boolean original = true;
			if(ancestors != null) {
				for(Object ancestor : ancestors) {
					if(addList.contains(ancestor))
						original = false;
				}
			}
			
			if(original)
				addList.add(node);
		}
		
		for(int i = 0; i < node.getChildCount(); i++) {
			auxGetAbstractionForNode(node.getChild(i), addList, abstraction);
		}
	}
	
	public ArrayList<GNode> getDisplayNodes() {
		return topLevelNodes;
	}
	
	public int getAbstractionIndex() {
		return abstractionIndex;
	}
}