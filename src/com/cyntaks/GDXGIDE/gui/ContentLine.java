package com.cyntaks.GDXGIDE.gui;

import java.util.Iterator;
import java.util.LinkedList;

import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.Corpus;

public class ContentLine extends LinkedList<Corpus>{
	private static final long serialVersionUID = 1L;
	private int attachLocation;
	public static final int ATTACH_BEGINNING = 0;
	public static final int ATTACH_MIDDLE = 1;
	public static final int ATTACH_END = 2;
	
	
	
	public int getAttachLocation() {
		return attachLocation;
	}
	public void setAttachLocation(int attachLocation) {
		this.attachLocation = attachLocation;
	}
	
	public Corpus getCorpusBefore(Corpus target) {
		Iterator<Corpus> iter = super.iterator();
		while(iter.hasNext()) {
			Corpus next = iter.next();
			if(next == target) {
				int index = super.indexOf(next);
				if(index > 0)
					return super.get(index - 1);
				else
					return null;
			}
		}
		
		return null;
	}
	
	public Corpus getCorpusAfter(Corpus target) {
		Iterator<Corpus> iter = super.iterator();
		while(iter.hasNext()) {
			Corpus next = iter.next();
			if(next == target) {
				if(iter.hasNext())
					return iter.next();
				else
					return null;
			}
		}
		
		return null;
	}
	
	public Cell getCellAfter(Cell cell) {
		Corpus after = cell;
		do {
			after = getCorpusAfter(after);
		} while (!(after instanceof Cell) && after != null);
		
		return (Cell)after;
	}
	
	public Cell getCellBefore(Cell cell) {
		Corpus before = cell;
		do {
			before = getCorpusBefore(before);
		} while (!(before instanceof Cell) && before != null);
		
		return (Cell)before;
	}
	
	public int getCellIndex(Cell cell) {
		Iterator<Corpus> iter = super.iterator();
		boolean foundIt = false;
		int index = 0;
		while(iter.hasNext()) {
			Corpus next = iter.next();
			if(next instanceof Cell) {
				index++;
			}
			if(next == cell) {
				foundIt = true;
				index--;
				break;
			}
		}
		
		if(!foundIt)
			index = -1;
		
		return index;
	}
	
	/**
	 * Does start with the "0th" Cell.
	 * @param n
	 * @return
	 */
	public Cell getNthCell(int n) {
		Iterator<Corpus> iter = super.iterator();
		int count = 0;
		while(iter.hasNext()) {
			Corpus next = iter.next();
			if(next instanceof Cell) {
				if(count == n)
					return (Cell)next;
				count++;
			}
		}
		
		return null;
	}
}