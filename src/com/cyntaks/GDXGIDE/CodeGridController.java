package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.LinkedList;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;

import com.cyntaks.GDXGIDE.code.CodeCell;
import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.code.CodeSelector;
import com.cyntaks.GDXGIDE.code.ANTLR.SimpleParser;
import com.cyntaks.GDXGIDE.code.ANTLR.SimpleParser.ParserSession;
import com.cyntaks.GDXGIDE.programs.CodeEditor;

/**
 * A variation on GridController that does the work to maintain an AST representation of the document
 * by re-parsing appropriate portions when Cells are inserted; thus, dropPickedUpCells() is overridden.
 * @author weston
 *
 */
public class CodeGridController extends GridController {

	public CodeGridController(Grid grid) {
		super(grid);
	}
	
	public void pickUpSelectedCells() {
		Grid grid = super.getGrid();
		ArrayList<Cell> cells = grid.getSelector().getSelectedCells();
		ArrayList<GNode> nodes = getNodesFromCells(cells);
		queueOperation(new PickUpCodeCellsOperation(nodes));
	}
	
	public void insertDraggedCells() {
		Grid grid = super.getGrid();
		CellDragger dragger = grid.getDragger();
		ArrayList<Cell> cells = dragger.getTop().getCells();
		System.out.println("here it is: " + cells);
		ArrayList<GNode> nodes = getNodesFromCells(cells);
		System.out.println("and the nodes: " + nodes);
		queueOperation(new InsertCodeCellsOperation(nodes, getAddressOfSelectedCell()));
	}
	
	
	protected class InsertCodeCellsOperation extends InsertCellsOperation {
		
		public InsertCodeCellsOperation(ArrayList<GNode> nodes, ArrayList<Integer> address) {
			super(nodes, address);
		}
		
		public Operation createInverse() {
			return new ExplicitDeleteOperation(this);
		}
		
		public void subExecute() {
			boolean success = false;
			Grid grid = getGrid();
			
			if(grid.getDragger().isDragging()) {
				CellDragger dragger = grid.getDragger();
				CodeSelector selector = (CodeSelector)grid.getSelector();
				ArrayList<Integer> lastSelectedAddress = selector.getLastSelected().getNode().getTreeAddress();
				CodeCell lastSelected = findAnchor((CodeCell)selector.getLastSelected());
				CodeNode lastSelectedNode = lastSelected.getNode();
				
				//Cells that were being dragged (i.e. Cells to insert)
				ArrayList<GNode> newNodes = new ArrayList<GNode>(super.getArgs());
				
				//--Insert the new nodes among the existing nodes--
				int addIndex = lastSelectedNode.getChildIndex() + 1;
				if(selector.getController().isAtFirstSlot()) {
					addIndex -= 1;
				}
				
				CodeNode immediateParent = lastSelectedNode.getParent(); //The idea is that the anchor's parent will also be parent to the Cells being inserted
				System.out.println("immediateParent: " + immediateParent);
				ArrayList<Integer> immediateParentAddress = immediateParent.getTreeAddress();
				ArrayList<GNode> before = new ArrayList<GNode>();
				for (int i = 0; i < immediateParent.getChildCount(); i++) {
					before.add(immediateParent.getChild(i));
				}
				if(insertingLetters(newNodes) && (immediateParent.isLeaf() || !immediateParent.getChild(0).isLetter()))
					mergeLetters(newNodes); //for dropping a group of letters where a word belongs
				for(int i = newNodes.size()-1; i > -1; i--) { //now getting to the insertion
					GNode node = newNodes.get(i);
					node.insertionIsPending();
					immediateParent.addChild(addIndex, node);
				}

				//--Re-parse the region containing the inserted nodes and attach the re-parsed
				//sub-tree to the main tree (in place of the old sub-tree)--.
				ParserSession result = parseRegionContainingNode(lastSelectedNode);
				LinkedList<String> errors = result.errors;
				if(errors.isEmpty()) {
					GNode newlyParsedNodesParent = insertNewlyParsedNodes(result);
					GNode newImmediateParent = newlyParsedNodesParent.getNodeAtAddress(immediateParentAddress);
					System.out.println("newImmediateParent: " + newImmediateParent);
					ArrayList<GNode> after = new ArrayList<GNode>();
					for (int i = 0; i < newImmediateParent.getChildCount(); i++) {
						after.add(newImmediateParent.getChild(i));
					}
					
					newNodes = new ArrayList<GNode>();
					int offset = 0;
					for (int i = 0; i < after.size(); i++) {
						int beforeIndex = i + offset;
						if(beforeIndex < before.size() && !after.get(i).toStringTree().equals(before.get(beforeIndex).toStringTree())) {
							newNodes.add(after.get(i));
							offset -= 1;
						} else if (beforeIndex >= before.size())
							newNodes.add(after.get(i));
					}
					
					System.out.println("NEWNODES");
					for (GNode gNode : newNodes) {
						System.out.println(gNode.toStringTree());
					}
					System.out.println("ENDNEWNODES");
					
					ArrayList<GNode> newNodesCopy = new ArrayList<GNode>(newNodes);
					CodeNode.expandMetaNodes(newNodesCopy);
					for (GNode gNode : newNodesCopy) {
						Cell cell = selector.getCorrespondingCell((CodeNode)gNode);
						System.out.println("GNODE: " + gNode);
						getListener().auxCellInserted(cell);
					}
					
					//for the undo operation
					super.setArgs(newNodes);
					super.setAddress(lastSelectedAddress);
					super.calculateArgumentAddresses();
					
					CodeNode selectNode = lastSelectedNode.getNodeAtAddress(lastSelectedAddress); //We use this method of finding the node since the Object for the old node has been re-created;
					//its location in the tree is the only thing that remains now.
					Cell selectCell = selector.getCorrespondingCell(selectNode);
					selector.clearSelection();
					selector.addToSelection(selectCell);
					selector.getController().selectNext();
					dragger.dropTop();
					
					success = true;
					
				} else { //Take back the insertion and notify user of an error
					//Undo the insertion
					for(int i = 0; i < newNodes.size(); i++) {
						//We were inserting a word into a group of letters, this means we need to delete for each letter, not just the word (see addChild in CodeNode).
						if(immediateParent.getChild(0).isLetter() && !((CodeNode)newNodes.get(i)).isLetter()) {
							int additionalCells = newNodes.get(i).getChildCount()-1;
							for(int j = 0; j < additionalCells; j++) {
								immediateParent.deleteChild(addIndex);
							}
						}
						
						immediateParent.deleteChild(addIndex);
					}
					getListener().errorOccurred(errors);
				}
				
				//Make sure nothing is left thinking an insertion is pending (none should be anywhere immediately after an insertion)
				immediateParent.getRoot().clearInsertionPending();
			}
			
			super.operationFinished(success);
		}

