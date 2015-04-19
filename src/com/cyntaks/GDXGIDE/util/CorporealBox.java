package com.cyntaks.GDXGIDE.util;

import com.cyntaks.GDXGIDE.*;

public class CorporealBox extends Corpus{
	private VariableSizeBox box;
	
	public CorporealBox() {
		
	}
	
	public void load() {
		super.load();
		
		box.load();
	}
	
	public void init() {
		super.init();
		
		box.init();
	}
	
	public CorporealBox(VariableSizeBox vbox) {
		setBox(vbox);
	}
	
	public void setBox(VariableSizeBox box) {
		this.box = box;
		super.setDrawable(box);
	}
	
	public VariableSizeBox getBox() {
		return box;
	}
}
