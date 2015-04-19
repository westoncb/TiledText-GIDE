package com.cyntaks.GDXGIDE.code.ANTLR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.tree.CommonTree;

import com.cyntaks.GDXGIDE.code.CodeNode;
import com.cyntaks.GDXGIDE.code.java.JavaLexer;
import com.cyntaks.GDXGIDE.code.java.JavaParser;

public class SimpleParser {
	private String parserClassName;
	private Grammar grammar;
	
	public SimpleParser(Grammar grammar, String qualifiedParserClassName) {
		this.parserClassName = qualifiedParserClassName;
		this.grammar = grammar;
	}
	
	/**
	 * Finds the first ancestor of this node that has a rule associated with it and parses
	 * all of the text beneath that node starting from the associated rule. 
	 * @param node A node representative of a piece of text needing parsing
	 * 
	 * @return
	 */
	public ParserSession parseFromNearestSufficientRule(CodeNode node) {
		boolean redo = false;
		ParserSession result = null;
		CodeNode parent = node;
		Rule rule = null;
		Rule lastRule = null;
		boolean checkWithoutSpace = false;
		boolean outOfRules = false;
		
		/* Try parsing from the nearest rule; it's possible it will be insufficient to cover all the text which needs to be parsed. If
		 * that's the case, a more general rule will be attempted; this also will occur if parse errors are found. If all the text was
		 * processed, but parse errors still occurred, another attempt will be made, this time without putting a space between the newly
		 * inserted nodes and the node directly before. This looping could be a performance concern, but really, a better place for optimizing
		 * would be on speeding up the parsing process in general, rather than eliminating a couple of iterations here (which probably isn't
		 * necessary)...
		 */
		do {
			if(!checkWithoutSpace) {
				parent = (CodeNode)parent.getNextParentAssociatedWithRule(grammar);
			}
			
			String text = parent.getTextBeneathThisNode(checkWithoutSpace);
			System.out.println("PARSING: " + text);
			
			String parentString = parent.getText();
			System.out.println("PARENT: " + parentString);
			lastRule = rule;
			rule = grammar.getRuleFromNodeName(parentString);
			if(rule == null)
				break;
			String ruleName = rule.getName();
			System.out.println("USING RULE: " + ruleName);
			
			result = parseText(text, ruleName);
			redo = !result.allTextProcessed; //Need a broader rule
			outOfRules = (lastRule == rule && !checkWithoutSpace);
			if(!redo && !result.errors.isEmpty() && !outOfRules) { //Rule was broad enough to process all the text, but still have errors
				if(!checkWithoutSpace) //if we haven't tried putting the text together without spaces, try it
					checkWithoutSpace = true;
				else { //spaces weren't the problem, try a broader rule still
					redo = true;
					checkWithoutSpace = false;
				}
			} else {//We checked without space and it worked, now set to false so we can leave the loop.
				checkWithoutSpace = false;
			}
			
			result.setRuleNode(parent);
		} while((redo && !outOfRules) || checkWithoutSpace);

		return result;
	}
	
	public ParserSession parseText(String text, String startingRule) {
		CommonTree t = null;
		ParserSession session = null;
		
		long start = System.nanoTime();
		
		JavaLexer lexer = new JavaLexer(new ANTLRStringStream(text));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		
		try {
			Method[] methods;
			methods = Class.forName(parserClassName).getDeclaredMethods();
			Method method = null;
			for (Method currentMethod : methods) {
				if(currentMethod.getName().equals(startingRule)) {
					method = currentMethod;
					break;
				}
			}
			
			ParserRuleReturnScope result = (ParserRuleReturnScope)method.invoke(parser, new Object[0]);
			t = (CommonTree)result.getTree();

			LinkedList<String> list = (LinkedList<String>)parser.getErrors();
			
			boolean allTextProcessed = lexer.getText().length() == 0 ? true : false;
			session = new ParserSession(t, list, allTextProcessed);
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}  catch (InvocationTargetException e) {
			e.printStackTrace();
		}  catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		System.out.println(((System.nanoTime()-start)/1000000) + " millis.");
		
		return session;
	}
	
	public class ParserSession {
		public CommonTree ast;
		public LinkedList<String> errors;
		public boolean allTextProcessed;
		public CodeNode ruleNode;
		
		public ParserSession(CommonTree ast, LinkedList<String> errors, boolean allTextProcessed) {
			this.ast = ast;
			this.errors = errors;
			this.allTextProcessed = allTextProcessed;
		}
		
		protected void setRuleNode(CodeNode node) {
			this.ruleNode = node;
		}
	}
}
