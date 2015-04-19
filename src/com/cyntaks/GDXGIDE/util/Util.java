package com.cyntaks.GDXGIDE.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.cyntaks.GDXGIDE.GIDEApp;

public class Util {
	private static final Vector3 scratch = new Vector3();
	
	public static boolean isRegionVisible(Rectangle rect) {
		Frustum frustrum = GIDEApp.camera.frustum;
		scratch.set(rect.x, rect.y, 0);
		if(frustrum.pointInFrustum(scratch))
			return true;
		scratch.set(rect.x + rect.width, rect.y, 0);
		if(frustrum.pointInFrustum(scratch))
			return true;
		scratch.set(rect.x + rect.width, rect.y + rect.height, 0);
		if(frustrum.pointInFrustum(scratch))
			return true;
		scratch.set(rect.x, rect.y + rect.height, 0);
		if(frustrum.pointInFrustum(scratch))
			return true;
		
		return false;
	}
	
	public static float getWorldY(float screenY) {
		scratch.set(scratch.x, screenY, 0);
		GIDEApp.camera.unproject(scratch);
		return scratch.y;
	}
	
	public static float getWorldX(float screenX) {
		scratch.set(screenX, scratch.y, 0);
		GIDEApp.camera.unproject(scratch);
		return scratch.x;
	}
	
	public static int getScreenY(float worldY) {
		scratch.set(scratch.x, worldY, 0);
		GIDEApp.camera.project(scratch);
		return (int)scratch.y;
	}
	
	public static int getScreenX(float worldX) {
		scratch.set(worldX, scratch.y, 0);
		GIDEApp.camera.project(scratch);
		return (int)scratch.x;
	}
	
	public static float getMouseYInWorldCoords() {
		scratch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		GIDEApp.camera.unproject(scratch);
		return scratch.y;
	}
	
	public static float getMouseXInWorldCoords() {
		scratch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		GIDEApp.camera.unproject(scratch);
		return scratch.x;
	}
	
	public static boolean intersect(Rectangle first, Rectangle second) {
		return first.contains(second.x, second.y) || first.contains(second.x + second.width, second.y) ||
			   first.contains(second.x + second.width, second.y + second.height) || first.contains(second.x, second.y + second.height) ||
			   second.contains(first.x, first.y) || second.contains(first.x + first.width, first.y) ||
			   second.contains(first.x + first.width, first.y + first.height) || second.contains(first.x, first.y + first.height);
	}
 }
