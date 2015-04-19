package com.cyntaks.GDXGIDE.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextAppearance {
	private TypeFace typeFace;
	private String fontStyle;
	private int fontSize;
	private BitmapFont font; 
	private Color color;
	private int minFontSize; //calculated based on which files are available
	private int maxFontSize; //calculated based on which files are available
	
	public TextAppearance(TextAppearance appearance) {
		this(appearance.getTypeFace(), appearance.getFontStyle(), appearance.getFontSize(), appearance.getColor());
	}
	
	public TextAppearance(TypeFace typeFace, String fontStyle, int fontSize, Color color) {
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.color = color;
		setTypeFace(typeFace);
	}
	
	private void setFontSizeBounds() {
		int[] fontSizes = typeFace.getSizesForStyle(fontStyle);
		if(fontSizes != null) {
			minFontSize = fontSizes[0];
			maxFontSize = fontSizes[fontSizes.length - 1];
		}
	}
	public TypeFace getTypeFace() { 
		return typeFace;
	}
	public void setTypeFace(TypeFace typeFace) {
		this.typeFace = typeFace;
		setFontStyle(fontStyle);
		setFontSize(fontSize);
	}
	public String getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(String fontStyle) {
		if(typeFace != null) {
			this.fontStyle = fontStyle;
			this.font = typeFace.getFont(fontSize, fontStyle);
			setFontSizeBounds();
		}
	}
	public int getFontSize() {
		return fontSize;
	}
	public boolean setFontSize(int fontSize) {
		if(fontSize <= maxFontSize && fontSize >= minFontSize && typeFace != null) {
			this.fontSize = fontSize;
			this.font = typeFace.getFont(fontSize, fontStyle);
			return true;
		}
		
		return false;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public BitmapFont getFont() {
		return font;
	}

	public int getMinFontSize() {
		return minFontSize;
	}

	public int getMaxFontSize() {
		return maxFontSize;
	}
}
