package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.sgf.event.EventListener;

/**
 * Grids works primarily as data structures for managing Cells, the visual representation of GNodes;
 * and StructureManagers work as data structures for managing GNodes; the GridController then comes
 * in to coordinate interaction between the two when the user is interested in making modifications
 * to the grid they're working with. So, this class serves as the connection between code for handling
 * user input, and the basic data structures the program is based on. 
 *  
 * @author weston
 *
 */

public class GridController {
	private Grid grid;
	private Program listener;
	private Queue<Operation> opQueue = new LinkedList<Operation>();
	private Stack<Operation> undoStack = new Stack<Operation>();
	private Stack<Operation> redoStack = new Stack<Operation>();
	
	public GridController(Grid grid) {
		this.grid = grid;
		grid.setController(this);
	}
	
	public void update(float delta) {
		if(!opQueue.isEmpty()) {
			if(opQueue.peek().isExecuting()) {
				if(opQueue.peek().isDone()) { //When the operation finishes, we decide what to do about modifying the undo/redo stacks in relation to it
					Operation op = opQueue.remove();
					if(op.getType() != Operation.TYPE_UNDO) {
						if(op.wasExecutionSuccessful()) {
							undoStack.push(op.createInverse());
							if(op.getType() != Operation.TYPE_REDO)
								redoStack.clear();
						}
							
					} else if(op.wasExecutionSuccessful())
						redoStack.push(op.createInverse());
				}
			} else
				opQueue.peek().execute();
		}
	}
	
	protected void queueOperation(Operation op) {
		opQueue.add(op);
	}
	
	public void undo() {
		if(!undoStack.isEmpty()) {
			Operation op = undoStack.pop();
			op.setType(Operation.TYPE_UNDO);
			queueOperation(op);
		}
	}
	
	public void redo() {
		if(!redoStack.isEmpty()) {
			Operation op = redoStack.pop();
			op.setType(Operation.TYPE_REDO);
			queueOperation(op);
		}
	}
	
	public void explicitDelete() {
		ArrayList<Cell> cells = grid.getSelector().getSelectedCells();
		queueOperation(new ExplicitDeleteOperation(getNodesFromCells(cells)));
	}
	
	protected void implicitDelete() {
		ArrayList<Cell> cells = grid.getSelector().getSelectedCells();
		new ImplicitDeleteOperation(getNodesFromCells(cells)).execute();
	}
	
	public void pickUpSelectedCells() {
		ArrayList<Cell> cells = grid.getSelector().getSelectedCells();
		queueOperation(new PickUpCellsOperation(getNodesFromCells(cells)));
	}
	
	public void insertDraggedCells() {
		CellDragger dragger = grid.getDragger();
		if(grid.getDragger().isDragging()) {
			ArrayList<Cell> cells = dragger.dropTop().getCells();
			queueOperation(new InsertCellsOperation(getNodesFromCells(cells), getAddressOfSelectedCell()));
		}
	}
	
	/**
	 * By way of the Grid's StructureManager, this operation will insert a group of Cells, and notify any listening Program
	 * that they have been inserted.
	 */
	protected class InsertCellsOperation extends Operation {
		
		public InsertCellsOperation(ArrayList<GNode> args, ArrayList<Integer> address) {
			super(args, address);
		}
		
		public InsertCellsOperation(Operation inverse) {
			super(inverse);
		}
		
		public Operation createInverse() {
			System.out.println("In InsertCells, creating inverse ExplicitDelete");
			return new ExplicitDeleteOperation(this);
		}

		public void subExecute() {
			boolean success = false; 
			if(getAntecedent() != null || !super.getArgs().isEmpty()) {
				if(getAntecedent() != null) {
					super.setArgs(getAntecedent().getArgs());
					super.setAddress(getAntecedent().getAddress());
				}
				GNode anchorNode = getNodeAtAddress(super.getAddress());
				
				ArrayList<GNode> newNodes = super.getArgs();
				grid.getStructureManager().add(newNodes, anchorNode);
				grid.getDisplay().skipAnimateNextFrame();
				if(grid.getStructureManager() instanceof AbstractionManager) {
					((AbstractionManager)grid.getStructureManager()).exposeCell(anchorNode);
				}
				
				for (GNode gNode : newNodes) {
					if(!gNode.isMeta()) {
						listener.auxCellInserted(grid.getCellForNode(gNode));
					}
				}
				
				//Select just the last one inserted
				grid.getSelector().clearSelection();
				grid.getSelector().addToSelection(grid.getCellForNode(newNodes.get(newNodes.size()-1)));
				super.calculateArgumentAddresses();
				success = true;
			}
			
			super.operationFinished(success);
		}
	}
	
