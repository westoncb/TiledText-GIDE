package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;


public class GrammarWalker {
	private boolean debug = true;
	
	private Rule start;
	private GrammarWalkDecider observer;
	private String generatedText = "";

	private static final GrammarWalkDecider DEFAULT_OBSERVER = new DefaultGWDecider();
	
	public GrammarWalker(Rule start) {
		this(start, DEFAULT_OBSERVER);
	}
	
	public GrammarWalker(Rule start, GrammarWalkDecider observer) {
		this.start = start;
		this.observer = observer;
	}
	
	public void walk() {
		walkAux(start);
	}
	
	private void walkAux(Rule rule) {
		if(debug) {
			System.out.println(this + " traversing rule: " + rule.getName());
		}
			
		ArrayList<Alternative> alts = observer.chooseAlternativesFromRule(rule);
		walkAlternatives(alts);
		observer.ruleFinished(rule);
		
		if(debug) {
			System.out.println(this + " finished rule: " + rule.getName());
		}
	}
	
	public String toString() {
		return "";
	}
	
	private void walkAlternatives(ArrayList<Alternative> alts) {
		if(alts != null) {
			for (int i = 0; i < alts.size(); i++) {
				walkAlternative(alts.get(i));
			}
			//if(!alts.isEmpty()) {
				//walkAlternative(alts.get(0));
			//}
		}
	}
	
	private void walkAlternative(Alternative alt) {
		ArrayList paths = alt.getPaths();
		ArrayList<Integer> types = alt.getPathTypes();
		
		int index = 0;
		for (Object path : paths) {
			if(debug) {
				System.out.println(this + " traversing path: " + path);
			}
			
			int type = types.get(index);
			
			if(type == Alternative.PATH_BLOCK || type == Alternative.PATH_CLOSURE || 
			   type == Alternative.PATH_POSITIVE_CLOSURE || type == Alternative.PATH_OPTIONAL) {
				walkBlock((Block)path, type);
			} else if(type == Alternative.PATH_TERMINAL) {
				String str = (String)path;
				if(str.startsWith("\\u")) {
					char c = (char)Integer.parseInt(str.substring(2), 16);
					str = "" + c;
				}
				generatedText += str + " ";
			} else if(type == Alternative.PATH_REF) {
				String ref = (String)path;
				Rule rule = start.getGrammar().getRule(ref);
				System.out.println("attempting to follow ref: " + ref + ", rule: " + rule.getName());
				if(rule != null) {
					walkAux(rule);
				}
			}
			
			index++;
		}
	}
	
	private void walkBlock(Block block, int type) {
		boolean walkedSomewhere = false;
		
		System.out.println("walk block: " + block + ", type: " + type);
		
		switch (type) {
		case Alternative.PATH_BLOCK:
			ArrayList<Alternative> alts = observer.chooseAlternativesFromBlock(block);
			walkAlternatives(alts);
		break;
		case Alternative.PATH_CLOSURE:
			while(observer.shouldOptionalBlockBeExplored(block)) {
				alts = observer.chooseAlternativesFromBlock(block);
				walkAlternatives(alts);
			}
		break;
		case Alternative.PATH_POSITIVE_CLOSURE:
			alts = observer.chooseAlternativesFromBlock(block);
			walkAlternatives(alts);
			
			while(observer.shouldOptionalBlockBeExplored(block)) {
				alts = observer.chooseAlternativesFromBlock(block);
				walkAlternatives(alts);
			}
		break;
		case Alternative.PATH_OPTIONAL:
			if(observer.shouldOptionalBlockBeExplored(block)) {
				alts = observer.chooseAlternativesFromBlock(block);
				walkAlternatives(alts);
			}
		break;
		}
	}
	
	public String getGeneratedText() {
		return generatedText;
	}
}
