package com.cyntaks.GDXGIDE.util;

import com.badlogic.gdx.graphics.Texture;

public class TextureRegion {
	public int x;
	public int y;
	public int width;
	public int height;
	public Texture tex;
	
	public TextureRegion(int x, int y, int width, int height, Texture tex) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.tex = tex;
	}
}
