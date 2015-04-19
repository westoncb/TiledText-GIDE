package com.cyntaks.GDXGIDE.util;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class PointList {
	private ArrayList<Float> list = new ArrayList<Float>();
	private static final Vector2 scratch = new Vector2();
	
	public void add(float x, float y) {
		list.add(x);
		list.add(y);
	}
	
	public Vector2 get(int index) {
		scratch.set(list.get(index * 2), list.get(index * 2 + 1));
		return scratch;
	}
	
	public void set(int index, float x, float y) {
		list.set(index*2, x);
		list.set(index*2 + 1, y);
	}
	
	public void remove(int index) {
		list.remove(index * 2 + 1);
		list.remove(index * 2);
	}
	
	public int size() {
		return list.size()/2;
	}
	
	public void clear() {
		list.clear();
	}
}
