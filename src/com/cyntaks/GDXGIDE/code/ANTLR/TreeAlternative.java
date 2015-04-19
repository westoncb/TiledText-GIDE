package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.antlr.runtime.tree.CommonTree;

public class TreeAlternative {
	private static final String NODE_INDICATOR = "TREE_BEGIN";
	
	private String nodeName;
	private Alternative alternative; //A TreeNode is basically an Alternative except that it has a name;
									 //rather than using inheritance, composition is preferred here.
	private ArrayList<ArrayList<Rule>> visited = new ArrayList<ArrayList<Rule>>(); //Used in the ancestor finding algorithm
	
	private TreeAlternative parent;
	private ArrayList<TreeAlternative> children;
	private Rule owner;
	
	private Alternative partnerAlternative;
	//private ArrayList<Rule> rulePathToParent
	
	public TreeAlternative(CommonTree treeAltNode, Rule owner, Alternative partnerAlternative) {
		this.owner = owner;
		this.partnerAlternative = partnerAlternative;
		children = new ArrayList<TreeAlternative>();
		
		//For all tree alternatives the first child is the node name, except on the root (which is
		//indicated in the grammar with '->' rather than 'TREE_BEGIN'); we give it the name of its
		//parent rule; this root is always followed by one ALT node, so we construct an Alternative
		//for it and then pull the children from the Alternative.
		if(!treeAltNode.getText().equals(NODE_INDICATOR)) {
			nodeName = owner.getName();
			CommonTree altChild = (CommonTree)treeAltNode.getChild(0);
			assert(altChild.getText().equals("ALT"));
			this.alternative = new Alternative(altChild, owner);
		}
		else {
			//A tree alternative only differs from a regular one
	        //in that it has an additional first node for a name.
			CommonTree child = (CommonTree)treeAltNode.getChild(0);
			nodeName = child.getText();
			treeAltNode.deleteChild(0);
			
			this.alternative = new Alternative(treeAltNode, owner);
			
			treeAltNode.insertChild(0, child);
		}
		
		findChildren(this.alternative, children);
	}
	
	private void findChildren(Alternative alt, ArrayList<TreeAlternative> children) {
		getChildrenFromBlocks(alt.getBlocks(), children);
		getChildrenFromBlocks(alt.getClosureBlocks(), children);
		getChildrenFromBlocks(alt.getPositiveClosureBlocks(), children);
		getChildrenFromBlocks(alt.getOptionalBlocks(), children);
		getChildrenFromTrees(alt.getTrees(), children);
	}
	
	private void getChildrenFromBlocks(Block[] blocks, ArrayList<TreeAlternative> children) {
		for (Block block : blocks) {
			Alternative[] alts = block.getAlternatives();
			for (Alternative alt : alts) {
				findChildren(alt, children);
				TreeAlternative[] tAlts = alt.getTrees();
				for (TreeAlternative tAlt : tAlts) {
					if(!children.contains(tAlt)) {//Seems strange to me that I need to make this check;
						addChild(tAlt);	          //the recursive call above causes duplicates to appear if I don't though
					}
				}
			}
		}
	}
	
	private void getChildrenFromTrees(TreeAlternative[] trees, ArrayList<TreeAlternative> children) {
		for(TreeAlternative tAlt : trees) {
			if(!children.contains(tAlt))
				children.add(tAlt);
		}
	}

	/**
	 * Accumulates the rule-ancestors of this TreeAlternative all the way back to the root; one of these
	 * rule-ancestors should contain (in a rewrite) a proper parent for this TreeAlternative. 
	 */
	private ArrayList<Rule> findAncestors() {
		ArrayList<Rule> possibilities = new ArrayList<Rule>();
		ArrayList<Rule> incoming = getOwner().getIncomingConnections();
		findAncestorsAux(incoming, possibilities);
		
		return possibilities;
	}
	
	private void findAncestorsAux(ArrayList<Rule> incoming, ArrayList<Rule> possibilities) {
		visited.add(incoming);
		
		for (Rule rule : incoming) {
			possibilities.add(rule);
		}
		
		for (Rule rule : incoming) {
			if(!visited.contains(rule.getIncomingConnections()))
				findAncestorsAux(rule.getIncomingConnections(), possibilities);
		}
	}

