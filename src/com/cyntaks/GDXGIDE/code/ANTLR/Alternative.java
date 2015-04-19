package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;

public class Alternative {
	private static final String TERMINAL_START = "\'";
	private static final String CLOSURE_STRING = "*";
	private static final String POSITIVE_CLOSURE_STRING = "+";
	private static final String OPTIONAL_STRING = "?";
	private static final String BLOCK_STRING = "BLOCK";
	private static final String TREE_STRING = "TREE_BEGIN";
	private static final String END_OF_ALTERNATIVE = "EOA";
	
	private String[] ruleRefs;
	private String[] terminals;
	private Block[] blocks;
	private Block[] closureBlocks;
	private Block[] pClosureBlocks;
	private Block[] optionalBlocks;
	private TreeAlternative[] trees;
	
	private ArrayList paths;
	private ArrayList<Integer> pathTypes;
	
	public static final int PATH_BLOCK = 0;
	public static final int PATH_CLOSURE = 1;
	public static final int PATH_POSITIVE_CLOSURE = 2;
	public static final int PATH_OPTIONAL = 3;
	public static final int PATH_REF = 4;
	public static final int PATH_TERMINAL = 5;	
	private Rule owner;
	
	public Alternative(CommonTree altNode, Rule owner) {
		this.owner = owner;
		
		processChildren(altNode, true);
		processChildren(altNode, false);
	}
	
	private void processChildren(CommonTree altNode, boolean creationPass) {

		int terminalCount = 0;
		int ruleRefsCount = 0;
		int blocksCount = 0;
		int closureBlocksCount = 0;
		int pClosureBlocksCount = 0;
		int optionalBlocksCount = 0;
		int treeCount = 0;
		for (int i = 0; i < altNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)altNode.getChild(i);
			
			if(child.getText().startsWith(TERMINAL_START) || child.getText().equals("..")) {
				if(!creationPass) {
					if(child.getText().equals(".."))
						terminals[terminalCount] = "X..X";//child.getChild(0).getText().substring(1, child.getText().length()-1);
					else
						terminals[terminalCount] = child.getText().substring(1, child.getText().length()-1);
					paths.add(terminals[terminalCount]);
					pathTypes.add(PATH_TERMINAL);
				}
				
				terminalCount++;
			} else if(child.getChildCount() > 0 && child.getText().equals(BLOCK_STRING)) {
				if(!creationPass) {
					blocks[blocksCount] = new Block(child, owner);
					paths.add(blocks[blocksCount]);
					pathTypes.add(PATH_BLOCK);
				};
				
				blocksCount++;
			} else if(child.getText().equals(CLOSURE_STRING)) {
				if(!creationPass) {
					closureBlocks[closureBlocksCount] = new Block((CommonTree)child.getChild(0), owner);
					paths.add(closureBlocks[closureBlocksCount]);
					pathTypes.add(PATH_CLOSURE);
				}
				
				closureBlocksCount++;
			} else if(child.getText().equals(POSITIVE_CLOSURE_STRING)) {
				if(!creationPass) {
					pClosureBlocks[pClosureBlocksCount] = new Block((CommonTree)child.getChild(0), owner);
					paths.add(pClosureBlocks[pClosureBlocksCount]);
					pathTypes.add(PATH_POSITIVE_CLOSURE);
				}
				
				pClosureBlocksCount++;
			} else if(child.getText().equals(OPTIONAL_STRING)) {
				if(!creationPass) {
					optionalBlocks[optionalBlocksCount] = new Block((CommonTree)child.getChild(0), owner);
					paths.add(optionalBlocks[optionalBlocksCount]);
					pathTypes.add(PATH_OPTIONAL);
				}
				
				optionalBlocksCount++;
			} else if(child.getText().equals(TREE_STRING)) {
				if(!creationPass) {
					trees[treeCount] = new TreeAlternative(child, owner, null);
				}
				
				treeCount++;
			} else if(!child.getText().equals(END_OF_ALTERNATIVE) && !child.getText().equals("EPSILON") &&
					  !child.getText().startsWith("{") && !child.getText().startsWith("~")) {
				if(!creationPass) {
					ruleRefs[ruleRefsCount] = child.getText();
					paths.add(ruleRefs[ruleRefsCount]);
					pathTypes.add(PATH_REF);
				}
				
				ruleRefsCount++;
			}
		}
		
		if(creationPass) {
			terminals = new String[terminalCount];
			ruleRefs = new String[ruleRefsCount];
			blocks = new Block[blocksCount];
			closureBlocks = new Block[closureBlocksCount];
			pClosureBlocks = new Block[pClosureBlocksCount];
			optionalBlocks = new Block[optionalBlocksCount];
			trees = new TreeAlternative[treeCount];
			paths = new ArrayList();
			pathTypes = new ArrayList<Integer>();
		}
	}
	
	public Block[] getBlocks() {
		return blocks;
	}
	
	public Block[] getClosureBlocks() {
		return closureBlocks;
	}
	
	public Block[] getPositiveClosureBlocks() {
		return pClosureBlocks;
	}
	
	public Block[] getOptionalBlocks() {
		return optionalBlocks;
	}
	
	public TreeAlternative[] getTrees() {
		return trees;
	}
	
	public String[] getRuleRefs() {
		return ruleRefs;
	}

	public ArrayList<String> getRuleRefsRecursive() {
		ArrayList<String> refs = new ArrayList<String>();
		
		for(String ref : ruleRefs) {
			refs.add(ref);
		}
		
		getRuleRefsFromBlocks(getBlocks(), refs);
		getRuleRefsFromBlocks(getClosureBlocks(), refs);
		getRuleRefsFromBlocks(getPositiveClosureBlocks(), refs);
		getRuleRefsFromBlocks(getOptionalBlocks(), refs);
		
		return refs;
	}
	
	private void getRuleRefsFromTrees(TreeAlternative[] trees, ArrayList<String> refs) {
		for (TreeAlternative tAlt : trees) {
			ArrayList<String> theseRefs = tAlt.getAlternative().getRuleRefsRecursive();
			for (String ref : theseRefs) {
				refs.add(ref);
			}
		}
	}
	
	private void getRuleRefsFromBlocks(Block[] blocks, ArrayList<String> refs) {
		for (Block block : blocks) {
			Alternative[] alts = block.getAlternatives();
			for (Alternative alt : alts) {
				ArrayList<String> theseRefs = alt.getRuleRefsRecursive();
				for (String ref : theseRefs) {
					refs.add(ref);
				}
			}
		}
	}
	
	public Rule getOwner() {
		return owner;
	}
	
	public ArrayList getPaths() {
		return paths;
	}
	
	public ArrayList<Integer> getPathTypes() {
		return pathTypes;
	}
	
	public String toString() {
		String str = "(A ";
		for (Object obj : paths) {
			str += obj;
		}
		str += ")";
		
		return str;
	}
}
