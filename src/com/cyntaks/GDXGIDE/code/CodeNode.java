package com.cyntaks.GDXGIDE.code;

import java.util.ArrayList;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.code.ANTLR.Grammar;
import com.cyntaks.GDXGIDE.code.ANTLR.Rule;
import com.cyntaks.GDXGIDE.config.Settings;
import com.cyntaks.GDXGIDE.util.Configuration;
import com.cyntaks.GDXGIDE.util.NodeConfig;

public class CodeNode extends GNode {
	
	public static final String GENERAL_CONFIG = "general_lang_properties";
	private boolean collapsable;
	private String alternateText;
	
	private boolean tabContainer;
	private boolean newlineBefore;
	private boolean newlineAfter;
	
	private int abstraction;
	private static int abstractionLevels = -1;
	private boolean letter;
	
	public CodeNode(String text, boolean meta, int type) {
		this(new CommonToken(-2, text), meta, type);
	}
	
	public CodeNode(String text, int tokenType, int type) {
		super(text, tokenType, type);
		init();
	}
	
	public CodeNode(String text) {
		this(new CommonToken(-2, text));
	}
	
	public CodeNode(String text, int type) {
		this(text, false, type);
	}
	
	public CodeNode(Token token) {
		this(token, false, GNode.DEFAULT);
	}
	
	public CodeNode(Token token, boolean meta, int type) {
		super(token, meta, type);
		init();
	}
	
	public CodeNode(CodeNode node) {
		super(node, node.getParent(), false);
		this.collapsable = node.collapsable;
		this.alternateText = node.alternateText;
		
		this.tabContainer = node.tabContainer;
		this.newlineBefore = node.newlineBefore;
		this.newlineAfter = node.newlineAfter;
		
		this.abstraction = node.abstraction;
	}
	
	/**
	 * Only used for tree copying.
	 * 
	 * @param node Root of the tree to copy.
	 * @param parent
	 * @param addChildren Whether to copy the full tree or just this node.
	 */
	public CodeNode(CommonTree node, CodeNode parent) {
		super(node, parent, false);
		
		for(int i = 0; i < node.getChildCount(); i++) {
			this.addChild(new CodeNode((CommonTree)node.getChild(i), this));
		}
		
		init();
		
		//Create child nodes for each letter of the text
		if(isLeaf() && node.getText().length() > 1 && getName() == null) { //If the name is not null, it's one of the recognized structure nodes (e.g. WCONTAINER)
			for(int i = 0; i < node.getText().length(); i++) {
				CodeNode letter = new CodeNode("" + node.getText().charAt(i));
				letter.letter = true;
				this.addChild(letter);
			}
		}
		
		
	}
	
	public static CodeNode copyTree(CommonTree tree) {
		return new CodeNode(tree, null);
	}
	
	protected Configuration init() {
		Configuration config = super.init();
		if(config == null || abstractionLevels == -1) {
			NodeConfig langConfig = Settings.getNodeConfig("java");
			
			if(abstractionLevels == -1) {
				config = langConfig.getBlock(GENERAL_CONFIG);
				abstractionLevels = config.getInt("abstractionLevels");
			}
			
			config = langConfig.getBlock(getText());
		}
		
		if(config != null) {
			collapsable = config.getBoolean("collapsable");
			
			newlineBefore = config.getBoolean("newlineBefore");
			
			newlineAfter = config.getBoolean("newlineAfter");

			tabContainer = config.getBoolean("tabcontainer");
			
			alternateText = config.getString("alternateText");
			
			abstraction = config.getInt("abstraction");
		}
		
		return config;
	}
	
	public CodeNode getChild(int i) {
		return (CodeNode)super.getChild(i);
	}
	
	public CodeNode getParent() {
		return (CodeNode)super.getParent();
	}
	
	public CodeNode getConcreteParent() {
		return (CodeNode)super.getConcreteParent();
	}

	public boolean isCollapsable() {
		return collapsable;
	}

	public String getAlternateText() {
		return alternateText;
	}
	
	public boolean hasAlternateText() {
		return alternateText != null;
	}

	public boolean isTabContainer() {
		return tabContainer;
	}

	public boolean hasNewlineBefore() {
		if(newlineBefore)
			return true;
		if(getChildIndex() == 0)
			return getParent().hasNewlineBefore();
		else
			return false;
	}
	
