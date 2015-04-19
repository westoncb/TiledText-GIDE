package com.cyntaks.GDXGIDE.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.*;
import com.cyntaks.GDXGIDE.code.java.JavaLexer;
import com.cyntaks.GDXGIDE.config.CellConfig;
import com.cyntaks.GDXGIDE.gui.CellDisplay;
import com.cyntaks.GDXGIDE.gui.ContentLine;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.programs.CodeEditor;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.text.GLStringAtom;

public class CodeCell extends Cell {
	private GLString code; // the code to display (all leaf cells and embedded
							// cells have some)
	private ArrayList<CodeNode> codeList; 
	private boolean textCell;
	private float textWidth; // dimensions of the code contained by the cell
	private float textHeight;
	private float minWidth;
	private float maxWidth;
	private float minHeight;
	private float maxHeight;

	private String firstNodeText;
	private String lastNodeText;

	private static final Color WHITE_SPACE_COLOR = new Color(1, 1, 1, 1);
	private static ArrayList<CodeCell> scratch = new ArrayList<CodeCell>();
	private boolean drawMemAddress = false;

	private static float colorSeed;
	
	static {
		Random random = new Random(System.currentTimeMillis());
		random.nextFloat();
		colorSeed = random.nextFloat();
	}
	
	public CodeCell(Grid owner, CodeNode t) {
		super(owner, t);

		super.setWidth(1);
		super.setHeight(1);
		super.setDebug(false);
	}

	public void init() {
		super.init();
		
		buildCodeList((CodeNode)getNode());
		
		int orientation = getOwner().getDisplay().getOrientation();
		int lineIndex = orientation == GridDisplay.HORIZONTAL ? CellDisplay.LINE_LEFT : CellDisplay.LINE_TOP;
		ContentLine line = super.getDisplay().getContentLine(
				CellDisplay.LAYER_MIDDLE, lineIndex);
		line.setAttachLocation(ContentLine.ATTACH_BEGINNING);
		line.add(code);
	}
	
	public void setTextFromCodeList() {
		setText(codeList, true);
	}
	
	private void setText(ArrayList<CodeNode> list, boolean reAdd) {
		if(getOwner() == null)
			System.err.println("null parent: " + this.getNode());
		
		int orientation = getOwner().getDisplay().getOrientation();
		int lineIndex = orientation == GridDisplay.HORIZONTAL ? CellDisplay.LINE_LEFT : CellDisplay.LINE_TOP;
		ContentLine line = super.getDisplay().getContentLine(
				CellDisplay.LAYER_MIDDLE, lineIndex);
		line.setAttachLocation(ContentLine.ATTACH_BEGINNING);
		if (reAdd)
			line.clear();

		GLStringAtom[] atoms = buildAtomsFromNodes(list);
		GLString codeString = new GLString(atoms, false);
		codeString.setHorizontalAlignment(GLString.LEFT);
		codeString.setVerticalAlignment(GLString.CENTER);
		codeString.setLockFontSize(true);
		codeString.setDebug(false);
		codeString.setAllowTruncation(false);

		textWidth = codeString.getTextWidth();
		textHeight = codeString.getTextHeight();
		this.code = codeString;
		textCell = true;

		if (reAdd) {
			line.add(code);
		}
		
		//This is necessary to get text positioned before rendering in some circumstances; if this
		//ends up being too costly, better figure out the circumstances where the explicit
		//update is required and just do it then (unlikely though). I added this because of
		//some text flashing onto the screen at 0, 0 after it had been updated; it came from
		//formatNewCells in CodeGrid, specifically from the cell before or after the new block
		//to be formatted.
		getDisplay().update(0, getAbsX(), getAbsY(), getWidth(), getHeight());
	}
	
