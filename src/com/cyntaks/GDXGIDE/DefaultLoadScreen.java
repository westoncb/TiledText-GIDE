package com.cyntaks.GDXGIDE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.text.TypeFace;

public class DefaultLoadScreen implements Drawable {
	private GLString loadString;
	private float yVol = 10;
	private float yOff;
	
	@Override
	public void draw(float x, float y, float width, float height) {
		float strWidth = loadString.getTextWidth();
		float strHeight = loadString.getTextHeight();
		float boxWidth = strWidth * 1.5f;
		float boxHeight = strHeight * 2.3f;
		float boxX = x + width/2 - boxWidth/2;
		float boxY = y + height/2 - boxHeight/2;
		Color old = GIDEApp.SPRITE_BATCH.getColor();
		
		GIDEApp.SPRITE_BATCH.setColor(0, 0, 0, 0.75f);
		Corpus.debugBox.draw(boxX, boxY, boxWidth, boxHeight);
		
		GIDEApp.SPRITE_BATCH.setColor(0, .9f, 0, 0.45f);
		Corpus.debugBox.draw(boxX, boxY + yOff, boxWidth, 3);
		//Corpus.debugBox.draw(boxX+boxWidth-10, boxY + yOff, 10, 3);
		yOff += yVol;
		if(yOff > boxHeight - 3) {
			yOff = boxHeight - 3;
			yVol *= -1;
		} else if(yOff < 0) {
			yOff = 0;
			yVol *= -1;
		}
		
		GIDEApp.SPRITE_BATCH.setColor(old);
		
		loadString.draw(boxX + boxWidth/2 - strWidth/2, boxY + boxHeight/2 - strHeight/2);
	}

	@Override
	public void draw() {
		draw(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void load() {
		TypeFace loadFace = ResourceManager.getTypeFace("droid");
		loadString = new GLString("Loading...", loadFace, TypeFace.STYLE_PLAIN, 27, Color.WHITE, true);
		loadString.setSpatialConstraints(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		loadString.load();
		Corpus.debugBox.load();
	}
	
	public void init() {
		loadString.init();
		Corpus.debugBox.init();
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHeight(float height) {
		// TODO Auto-generated method stub
		
	}

}