	protected class PickUpCellsOperation extends Operation {
		
		public PickUpCellsOperation(ArrayList<GNode> nodes) {
			super(nodes, getAddressOfCellNearSelection());
		}
		
		public Operation createInverse() {
			System.out.println("In PickUpCells, creating inverse InsertCells, insertAddress: " + super.getAddress());
			return new InsertCellsOperation(this);
		}
		
		/**
		 * After a selection has been made, this method is used to to remove the Cells from the Grid and
		 * add them to the CellDragger.
		 */
		public void subExecute() {
			boolean success = false;
			
			if(grid.isDraggingEnabled()) {
				CellDragger dragger = grid.getDragger();
				Selector selector = grid.getSelector();
				Cell nearCell = getCellNearSelection();
				
				dragger.addCells(getCellsFromNodes(super.getArgs()));
				implicitDelete();
				
				selector.getController().endMultiSelect();
				selector.getController().clearSelection();
				
				//attach the dragged selection to the cell above where it was detached
				selector.addToSelection(nearCell);
				
				grid.getCursor().placeAfter(nearCell, false);
				
				//notify listener
				ArrayList<Cell> justLifted = dragger.getTop().getCells();
				for (Cell cell : justLifted) {
					listener.auxCellLifted(cell);
				}
				
				success = true;
			} else { //Dragging was disabled, so just clear the selection
				Selector selector = grid.getSelector();
				Cell last = selector.getLastSelected();
				selector.getController().endMultiSelect();
				selector.getController().clearSelection();
				if(last != null)
					selector.addToSelection(last);
				
			}
			
			super.operationFinished(success);
		}
	}
	
	protected class ImplicitDeleteOperation extends ExplicitDeleteOperation {
		public ImplicitDeleteOperation(ArrayList<GNode> args) {
			super(args);
		}
		
		public ImplicitDeleteOperation(Operation inverseOp) {
			super(inverseOp);
		}
		
		protected void subExecute() {
			boolean success = false;
			if(super.getAntecedent() != null) {
				super.setAddress(super.getAntecedent().getAddress());
				super.setArgs(super.getAntecedent().getNodesFromArgumentAddresses());
			}
			
			ArrayList<GNode> deleteBuffer = new ArrayList<GNode>();
			for(GNode node : super.getArgs()) {
				Cell cell = grid.getCellForNode(node);
				if(cell != null && !cell.isDragging()) {
					cell.deselect();
				}
				deleteBuffer.add(node);
			}
			for (GNode node : deleteBuffer) {
				ArrayList<GNode> list = new ArrayList<GNode>();
				list.add(node);
				grid.getStructureManager().remove(list);
				success = true; //makes sure at least one node is deleted
			}
			
			super.operationFinished(success);
		}
	}
	
	protected class ExplicitDeleteOperation extends Operation implements EventListener {
		private static final int EVENT_DELETE_SHRINK = 555;
		private ArrayList<GNode> deleteList = new ArrayList<GNode>();
		
		public ExplicitDeleteOperation(ArrayList<GNode> args) {
			super(args, getAddressOfCellNearSelection());
		}
		
		public ExplicitDeleteOperation(Operation inverse) {
			super(inverse);
		}
		
		public Operation createInverse() {
			System.out.println("In Explicit/ImplicitDelete, creating inverse InsertCells, insertAddress: " + getAddress());
			return new InsertCellsOperation(this);
		}
		
		/**
		 * Initiates a delete operation preceded by an animation; finish(...) is where the process
		 * continues. 
		 * @return whether the operation was a success or not
		 */
		protected void subExecute() {
			boolean success = false;
			
			if(grid.isDeleteEnabled()) {
				if(getAntecedent() != null) {
					setAddress(getAntecedent().getAddress());
					setArgs(getAntecedent().getNodesFromArgumentAddresses());
					if(getArgs().isEmpty())
						return;
					if(grid.getStructureManager() instanceof AbstractionManager) {
						((AbstractionManager)grid.getStructureManager()).exposeCell(getArgs().get(0));
					}
				}
				
				for(GNode node : super.getArgs()) {
					Cell cell = grid.getCellForNode(node);
					if(cell != null) {
						if(!cell.isDragging()) {
							cell.deselect();
						}
						cell.shrinkAlongMainAxis(this, EVENT_DELETE_SHRINK);
						deleteList.add(node);
					} else {
						ArrayList<GNode> list = new ArrayList<GNode>();
						list.add(node);
						grid.getStructureManager().remove(list);
					}
					success = true;
				}
				
				grid.getSelector().getController().endMultiSelect();
			} 

			if(!success)
				super.operationFinished(false);
			else if(deleteList.isEmpty())
				super.operationFinished(true);
		}
		
