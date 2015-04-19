package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;

public class SkeletonSourceBuilder {
	private boolean debug = true;
	private GrammarWalker walker;
	
	public SkeletonSourceBuilder(Grammar grammar, String topNode, String bottomNode) {
		ArrayList<TreeAlternative> path = findPath(grammar.getValidationTreeRoot(), topNode, bottomNode);
	
		if(debug) {
			System.out.print("***Path: ");
			for (TreeAlternative alt : path) {
				System.out.print(alt + ", ");
			}
			System.out.println("");
		}
		
		this.walker = new GrammarWalker(grammar.getRule(grammar.getTopRule()),
										new PathFollowingDecider(path));
		walker.walk();
		System.out.println("Generated Text: " + walker.getGeneratedText());
	}
	
	public ArrayList<TreeAlternative> findPath(TreeAlternative root, String topNode, String bottomNode) {
		ArrayList<TreeAlternative> topCandidates = findNodesWithName(root, topNode);
		ArrayList<TreeAlternative> bottomCandidates = findNodesWithName(root, bottomNode);
		
		ArrayList<TreeAlternative> path = null;
		for (TreeAlternative top : topCandidates) {
			for (TreeAlternative bottom : bottomCandidates) {
				path = tracePath(top, bottom);
				if(path != null)
					return path;
			}
		}
		
		return null;
	}
	
	/**
	 * Does the work of finding out whether a path exists between the two and accumulates the nodes
	 * between them them. If a path is not found to exist, null will be returned.
	 * @param top
	 * @param bottom
	 * @return
	 */
	private ArrayList<TreeAlternative> tracePath(TreeAlternative top, TreeAlternative bottom) {
		ArrayList<TreeAlternative> path = new ArrayList<TreeAlternative>();
	
		tracePathAux(top, bottom, path);
		
		if(path.isEmpty() || path.get(path.size()-1) != bottom)
			return null;
		else
			return path;
	}
	
	private void tracePathAux(TreeAlternative top, TreeAlternative bottom, ArrayList<TreeAlternative> path) {
		path.add(top);
		
		if(top == bottom)
			return;
		
		int deadEndCount = 0;
		for (int i = 0; i < top.getChildCount(); i++) {
			TreeAlternative alt = top.getChild(i);
			if(alt == bottom) {
				path.add(bottom);
				return;
			} else if(!alt.isLeaf()) {
				tracePathAux(alt, bottom, path);
				if(!path.isEmpty() && path.get(path.size()-1) == bottom) {
					return;
				}
			}
		}
		
		path.remove(top);
	}
	
	private ArrayList<TreeAlternative> findNodesWithName(TreeAlternative root, String name) {
		ArrayList<TreeAlternative> list = new ArrayList<TreeAlternative>();
		
		findNodesWithNameAux(root, name, list);
		
		return list;
	}
	
	private void findNodesWithNameAux(TreeAlternative tree, String name, ArrayList<TreeAlternative> list) {
		if(tree.getNodeName().equals(name))
			list.add(tree);
		
		for (int i = 0; i < tree.getChildCount(); i++) {
			findNodesWithNameAux(tree.getChild(i), name, list);
		}
	}
}
