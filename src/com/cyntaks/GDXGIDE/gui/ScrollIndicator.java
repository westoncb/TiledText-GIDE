package com.cyntaks.GDXGIDE.gui;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.ResourceManager;
import com.cyntaks.GDXGIDE.ResourceUser;
import com.cyntaks.sgf.event.DefaultInterpolator;
import com.cyntaks.sgf.event.Event;
import com.cyntaks.sgf.event.EventListener;
import com.cyntaks.sgf.event.Interpolator;

public class ScrollIndicator implements EventListener, ResourceUser {
	private int side;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	
	private static final int TIP_HEIGHT = 6;
	private static final int TIP_WIDTH = 13;
	private static final int CENTER_WIDTH = 13;
	private static final int CENTER_HEIGHT = 1;
	private static final int X_BLUR_SIZE = 5;
	private static final int Y_BLUR_SIZE = 2;
	
	private static final int FADE_IN_ID = 123;
	private static final int FADE_OUT_ID = 126;
	private static final int PULSE_ID = 134;
	private static final int PULSE_TIME = 2000;
	private static final int FADE_TIME = 600;
	
	private Texture barTex;
	
	private float alpha;
	private boolean displaying;
	
	private Event pulseEvent;
	private Event fadeInEvent;
	private Event fadeOutEvent;
	
	public ScrollIndicator(int side) {
		this.side = side;
	}
	
	public void load() {
		ResourceManager.queueTexture("images/barTex.png", "images/barTex.png", TextureFilter.Linear);	
	}
	
	public void init() {
		barTex = ResourceManager.getTexture("images/barTex.png");
	}
	
	public void gridPositionUpdate(float gridX, float winX, float gridY, float winY, float gridWidth, float gridHeight, float windowWidth, float windowHeight) {
		if(!displaying) {
			if(side == LEFT && gridX + winX < winX) {
				start();
			} else if (side == RIGHT && winX + gridX + gridWidth > winX + windowWidth)
				start();
			else if (side == UP && winY + gridY + gridHeight > winY + windowHeight)
				start();
			else if(side == DOWN && gridY + winY < winY)
				start();
		} else {
			if(side == LEFT && gridX + winX >= winX) {
				end();
			} else if (side == RIGHT && winX + gridX + gridWidth <= winX + windowWidth)
				end();
			else if (side == UP && winY + gridY + gridHeight <= winY + windowHeight)
				end();
			else if(side == DOWN && gridY + winY >= winY)
				end();
		}
	}
	
			
	private void start() {
		displaying = true;
		fadeInEvent = new Event(FADE_IN_ID, FADE_IN_ID, FADE_TIME/2, Event.EVERY_FRAME, false, this);
	}
	
	private void end() {
		if((pulseEvent != null || fadeInEvent != null)) {
			if(pulseEvent != null)	 {
				pulseEvent.kill();
				pulseEvent = null;
			} 
			if(fadeInEvent != null) {
				fadeInEvent.killSilently();
				alpha = 0;
			}
		}
		
		displaying = false;
		if(fadeOutEvent == null) {
			Interpolator interp = new DefaultInterpolator(alpha, 0);
			fadeOutEvent = new Event(FADE_OUT_ID, FADE_OUT_ID, FADE_TIME/2, Event.EVERY_FRAME, false, interp, this);
		}
	}
	
	public void draw(float x, float y, float width, float height) {
		GIDEApp.SPRITE_BATCH.setColor(0, 0.75f, 0.35f, alpha);
		GIDEApp.SPRITE_BATCH.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		float heightMod = height*1f;
		float widthMod = width*1f;
		
		if(side == LEFT || side == RIGHT) {
			if(side == LEFT)
				x -= X_BLUR_SIZE;
			else {
				x += width;
				x -= (CENTER_WIDTH - X_BLUR_SIZE);
			}
			for (int i = 0; i < 3; i++) {
				GIDEApp.SPRITE_BATCH.draw(barTex, x, y - Y_BLUR_SIZE + heightMod, TIP_WIDTH, TIP_HEIGHT, 0, 0, TIP_WIDTH, TIP_HEIGHT, false, true);
				GIDEApp.SPRITE_BATCH.draw(barTex, x, y + height - TIP_HEIGHT + Y_BLUR_SIZE - heightMod, TIP_WIDTH, TIP_HEIGHT, 0, 0, TIP_WIDTH, TIP_HEIGHT, false, false);
				GIDEApp.SPRITE_BATCH.draw(barTex, x, y + TIP_HEIGHT - Y_BLUR_SIZE + heightMod, CENTER_WIDTH, height - TIP_HEIGHT*2 + (Y_BLUR_SIZE*2) - heightMod*2, 0, TIP_HEIGHT, TIP_WIDTH, CENTER_HEIGHT, false, false);
			}
		} else {
			if(side == DOWN) {
				y -= X_BLUR_SIZE - 1; // "-1" here is a temporary hack
			}
			else {
				y += height;
				y -= (CENTER_WIDTH - X_BLUR_SIZE + 1); // "+1" here is a temporary hack
			}
			
			
			for (int i = 0; i < 3; i++) {
				GIDEApp.SPRITE_BATCH.draw(barTex, x - Y_BLUR_SIZE + widthMod, y, TIP_HEIGHT, TIP_WIDTH, barTex.getWidth() - TIP_HEIGHT, 0, TIP_HEIGHT, TIP_WIDTH, true, false);
				GIDEApp.SPRITE_BATCH.draw(barTex, x + width - TIP_HEIGHT + Y_BLUR_SIZE - widthMod, y, TIP_HEIGHT, TIP_WIDTH, barTex.getWidth() - TIP_HEIGHT, 0, TIP_HEIGHT, TIP_WIDTH, false, false);
				GIDEApp.SPRITE_BATCH.draw(barTex, x + TIP_HEIGHT - Y_BLUR_SIZE + widthMod, y, width - TIP_HEIGHT*2 + (Y_BLUR_SIZE*2) - widthMod*2, CENTER_WIDTH, barTex.getWidth() - (TIP_HEIGHT + CENTER_HEIGHT), 0, CENTER_HEIGHT, CENTER_WIDTH, false, false);
			}
		}
		GIDEApp.SPRITE_BATCH.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void update(int id, int type, float value) {
		alpha = value;
	}

	public void finish(int id, int type) {
		if(id == FADE_IN_ID && displaying) {
			fadeInEvent = null;
			Interpolator interp = new DefaultInterpolator(1, .1f, 1);
			pulseEvent = new Event(PULSE_ID, PULSE_ID, PULSE_TIME, Event.EVERY_FRAME, true, interp, this);
		} else if(id == FADE_OUT_ID)
			fadeOutEvent = null;
	}
	
	public boolean isDisplaying() {
		return displaying;
	}
}