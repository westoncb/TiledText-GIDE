package com.cyntaks.GDXGIDE.util;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import com.cyntaks.GDXGIDE.code.ANTLR.TreeAlternative;

public class SwingTree {
	
	public SwingTree(CommonTree tree) {
		showSwingTree(tree);
	}
	
	private void showSwingTree(CommonTree tree) {
		JFrame frame = new JFrame();
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(tree.getText());
		createNodes(top, tree);
		JTree jtree = new JTree(top);
		jtree.setExpandsSelectedPaths(true);
		JScrollPane panel = new JScrollPane(jtree);
		
		panel.setPreferredSize(new Dimension(1024, 680));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setLocation(0, 0);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void createNodes(DefaultMutableTreeNode j, CommonTree t) {
		for(int i = 0; i < t.getChildCount(); i++) {
			Tree curT = t.getChild(i);
			DefaultMutableTreeNode curJ = new DefaultMutableTreeNode(curT.getText());
			j.add(curJ);
			createNodes(curJ, (CommonTree)curT);
		}
	}
	
	public SwingTree(TreeAlternative tree) {
		showSwingTree(tree);
	}
	
	private void showSwingTree(TreeAlternative tree) {
		JFrame frame = new JFrame();
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(tree.getNodeName());
		createNodes(top, tree);
		JTree jtree = new JTree(top);
		jtree.setExpandsSelectedPaths(true);
		JScrollPane panel = new JScrollPane(jtree);
		
		panel.setPreferredSize(new Dimension(1024, 680));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setLocation(0, 0);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void createNodes(DefaultMutableTreeNode j, TreeAlternative t) {
		for(int i = 0; i < t.getChildCount(); i++) {
			TreeAlternative curT = t.getChild(i);
			DefaultMutableTreeNode curJ = new DefaultMutableTreeNode(curT.getNodeName());
			j.add(curJ);
			createNodes(curJ, curT);
		}
	}
}
