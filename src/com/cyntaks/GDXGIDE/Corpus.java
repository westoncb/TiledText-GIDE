package com.cyntaks.GDXGIDE;

import org.omg.PortableServer.ForwardRequestHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;
import com.cyntaks.sgf.event.Event;
import com.cyntaks.sgf.event.EventListener;

public class Corpus implements EventListener, Drawable {
	private boolean debug;
	private State state = new State();
	private Drawable drawable;
	public static final int CENTER_DRAWABLE = 0;
	public static final int STRETCH_DRAWABLE = 1;
	public static final int TILE_DRAWABLE = 2;
	private int drawStyle = STRETCH_DRAWABLE;
	
	public static final int CHANGE_WIDTH_TYPE = 518;
	public static final int CHANGE_HEIGHT_TYPE = 519;
	public static final int CHANGE_X_TYPE = 520;
	public static final int CHANGE_Y_TYPE = 521;
	public static final int CHANGE_RED_TYPE = 522;
	public static final int CHANGE_GREEN_TYPE = 523;
	public static final int CHANGE_BLUE_TYPE = 524;
	public static final int CHANGE_ALPHA_TYPE = 525;
	
	public static final int CHANGE_WIDTH_ID = 418;
	public static final int CHANGE_HEIGHT_ID = 419;
	public static final int CHANGE_X_ID = 420;
	public static final int CHANGE_Y_ID = 421;
	public static final int CHANGE_RED_ID = 422;
	public static final int CHANGE_GREEN_ID = 423;
	public static final int CHANGE_BLUE_ID = 424;
	public static final int CHANGE_ALPHA_ID = 425;
	
	private Event changeXEvent;
	private Event changeYEvent;
	private Event changeWidthEvent;
	private Event changeHeightEvent;
	private Event changeREvent;
	private Event changeGEvent;
	private Event changeBEvent;
	private Event changeAEvent;

	private float destX;
	private float destY;
	private float destWidth;
	private float destHeight;
	
	private Corpus afterBlock; //transitory spacers which can be placed after a cell to alter the layout (see GridDisplay.setContents())
	private Corpus beforeBlock;
	
	public static VariableSizeBox debugBox;

	static {
		debugBox = new VariableSizeBox("images/newtest.png");
		debugBox.load();
		debugBox.init();
	}
	
	public Corpus() {
		
	}
	
	public Corpus(Drawable drawable) {
		this();
		this.drawable = drawable;
	}
	
	public void load() {
		if(this.drawable != null)
			this.drawable.load();
	}
	
	public void init() {
		if(this.drawable != null)
			this.drawable.init();
	}
	
	public State copyState(State state) {
		State newState = new State();
		newState.bounds.x = state.bounds.x;
		newState.bounds.y = state.bounds.y;
		newState.bounds.width = state.bounds.width;
		newState.bounds.height = state.bounds.height;
		newState.red = state.red;
		newState.green = state.green;
		newState.blue = state.blue;
		newState.alpha = state.alpha;
		
		return newState;
	}
	
	public void draw() {
		draw(getX(), getY(), getWidth(), getHeight(), getRed(), getGreen(), getBlue(), getAlpha());
	}
	
	public void draw(float x, float y) {
		draw(x, y, getWidth(), getHeight(), getRed(), getGreen(), getBlue(), getAlpha());
	}
	
	public void draw(float x, float y, float width, float height) {
		draw(x, y, width, height, getRed(), getGreen(), getBlue(), getAlpha());
	}
	