	/**
	 * Used to find a parent TreeAlternative which is related to this one by Rule references; without
	 * this, only TreeAlternative relatives that appear within the same rule can be connected.
	 * E.g. nodes from normalClassDeclaration should be connected to the TreeAlternative for
	 * compilationUnit -- but it won't be without this method.
	 */
	public void connectToParent() {
		//These will be ordered so that the most immediate ancestor is first, and the root is last.
		ArrayList<Rule> possibilities = findAncestors();
		
		/*System.out.println(this.getNodeName() + ", owner: " + owner.getName() + " " +  " possibilities: ");
		for (Rule rule : possibilities) {
			System.out.print(rule.getName() + " -> ");
		}
		System.out.println("");
		*/
		
		int index = 0; //tracks how many possibilities we go through, so we know which ancestor in the list was the correct one
		
		//Looking for a rule with an alternative that has a rewrite and...
		for (Rule rule : possibilities) {
			TreeAlternative parent = findConnectedParentNode(possibilities, index);
			if(parent != null) {
				ArrayList<TreeAlternative> siblings = parent.children;
				String addName = this.getNodeName();
				if(index > 0) { //wasn't the first ancestor
					
					//When a possibility is selected, it corresponds to the parent rule of the
					//ancestor we'd actually like to add to; we'll use this to get get the name
					//of the node we want to add to.
					int addIndex = index - 1;
					
					addName = possibilities.get(addIndex).getName();
				}

				TreeAlternative addTo = parent; //as a default we add to that top-level TreeAlternative
				for (TreeAlternative kid : siblings) { //but the reference which connects this TreeAlternative may actually be in one of the children
					ArrayList<String> refs = kid.getAlternative().getRuleRefsRecursive();
					for (String ref : refs) {
						if(ref.equals(addName)) {
							//System.out.println("ref and addName: " + ref + ", from this kid: " + kid);
							addTo = kid;
						}
					}
				}
				
				addTo.addChild(this);
				return;
			}
			
			index++;
		}
	}

	/**
	 * We want to find a rewrite for connecting this node to. The criteria for the rewrite are that
	 * it make a reference to some rule beneath it (closer in relationship to this TreeAlternative),
	 * or to this TreeAlternative directly.
	 * @param possibilities
	 * @param pos
	 * @return
	 */
	private TreeAlternative findConnectedParentNode(ArrayList<Rule> possibilities, int pos) {
		Rule possibility = possibilities.get(pos);
		Alternative[] alts = possibility.getBlock().getAlternatives();
		for (Alternative alt : alts) {
			if(possibility.getBlock().getRewriteMap().get(alt) != null) { //There's only potential if a rewrite exists
				ArrayList<String> refs = alt.getRuleRefsRecursive();
				for (String ref : refs) {
					for (int i = pos; i > -1; i--) {
						String possibleLowerConnection = this.getNodeName(); //Use this node if we're at zero
						if(i > 0) {
							possibleLowerConnection = possibilities.get(i - 1).getName(); 
						}
						
						if(ref.equals(possibleLowerConnection))
							return possibility.getBlock().getRewriteMap().get(alt);
					}
				}
			}
		}
		
		return null;
	}

	public Alternative getAlternative() {
		return this.alternative;
	}

	public String getNodeName() {
		return nodeName;
	}
	
	public Rule getOwner() {
		return owner;
	}
	
	public TreeAlternative getChild(int index) {
		return children.get(index);
	}
	
	public void addChild(TreeAlternative child) {
		children.add(child);
		child.parent = this;
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public String toStringTree() {
		String childrenString = "";
	
		for (TreeAlternative tAlt : children) {
			childrenString += " " + tAlt.toStringTree();
		}
		
		String opening = children.isEmpty() ? "" : "(";
		String closing = children.isEmpty() ? "" : ")";
		
		return opening + getNodeName() + childrenString + closing;
	}
	
	public String toString() {
		return getNodeName();
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	public TreeAlternative getParent() {
		return parent;
	}
	
	boolean partnerFound = false;
	public Alternative getPartnerAlternative() {
		if(!partnerFound) {
			partnerFound = true;
			HashMap<Alternative, TreeAlternative> map = owner.getBlock().getRewriteMap();
			Collection<TreeAlternative> rewrites = owner.getBlock().getRewrites();
			TreeAlternative parent = this;
			while(!rewrites.contains(parent)) {
				if(parent == null)
					break;
				parent = parent.parent;
			}
			
			if(parent != null) {
				System.out.println("not null parent: " + parent.getNodeName());
				Alternative[] alternatives = owner.getBlock().getAlternatives();
				for (Alternative alternative : alternatives) {
					if(map.get(alternative) == parent) {
						this.partnerAlternative = alternative;
						break;
					}
				}
			}
		}
		
		return partnerAlternative;
	}
}
