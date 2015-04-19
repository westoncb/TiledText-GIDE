package com.cyntaks.GDXGIDE;

import java.util.ArrayList;

public interface LinkedStructureManager {
	public void initialize(GNode root, Grid grid);
	public void updateGrid();
	public void goTo(GNode node);
	public void add(ArrayList<GNode> nodes, GNode anchor);
	public void remove(ArrayList<GNode> nodes);
	public void undo();
	public GNode getActiveNode();
}