	public void draw(float x, float y, float width, float height, float r, float g, float b, float a) {
		if(drawable != null) {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(r, g, b, a);
			
			if(drawStyle == STRETCH_DRAWABLE) {
				drawable.draw(x, y, width, height);
			} else if(drawStyle == TILE_DRAWABLE) {
				//System.out.println("tiling: x" + x + ", y: " + y + ", width: " + width + ", height: " + height+ ", a: " + a);
				int xRepeat = (int)Math.ceil(width/drawable.getWidth());
				int yRepeat = (int)Math.ceil(height/drawable.getHeight());
				float xOff = 0;
				float yOff = 0;
				for (int i = 0; i < yRepeat; i++) {
					for (int j = 0; j < xRepeat; j++) {
						drawable.draw(x+xOff, y+yOff, drawable.getWidth(), drawable.getHeight());
						xOff += drawable.getWidth();
					}
					yOff += drawable.getHeight();
					xOff = 0;
				}
			} else
				drawable.draw(x + width/2 - drawable.getWidth()/2, y + height/2 - drawable.getHeight()/2, drawable.getWidth(), drawable.getHeight());
			GIDEApp.SPRITE_BATCH.setColor(old);
		}
		if(debug) {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(0, 1, 0, 0.2f);
			debugBox.draw(x, y, width, height);
			GIDEApp.SPRITE_BATCH.setColor(old);
		}
	}
	
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	
	public void setDestX(float x, float rate, boolean relToDistance, int id) {
		changeXEvent = new Event(id, CHANGE_X_TYPE, state.bounds.x, x, rate, 0.001f, relToDistance, this);
		changeXEvent.setReplaceable(true);
		this.destX = x;
	}
	
	public void setDestX(float x, float rate, boolean relToDistance) {
		setDestX(x, rate, relToDistance, CHANGE_X_ID);
	}
	
	public void setDestX(float x, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestX(x, rate, relToDistance, id);
		changeXEvent.addEventListener(listener);
	}
	
	public void setDestY(float y, float rate, boolean relToDistance, int id) {
		changeYEvent = new Event(id, CHANGE_Y_TYPE, state.bounds.y, y, rate, 0.001f, relToDistance, this);
		changeYEvent.setReplaceable(true);
		this.destY = y;
	}
	
	public void setDestY(float y, float rate, boolean relToDistance) {
		setDestY(y, rate, relToDistance, CHANGE_Y_ID);
	}
	
	public void setDestY(float y, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestY(y, rate, relToDistance, id);
		changeYEvent.addEventListener(listener);
	}
	
	public void setDestWidth(float width, float rate, boolean relToDistance, int id) {
		changeWidthEvent = new Event(id, CHANGE_WIDTH_TYPE, state.bounds.width, width, rate, 0.001f, relToDistance, this);
		changeWidthEvent.setReplaceable(true);
		
		this.destWidth = width;
	}
	
	public void setDestWidth(float width, float rate, boolean relToDistance) {
		setDestWidth(width, rate, relToDistance, CHANGE_WIDTH_ID);
	}
	
	public void setDestWidth(float width, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestWidth(width, rate, relToDistance, id);
		changeWidthEvent.addEventListener(listener);
	}
	
	public void setDestHeight(float height, float rate, boolean relToDistance, int id) {
		changeHeightEvent = new Event(id, CHANGE_HEIGHT_TYPE, state.bounds.height, height, rate, 0.001f, relToDistance, this);
		changeHeightEvent.setReplaceable(true);
		
		this.destHeight = height;
	}
	
	public void setDestHeight(float height, float rate, boolean relToDistance) {
		setDestHeight(height, rate, relToDistance, CHANGE_HEIGHT_ID);
	}
	
	public void setDestHeight(float height, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestHeight(height, rate, relToDistance, id);
		changeHeightEvent.addEventListener(listener);
	}
	
	public void setDestRed(float r, float rate, boolean relToDistance, int id) {
		changeREvent = new Event(id, CHANGE_RED_TYPE, state.red, r, rate, .01f, relToDistance, this);
		changeREvent.setReplaceable(true);
	}
	
	public void setDestRed(float r, float rate, boolean relToDistance) {
		setDestRed(r, rate, relToDistance, CHANGE_RED_ID);
	}
	
	public void setDestRed(float r, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestRed(r, rate, relToDistance, id);
		changeREvent.addEventListener(listener);
	}
	
	public void setDestGreen(float g, float rate, boolean relToDistance, int id) {
		changeGEvent = new Event(id, CHANGE_GREEN_TYPE, state.green, g, rate, .01f, relToDistance, this);
		changeGEvent.setReplaceable(true);
	}
	
	public void setDestGreen(float g, float rate, boolean relToDistance) {
		setDestGreen(g, rate, relToDistance, CHANGE_GREEN_ID);
	}
	
	public void setDestGreen(float g, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestGreen(g, rate, relToDistance, id);
		changeGEvent.addEventListener(listener);
	}
	
