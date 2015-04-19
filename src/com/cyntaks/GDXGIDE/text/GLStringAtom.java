package com.cyntaks.GDXGIDE.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.programs.CodeEditor;

/**
 * GLStringAtoms are the basic components of GLStrings; they are used to break up strings along arbitrary boundaries so that it's possible
 * to wrap text on more than just words. Additionally, each atom of a GLString can have its own TextAppearance -- the most obvious use of this
 * being multi-colored strings.
 * 
 * @author weston
 *
 */

public class GLStringAtom {
	private String text;
	private TextAppearance appearance;
	private BitmapFontCache fCache;
	private float x, y;
	private int minFontSize = 0; //used to enforce an arbitrary minimum
	private int maxFontSize = Integer.MAX_VALUE; //used to enforce an arbitrary maximum
	private int leftOffset;
	private long splitID;
	private boolean metaAtom; //indicates that this atom is functional rather than visual
	private boolean isTransient; //indicates that this atom is used for a string's current layout; not part of the original data
	
	public static int DEFAULT = 0;
	public static int NEWLINE = 1;
	private int type = DEFAULT;
	
	private static TextAppearance EMPTY_APPEARANCE = new TextAppearance(null, TypeFace.STYLE_PLAIN, 0, Color.BLACK);
	private static BitmapFontCache EMPTY_CACHE = new BitmapFontCache(new BitmapFont(Gdx.files.internal("fonts/droid/" + "droid-14_plain" + ".fnt"), false));
	
	public GLStringAtom(String text, TextAppearance appearance) {
		this.text = text;
		this.appearance = appearance;
		splitID = -1; //indicates that the atom has not been split
		updateFCache();
	}
	
	public GLStringAtom(String text, TypeFace typeFace, String fontStyle, int fontSize, Color color) {
		this(text, new TextAppearance(typeFace, fontStyle, fontSize, color));
	}
	
	public GLStringAtom(int type, boolean isTransient) {
		this.splitID = -1;
		this.metaAtom = true;
		this.setType(type);
		this.setTransient(isTransient);
		this.fCache = EMPTY_CACHE;
		this.appearance = EMPTY_APPEARANCE;
	}
	
	public static GLStringAtom getNewlineAtom(boolean isTransient) {
		return new GLStringAtom(NEWLINE, isTransient);
	}
	
	protected void draw() {
		if(!metaAtom) {
			fCache.translate(0, appearance.getFont().getCapHeight() - appearance.getFont().getXHeight());
			fCache.draw(GIDEApp.SPRITE_BATCH);
			fCache.translate(0, -(appearance.getFont().getCapHeight() - appearance.getFont().getXHeight()));
		}
	}
	
	private void updateFCache() {
		fCache = new BitmapFontCache(appearance.getFont());
		fCache.setColor(appearance.getColor());
		fCache.setText(text, 0, 0);
	}
	
	public boolean setFontSize(int size) {
		boolean success = false; 
		if(size <= maxFontSize && size >= minFontSize) {
			success = appearance.setFontSize(size);
			if(success)
				updateFCache();
			else
				System.err.println("Failed to adjust font size.");
		} else
			System.err.println("Font size out of range.");
		
		return success;
	}
	
	public void setFontStyle(String style) {
		appearance.setFontStyle(style);
		updateFCache();
	}
	
	public void setTypeFace(TypeFace typeFace) {
		appearance.setTypeFace(typeFace);
		updateFCache();
	}
	
	public void setColor(Color color) {
		appearance.setColor(color);
		fCache.setColor(color);
	}
	
	public float getWidth() {
		if(!metaAtom)
			return fCache.getBounds().width;
		else
			return 0;
	}

	public float getHeight() {
		if(!metaAtom)
			return fCache.getBounds().height;
		else
			return 0;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		fCache.setPosition(x, y);
	}
	
	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
		fCache.translate(x, y);
	}
	
	public BitmapFont getFont() {
		return appearance.getFont();
	}
	
	public TypeFace getTypeFace() {
		return appearance.getTypeFace();
	}
	
	public int getFontSize() {
		return appearance.getFontSize();
	}
	
	public String getFontStyle() {
		return appearance.getFontStyle();
	}
	
	public Color getColor() {
		return appearance.getColor();
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public String getText() {
		return text;
	}

	public int getMinFontSize() {
		return minFontSize;
	}

	public void setMinFontSize(int minFontSize) {
		this.minFontSize = minFontSize >= appearance.getMinFontSize() ? minFontSize : appearance.getMinFontSize();
	}

	public int getMaxFontSize() {
		return maxFontSize;
	}

	public void setMaxFontSize(int maxFontSize) {
		this.maxFontSize = maxFontSize <= appearance.getMaxFontSize() ? maxFontSize : appearance.getMaxFontSize();
	}
	
	public boolean isSplit() {
		return splitID != -1;
	}

	protected long getSplitID() {
		return splitID;
	}

	protected void setSplitID(long splitID) {
		this.splitID = splitID;
	}
	
	public int getLeftOffset() {
		return leftOffset;
	}

	public void setLeftOffset(int leftOffset) {
		this.leftOffset = leftOffset;
	}
	
	public String toString() {
		String text = getText();
		if(getType() != GLStringAtom.NEWLINE)
			text += "|";
		else
			text = "\\n|";
		return text;
	}

	public boolean isMetaAtom() {
		return metaAtom;
	}

	public void setMetaAtom(boolean metaAtom) {
		this.metaAtom = metaAtom;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}
}