		/**
		 * Creates a single Cell representing a word, given a collection of Cells with letter nodes.
		 * The letter Cells are then deleted from the collection and the word Cell is added.
		 * @param newCells
		 */
		private void mergeLetters(ArrayList<GNode> newNodes) {
			String s = "";
			for (GNode node : newNodes) {
				s += node.toString();
			}
			
			//The ID given to the CommonToken below shouldn't matter since this CodeNode will be replaced with a new one after parsing directly after this
			CodeNode node = new CodeNode(new CommonTree(new CommonToken(-1, s)), (CodeNode)newNodes.get(0).getParent());
			//CodeCell cell = new CodeCell(getGrid(), node);
			//cell.setConfig(newCells.get(0).getConfig());
			//cell.load();
			//cell.init();
			//newCells.clear();
			newNodes.clear();
			newNodes.add(node);
		}
		
		/**
		 * Whether the given collection of Cells is a collection of letters or something else.
		 * @param newCells
		 * @return
		 */
		private boolean insertingLetters(ArrayList<GNode> newNodes) {
			boolean letters = false;
			boolean other = false;
			
			for (GNode node : newNodes) {
				if(((CodeNode)node).isLetter())
					letters = true;
				else
					other = true;
			}
			
			assert !(letters && other); //should never have a mix
			
			return letters;
		}
		
		private CodeCell findAnchor(CodeCell lastSelected) {
			CodeNode node = (CodeNode)lastSelected.getNode();
			Grid grid = getGrid();
			AbstractionManager manager = (AbstractionManager)grid.getStructureManager();
			CodeSelector selector = (CodeSelector)grid.getSelector();
			
			//should be the last child of some non-meta parent node
			if(node.getParent() == null || node.childIndex < node.getParent().getChildCount()-1 || node.getParent().isMeta())
				return lastSelected;
			else if (node.getAbstraction() < manager.getAbstractionIndex()) { //we only desire the behavior described below when this condition exists too
				decreaseAbstraction();
				return (CodeCell)selector.getLastSelected(); //if we are pasting after some last child, we treat the parent as the last selected
															 //instead; otherwise the inserted cells will be inside of this parent whereas we really
															 //want them after the parent in this case.
			} else {
				return lastSelected;
			}
		}
		
		/**
		 * Finds the nearest parent of the node passed in having a rule associated with it, then parses
		 * its children using that rule. The new parse sub-tree then replaces the old subtree, now
		 * containing nodes for the newly inserted text (presumably that's why this method is being used).
		 * @param node
		 * @return
		 */
		private ParserSession parseRegionContainingNode(CodeNode node) {
			//Parse the block of nodes containing the newly inserted text
			SimpleParser parser = new SimpleParser(CodeEditor.jGrammar, "com.cyntaks.GDXGIDE.code.java.JavaParser");
			ParserSession result = parser.parseFromNearestSufficientRule(node);
			if(result == null) {
				LinkedList<String> errors = new LinkedList<String>();
				errors.add("Somethin' went wrong with parsing.");
			}
			
			return result;
		}
		
