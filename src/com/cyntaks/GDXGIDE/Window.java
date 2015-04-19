package com.cyntaks.GDXGIDE;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.cyntaks.GDXGIDE.util.Util;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

/**
 *
 * Look at draw() -- drawing the overgraphic has been disabled since it's just annoying for now. 
 * 
 * @author weston
 *
 */

public class Window extends Corpus implements Comparable<Window>, ResourceUser{
	private Window parent;
	private ArrayList<Window> children;
	private Grid grid;
	private float weight;
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	private int orientation = HORIZONTAL;

	private String name;
	private Drawable underGraphic;
	private Drawable overGraphic;
	private int insetx1;
	private int insetx2;
	private int insety1;
	private int insety2;
	private AbstractWindowTransition inTransition;
	private AbstractWindowTransition outTransition;

	private boolean loaded;
	private boolean stretchUnderGraphic;
	private boolean skipScissor;
	private static final Color DEFAULT_COLOR = new Color(1, 1, 1, 1);
	private Color underGraphicColor = DEFAULT_COLOR;
	
	public Window(float x, float y, float width, float height) {
		super.setBounds(new Rectangle(x, y, width, height));
		children = new ArrayList<Window>();
		weight = 1.0f;
	}
	
	public Window() {
		this(0, 0, 0, 0);
	}
	
	public void load() {
		super.load();
		
		if(underGraphic != null)
			underGraphic.load();
		if(overGraphic != null)
			overGraphic.load();
		
		if(inTransition != null)
			inTransition.load();
		if(outTransition != null)
			outTransition.load();
		
		if(grid != null)
			grid.load();
			
		for(Window c : children) {
			c.load();
		}
		
		loaded = true;
	}
	
	public void init() {
		if(underGraphic != null)
			underGraphic.init();
		if(overGraphic != null)
			overGraphic.init();
		
		if(inTransition != null)
			inTransition.init();
		if(outTransition != null)
			outTransition.init();
		
		if(grid != null)
			grid.init();
			
		for(Window c : children) {
			c.init();
		}
	}
	
	public void addChild(Window window, int insertAfterIndex) {
		float weight = orientation == Window.VERTICAL ? window.getHeight()/this.getHeight() : window.getWidth()/this.getWidth();
		addChild(window, weight, insertAfterIndex);
	}
	
	public void addChild(Window window, float weight, int insertAfterIndex) {
		if(weight < 0 || weight > 1)
			throw new IllegalArgumentException("weight must be between zero and one.");
		window.setParent(this);
		window.setWeight(weight);
		children.add(insertAfterIndex, window);
		doLayout();
	}
	
	public void removeChild(int index) {
		Window child = children.remove(index);
		child.setParent(null);
		doLayout();
	}
	
	private void doLayout() {
		int position = 0;
		for (Window c : children) {
			if (orientation == HORIZONTAL) {
				c.setX(getInsetx1() + position);
				c.setY(getInsety1());
				c.setWidth(getInteriorWidth() * c.getWeight());
				c.setHeight(getInteriorHeight());
				position += c.getWidth();
			} else {
				c.setY(getInsety1() + position);
				c.setX(getInsetx1());
				c.setHeight(getInteriorHeight() * c.getWeight());
				c.setWidth(getInteriorWidth());
				position += c.getHeight();
			}
			
			c.doLayout();
		}
	}
	
	public void update(float delta) {
		super.update(delta);
		doLayout();
		if(grid != null)
			grid.update(delta);
		for(Window c : children)
			c.update(delta);
		if(inTransition != null && inTransition.isActive())
			inTransition.update(delta);
		if(outTransition != null && outTransition.isActive())
			outTransition.update(delta);
	}
	
