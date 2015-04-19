package com.cyntaks.GDXGIDE.code;

import java.util.ArrayList;
import java.util.HashMap;

import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.config.Settings;
import com.cyntaks.GDXGIDE.util.Configuration;

public class CodeFormatter {
	private ArrayList<String[]> nospacePairs;
	private static final String WILD = "a";
	public static final String TAB = "     ";
	private boolean debug = false; //I'm not positive that the visible formatting enabled by this flag doesn't
								   //actually affect the layout routine. Use with caution.
	
	public CodeFormatter(String language) {
		nospacePairs = new ArrayList<String[]>();
		
		Configuration config = Settings.getNodeConfig(language).getBlock(CodeNode.GENERAL_CONFIG);
		String nospaceString = config.getString("nospace");
		findPairsFromString(nospaceString);
	}
	
	private void findPairsFromString(String string) {
		String[] pairs = string.split(" o ");
		for(String s : pairs) {
			String[] pair = s.split("p");
			nospacePairs.add(pair);
		}
	}
	
	public boolean insertSpaceFor(String first, String second) {
		boolean doIt = true;
		for(String[] a: nospacePairs) {
			if((a[0].equalsIgnoreCase(first) && a[1].equalsIgnoreCase(second)) ||
					(a[0].equalsIgnoreCase(first) && a[1].equals(WILD)) ||
					(a[1].equalsIgnoreCase(second) && a[0].equals(WILD))) {
				doIt = false;
				break;
			}
		}
		
		return doIt;
	}
	
	public void addWhiteSpace(ArrayList<CodeNode> nodes, ArrayList<Cell> cells) {
		int index = 0;
		CodeNode node1 = null;
		CodeNode node2 = null;
		HashMap<CodeNode, ArrayList<CodeNode>> formattingAdditions = new HashMap<CodeNode, ArrayList<CodeNode>>();
		
		//figure out where the whitespace should be inserted and use the hashmap to associate it with the node it goes after
		while(index < nodes.size()) {
			node1 = (CodeNode)nodes.get(index);
			
			if(index+1 < nodes.size()) {
				node2 = (CodeNode)nodes.get(index + 1);

				if(node1.isWhiteSpace() || node2.isWhiteSpace()) {
					index++;
					continue;
				}
				
				boolean differentLines = node1.hasNewlineAfter() || node2.hasNewlineBefore();
				if(differentLines) {
					ArrayList<CodeNode> whitespace = new ArrayList<CodeNode>();
					
					for(int i = 0; i < getTabDepth(node2); i++) {
						if(debug)
							whitespace.add(new CodeNode("| T |", GNode.TAB));
						String tabString = debug ? "" : CodeFormatter.TAB;
						whitespace.add(new CodeNode(tabString, GNode.TAB));
					}
					if(debug)
						whitespace.add(new CodeNode("|N|", GNode.SPACE));
					whitespace.add(new CodeNode("\n", GNode.NEWLINE));
					if(node1.hasNewlineAfter() && node2.hasNewlineBefore()) {
						if(debug)
							whitespace.add(new CodeNode("|N|", GNode.SPACE));
						whitespace.add(new CodeNode("\n", GNode.NEWLINE));
					}
					
					formattingAdditions.put(node1, whitespace);
				}
				else {
					boolean insert = insertSpaceFor(node1.getText(), node2.getText());
					if(insert) {
						ArrayList<CodeNode> whitespace = new ArrayList<CodeNode>();
						String spaceString = debug ? "--" : " ";
						whitespace.add(new CodeNode(spaceString, GNode.SPACE));
						formattingAdditions.put(node1, whitespace);
					}
				}
			}
			index++;
		}

		//actually add the whitespace to the node list
		for(int i = nodes.size()-1; i > 0; i--) {
			ArrayList<CodeNode> additions = formattingAdditions.get(nodes.get(i));
			int additionCount = 0;
			if(additions != null) {
				
				for(int j = additions.size()-1; j > -1; j--) {
					nodes.add(i+1, additions.get(j));
					additionCount++;
				}
				
			}
			/*System.out.print(nodes.get(i) + ": ");
			for(int j = 0; j < additionCount; j++) {
				System.out.print(nodes.get(i+j+1).getType() + " ");
			}
			System.out.println("\n-----------------------");
			*/
			
		}

		//The node list passed in is just for figuring out whitespace locations; this section is needed to actually
		//incorporate the whitespace into the node lists (code lists) inside of individual Cells.
		CodeNode last = null;
		for (Cell codeCell : cells) {
			ArrayList<CodeNode> codeList = new ArrayList<CodeNode>();
			ArrayList<CodeNode> oldList = ((CodeCell)codeCell).getCodeList();
			//System.out.println("New cell: " + codeCell.getNode());
			for(CodeNode oldNode : oldList) {
				int start = nodes.indexOf(oldNode);
				//System.out.println("node: |" + oldNode + "|, start: " + start);
				assert (oldNode != null && start > -1) :"SHIIIIITTT";
				int off = -1;
				if(last != null) {
					CodeNode next = nodes.get(start + off);
					while(next != last) {
						codeList.add(next);
						off--;
						next = nodes.get(start + off);
					}
				}
				
				codeList.add(oldNode);
				last = oldNode;
			}
			
			((CodeCell)codeCell).setCodeList(codeList);
		}
	}
	
	private int getTabDepth(CodeNode node) {
		int containers = 0;
		CodeNode cur = node;
		while(cur.getParent() != null) {
			if(cur.getParent().isTabContainer())
				containers++;
			cur = cur.getParent();
		}
		
		return containers;
	}
}
