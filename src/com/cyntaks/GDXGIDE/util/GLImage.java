package com.cyntaks.GDXGIDE.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.cyntaks.GDXGIDE.Corpus;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.ResourceManager;
import com.cyntaks.GDXGIDE.ResourceUser;

public class GLImage extends Corpus implements ResourceUser {
	private Texture texture;
	private String texPath;
	
	public GLImage(Texture texture) {
		this.texture = texture;
		super.setWidth(texture.getWidth());
		super.setHeight(texture.getHeight());
	}
	
	public GLImage(String texPath) {
		this.texPath = texPath;
	}
	
	public void load() {
		ResourceManager.queueTexture(texPath, texPath, TextureFilter.Linear);
	}
	
	public void init() {
		this.texture = ResourceManager.getTexture(texPath);
		if(texture != null) {
			super.setWidth(texture.getWidth());
			super.setHeight(texture.getHeight());
		}
	}
	
	public void draw(float r, float g, float b, float a, float x, float y) {
	}
	
	public void draw(float x, float y, float width, float height, float r, float g, float b, float a) {
		if(super.isDebug()) {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(0, 1, 0, 0.2f);
			debugBox.draw(x, y, getWidth(), getHeight());
			GIDEApp.SPRITE_BATCH.setColor(old);
		}
		
		Color old = GIDEApp.SPRITE_BATCH.getColor();
		GIDEApp.SPRITE_BATCH.setColor(r, g, b, a);
		GIDEApp.SPRITE_BATCH.draw(texture, x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), false, true);
		GIDEApp.SPRITE_BATCH.setColor(old);
	}
	
	public void draw(float x, float y, float width, float height) {
		Color color = GIDEApp.SPRITE_BATCH.getColor();
		draw(x, y, width, height, color.r, color.g, color.b, color.a);
	}
}
