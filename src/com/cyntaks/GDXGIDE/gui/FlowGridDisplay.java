package com.cyntaks.GDXGIDE.gui;

import java.util.ArrayList;

import com.cyntaks.GDXGIDE.Corpus;
import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.code.CodeCell;
import com.cyntaks.GDXGIDE.code.CodeNode;

public class FlowGridDisplay extends GridDisplay{
	
	public void setContents(ArrayList incomingCells) {
		ArrayList<Corpus> rows = new ArrayList<Corpus>();
		GridDisplay row = getNewRow();
		
		ArrayList<Corpus> cells = new ArrayList<Corpus>();
		Corpus pendingAfterBlock = null;
		for(Object o : incomingCells) {
			CodeCell cell = (CodeCell)o;
			if(cell.getNode().isMeta())
				continue;
			ArrayList<CodeNode> code = cell.getCodeList();

			if(code.get(0).getType() == GNode.NEWLINE) {
				row.setContents(cells);
				rows.add(row);
				
				//spacer stuff
				if(pendingAfterBlock != null) {
					rows.add(pendingAfterBlock);
					pendingAfterBlock = null;
				}
				
				cells = new ArrayList<Corpus>();
				row = getNewRow();
			}
			
			//spacer stuff
			if(cell.getOrientation() == GridDisplay.VERTICAL) {
				if(cell.getBeforeBlock() != null) {
					rows.add(cell.getBeforeBlock());
					cell.setBeforeBlock(null);
				}
				if(cell.getAfterBlock() != null) {
					pendingAfterBlock = cell.getAfterBlock();
					cell.setAfterBlock(null);
				}
			}
			
			if(!cell.isHeightChanging() && !cell.isWidthChanging())
				cell.fitToContents(false);
			cells.add(cell);
			
			if(code.get(code.size()-1).getType() == GNode.NEWLINE) {
				row.setContents(cells);
				
				rows.add(row);

				//spacer stuff
				if(pendingAfterBlock != null) {
					rows.add(pendingAfterBlock);
					pendingAfterBlock = null;
				}
				
				cells = new ArrayList<Corpus>();
				row = getNewRow();
			} else if(incomingCells.indexOf(cell) == incomingCells.size()-1) {
				//spacer stuff
				if(pendingAfterBlock != null) {
					rows.add(pendingAfterBlock);
					pendingAfterBlock = null;
				}
				
				row.setContents(cells);
				rows.add(row);
			}
		}
		
		super.setContents(rows);
	}
	
	private GridDisplay getNewRow() {
		GridDisplay row = new GridDisplay();
		row.configureAppearance(GridDisplay.HORIZONTAL, 1, 0);
		return row;
	}
	
	public void update(float delta, float x, float y, float width, float height) {
		ContentLine contents = super.getContentLine(0);
		
		for(Corpus corpus : contents) {
			if(corpus instanceof GridDisplay) {
				GridDisplay display = (GridDisplay)corpus;
				display.update(delta, x, y, width, height);
				display.setWidth(display.getGridWidth());
				display.setHeight(display.getGridHeight());
			}
		}
		
		super.update(delta, x, y, width, height);
		
		for(Corpus corpus : contents) {
			if(corpus instanceof GridDisplay) {
				GridDisplay display = (GridDisplay)corpus;
				display.update(delta, x, y, width, height);
				display.setWidth(display.getGridWidth());
				display.setHeight(display.getGridHeight());
			}
		}
	}
}
