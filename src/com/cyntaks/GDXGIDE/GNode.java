package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.cyntaks.GDXGIDE.config.Settings;
import com.cyntaks.GDXGIDE.gui.GridDisplay;
import com.cyntaks.GDXGIDE.util.Configuration;
import com.cyntaks.GDXGIDE.util.NodeConfig;

public class GNode extends CommonTree {
	private String name;
	private int orientation;
	private boolean meta;
	private boolean beingViewed;
	private String text;
	public static final int DEFAULT = 0;
	public static final int NEWLINE = 1;
	public static final int SPACE = 2;
	public static final int TAB = 3;
	public static final int COLLAPSE_STRING = 4;
	private int type = DEFAULT;
	private boolean insertionPending;
	
	public GNode(String text, int tokenType, int type) {
		this(new CommonToken(tokenType, text));
		this.text = text;
		this.type = type;
	}
	
	public GNode(String text) {
		this(text, -2, DEFAULT);
	}
	
	public GNode(String text, int type) {
		this(text, -2, type);
	}
	
	public GNode(Token token) {
		this(token, false, DEFAULT);
	}
	
	
	public GNode(Token token, boolean meta, int type) {
		super(token);
		if(token != null)
			setText(token.getText());
		this.setMeta(meta);
		this.type = type;
		init();
	}
	
	/**
	 * Only used for tree copying.
	 * 
	 * @param node Root of the tree to copy.
	 * @param parent
	 * @param addChildren Whether to copy the full tree or just this node.
	 */
	protected GNode(CommonTree node, GNode parent, boolean addChildren) {
		this(node.getToken());
		this.setParent(parent);
		
		if(addChildren) {
			for(int i = 0; i < node.getChildCount(); i++) {
				this.addChild(new GNode((CommonTree)node.getChild(i), this, true));
			}
		}
	}
	
	public void addChild(int index, GNode child) {
		super.addChild(child);
		
		int offset = 1;
		while(child.getChildIndex() > index) {
			int swapIndex = (getChildCount() - 1) - offset;
			GNode temp = getChild(swapIndex);
			setChild(swapIndex + 1, temp);
			setChild(swapIndex, child);
			offset++;
		}
	}
	
	public static GNode copyTree(CommonTree node) {
		return new GNode(node, null, true);
	}
	
	protected Configuration init() {
		NodeConfig nodeConfig = Settings.getNodeConfig("java");
		Configuration config = nodeConfig.getBlock(getText());
		
		if(config != null) {
			name = config.getString("name");
			
			String alignString = config.getString("orientation");
			if(alignString != null && alignString.equalsIgnoreCase("horizontal"))
				orientation = GridDisplay.HORIZONTAL;
			else
				orientation = GridDisplay.VERTICAL;
			
			meta = config.getBoolean("meta");
		}
		
		return config;
	}
	
	public GNode getChild(int i) {
		return (GNode)super.getChild(i);
	}
	
	public GNode getParent() {
		return (GNode)super.getParent();
	}
	
	private GNode getLastMetaChild(GNode node) {
		if(node.getChild(0).isMeta())
			return getLastMetaChild(node.getChild(0));
		else
			return node.getChild(0).getParent();
	}
	
	public int getConcreteChildCount() {
		return getLastMetaChild(this).getChildCount();
	}
	
	public GNode getConcreteChild(int i) {
		return getLastMetaChild(this).getChild(i);
	}
	
	public GNode getConcreteParent() {
		GNode parent = getParent();
		while(parent != null && parent.isMeta()) {
			parent = parent.getParent();
		}
		return parent;
	}
	
	public ArrayList<GNode> getChildrenRecursive() {
		ArrayList<GNode> list = new ArrayList<GNode>();
		getChildrenRecursiveAux(list);
		return list;
	}
	
	private void getChildrenRecursiveAux(ArrayList<GNode> list) {
		for (int i = 0; i < this.getChildCount(); i++) {
			GNode child = this.getChild(i);
			list.add(child);
			child.getChildrenRecursiveAux(list);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getOrientation() {
		return orientation;
	}
	
	protected void setMeta(boolean meta) {
		this.meta = meta;
	}
	
	public boolean isMeta() {
		return meta;
	}
	
	public boolean isLeaf() {
		return super.getChildCount() == 0;
	}

	public int getDepth() {
		List ancestors = this.getAncestors();
		return ancestors == null ? 0 : ancestors.size();
	}
	
	public GNode getRoot() {
		GNode root = this;
		for(int i = 0; i < getDepth(); i++) {
			root = root.getParent();
		}
		
		return root;
	}
	
	public String getTextBeneathThisNode() {
		String text = "";
		for (int i = 0; i < getChildCount(); i++) {
			text += getChild(i).getTextRecursive() + " ";
		}
		
		return text;
	}
	
	public String getTextRecursive() {
		String text = "";
		if(!isLeaf()) {
			for (int i = 0; i < this.getChildCount(); i++) {
				text += this.getChild(i).getTextRecursive() + " ";
			}
		}
		else
			text += this.getText();
		
		return text;
	}
	
	public boolean doesGroupCompriseParent(ArrayList<GNode> nodes) {
		if(nodes.isEmpty() || nodes.size() != nodes.get(0).getParent().getChildCount())
			return false;
		else {
			GNode parent = nodes.get(0).getParent();
			for (GNode node : nodes) {
				if(node.getParent() != parent)
					return false;
			}
			
			return true;
		}
	}
	
	public ArrayList<Integer> getTreeAddress() {
		ArrayList<Integer> address = new ArrayList<Integer>();
		GNode node = this;
		do {
			int childIndex = node.getChildIndex();
			if(childIndex != -1) {
				address.add(0, childIndex);
			}
			node = node.getParent();
		} while (node != null);
		
		return address;
	}
	
	public GNode getNodeAtAddress(ArrayList<Integer> address) {
		GNode node = this.getRoot();
		
		System.out.print("address: ");
		for (int i = 0; i < address.size(); i++) {
			System.out.print(address.get(i) + ", ");
		}
		System.out.println();
		
		
		for (int i = 0; i < address.size(); i++) {
			int index = address.get(i);
			node = node.getChild(index);
		}
		
		return node;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public boolean isBeingViewed() {
		return beingViewed;
	}
	
	public boolean isAncestorOf(GNode node) {
		while(node.getParent() != null) {
			if(this == node.getParent())
				return true;
			node = node.getParent();
		}
		
		return false;
	}

	public void setBeingViewed(boolean beingViewed) {
		this.beingViewed = beingViewed;
	}
	
	public boolean isWhiteSpace() {
		return type == GNode.NEWLINE || type == GNode.TAB || type == GNode.SPACE;
	}
	
	public int getType() {
		return type;
	}
	
	public int getTokenType() {
		return getToken().getType();
	}
	
	public String toString() {
		return getText();
	}

	public boolean isInsertionPending() {
		return insertionPending;
	}

	public void insertionIsPending() {
		this.insertionPending = true;
	}
	
	public void clearInsertionPending() {
		this.insertionPending = false;
		for (int i = 0; i < super.getChildCount(); i++) {
			getChild(i).clearInsertionPending();
		}
	}
}