	public void setDestBlue(float b, float rate, boolean relToDistance, int id) {
		changeBEvent = new Event(id, CHANGE_BLUE_TYPE, state.blue, b, rate, .01f, relToDistance, this);
		changeBEvent.setReplaceable(true);
	}
	
	public void setDestBlue(float b, float rate, boolean relToDistance) {
		setDestBlue(b, rate, relToDistance, CHANGE_BLUE_ID);
	}
	
	public void setDestBlue(float b, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestBlue(b, rate, relToDistance, id);
		changeBEvent.addEventListener(listener);
	}
	
	public void setDestAlpha(float a, float rate, boolean relToDistance, int id) {
		changeAEvent = new Event(id, CHANGE_ALPHA_TYPE, state.alpha, a, rate, .01f, relToDistance, this);
		changeAEvent.setReplaceable(true);
	}
	
	public void setDestAlpha(float a, float rate, boolean relToDistance) {
		setDestAlpha(a, rate, relToDistance, CHANGE_ALPHA_ID);
	}
	
	public void setDestAlpha(float a, float rate, boolean relToDistance, int id, EventListener listener) {
		setDestAlpha(a, rate, relToDistance, id);
		changeAEvent.addEventListener(listener);
	}
	
	public void setDestColor(float r, float g, float b, float a, float rate) {
		setDestRed(r, rate, false);
		setDestGreen(g, rate, false);
		setDestBlue(b, rate, false);
		setDestAlpha(a, rate, false);
	}
	
	public boolean isXChanging() {
		return changeXEvent != null;
	}
	
	public boolean isYChanging() {
		return changeYEvent != null;
	}
	
	public boolean isWidthChanging() {
		return changeWidthEvent != null;
	}
	
	public boolean isHeightChanging() {
		return changeHeightEvent != null;
	}
	
	public boolean isRedChanging() {
		return changeREvent != null;
	}
	
	public boolean isGreenChanging() {
		return changeGEvent != null;
	}
	
	public boolean isBlueChanging() {
		return changeBEvent != null;
	}
	
	public boolean isAlphaChanging() {
		return changeAEvent != null;
	}
	
	public void update(float delta) {
		
	}

	public void update(int id, int type, float value) {
		if(type == CHANGE_X_TYPE)
			state.bounds.x = value;
		else if(type == CHANGE_Y_TYPE)
			state.bounds.y = value;
		else if(type == CHANGE_RED_TYPE)
			state.red = value < 1 ? value : 1;
		else if(type == CHANGE_GREEN_TYPE)
			state.green = value < 1 ? value : 1;
		else if(type == CHANGE_BLUE_TYPE)
			state.blue = value < 1 ? value : 1;
		else if(type == CHANGE_ALPHA_TYPE)
			state.alpha = value < 1 ? value : 1;
		else if(type == CHANGE_WIDTH_TYPE)
			state.bounds.width = value;
		else if(type == CHANGE_HEIGHT_TYPE)
			state.bounds.height = value;
	}

	public void finish(int id, int type) {
		if(type == CHANGE_X_TYPE) {
			changeXEvent = null;
		}
		else if(type == CHANGE_Y_TYPE) {
			changeYEvent = null;
		}
		else if(type == CHANGE_RED_TYPE) {
			changeREvent = null;
		}
		else if(type == CHANGE_GREEN_TYPE) {
			changeGEvent = null;
		}
		else if(type == CHANGE_BLUE_TYPE) {
			changeBEvent = null;
		}
		else if(type == CHANGE_ALPHA_TYPE) {
			changeAEvent = null;
		}
		else if(type == CHANGE_WIDTH_TYPE) {
			changeWidthEvent = null;
		}
		else if(type == CHANGE_HEIGHT_TYPE) {
			changeHeightEvent = null;
		}
	}
	
	public void killXEvents() {
		if(changeXEvent != null) {
			changeXEvent.kill();
		}
	}
	
	public void killYEvents() {
		if(changeYEvent != null){
			changeYEvent.kill();
		}
	}
	
	public void killMotionEvents() {
		killXEvents();
		killYEvents();
	}
	
	public void killResizeEvents() {
		if(changeWidthEvent != null) {
			changeWidthEvent.kill();
		}
		if(changeHeightEvent != null){
			changeHeightEvent.kill();
		}
	}
	
