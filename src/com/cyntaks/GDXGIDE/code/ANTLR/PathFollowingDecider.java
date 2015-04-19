package com.cyntaks.GDXGIDE.code.ANTLR;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The problem you were having with getting stuck in cycles could probably be solved with a little
 * pre-processing on the rule graph which would build a list of all of the cycles in the graph;
 * then just check the past x nodes against all the cycles with length x and change route if
 * you are in a cycle afterall.
 * @author weston
 *
 */

public class PathFollowingDecider implements GrammarWalkDecider{
	private ArrayList<TreeAlternative> path;
	private int pos = 0;
	
	public PathFollowingDecider(ArrayList<TreeAlternative> path) {
		this.path = path;
	}
	
	public ArrayList<Alternative> newRuleApproached(Rule rule, boolean test) {
		System.out.println("new rule: " + rule.getName() + ", seeking: " + path.get(pos).getPartnerAlternative());
		
		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		Collection<TreeAlternative> rewrites = rule.getBlock().getRewrites();

		lookForMatchingRewrites(alts, rewrites, test);
		
		return alts;
	}
	
	public ArrayList<Alternative> chooseAlternativesFromRule(Rule rule) {
		return newRuleApproached(rule, false);
	}
	
	private void lookForMatchingRewrites(ArrayList<Alternative> alts, Collection<TreeAlternative> rewrites, boolean test) {
		for (TreeAlternative tAlt : rewrites) {
			if(tAlt == path.get(pos))
				alts.add(tAlt.getPartnerAlternative());
		}
		
		if(!test) {
			System.out.print("ALTS: ");
			for (Alternative alt : alts) {
				System.out.print(alt + ", ");
			}
			System.out.println("");
		}
		
		if(!alts.isEmpty() && !test)
			pos++;
	}

	public ArrayList<Alternative> newBlockApproached(Block block, boolean test) {
		System.out.println("new block: " + block + ", seeking: " + path.get(pos).getPartnerAlternative());
		
		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		Collection<TreeAlternative> rewrites = block.getRewrites();

		lookForMatchingRewrites(alts, rewrites, test);
		
		return alts;
	}
	
	public ArrayList<Alternative> chooseAlternativesFromBlock(Block block) {
		return newBlockApproached(block, false);
	}

	public void ruleFinished(Rule rule) {
		
	}

	public boolean shouldOptionalBlockBeExplored(Block block) {
		if(!newBlockApproached(block, true).isEmpty())
			return true;
		else
			return false;
	}
}
