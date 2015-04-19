package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultGWDecider implements GrammarWalkDecider{
	private HashMap<Rule, ArrayList<Block>> visitedMap;
	private ArrayList<Rule> active;
	private Rule buffer;
	
	public DefaultGWDecider() {
		visitedMap = new HashMap<Rule, ArrayList<Block>>();
		active = new ArrayList<Rule>();
	}
	
	public DefaultGWDecider(DefaultGWDecider obs) {
		this();
		HashMap<Rule, ArrayList<Block>> oVisitedMap = obs.visitedMap;
		ArrayList<Rule> oActive = obs.active;
		
		for (Rule rule : oVisitedMap.keySet()) {
			ArrayList<Block> oValue = oVisitedMap.get(rule);
			ArrayList<Block> value = new ArrayList<Block>();
			for (Block block : oValue) {
				value.add(block);
			}
			visitedMap.put(rule, value);
		}
		
		for (Rule rule : oActive) {
			active.add(rule);
		}
		
		this.buffer = obs.buffer;
	}
	
	public ArrayList<Alternative> chooseAlternativesFromRule(Rule rule) {
			Alternative[] alts = rule.getBlock().getAlternatives();
			String sub = rule.getName().substring(0, 1);
			if(sub.toUpperCase().equals(sub)) { //dealing with an ANTLR token
				ArrayList<Alternative> a = new ArrayList<Alternative>();
				a.add(alts[0]);
				return a;
			}
			
			active.add(rule);
			//System.out.println("New rule: " + rule.getName() + ", active: " + getActiveString());
			
			System.out.println("ALTS: " + alts);
			alts = removeLoopPotentials(alts);
			System.out.println("ALTS-after: " + alts);

			return arrayToList(alts);
		
	}
	
	private ArrayList<Alternative> arrayToList(Alternative[] array) {
		ArrayList<Alternative> list = new ArrayList<Alternative>();
		for (Alternative alternative : array) {
			list.add(alternative);
		}
		
		return list;
	}
	
	public ArrayList<Alternative> chooseAlternativesFromBlock(Block block) {
		ArrayList<Block> visited = visitedMap.get(block.getOwner());
		if(visited == null) {
			visited = new ArrayList<Block>();
			visitedMap.put(block.getOwner(), visited);
		}
		visited.add(block);
		
		Alternative[] alts = block.getAlternatives();

		alts = removeLoopPotentials(alts);
		
		return arrayToList(alts);
	}
	
	private String getActiveString() {
		String str = "(";
		for (Rule rule : active) {
			str += rule.getName() + " ";
		}
		str += ")";
		
		return str;
	}
	
	private Alternative[] removeLoopPotentials(Alternative[] alts) {
		ArrayList<Integer> removeIndices = new ArrayList<Integer>(); 
		for (int i = 0; i < alts.length; i++) {
			Alternative alt = alts[i];
			//System.out.println("i: " + i + ", alt: " + alt);
			String[] refs = alt.getRuleRefs();
			for (String ref : refs) {
				//System.out.println("Ref: " + ref + ", active: " + getActiveString());
				if(ruleActive(ref)) {
					removeIndices.add(i);
					break;
				}
			}
		}
		
		if(removeIndices.size() != 0) {
			if(removeIndices.size() == alts.length) {
				//System.out.println("no viable alts.");
				return null;
			}
			
			//System.out.println("altlength: " + alts.length + ", remove count: " + removeIndices.size());
			Alternative[] temp = new Alternative[alts.length - removeIndices.size()];
			int index = 0;
			for (int i = 0; i < alts.length; i++) {
				Alternative alt = alts[i];
				if(!removeIndices.contains(i)) {
					temp[index] = alt;
					index++;
				}
			}
			
			alts = temp;
		}
		
		return alts;
	}
	
	private boolean ruleActive(String name) {
		for (Rule rule : active) {
			if(rule.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public boolean shouldOptionalBlockBeExplored(Block block) {
		ArrayList<Block> visited = visitedMap.get(block.getOwner());
		if(visited == null || (visited != null && !visited.contains(block))) {
			Alternative[] alts = removeLoopPotentials(block.getAlternatives());
			if(alts == null || alts.length != block.getAlternatives().length)
				return false;
			
			return true;
		}
		else
			return false;
	}

	@Override
	public void ruleFinished(Rule rule) {
		//System.out.println("finished rule: " + rule.getName());
		active.remove(buffer);
		visitedMap.remove(buffer);
		
		buffer = rule;
	}
}