	public void draw() {
		GL20 gl = Gdx.gl20;
		
		if(parent != null && !skipScissor)
			gl.glScissor((int)getAbsX(), Gdx.graphics.getHeight() - ((int)getAbsY() + (int)getHeight()), (int)getWidth(), (int)getHeight());
		if(parent == null && !skipScissor)
			gl.glEnable(GL20.GL_SCISSOR_TEST);
		drawGraphic(underGraphic, true);
		if(grid != null)
			grid.draw();
		for(Window c : children)
			c.draw();
		drawGraphic(overGraphic, false);
		
		if(inTransition != null && inTransition.isActive())
			inTransition.draw();
		if(outTransition != null && outTransition.isActive())
			outTransition.draw();
		GIDEApp.SPRITE_BATCH.flush();
		if(parent == null && !skipScissor)
			gl.glDisable(GL20.GL_SCISSOR_TEST);
	}
	
	private void drawGraphic(Drawable drawable, boolean under) {
		if(drawable == null)
			return;
		
		GL20 gl = Gdx.gl20;
		
		if(!skipScissor)
			gl.glScissor((int)getAbsX(), Gdx.graphics.getHeight() - ((int)getAbsY() + (int)getHeight()), (int)getWidth(), (int)getHeight());

		if(drawable instanceof VariableSizeBox)
			super.setDrawStyle(Corpus.STRETCH_DRAWABLE);
		else
			super.setDrawStyle(Corpus.TILE_DRAWABLE);
		
		float oldR = super.getRed();
		float oldG = super.getGreen();
		float oldB = super.getBlue();
		float oldA = super.getAlpha();
		if(under)
			super.setColor(underGraphicColor.r, underGraphicColor.g, underGraphicColor.b, underGraphicColor.a);
		super.setDrawable(drawable);
		super.draw(getAbsX(), getAbsY(), getWidth(), getHeight());
		super.setColor(oldR, oldG, oldB, oldA);
	}
	
	//public void draw(float r, float g, float b, float a) {
		//super.draw(super.getRed(), super.getGreen(), super.getBlue(), super.getAlpha(), getAbsX(), getAbsY());
	//}
	
	public void dispose() {
		if(grid != null)
			grid.dispose();
		for(Window c : children)
			c.dispose();
		//GridMaster.removeWindow(this);
	}
	
	public boolean contains(float x, float y) {
		boolean contained = true;
		x = Util.getWorldX(x);
		y = Util.getWorldY(y);
		if(x < getAbsX() || x > getAbsX() + getWidth())
			contained = false;
		if(y < getAbsY() || y > getAbsY() + getHeight())
			contained = false;
		
		return contained;
	}
	
	public Window getNamedWindow(String name) {
		return auxGetNamedWindow(name, this);
	}
	
	private Window auxGetNamedWindow(String name, Window win) {
		Window theWindow = null;
		if(win.getName().equalsIgnoreCase(name))
			theWindow = win;
		else {
			for (Window sub : win.getChildren()) {
				theWindow = auxGetNamedWindow(name, sub);
				if(theWindow != null)
					break;
			}
		}
		
		return theWindow;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		if(!loaded) {
			this.grid = grid;
			grid.setWindow(this);
		} else
			System.err.println("Can't set grid for window after window has been loaded.");
	}

	public ArrayList<Window> getChildren() {
		return children;
	}

	public Window getParent() {
		return parent;
	}

	private void setParent(Window parent) {
		this.parent = parent;
	}
	
	public float getAbsX() {
		return (parent != null ? parent.getAbsX() : 0) + super.getX();
	}
	
	public float getAbsY() {
		return  (parent != null ? parent.getAbsY() : 0) + super.getY();
	}
	
	public float getAbsInteriorX() {
		return getAbsX() + getInsetx1();
	}
	
	public float getAbsInteriorY() {
		return getAbsY() + getInsety1();
	}
	
	public void transitionInFinished() {
		
	}
	
	public void transitionOutFinished() {
		dispose();
	}

	public Drawable getUnderGraphic() {
		return underGraphic;
	}

	public void setUnderGraphic(Drawable underGraphic) {
		if(!loaded)
			this.underGraphic = underGraphic;
		else
			System.err.println("Can't set underGraphic after window has been loaded.");
	}