		@Override
		public void finish(int id, int type) {
			GNode toDelete = null;
			for(GNode candidate : deleteList) {
				if(id == EVENT_DELETE_SHRINK) {
					toDelete = candidate;
					break;
				}
			}
			if(toDelete != null) {
				doDelete(toDelete);
			}

			deleteList.remove(toDelete);
		}
		
		private void doDelete(GNode toDelete) {
			Selector selector = grid.getSelector();
			Cell anchor = null; //The Cell to be selected when the delete operation completes
			Cell deleteCell = grid.getCellForNode(toDelete);
			
			if(deleteCell != null) {
				//get the cell after the one that was deleted
				if(grid.getCells().indexOf(deleteCell) != grid.getCells().size()-1) {
					anchor = selector.getCellAfter(deleteCell);
				} else if(grid.getCells().size() > 1) {
					anchor = selector.getCellBefore(deleteCell);
				}
				listener.auxCellDeleted(deleteCell);
			}
			
			ArrayList<GNode> list = new ArrayList<GNode>();
			list.add(toDelete);
			grid.getStructureManager().remove(list);
			deleteList.remove(toDelete);
			
			grid.getSelector().clearSelection();
			selector.addToSelection(anchor);
			
			if(deleteList.isEmpty()) //On the last cell
				super.operationFinished(true);
		}
		
		@Override
		public void update(int id, int type, float value) {
			
		}
	}
	
	protected abstract class Operation {
		private boolean addToUndo = true; //Whether the execution of this operation should impact the undo stack
		private ArrayList<GNode> args; //The Cells which this operation will operate on (short for 'arguments,' if that's not obvious)
		private ArrayList<ArrayList<Integer>> argumentAddresses; //Tree locations for the arguments (since the original node objects themselves may no longer exist at later points) 
		private ArrayList<Integer> address; //Tree location where the operation will take place
		private Operation antecedent; //If this operation was created as the inverse of another, 'antecedent' refers to the parent operation
		private int type;
		public static final int TYPE_NORMAL = 0;
		public static final int TYPE_UNDO = 1;
		public static final int TYPE_REDO = 2;
		private boolean executionSuccessful;
		private boolean executing;
		private boolean done;
		
		public Operation(ArrayList<GNode> nodes, ArrayList<Integer> address) {
			if(nodes != null)
				this.args = new ArrayList<GNode>(nodes);
			this.address = address;
			this.type = TYPE_NORMAL;
		}
		
		public Operation(Operation inverse) {
			this.antecedent = inverse;
		}
		
		public Operation(ArrayList<GNode> nodes) {
			this(nodes, null);
		}
		
		protected void calculateArgumentAddresses() {
			argumentAddresses = new ArrayList<ArrayList<Integer>>();
			for (GNode node : args) {
				argumentAddresses.add(node.getTreeAddress());
			}
		}
		
		public final void execute() {
			assert !executing : "Execute should never be called twice on the same Operation.";
			
			executing = true;
			subExecute();
		}
		
		protected abstract void subExecute();
		public abstract Operation createInverse();
		
		protected void setArgs(ArrayList<GNode> nodes) {
			this.args = new ArrayList<GNode>(nodes);
		}
		
		public ArrayList<GNode> getArgs() {
			return args;
		}
		
		protected void setAddress(ArrayList<Integer> address) {
			this.address = address;
		}
		
		public ArrayList<Integer> getAddress() {
			return address;
		}

		public boolean isExecuting() {
			return executing;
		}

		public boolean isDone() {
			return done;
		}

		protected void operationFinished(boolean success) {
			this.done = true;
			this.executionSuccessful = success;
		}

		public boolean isAddToUndo() {
			return addToUndo;
		}

		protected void setAddToUndo(boolean addToUndo) {
			this.addToUndo = addToUndo;
		}
		
		public ArrayList<ArrayList<Integer>> getArgumentAddresses() {
			return argumentAddresses;
		}
		
		public ArrayList<GNode> getNodesFromArgumentAddresses() {
			ArrayList<GNode> nodes = new ArrayList<GNode>();
			for (ArrayList<Integer> address : argumentAddresses) {
				GNode node = getNodeAtAddress(address);
				System.out.println("Address: " + address + ", safe node: " + node);
				nodes.add(node);
			}
			
			return nodes;
		}

		public void setAntecedent(Operation antecedent) {
			this.antecedent = antecedent;
		}
		
		public Operation getAntecedent() {
			return this.antecedent;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public boolean wasExecutionSuccessful() {
			return executionSuccessful;
		}
	}
	
