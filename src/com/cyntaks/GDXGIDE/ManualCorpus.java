package com.cyntaks.GDXGIDE;

public class ManualCorpus extends Corpus{

	private float xAccel;
	private float yAccel;
	private float xVol;
	private float yVol;
	private float xVolCap;
	private float yVolCap;
	private float xDelta;
	private float yDelta;
	private float lastX;
	private float lastY;
	private float yMax = Float.MAX_VALUE;
	private float yMin = -Float.MAX_VALUE;
	private float xMax = Float.MAX_VALUE;
	private float xMin = -Float.MAX_VALUE;
	
	public void update(float delta) {
		super.update(delta);
		
		xVol += xAccel * delta;
		yVol += yAccel * delta;

		if((xAccel > 0 && xVol >= xVolCap) || (xAccel < 0 && xVol <= xVolCap)) {
			xAccel = 0;
			xVol = xVolCap;
		}
		if((yAccel > 0 && yVol >= yVolCap) || (yAccel < 0 && yVol <= yVolCap)) {
			yAccel = 0;
			yVol = yVolCap;
		}
		
		float newX = getX() + xVol*delta;
		float newY = getY() + yVol*delta;
		
		if(newX > xMax)
			newX = xMax;
		else if(newX < xMin)
			newX = xMin;
		
		if(newY > yMax)
			newY = yMax;
		else if(newY < yMin)
			newY = yMin;
		
		if(Math.abs(newY) < 0.5)
			newY = 0;
		if(Math.abs(newX) < 0.5)
			newX = 0;
		
		setX(newX);
		setY(newY);
		
		xDelta = getX() - lastX;
		yDelta = getY() - lastY;
		
		lastX = getX();
		lastY = getY();
	}
	
	public float getXVol() {
		return xVol;
	}
	
	public void setXVol(float vol) {
		this.xVol = vol;
	}
	
	public float getYVol() {
		return yVol;
	}
	
	public void setYVol(float vol) {
		this.yVol = vol;
	}
	
	public float getXAccel() {
		return xAccel;
	}
	
	public void setXAccel(float accel) {
		this.xAccel = accel;
	}
	
	public float getYAccel() {
		return yAccel;
	}
	
	public void setYAccel(float accel) {
		this.yAccel = accel;
	}
	
	public float getXDelta() {
		return xDelta;
	}
	
	public float getYDelta() {
		return yDelta;
	}
	
	public float getLastX() {
		return lastX;
	}
	
	public float getLastY() {
		return lastY;
	}

	public float getYMax() {
		return yMax;
	}

	public void setYMax(float yMax) {
		this.yMax = yMax;
	}

	public float getYMin() {
		return yMin;
	}

	public void setYMin(float yMin) {
		this.yMin = yMin;
	}
	
	public float getXMax() {
		return xMax;
	}

	public void setXMax(float xMax) {
		this.xMax = xMax;
	}

	public float getXMin() {
		return xMin;
	}

	public void setXMin(float xMin) {
		this.xMin = xMin;
	}
}