	public void killColorEvents() {
		if(changeREvent != null)
			changeREvent.kill();
		if(changeGEvent != null)
			changeGEvent.kill();
		if(changeBEvent != null)
			changeBEvent.kill();
		if(changeAEvent != null)
			changeAEvent.kill();
	}
	
	public boolean isWidthIncreasing() {
		if(changeWidthEvent != null) {
			if(changeWidthEvent.getDestValue() - changeWidthEvent.getCurrentValue() > 0)
				return true;
			else
				return false;
		} else
			return false;
	}
	
	public boolean isWidthDecreasing() {
		if(changeWidthEvent != null) {
			if(changeWidthEvent.getDestValue() - changeWidthEvent.getCurrentValue() < 0)
				return true;
			else
				return false;
		} else
			return false;
	}
	
	public boolean isHeightIncreasing() {
		if(changeHeightEvent != null) {
			if(changeHeightEvent.getDestValue() - changeHeightEvent.getCurrentValue() > 0)
				return true;
			else
				return false;
		} else
			return false;
	}
	
	public boolean isHeightDecreasing() {
		if(changeHeightEvent != null) {
			if(changeHeightEvent.getDestValue() - changeHeightEvent.getCurrentValue() < 0)
				return true;
			else
				return false;
		} else
			return false;
	}
	
	public float getX() {
		return state.bounds.x;
	}
	public void setX(float x) {
		state.bounds.x = x;
	}
	
	public float getY() {
		return state.bounds.y;
	}
	public void setY(float y) {
		state.bounds.y = y;
	}
	
	public int getZ() {
		return state.z;
	}
	public void setZ(int z) {
		state.z = z;
	}
	
	public float getWidth() {
		return state.bounds.width;
	}
	public void setWidth(float width) {
		assert(width >= 0);
		state.bounds.width = width;
		if(drawable != null)
			drawable.setWidth(width);
	}
	
	public float getHeight() {
		return state.bounds.height;
	}
	public void setHeight(float height) {
		assert(height >= 0);
		state.bounds.height = height;
		if(drawable != null)
			drawable.setHeight(height);
	}
	
	public float getRed() {
		return state.red;
	}
	public void setRed(float red) {
		state.red = red;
	}
	
	public float getGreen() {
		return state.green;
	}
	public void setGreen(float green) {
		state.green = green;
	}
	
	public float getBlue() {
		return state.blue;
	}
	public void setBlue(float blue) {
		state.blue = blue;
	}
	
	public float getAlpha() {
		return state.alpha;
	}
	public void setAlpha(float alpha) {
		state.alpha = alpha;
	}
	
	public void setColor(float r, float g, float b, float a) {
		setRed(r);
		setGreen(g);
		setBlue(b);
		setAlpha(a);
	}
	
	public void setBounds(Rectangle rect) {
		state.bounds.set(rect);
	}
	
	public Rectangle getBounds() {
		return state.bounds;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public String toString() {
			return ": x: " + state.bounds.x + ", y: " + state.bounds.y + ", width: " + state.bounds.width + ", height: " + state.bounds.height;
	}
	
	public String parentToString() {
		return super.toString();
	}
	
	public float getDestX() {
		return destX;
	}
	
	public float getDestY() {
		return destY;
	}
	
	public float getDestWidth() {
		return destWidth;
	}
	
	public float getDestHeight() {
		return destHeight;
	}
	
	public Corpus getAfterBlock() {
		return afterBlock;
	}

	public void setAfterBlock(Corpus afterBlock) {
		this.afterBlock = afterBlock;
	}
	
	public Corpus getBeforeBlock() {
		return beforeBlock;
	}

	public void setBeforeBlock(Corpus beforeBlock) {
		this.beforeBlock = beforeBlock;
	}
	
	public Event getChangeWidthEvent() {
		return changeWidthEvent;
	}
	
	public Event getChangeHeightEvent() {
		return changeHeightEvent;
	}
	
	public int getDrawStyle() {
		return drawStyle;
	}

	public void setDrawStyle(int drawStyle) {
		this.drawStyle = drawStyle;
	}

	class State {
		private Rectangle bounds = new Rectangle();
		private int z;
		private float red = 1;
		private float green = 1;
		private float blue = 1;
		private float alpha = 1;
	}
}
