package com.cyntaks.GDXGIDE.code.ANTLR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

public class Grammar {
	private CommonTree tree;
	private String grammarPath;
	public HashMap<String, Rule> namesToRules = new HashMap<String, Rule>();

	//Used by treeConnectRules to avoid incorporating cycles while connecting up rules
	ArrayList<Rule> visited = new ArrayList<Rule>();
	
	private static final String RULE_TEXT = "RULE"; //for identifying rule nodes in the grammar
	private String topRule;
	
	public Grammar(String grammarPath, String topRule) {
		this.grammarPath = grammarPath;
		this.topRule = topRule;
	}
	
	public void load() {
		buildANTLRGrammarParser();
		findRules(this.tree);
		treeConnectRules(); //builds a spanning tree from the root node of the grammar rather 
							//than building the full graph of rule references
		connectRewriteTree();
		connectRules();
		
		TreeAlternative alt = getValidationTreeRoot();
		//new SwingTree(alt);
	}
	
	public TreeAlternative getValidationTreeRoot() {
		return getRule(topRule).getBlock().getRewriteMap().values().iterator().next();
	}
	
	private void buildANTLRGrammarParser() {
		ANTLRInputStream in = null;
		try {
			in = new ANTLRInputStream(new FileInputStream(new File(grammarPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
        // BUILD AST
        ANTLR3Lexer lex = new ANTLR3Lexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        ANTLR3Parser g = new ANTLR3Parser(tokens);
        ANTLR3Parser.grammarDef_return r = null;
		try {
			r = g.grammarDef();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
        this.tree = (CommonTree)r.getTree();
	}
	
	private void findRules(CommonTree tree) {
		if(tree.getText().equals(RULE_TEXT) && tree.getChildCount() > 0) {
			namesToRules.put(tree.getChild(0).getText(), new Rule(tree, this));
		}
		else {
			for (int i = 0; i < tree.getChildCount(); i++) {
				findRules((CommonTree)tree.getChild(i));
			}
		}
	}
	
	/**
	 * Used to build a spanning tree out of what would otherwise be a graph of connections between
	 * rules. This way all rules have a connection back to the root, without the nastiness of walking
	 * in cycles that you'd get working with the actual graph.
	 * @param incoming
	 * @param possibilities
	 */
	private void treeConnectRules() {
		Rule top = getRule(topRule);
		treeConnectRulesAux(top);
	}
	
	private void treeConnectRulesAux(Rule rule) {
		visited.add(rule);
		Alternative[] alts = rule.getBlock().getAlternatives();
		for (Alternative alternative : alts) {
			ArrayList<String> refs = alternative.getRuleRefsRecursive();
			for (String ref : refs) {
				Rule refedRule = getRule(ref);
				//check for nullness 'cause Alternative will treat unrecognized
				//children as rule references (see processChildren(...))
				if(refedRule != null && !visited.contains(refedRule)) {
					refedRule.addIncomingConnection(rule);
					rule.addOutgoingConnection(refedRule);
					treeConnectRulesAux(refedRule);
				}
			}
		}
	}

	/**
	 * Builds the graph of rule connections based on how they reference each other in the grammar.
	 */
	private void connectRules() {
		Collection<Rule> rules = getRules();
		for (Rule rule : rules) {
			Alternative[] alts = rule.getBlock().getAlternatives();
			for (Alternative alternative : alts) {
				ArrayList<String> refs = alternative.getRuleRefsRecursive();
				for (String ref : refs) {
					Rule refedRule = getRule(ref);
					//Alternative takes certain unrecognized things and makes
					//them rule references; see processChildren(...)
					if(refedRule != null) {
						refedRule.addIncomingConnection(rule);
						rule.addOutgoingConnection(refedRule);
					}
				}
			}
		}
	}
	
	/**
	 * Until this method is called, only TreeAlternatives which appear within the same rule can be
	 * connected with each other; this method initiates a process which will locate parent nodes by
	 * rule references (that show up in Alternatives for Rules), and connect them with their proper
	 * children.
	 */
	private void connectRewriteTree() {
		Collection<Rule> rules = getRules();
		for (Rule rule : rules) {
			Collection<TreeAlternative> rewrites = rule.getBlock().getRewrites();
			for (TreeAlternative tAlt : rewrites) {
				tAlt.connectToParent();
			}
		}
	}
	
	public CommonTree getTree() {
		return tree;
	}
	
	public Rule getRule(String name) {
		return namesToRules.get(name);
	}
	
	/**
	 * Behaves straightforwardly, except that there is a certain convention
	 * involved: node names correspond to rule names except they are all
	 * caps and are prefixed with a 'W'; so for the rule 'fieldDeclaration',
	 * the node name must be 'WFIELDDECLARATION'. The node names come from
	 * the node configuration file. 
	 * @param nodeName
	 * @return
	 */
	public Rule getRuleFromNodeName(String nodeName) {
		if(nodeName != null && nodeName.length() > 1) {
			String name = nodeName.substring(1);
			Collection<Rule> rules = getRules();
			Iterator<Rule> iter = rules.iterator();
			while(iter.hasNext()) {
				Rule next = iter.next();
				if(next.getName().equalsIgnoreCase(name))
					return next;
			}
		}
		
		return null;
	}
	
	public Collection<Rule> getRules() {
		return namesToRules.values();
	}
	
	public String getTopRule() {
		return topRule;
	}
}