	private GLStringAtom[] buildAtomsFromNodes(ArrayList<CodeNode> nodes) {
		CellConfig config = super.getConfig();
		ArrayList<GLStringAtom> atoms = new ArrayList<GLStringAtom>();
		
		for (int i = 0; i < nodes.size(); i++) {
			CodeNode node = nodes.get(i);
			String text = null;
			if(node.hasAlternateText())
				text = node.getAlternateText();
			else
				text = node.getText();
			
			if(node.getType() == 1) { //newline
				GLStringAtom atom = new GLStringAtom(GLStringAtom.NEWLINE, false);
				atom.setColor(WHITE_SPACE_COLOR);
				atoms.add(atom);
			} else { //everything else
				Color color;

				int id = node.getTokenType();
				if(node.isLetter())
					id = node.getParent().getTokenType();
				
				if(node.isWhiteSpace())
					color = WHITE_SPACE_COLOR;
				else if(id == JavaLexer.IDENTIFIER && (node.getText().charAt(0) == node.getText().toUpperCase().charAt(0) && 
                        !node.getText().equals(node.getText().toUpperCase()))) {
						color = new Color(252/255f, 120/255f, 120/255f, 1);
						
				} else if(id == JavaLexer.WHILE || id == JavaLexer.FOR || id == JavaLexer.DO || 
						  id == JavaLexer.BREAK || id == JavaLexer.CONTINUE || id == JavaLexer.IF ||
						  id == JavaLexer.ELSE || id == JavaLexer.SWITCH || id == JavaLexer.CASE ||
						  id == JavaLexer.DEFAULT) {
					color = new Color(118/255f, 96/255f, 253/255f, 1);
				} else if(id == JavaLexer.FLOAT || id == JavaLexer.INT || id == JavaLexer.DOUBLE ||
						  id == JavaLexer.LONG || id == JavaLexer.SHORT || id == JavaLexer.BOOLEAN || 
						  id == JavaLexer.BYTE || id == JavaLexer.CHAR) {
					color = new Color(76/255f, 181/255f, 118/255f, 1);
				} else if(id == JavaLexer.INTLITERAL || id == JavaLexer.FLOATLITERAL || 
						  id == JavaLexer.DOUBLELITERAL || id == JavaLexer.PACKAGE ||
						  id == JavaLexer.STRINGLITERAL || id == JavaLexer.CHARLITERAL) {
					color = new Color(185/255f, 255/255f, 0/255f, 1);
				} else if(id == JavaLexer.IMPORT) {
					color = new Color(125/255f, 195/255f, 0/255f, 1);
				} else if (id == JavaLexer.PRIVATE ||
						   id == JavaLexer.PROTECTED || id == JavaLexer.PUBLIC || id == JavaLexer.NEW ||
						   id == JavaLexer.STATIC || id == JavaLexer.FINAL || id == JavaLexer.EXTENDS ||
						   id == JavaLexer.IMPLEMENTS) {
					color = new Color(4/255f, 208/255f, 208/255f, 1);
				} else if(id == JavaLexer.DOT || id == JavaLexer.COMMA) {
					color = new Color(1, 1, 1, 1);
				} else if(id == JavaLexer.LT || id == JavaLexer.GT || id == JavaLexer.EQ || id == JavaLexer.EQEQ ||
						  id == JavaLexer.PLUS || id == JavaLexer.PLUSPLUS || id == JavaLexer.PLUSEQ ||
						  id == JavaLexer.STAR || id == JavaLexer.STAREQ || id == JavaLexer.SUB ||
						  id == JavaLexer.SUBSUB || id == JavaLexer.SUBEQ || id == JavaLexer.BANGEQ ||
						  id == JavaLexer.BANG || id == JavaLexer.BARBAR || id == JavaLexer.BAR ||
						  id == JavaLexer.BAREQ || id == JavaLexer.AMP || id == JavaLexer.AMPAMP ||
						  id == JavaLexer.AMPEQ || id == JavaLexer.QUES || id == JavaLexer.COLON) {
					color = new Color(115/255f, 160/255f, 255/255f, 1);
				} else {
					color = new Color(190/255f, 235/255f, 255/255f, 1);
				}

				Random random = new Random((int)(node.getToken().getType() * colorSeed));
				random.nextFloat();
				float red = modRand(1, random.nextFloat());
				float green = modRand(Math.round(colorSeed), random.nextFloat());
				float blue = modRand(Math.round(colorSeed), random.nextFloat());
				color = new Color(red, green, blue, 1);
				
				int fontSize = config.getFontSize();
				
				atoms.add(new GLStringAtom(text, config.getTypeFace(), config.getFontStyle(), fontSize, color));
			}
		}
		
		GLStringAtom[] atomArray = new GLStringAtom[atoms.size()];
		int index = 0;
		for (GLStringAtom glStringAtom : atoms) {
			atomArray[index] = glStringAtom;
			index++;
		}
		
		return atomArray;
	}
	
	private float modRand(int type, float rand) {
		if(type == 1) {
			return rand*.65f + .35f;
		} else {
			return rand*.35f + .65f;
		}
	}
	
	public void stripWhiteSpace() {
		ArrayList<CodeNode> remove = new ArrayList<CodeNode>();
		for (CodeNode codeNode : codeList) {
			if(codeNode.isWhiteSpace())
				remove.add(codeNode);
		}
		
		for(CodeNode node : remove)
			codeList.remove(node);
	}
	
