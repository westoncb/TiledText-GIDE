package com.cyntaks.GDXGIDE;

public interface Drawable extends ResourceUser{
	
	public void draw(float x, float y, float width, float height);
	public void draw();
	public float getX();
	public void setX(float x);
	public float getY();
	public void setY(float y);
	public float getWidth();
	public void setWidth(float width);
	public float getHeight();
	public void setHeight(float height);
}
