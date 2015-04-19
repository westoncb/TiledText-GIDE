package com.cyntaks.GDXGIDE.programs;

import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.CodeGridController;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.Grid;
import com.cyntaks.GDXGIDE.GridController;
import com.cyntaks.GDXGIDE.InputAbstractor;
import com.cyntaks.GDXGIDE.Program;
import com.cyntaks.GDXGIDE.ProgramConfig;
import com.cyntaks.GDXGIDE.ResourceManager;
import com.cyntaks.GDXGIDE.Scroller;
import com.cyntaks.GDXGIDE.Task;
import com.cyntaks.GDXGIDE.Selector.CyclingController;
import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.code.ANTLR.Grammar;
import com.cyntaks.GDXGIDE.code.ANTLR.GrammarWalker;
import com.cyntaks.GDXGIDE.code.ANTLR.SkeletonSourceBuilder;
import com.cyntaks.GDXGIDE.code.java.*;
import com.cyntaks.GDXGIDE.input.DefaultInputHandler;
import com.cyntaks.GDXGIDE.util.GLImage;
import com.cyntaks.GDXGIDE.util.SwingTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CodeEditor extends Program {
	public static Grammar jGrammar;
	public static final boolean UNIFORM_ABSTRACTION_LEVEL = false;
	
	public CodeEditor() {
		super(new ProgramConfig("config/programs/start.xml"));
	}
	
	public void load() {
		jGrammar = new Grammar("grammar stuff/Java.g", "compilationUnit");
		jGrammar.load();
		//new SwingTree(jGrammar.getTree());
		//GrammarWalker walker = new GrammarWalker(jGrammar.getRule("compilationUnit"));
		//walker.walk();
		//System.out.println("THE WALK:");
		//System.out.println(walker.getGeneratedText());
		//System.out.println("DONE WALKING.");
		
		//SkeletonSourceBuilder builder = new SkeletonSourceBuilder(jGrammar, "compilationUnit", "WSTATEMENT");
		
		ResourceManager.queueTask(new Task() {
			public Object doTask() {
				CommonTree commonRoot = buildANTLRTree();
				//new SwingTree(commonRoot);
				CodeNode root = CodeNode.copyTree(commonRoot);
				setInput(root);
				return null;
			}
			
		}, "buildGrammarTree");
	}
	
	private CommonTree buildANTLRTree() {
		Tree t = null;
		ANTLRInputStream in = null;
		try {
			in = new ANTLRInputStream(new FileInputStream(new File("startpoint/Program.java")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long start = System.nanoTime();
		JavaLexer lexer = new JavaLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		
		try {
			JavaParser.compilationUnit_return result = parser.compilationUnit();
			t = (Tree)result.getTree();
			//System.out.println(t.toStringTree());
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		
		CommonTree commonRoot = new CommonTree(new CommonToken(-1, "WCOMPILATIONUNIT"));
		for(int i = 0; i < t.getChildCount(); i++)
			commonRoot.addChild(t.getChild(i));
		
		//new SwingTree(commonRoot);
		
		System.out.println(((System.nanoTime()-start)/1000000) + " millis.");
		
		return commonRoot;
	}

	@Override
	public void init() {
		Grid main = getActiveGrid();
		main.setDrawCursor(true);
		main.setInputHandler(new CodeEditorInputHandler(main.getController()));
		main.getScroller().setTrackMode(Scroller.MODE_TEXT_EDITOR);
		main.getScroller().setXTrackSpeed(3);
		main.setMultiSelectEnabled(true);
		((CyclingController)main.getSelector().getController()).setTrackSequence(true);
		((CyclingController)main.getSelector().getController()).setTrackX(false);
		((CyclingController)main.getSelector().getController()).setTrackY(false);
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
	public void cellSelected(Cell cell) {
		
	}	

	@Override
	public void cellDeselected(Cell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellSwitchedTo(Cell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellSwitchedFrom(Cell cell) {
		// TODO Auto-generated method stub
		
	}
	
	public void selectionSet(ArrayList<Cell> selection) {
		
	}
	
	public void abstractionIncreased() {
		ResourceManager.getSound("abstract").play(0.3f);
	}
	
	public void abstractionDecreased() {
		ResourceManager.getSound("abstract").play(0.3f);
	}
	
	public void errorOccurred(LinkedList<String> errors) {
		String[] messages =  new String[errors.size()];
		int index = 0;
		for (String string : errors) {
			messages[index++] = string;
		}
		
		MessageProgram message = new MessageProgram();
		message.setInput(messages, this.getRootWindow());
		GIDEApp.executeProgram(message);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void dispose() {
		
	}
	
	private class CodeEditorInputHandler extends DefaultInputHandler {

		public CodeEditorInputHandler(GridController controller) {
			super(controller);
		}
		
		public void symbolActivated(String symbol) {
			super.symbolActivated(symbol);
			
			if(symbol.equalsIgnoreCase(InputAbstractor.GENERIC_SYMBOL_1)) {
				((CodeGridController)super.getGridController()).decreaseAbstraction();
				getSelector().getController().endMultiSelect();
			}
		}
		
		public void activatePressed() {
			((CodeGridController)super.getGridController()).increaseAbstraction();
		}
		
		public void selectNextPressed() {
			((CyclingController)getSelector().getController()).requestSelectNext();
		}
		
		public void selectPreviousPressed() {
			((CyclingController)getSelector().getController()).requestSelectPrevious();
		}
		
		public void selectUpPressed() {
		}
		
		public void selectDownPressed() {
		}
		
		public void selectLeftPressed() {
		}
		
		public void selectRightPressed() {
		}
	}
}