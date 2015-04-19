package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;


public class Rule {
	private String name;
	private Block block;
	
	private ArrayList<Rule> incomingConnections = new ArrayList<Rule>();
	private ArrayList<Rule> outgoingConnections = new ArrayList<Rule>();
	
	private Grammar walker;
	
	public Rule(CommonTree ruleNode, Grammar walker) {
		name = ruleNode.getChild(0).getText();
		this.walker = walker;
		
		block = new Block((CommonTree)ruleNode.getChild(1), this);
	}

	public String getName() {
		return name;
	}

	public Block getBlock() {
		return block;
	}
	
	public String toString() {
		return "RULE: " + name;
	}
	
	public Grammar getGrammar() {
		return walker;
	}

	public ArrayList<Rule> getIncomingConnections() {
		return incomingConnections;
	}

	public void addIncomingConnection(Rule rule) {
		if(!incomingConnections.contains(rule)) {
			incomingConnections.add(rule);
		}
	}

	public ArrayList<Rule> getOutgoingConnections() {
		return outgoingConnections;
	}

	public void addOutgoingConnection(Rule rule) {
		if(!outgoingConnections.contains(rule)) {
			outgoingConnections.add(rule);
		}
	}
}
