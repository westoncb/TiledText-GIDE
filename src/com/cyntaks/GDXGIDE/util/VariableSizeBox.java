package com.cyntaks.GDXGIDE.util;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.cyntaks.GDXGIDE.Drawable;
import com.cyntaks.GDXGIDE.GIDEApp;
import com.cyntaks.GDXGIDE.ResourceManager;
import com.cyntaks.GDXGIDE.ResourceUser;

public class VariableSizeBox implements Drawable, ResourceUser{
	private TextureRegion corner;
	private TextureRegion hEdge;
	private TextureRegion vEdge;
	private TextureRegion center;
	private float x, y;
	private float width, height;
	private String imgPath;
	private int[][] regions; //used for marking off parts of an image as representing the corder, center, etc.
	private boolean initialized;
	
	public VariableSizeBox(TextureRegion corner, TextureRegion hEdge, TextureRegion vEdge, TextureRegion center) {
		this.corner = corner;
		this.hEdge = hEdge;
		this.vEdge = vEdge;
		this.center = center;
	}
	
	public VariableSizeBox(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public static boolean datFileExistsForImage(String imgPath) {
		return Gdx.files.internal(imgPath + ".dat").exists();
	}
	
	public void load() {
		if(imgPath != null) {
			ResourceManager.queueTexture(imgPath, imgPath, TextureFilter.Nearest);
			
			regions = new int[4][4];
			
			try {
				Scanner scanner = new Scanner(Gdx.files.internal(imgPath + ".dat").read());
				ArrayList<String> lines = new ArrayList<String>();
				while(scanner.hasNextLine()) {
					lines.add(scanner.nextLine());
				}
				scanner.close();
				
				int index = 0;
				for(String line : lines) {
					String[] strValues = line.split(", ");
					int[] lineValues = new int[4];
					int index2 = 0;
					for(String value : strValues) {
						lineValues[index2++] = Integer.parseInt(value);
					}
					
					regions[index++] = lineValues;
				}
			} catch(Exception ex) {
				System.err.println("An image (named [imgname]) used for a VariableSizeBox must use an accompanying file [imgname].dat, including the extension, to specify the various parts of the image. ");
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void init() {
		
		GL20 gl = Gdx.graphics.getGL20();
		
		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S,
				GL20.GL_REPEAT);
		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T,
				GL20.GL_REPEAT);
		ResourceManager.loadTexture(imgPath, imgPath, TextureFilter.Nearest); //in case the load queue hasn't been processed yet
		Texture tex = ResourceManager.getTexture(imgPath);
		TextureRegion center = new TextureRegion(regions[0][0], regions[0][1], regions[0][2], regions[0][3], tex);
		TextureRegion vEdge = new TextureRegion(regions[1][0], regions[1][1], regions[1][2], regions[1][3], tex);
		TextureRegion hEdge = new TextureRegion(regions[2][0], regions[2][1], regions[2][2], regions[2][3], tex);
		TextureRegion corner = new TextureRegion(regions[3][0], regions[3][1], regions[3][2], regions[3][3], tex);

		this.corner = corner;
		this.hEdge = hEdge;
		this.vEdge = vEdge;
		this.center = center;
		
		initialized = true;
	}
	
	public void draw(float x, float y, float width, float height) {
		if(initialized) {
			drawSquareCorners(x, y, width, height);
			drawSquareCenter(x, y, width, height);
			drawSquareEdges(x, y, width, height);
		}
	}
	
	public void draw() {
		draw(x, y, width, height);
	}

	private void drawSquareCenter(float x, float y, float width, float height) {
		GIDEApp.SPRITE_BATCH.draw(center.tex, x + hEdge.width, y + vEdge.height,
				(width - hEdge.width * 2),
				(height - vEdge.height * 2), center.x, center.y, center.width, center.height, false, false);
	}

	private void drawSquareCorners(float x, float y, float width, float height) {
		//lower left
		GIDEApp.SPRITE_BATCH.draw(corner.tex, x, y, corner.width, corner.height, corner.x, corner.y, corner.width, corner.height, true, true);
		//upper left
		GIDEApp.SPRITE_BATCH.draw(corner.tex, x, y + (height - corner.height), corner.width, corner.height, corner.x, corner.y, corner.width, corner.height, true, false);
		//upper right
		GIDEApp.SPRITE_BATCH.draw(corner.tex, x + (width - corner.width),
				y + (height - corner.height), corner.width, corner.height, corner.x, corner.y, corner.width, corner.height, false, false);
		//lower right
		GIDEApp.SPRITE_BATCH.draw(corner.tex, x + (width - corner.width), y, corner.width, corner.height, corner.x, corner.y, corner.width, corner.height, false, true);
	}

	private void drawSquareEdges(float x, float y, float width, float height) {
		//bottom
		GIDEApp.SPRITE_BATCH.draw(vEdge.tex, x + corner.width, y, (width - corner.width * 2),
				vEdge.height, vEdge.x, vEdge.y, vEdge.width, vEdge.height, false, true);
		//top
		GIDEApp.SPRITE_BATCH.draw(vEdge.tex, x + corner.width, y + (height - vEdge.height),
				(width - corner.width * 2), vEdge.height, vEdge.x, vEdge.y, vEdge.width, vEdge.height, false, false);
		//left
		GIDEApp.SPRITE_BATCH.draw(hEdge.tex, x, y + corner.height, hEdge.width,
				(height - corner.height * 2), hEdge.x, hEdge.y, hEdge.width, hEdge.height, true, false);
		
		//right
		GIDEApp.SPRITE_BATCH.draw(hEdge.tex, x + (width - hEdge.width), y + corner.height,
				hEdge.width, (height - corner.height * 2), hEdge.x, hEdge.y, hEdge.width, hEdge.height, false, false);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
