package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.antlr.runtime.tree.CommonTree;

public class Block {
	private Alternative[] alternatives;
	private HashMap<Alternative, TreeAlternative> rewriteMap = new HashMap<Alternative, TreeAlternative>();
	private static final String ALT_STRING = "ALT";
	private static final String TREE_STRING = "->";
	private Rule owner;
	
	public Block(CommonTree blockNode, Rule owner) {
		this.owner = owner;
		
		//count the alternatives
		int altCount = 0;
		for (int i = 0; i < blockNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)blockNode.getChild(i);
			if(child.getText().equals(ALT_STRING))
				altCount++;
		}
		alternatives = new Alternative[altCount];
		
		//create/store alternatives and rewrites
		int altIndex = 0;
		for (int i = 0; i < blockNode.getChildCount(); i++) {
			CommonTree child = (CommonTree)blockNode.getChild(i);
			if(child.getText().equals(ALT_STRING)) {
				alternatives[altIndex] = new Alternative((CommonTree)blockNode.getChild(i), owner);
				if(i < blockNode.getChildCount()-1 && blockNode.getChild(i + 1).getText().equals(TREE_STRING)) {
					rewriteMap.put(alternatives[altIndex], new TreeAlternative((CommonTree)blockNode.getChild(i + 1), owner, alternatives[altIndex]));
					i++;
				}
				
				altIndex++;
			}
		}
	}
	
	public Alternative[] getAlternatives() {
		return alternatives;
	}
	
	public HashMap<Alternative, TreeAlternative> getRewriteMap() {
		return rewriteMap;
	}
	
	public Collection<TreeAlternative> getRewrites() {
		return rewriteMap.values();
	}
	
	public ArrayList<TreeAlternative> getRewritesExtended() {
		return null;
	}
	
	public Rule getOwner() {
		return owner;
	}
	
	public String toString() {
		String str = "(B ";
		for (int i = 0; i < alternatives.length; i++) {
			str += alternatives[i] + " ";
			TreeAlternative rewrite = rewriteMap.get(alternatives[i]);
			if(rewrite != null)
				str += rewrite + " ";
		}
		str += ")";
		
		return str;
	}
}