	public void trimWhitespaceFromBeginning() {
		int index = 0;
		ArrayList<CodeNode> remove = new ArrayList<CodeNode>();
		while(index < codeList.size() && codeList.get(index).isWhiteSpace()) {
			remove.add(codeList.get(index));
			index++;
		}
			
		for(CodeNode node : remove)
			codeList.remove(node);
		
		this.setTextFromCodeList();
	}
	
	public void trimWhitespaceFromEnd() {
		int index = codeList.size()-1;
		ArrayList<CodeNode> remove = new ArrayList<CodeNode>();
		while(index > -1 && codeList.get(index).isWhiteSpace()) {
			remove.add(codeList.get(index));
			index--;
		}
			
		for(CodeNode node : remove)
			codeList.remove(node);
		
		this.setTextFromCodeList();
	}
	
	public CodeNode getNode() {
		return (CodeNode)super.getNode();
	}

	private void calculateSpatialConstraints() {
		Grid parentGrid = super.getOwner();
		int orientation = parentGrid.getDisplay().getOrientation();
		Window parentWindow = parentGrid.getWindow();

		if (orientation == GridDisplay.HORIZONTAL) {
			minHeight = parentWindow.getHeight();
			maxHeight = minHeight;
			minWidth = 0;

			int numCells = parentGrid.getCells().size();
			int colsPerScreen = parentGrid.getColsPerScreen();
			float availableWidth = parentWindow.getWidth();
			if (numCells < colsPerScreen)
				maxWidth = availableWidth / numCells - (numCells - 1)
						* parentGrid.getDisplay().getGapSize();
			else
				maxWidth = availableWidth / colsPerScreen - (colsPerScreen - 1)
						* parentGrid.getDisplay().getGapSize();
		} else {
			minWidth = parentWindow.getWidth();
			maxWidth = minWidth;
			minHeight = 0;

			int numCells = parentGrid.getCells().size();
			int rowsPerScreen = parentGrid.getRowsPerScreen();
			float availableHeight = parentWindow.getHeight();
			if (numCells < rowsPerScreen)
				maxHeight = availableHeight / numCells - (numCells - 1)
						* parentGrid.getDisplay().getGapSize();
			else
				maxHeight = availableHeight / rowsPerScreen
						- (rowsPerScreen - 1)
						* parentGrid.getDisplay().getGapSize();
		}

		if (maxHeight < 0)
			maxHeight = 0;
		if (maxWidth < 0)
			maxWidth = 0;
	}
	
	public void buildCodeList(CodeNode node) {
		codeList = new ArrayList<CodeNode>();
		buildCodeListAux(node, codeList);
	}
	
	public boolean hasCollapsedNode() {
		for (CodeNode node : codeList) {
			if(node.getType() == GNode.COLLAPSE_STRING)
				return true;
		}
		
		return false;
	}

