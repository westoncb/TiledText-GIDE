package com.cyntaks.GDXGIDE;


public abstract class AbstractWindowTransition implements ResourceUser {
	private Window owner;
	private boolean inTransition;
	private boolean active;
	
	public AbstractWindowTransition() {
		
	}
	
	public void load() {
		
	}
	
	public void init() {
		
	}
	
	public final void begin() {
		if(!active) {
			active = true;
			customBegin();
		}
	}
	
	public abstract void customBegin();

	public final void finish() {
		active = false;
		if(inTransition)
			owner.transitionInFinished();
		else
			owner.transitionOutFinished();
	}
	
	public void update(float delta) {
		
	}
	
	public void draw() {
		
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isInTransition() {
		return inTransition;
	}
	
	public void setIsInTransition(boolean inTransition) {
		this.inTransition = inTransition;
	}
	
	public Window getOwner() {
		return owner;
	}
	
	public void setOwner(Window window) {
		this.owner = window;
	}
}