	public boolean hasNewlineAfter() {
		if(newlineAfter)
			return true;
		if(getParent() != null && getChildIndex() == getParent().getChildCount() - 1)
			return getParent().hasNewlineAfter();
		else
			return false;
	}
	
	public String getTextRecursive() {
		String text = "";
		if(!isLeaf() && !getChild(0).isLetter()) {
			for (int i = 0; i < this.getChildCount(); i++) {
				text += this.getChild(i).getTextRecursive() + " ";
			}
		}
		else if(!isStructureSpecificationLeaf())
			text += this.getText();
		
		return text;
	}

	public int getAbstraction() {
		if(super.isLeaf())
			return abstractionLevels;
		else if(getChild(0).letter)
			return abstractionLevels - 1;
		else
			return abstraction;
	}
	
	/**
	 * Recursively grabs all the text beneath this node, inserting spaces between nodes, unless the
	 * nodes are letters.
	 * @return 
	 */
	public String getTextBeneathThisNode(boolean noSpace) {
		String text = "";
		
		for (int i = 0; i < getChildCount(); i++) {
			boolean noSpaceThisTime = getChild(i).isLetter() || i == getChildCount()-1 || (noSpace && getChild(i + 1).isInsertionPending());
			
			text += getChild(i).getTextRecursive() + (noSpaceThisTime ? "" : " ");
		}
		
		return text;
	}
	
	public String getTextBeneathThisNode() {
		return getTextBeneathThisNode(false);
	}
	
	/**
	 * This is for the case when a structure specification node (e.g. WCONTAINER) ends up as a leaf somewhere --
	 * a situation that must be handled specially since ordinarily it's display text will come from it's children;
	 * instead the label for the structure type would show up.
	 * @return
	 */
	public boolean isStructureSpecificationLeaf() {
		return (this.isLeaf() && !this.isLetter() && this.getText().length() > 1);
	}
	
	public Object deleteChild(int index) {
		Object deleted = super.deleteChild(index);
		if(deleted != null && !this.isLeaf() && this.getChild(0).isLetter()) {
			String text = getText().substring(0, index) + getText().substring(index + 1);
			super.setText(text);
		}
		
		return deleted;
	}
	
	public void addChild(int index, GNode gNode) {
		CodeNode node = (CodeNode)gNode;
		if(!this.isLeaf() && getChild(0).isLetter() && getChildrenText().equals(this.getText())) { //last condition is for something like a couple of parenthesis whose parent would be a WSTATEMENT
			if(!node.isLetter() && node.getChildCount() > 1) { //we're trying to add some composite thing to a group of letters -- break it down to letters before adding
				for (int i = 0; i < node.getChildCount(); i++) {
					addChild(index + i, node.getChild(i));
				}
				return;
			}
			
			String text = getText().substring(0, index) + node.getText() + getText().substring(index);
			super.setText(text);
		}
		
		super.addChild(index, node);
	}
	
	/**
	 * Walks the given list, replacing each inner meta node with its children
	 * @param nodes
	 */
	public static void expandMetaNodes(ArrayList<GNode> nodes) {
		for(int i = 0; i < nodes.size(); i++) {
			CodeNode node = (CodeNode)nodes.get(i);
			if(node.isMeta() && !node.isLeaf()) {
				for(int k = 0; k < node.getChildCount(); k++) { //if the node is a meta node, add its children in its place
					if(k == 0) {
						nodes.set(i, node.getChild(k));
						i--; //Accomplishes recursive behavior; we will apply the same check as above to each child added now
					}
					else {
						nodes.add(i + (k+1), node.getChild(k));
					}
				}
			}
		}
	}
	
	public String getChildrenText() {
		String s = "";
		for (int i = 0; i < getChildCount(); i++) {
			s += getChild(i).getText();
		}
		
		return s;
	}
	
	public CodeNode getNodeAtAddress(ArrayList<Integer> address) {
		return (CodeNode)super.getNodeAtAddress(address);
	}
	
	public GNode getNextParentAssociatedWithRule(Grammar grammar) {
		Rule rule = null;
		CodeNode parent = this;
		
		do {
			if(parent.getParent() != null)
				parent = parent.getParent();
			System.out.println("looking for rule: " + parent.getName());
			rule = grammar.getRuleFromNodeName(parent.getName());
			
		} while (rule == null && parent.getParent() != null);
		
		return parent;
	}
	
	public static int getNumAbstractionLevels() {
		return abstractionLevels;
	}
	
	public boolean isLetter() {
		return letter;
	}
	
}