	private void buildCodeListAux(CodeNode node, ArrayList<CodeNode> list) {
		AbstractionManager manager = (AbstractionManager)getOwner().getStructureManager();
		
		if (!node.isStructureSpecificationLeaf() && (node.isLeaf() || node.getChild(0).isLetter() || node.hasAlternateText())) {
			list.add(node);
		} else if (node.isCollapsable() && (!CodeEditor.UNIFORM_ABSTRACTION_LEVEL || node.getAbstraction() >= manager.getAbstractionIndex()) && !node.isStructureSpecificationLeaf()) {
			list.add(new CodeNode("...", true, GNode.COLLAPSE_STRING));
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				buildCodeListAux(node.getChild(i), list);
			}
		} 
	}
	
	public boolean lineBetween(CodeCell next) {
		if(codeList.isEmpty()) {
			System.err.println("something funky goin' on here...");
			return false;
		}
		
		GNode nodeBefore = codeList.get(codeList.size()-1);
		GNode nodeAfter = next.getCodeList().get(0);
		
		if(nodeBefore.getType() == GNode.NEWLINE || nodeAfter.getType() == GNode.NEWLINE)
			return true;
		else
			return false;
	}
	
	public int getOrientation() {
		CodeCell cellAfter = (CodeCell)getOwner().getSelector().getCellAfter(this);
		if(cellAfter == null)
			cellAfter = this;
		
		if(this.getNode().isMeta() || cellAfter.getNode().isMeta() || lineBetween(cellAfter))
			return GridDisplay.VERTICAL;
		else
			return GridDisplay.HORIZONTAL;
	}
	
	public void drawTopLayer() {
		super.drawTopLayer();
		
		if(super.isDebug()) {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(0, 1, 0, 0.2f);
			debugBox.draw(getAbsX(), getAbsY(), getWidth(), getHeight());
			GIDEApp.SPRITE_BATCH.setColor(old);
		}
		
		if(drawMemAddress) {
			String address = super.parentToString();
			String text = address.substring(address.lastIndexOf("@") + 1, address.length());
			renderSomeText(text);
		} else if(super.isSelected()) {
			String first = "";
			if(super.getNode().getParent() != null)
				first = super.getNode().getParent() + "->";
			//renderSomeText(first + super.getNode().getText());
		}
	}
	
	private void renderSomeText(String text) {
		CellConfig config = super.getConfig();
		GLString codeString = new GLString(text, ResourceManager.getTypeFace("droid"),
				config.getFontStyle(), 14,
				new Color(0, 1f, .2f, 1), false);
		
		Color old = GIDEApp.SPRITE_BATCH.getColor();
		GIDEApp.SPRITE_BATCH.setColor(.25f, .25f, .25f, 0.85f);
		debugBox.draw(getAbsX(), getAbsY(), codeString.getTextWidth(), codeString.getTextHeight());
		GIDEApp.SPRITE_BATCH.setColor(old);
		codeString.draw(getAbsX(), getAbsY());
	}
 
	public void load() {
		super.load();

		init();

		//if (!this.isEmbedded())
			//super.setUnderGraphic(super.getConfig()
				//	.getUnderGraphicInstance());
		//super.getUnderGraphic().load();
	}

	public void resize(float width, float height) {
		if(getWidth() != width || getHeight() != height) {
			super.resize(width, height);
		
			if(code != null)
				code.setSpatialConstraints(width, height);
				
			super.getUnderGraphic().setWidth(width);
			super.getUnderGraphic().setHeight(height);
		}
	}
	
	public CodeGrid getOwner() {
		return (CodeGrid)super.getOwner();
	}
	
	public void dispose() {
		if(!super.isDragging()) { //the cells were just deleted from the grid in this case: don't null everything
			super.dispose();
			code = null;
		}
	}
	
	public void brightenUnderGraphic(float increment) {
		float r = super.getUnderGraphic().getRed() + increment;
		float g = super.getUnderGraphic().getGreen() + increment;
		float b = super.getUnderGraphic().getBlue() + increment;
		float a = super.getUnderGraphic().getAlpha();
		if(r > 1)
			r = 1;
		if(g > 1)
			g = 1;
		if(b > 1)
			b = 1;
		super.getUnderGraphic().setColor(r, g, b, a);
	}
	
	public float getTopWindowWidth() {
		return getOwner().getWindow().getWidth();
	}
	
	public float getTopWindowHeight() {
		return getOwner().getWindow().getHeight();
	}
	
	public boolean isTextCell() {
		return textCell;
	}

	public void update(float delta) {
		super.update(delta);

		if(isDragging())
			fitToContents(false);
	}

	public void enforceSpatialConstraints() {
		calculateSpatialConstraints();
		resize(constrainWidth(getWidth()), constrainHeight(getHeight()));
	}

	public void enforceMinSize() {
		calculateSpatialConstraints();
		float width = getWidth();
		float height = getHeight();
		if (width < minWidth)
			width = minWidth;
		if (height < minHeight)
			height = minHeight;
		resize(width, height);
	}

	public void fitToContents(boolean enforceMinimums) {
		calculateSpatialConstraints();

		textWidth = code.getTextWidth();
		textHeight = code.getTextHeight();

		if(enforceMinimums) {
			if (textWidth < minWidth)
				textWidth = minWidth;
			if (textHeight < minHeight)
				textHeight = minHeight;
		}

		resize(textWidth, textHeight);
	}

	private float constrainWidth(float width) {
		if (width < minWidth)
			width = minWidth;
		else if (width > maxWidth)
			width = maxWidth;

		return width;
	}

	private float constrainHeight(float height) {
		if (height < minHeight)
			height = minHeight;
		else if (height > maxHeight)
			height = maxHeight;

		return height;
	}
	
	public GLString getCode() {
		return code;
	}

	public String getFirstNodeText() {
		return firstNodeText;
	}

	public String getLastNodeText() {
		return lastNodeText;
	}
	
	public float getAbsY() {
		return super.getAbsY();
	}
	
	public float getBlue() {
		return super.getUnderGraphic().getBlue();
	}
	
	public float getRed() {
		return super.getUnderGraphic().getRed();
	}
	
	public float getGreen() {
		return super.getUnderGraphic().getGreen();
	}
	
	public float getAlpha() {
		return super.getUnderGraphic().getAlpha();
	}
	
	public ArrayList<CodeNode> getCodeList() {
		return codeList;
	}
	
	public void setCodeList(ArrayList<CodeNode> list) {
		this.codeList = list;
	}
}