package com.cyntaks.GDXGIDE.programs;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.Grid;
import com.cyntaks.GDXGIDE.GridController;
import com.cyntaks.GDXGIDE.InputAbstractor;
import com.cyntaks.GDXGIDE.Program;
import com.cyntaks.GDXGIDE.ProgramConfig;
import com.cyntaks.GDXGIDE.TextCell;
import com.cyntaks.GDXGIDE.Window;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.input.DefaultInputHandler;

public class MessageProgram extends Program {
	private Window parent;
	
	public MessageProgram() {
		super(new ProgramConfig("config/programs/message.xml"));
	}

	public void setInput(String[] messages, Window parent) {
		super.setInput(messages);
		this.parent = parent;
	}
	
	public void load() {
		setupAppearance();
	}
	
	public void init() {
		Grid main = getActiveGrid();
		main.setInputHandler(new MessageInputHandler(main.getController()));
		main.setDeleteEnabled(true);
		main.setDraggingEnabled(true);
		main.setMultiSelectEnabled(true);
		main.setOrientation(GridDisplay.VERTICAL);
		embed(parent, Window.HORIZONTAL, 20, true);
	}
	
	public void dispose() {
		
	}
	
	private void setupAppearance() {
		Window window = super.getRootWindow();
		window.setX(Gdx.graphics.getWidth()/2 - window.getWidth()/2);
		window.setY(Gdx.graphics.getHeight()/2 - window.getHeight()/2);
		window.setColor(.75f, .75f, .75f, .75f);
	}
	
	public void selectionSet(ArrayList<Cell> selection) {
		
	}

	@Override
	public void cellActivated(Cell cell, int typeID) {
	}

	@Override
	public void cellLifted(Cell cell) {

	}

	@Override
	public void cellInserted(Cell cell) {
		
	}

	@Override
	public void cellDeleted(Cell cell) {
		
	}

	@Override
	public void update(float delta) {
	}
	
	@Override
	public void cellSelected(Cell cell) {
		
	}

	@Override
	public void cellDeselected(Cell cell) {
		
	}
	
	@Override
	public void cellSwitchedTo(Cell cell) {
		
	}

	@Override
	public void cellSwitchedFrom(Cell cell) {
		
	}

	private void kill() {
		GIDEApp.disposeProgram(this);
	}
	
	public void embedCompleted() {
		Grid grid = getActiveGrid();
		for (Cell cell : grid.getCells()) {
			TextCell tCell = (TextCell)cell;
			tCell.updateTextBounds();
			tCell.getText().enforceSpatialConstraints();
		}
	}
	
	public void unEmbedCompleted() {
		kill();
	}

	private class MessageInputHandler extends DefaultInputHandler {

		public MessageInputHandler(GridController controller) {
			super(controller);
		}
		
		public void symbolActivated(String symbol) {
			super.symbolActivated(symbol);
			
			if(symbol.equalsIgnoreCase(InputAbstractor.GENERIC_SYMBOL_1)) {
				unEmbed(800);
			}
		}
	}
}
