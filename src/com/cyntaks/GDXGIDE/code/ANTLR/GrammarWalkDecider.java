package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;

public interface GrammarWalkDecider {
	public ArrayList<Alternative> chooseAlternativesFromRule(Rule rule);
	public ArrayList<Alternative> chooseAlternativesFromBlock(Block block);
	public boolean shouldOptionalBlockBeExplored(Block block);
	public void ruleFinished(Rule rule);
}
