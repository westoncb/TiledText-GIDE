package com.cyntaks.GDXGIDE.code;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.CodeGridController;
import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.Grid;
import com.cyntaks.GDXGIDE.config.Settings;
import com.cyntaks.GDXGIDE.gui.FlowGridDisplay;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.input.DefaultInputHandler;

public class CodeGrid extends Grid {
	private static CodeFormatter formatter;

	public CodeGrid() {
		super();
		super.setController(new CodeGridController(this));
		super.setOrientationSource(NODE_DETERMINED);
		GridDisplay display = new FlowGridDisplay();
		display.setAnimate(false);
		super.setGridDisplay(display);
	}
	
	public ArrayList<Cell> addCells(ArrayList<GNode> nodes, GNode anchor) {
		return addCells(nodes, anchor, false);
	}
	
	public ArrayList<Cell> addCells(ArrayList<GNode> nodes, GNode anchor, boolean addBefore) {
		return addCells(nodes, anchor, true, false, addBefore);
	}
	
	public ArrayList<Cell> addCells(ArrayList<GNode> nodes, GNode anchor, boolean format, boolean glow, boolean addBefore) {
		ArrayList<Cell> newCells = new ArrayList<Cell>();
		
		if(super.isLoaded()) {
			CodeNode.expandMetaNodes(nodes);
			
			for(int i = nodes.size()-1; i > -1; i--) {
				CodeNode node = (CodeNode)nodes.get(i);
				
				CodeCell cell = new CodeCell(this, node);
				cell.setConfig(super.getCellConfig());
				cell.changeBehavior(super.getCellConfig().getBehaviorInstance());
				cell.load();
				if(!super.isAddingInitialNodes()) //We'll initialize the initial Cells in init() 
					cell.init();
				if(glow) {
					//cell.getUnderGraphic().setAlpha(1);
					//cell.getUnderGraphic().setDestAlpha(0, .75f, false);
				}
				newCells.add(0, cell);
				int addIndex = findAddIndex(anchor);
				if(addBefore)
					addIndex -= 1;
				super.getCells().add(addIndex, cell);
			}

			if(!super.isAddingInitialNodes())
				formatNewCells(newCells, format); //Do the initial formatting in init() as well

		} else {
			for(GNode t : nodes)
				super.getInitialNodes().add(t);
		}
		
		return newCells;
	}
	
	public void load() {
		Settings.getNodeConfig("java"); //just taking care of the loading here
		if(formatter == null)
			formatter = new CodeFormatter("java");
		
		super.load(); //this is going to add any cells that are queued -- want the formatter constructed first
	}
	
	public void init() {
		super.init();
		
		formatNewCells(getCells(), true);
	}
	
	public void removeCells(ArrayList<GNode> nodes) {
		CodeNode.expandMetaNodes(nodes);
		super.removeCells(nodes);
	}
	
	public void formatAroundCursor() {
		if(getCursor().getInsertCell() != null) {
			ArrayList<Cell> cellList = new ArrayList<Cell>();
			cellList.add(getCursor().getInsertCell());
			formatNewCells(cellList, true);
		}
	}
	
	public void formatNewCells(ArrayList<Cell> newCells, boolean format) {
		if(!newCells.isEmpty()) {
			ArrayList<CodeNode> codeList = new ArrayList<CodeNode>();
			ArrayList<Cell> cells = getCells();
			int startIndex = cells.indexOf(newCells.get(0)); 
			
			int endIndex = cells.indexOf(newCells.get(newCells.size()-1));
			if(startIndex > 0) {
				CodeCell cell = (CodeCell)cells.get(startIndex-1);
				newCells.add(0, cell);
			}
			
			if(endIndex < cells.size()-1) {
				CodeCell cell = (CodeCell)cells.get(endIndex+1);
				newCells.add(cell);
			}
			
			for(Cell cell : newCells) {
				for(CodeNode node : ((CodeCell)cell).getCodeList())
					codeList.add(node);
			}
			
			if(formatter != null && format) {
				formatter.addWhiteSpace(codeList, newCells);
			}
			
			for(Cell cell : newCells)
				((CodeCell)cell).setTextFromCodeList();
		}
	}
	
	public void configureAppearance() {
		getDisplay().configureAppearance(GridDisplay.VERTICAL, 1, 0);
	}
	
	/**
	 * Overridden to do nothing here: since CodeGrids don't use uniform Cell sizes, their behavior
	 * regarding Cell sizes is fundamentally different from the basic Grids.
	 */
	public void updateCellSizes() {
		
	}
	
	public static CodeFormatter getCodeFormatter() {
		if(formatter == null)
			formatter = new CodeFormatter("java");
		
		return formatter;
	}
	
	public void setInputHandler(DefaultInputHandler handler) {
		handler.setSelector(new CodeSelector(this));
		super.setInputHandler(handler);
	}
	
	public String toString() {
		String s = "";
		int count = 0;
		for (Cell cell : getCells()) {
			CodeCell c = (CodeCell)cell;
			s += "" + count + ": " + c.getNode().toString() + "\n";
			count++;
		}
		return s;
	}
}