	public Drawable getOverGraphic() {
		return overGraphic;
	}

	public void setOverGraphic(Drawable overGraphic) {
		if(!loaded)
			this.overGraphic = overGraphic;
		else
			System.err.println("Can't set overGraphic after window has been loaded.");
	}
	
	public void setInsets(int x1, int x2, int y1, int y2) {
		this.insetx1 = x1;
		this.insetx2 = x2;
		this.insety1 = y1;
		this.insety2 = y2;
	}
	
	public void setXInsets(int x1, int x2) {
		this.insetx1 = x1;
		this.insetx2 = x2;
	}
	
	public void setYInsets(int y1, int y2) {
		this.insetx1 = y1;
		this.insetx2 = y2;
	}
	
	public void setInTransition(AbstractWindowTransition transition) {
		if(!loaded) {
			inTransition = transition;
			inTransition.setOwner(this);
		}
		else
			System.err.println("Can't set in-transition after window has been loaded.");
	}
	
	public void setOutTransition(AbstractWindowTransition transition) {
		if(!loaded) {
			outTransition = transition;
			outTransition.setOwner(this);
		}
		else
			System.err.println("Can't set out-transition after window has been loaded.");
	}
	
	public AbstractWindowTransition getInTransition() {
		return inTransition;
	}
	
	public AbstractWindowTransition getOutTransition() {
		return outTransition;
	}
	
	public int compareTo(Window window) {
		return this.getZ() - window.getZ();
	}

	public float getWeight() {
		return weight;
	}
	
	public void setWeightSilently(float weight) {
		this.weight = weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
		if(this.getParent() != null)
			this.getParent().doLayout();
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public String toString() {
		String str = "([width:" + super.getWidth() + ", height:" + super.getHeight() + ", weight:" + this.getWeight()  + "]" + childrenToStrings() + ")";
		return str;
	}
	
	private String childrenToStrings() {
		String str = " ";
		for(Window c : children)
			str += c.toString();
		
		return str;
	}

	public boolean isSkipScissor() {
		return skipScissor;
	}

	public void setSkipScissor(boolean skipScissor) {
		this.skipScissor = skipScissor;
	}
	
	public void fitToGrid() {
		setWidth(grid.getDisplay().getGridWidth());
		setHeight(grid.getDisplay().getGridHeight());
	}
	
	public void update(int id, int type, float value) {
		super.update(id, type, value);
		if(type == Corpus.CHANGE_WIDTH_TYPE) {
			if(grid != null)
				setWidth(value);
		} else if(type == Corpus.CHANGE_HEIGHT_TYPE) {
			if(grid != null)
				setHeight(value);
		}
	}
	
	public int getInsetx1() {
		return insetx1;
	}

	public void setInsetx1(int insetx1) {
		this.insetx1 = insetx1;
	}

	public int getInsetx2() {
		return insetx2;
	}

	public void setInsetx2(int insetx2) {
		this.insetx2 = insetx2;
	}

	public int getInsety1() {
		return insety1;
	}

	public void setInsety1(int insety1) {
		this.insety1 = insety1;
	}

	public int getInsety2() {
		return insety2;
	}

	public void setInsety2(int insety2) {
		this.insety2 = insety2;
	}
	
	public float getInteriorX() {
		return getX() + getInsetx1();
	}
	
	public float getInteriorY() {
		return getY() + getInsety1();
	}
	
	public float getInteriorWidth() {
		return getWidth() - (getInsetx1() + getInsetx2());
	}
	
	public float getInteriorHeight() {
		return getHeight() - (getInsety1() + getInsety2());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getUnderGraphicColor() {
		return underGraphicColor;
	}

	public void setUnderGraphicColor(Color underGraphicColor) {
		this.underGraphicColor = underGraphicColor;
	}

	public boolean isStretchUnderGraphic() {
		return stretchUnderGraphic;
	}

	public void setStretchUnderGraphic(boolean stretchUnderGraphic) {
		this.stretchUnderGraphic = stretchUnderGraphic;
	}
}