		private CodeNode insertNewlyParsedNodes(ParserSession result) {
			Grid grid = getGrid();
			CodeSelector selector = (CodeSelector)grid.getSelector();
			
			
			CodeNode parent = result.ruleNode; //The point from which we have parsed, rebuilding the tree from there down
											   //Right now though, it's just a reference to the node -- it still contains the old tree
			CommonTree commonRoot = result.ast; //This is the newly parsed tree.
			CodeNode root = CodeNode.copyTree(commonRoot);
			
			//Used solely for finding the anchor
			Cell replacement = selector.replaceLessAbstractCells(parent);
			//The cell directly preceding the parsed region (we add the re-parsed cells after this)
			Cell anchor = selector.getCellBefore(replacement);
			
			//Gotta get rid of the cells corresponding to the area we have re-parsed since we want to replace them with
			//new cells.
			ArrayList<GNode> scratch = parent.getChildrenRecursive();
			scratch.add(parent);
			grid.removeCells(scratch);
			scratch.clear();
			
			//Clear the root of it's old children
			int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++) {
				parent.deleteChild(0);
			}
			
			//Replace them with the newly parsed children (containing the inserted nodes)
			for (int i = 0; i < root.getChildCount(); i++) {
				GNode child = root.getChild(i);
				parent.addChild(child);
				scratch.add(child);
			}
			
			GNode anchorNode = null;
			if(anchor != null)
				anchorNode = anchor.getNode();
			grid.addCells(scratch, anchorNode); //Will add them as the first cells if anchorNode is null
			
			return parent;
		}
	}
	
	protected class PickUpCodeCellsOperation extends PickUpCellsOperation {
		
		public PickUpCodeCellsOperation(ArrayList<GNode> nodes) {
			super(nodes);
		}

		public void subExecute() {
			Grid grid = getGrid();
			CodeSelector selector = (CodeSelector)grid.getSelector();
			if(selector.selectionComprisesParent())
				decreaseAbstraction();
			
			super.subExecute();
		}
	}
	
	public void increaseAbstraction() {
		Grid grid = super.getGrid();
		AbstractionManager manager = (AbstractionManager)grid.getStructureManager();
		int oldIndex = manager.getAbstractionIndex();
		manager.changeAbstractionIndex(true);
		if(oldIndex != manager.getAbstractionIndex())
			getListener().abstractionIncreased();
	}
	
	public void decreaseAbstraction() {
		Grid grid = super.getGrid();
		AbstractionManager manager = (AbstractionManager)grid.getStructureManager();
		int oldIndex = manager.getAbstractionIndex();
		manager.changeAbstractionIndex(false);
		if(oldIndex != manager.getAbstractionIndex())
			getListener().abstractionIncreased();
	}
	
	protected String getOperationTypeString(Operation op) {
		String type = super.getOperationTypeString(op);
		if(op instanceof InsertCodeCellsOperation)
			type = "insCCells";
		else if(op instanceof PickUpCodeCellsOperation)
			type = "liftCCells";
		
		return type;
	}
	
	public ArrayList<Cell> getCellsFromNodes(ArrayList<GNode> nodes) {
		ArrayList<GNode> nodes2 = new ArrayList<GNode>(nodes);
		CodeNode.expandMetaNodes(nodes2);
		return super.getCellsFromNodes(nodes2);
	}
	
	public ArrayList<GNode> getNodesFromCells(ArrayList<Cell> cells) {
		ArrayList<GNode> nodes = super.getNodesFromCells(cells);
		substituteMetaParentsForChildren(nodes);
		return nodes;
	}
	

	/**
	 * In an attempt to find the nodes corresponding to a group of cells, an additional step must be taken to recover meta-nodes
	 * since by definition they aren't represented by cells; this is that extra step.
	 * @param nodes
	 */
	private void substituteMetaParentsForChildren(ArrayList<GNode> nodes) {
		ArrayList<GNode> metaParents = new ArrayList<GNode>();
		for (GNode node : nodes) {
			if(node.getParent() != null && node.getParent().isMeta() && !metaParents.contains(node.getParent()))
				metaParents.add(node.getParent());
		}
		
		boolean aSwapOccurred = false;
		for (GNode parent : metaParents) {
			boolean allChildrenPresent = true;
			for (int i = 0; i < parent.getChildCount(); i++) { //verify that all children are present
				GNode child = parent.getChild(i);
				if(!nodes.contains(child)) {
					allChildrenPresent = false;
					break;
				}
			}
			
			if(allChildrenPresent) {
				int insertIndex = 0;
				for (int i = 0; i < parent.getChildCount(); i++) { //remove children
					GNode child = parent.getChild(i);
					insertIndex = nodes.indexOf(child);
					nodes.remove(child);
				}
				nodes.add(insertIndex, parent); //add meta parent
				aSwapOccurred = true;
			}
		}
			
		if(aSwapOccurred)
			substituteMetaParentsForChildren(nodes);
	}
	
	public CodeEditor getListener() {
		return (CodeEditor)super.getListener();
	}
}