	public void draw() {
		Color old = GIDEApp.SPRITE_BATCH.getColor();
		GIDEApp.SPRITE_BATCH.setColor(.25f, .25f, .25f, 0.85f);
		
		GLString codeString = new GLString("test", ResourceManager.getTypeFace("droid"),
				"plain", 14,
				new Color(0, 1f, .2f, 1), false);
		float x = 0;
		float width = 80;
		float height = codeString.getHeight();
		float origY = Gdx.graphics.getHeight() - height;
		float y = origY;
		for (Operation op : opQueue) {
			String type = getOperationTypeString(op);
			codeString = new GLString(type, ResourceManager.getTypeFace("droid"),
					"plain", 14,
					new Color(0, 1f, .2f, 1), false);
			Corpus.debugBox.draw(x, y, width, height);
			codeString.draw(x + width/2 - codeString.getTextWidth()/2, y + height/2 - codeString.getHeight()/2);
			y -= codeString.getTextHeight();
		}
		x += width + 5;
		y = origY;
		for (Operation op : undoStack) {
			String type = getOperationTypeString(op);
			codeString = new GLString(type, ResourceManager.getTypeFace("droid"),
					"plain", 14,
					new Color(0, 1f, .2f, 1), false);
			Corpus.debugBox.draw(x, y, width, height);
			codeString.draw(x + width/2 - codeString.getTextWidth()/2, y + height/2 - codeString.getHeight()/2);
			y -= height;
		}
		x += width + 5;
		y = origY;
		for (Operation op : redoStack) {
			String type = getOperationTypeString(op);
			codeString = new GLString(type, ResourceManager.getTypeFace("droid"),
					"plain", 14,
					new Color(0, 1f, .2f, 1), false);
			Corpus.debugBox.draw(x, y, width, height);
			codeString.draw(x + width/2 - codeString.getTextWidth()/2, y + height/2 - codeString.getHeight()/2);
			y -= height;
		}
		
		GIDEApp.SPRITE_BATCH.setColor(old);
	}
	
	protected String getOperationTypeString(Operation op) {
		String type = "OP";
		if(op instanceof ImplicitDeleteOperation)
			type = "impDel";
		else if(op instanceof ExplicitDeleteOperation)
			type = "expDel";
		else if(op instanceof PickUpCellsOperation)
			type ="pickUup";
		else if (op instanceof InsertCellsOperation)
			type = "insert";
		
		return type;
	}
	
	protected GNode getNodeAtAddress(ArrayList<Integer> address) {
		if(!grid.getCells().isEmpty())
			return grid.getCells().get(0).getNode().getRoot().getNodeAtAddress(address);
		else
			return null;
	}
	
	/**
	 * Gives the address of the Cell immediately before the first Cell of the active selection, or, if there is no such cell
	 * (i.e. at the top of the document), the Cell immediately after is used instead.
	 * @return
	 */
	protected ArrayList<Integer> getAddressOfCellNearSelection() {
		Selector selector = grid.getSelector();
		selector.sortSelectedCells();
		Cell firstCell = selector.getSelectedCells().get(0);
		Cell nearCell = null;
		if(grid.getCells().indexOf(firstCell) > 0)
			nearCell = selector.getCellBefore(firstCell);
		else
			nearCell = selector.getCellAfter(firstCell);
		
		if(nearCell != null)
			return nearCell.getNode().getTreeAddress();
		else {
			ArrayList<Integer> address = new ArrayList<Integer>();
			System.err.println("Just got to this code path, make sure this behavior is desired after all!");
			return address;
		}
	}
	
	protected Cell getCellNearSelection() {
		return grid.getCellForNode(getNodeAtAddress(getAddressOfCellNearSelection()));
	}
	
	public ArrayList<Integer> getAddressOfSelectedCell() {
		Selector selector = grid.getSelector();
		Cell anchor = selector.getLastSelected();
		GNode anchorNode = anchor != null ? anchor.getNode() : null;
		if(anchorNode != null)
			return anchorNode.getTreeAddress();
		else
			return null;
	}
	
	public ArrayList<Cell> getCellsFromNodes(ArrayList<GNode> nodes) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (GNode node : nodes) {
			Cell cell = grid.getCellForNode(node);
			if(cell != null)
				cells.add(cell);
		}
		return cells;
	}
	
	public ArrayList<GNode> getNodesFromCells(ArrayList<Cell> cells) {
		ArrayList<GNode> newNodes = new ArrayList<GNode>();
		for (Cell cell : cells) {
			newNodes.add(cell.getNode());
		}

		return newNodes;
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void setListener(Program program) {
		this.listener = program;
	}
	
	public Program getListener() {
		return listener;
	}
}