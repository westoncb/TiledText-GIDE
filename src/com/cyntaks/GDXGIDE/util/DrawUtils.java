package com.cyntaks.GDXGIDE.util;

import com.badlogic.gdx.graphics.Texture;
import com.cyntaks.GDXGIDE.GIDEApp;

public class DrawUtils {
	public static void drawSquareCenter(Texture tex, float x, float y, float width, float height, float texSize) {
		GIDEApp.SPRITE_BATCH.draw(tex, x + texSize, y + texSize,
				(width - texSize * 2),
				(height - texSize * 2), 0, 6, 2, 8, false, false);
	}

	public static void drawSquareCorners(Texture tex, float x, float y, float width, float height, int texSize) {
		//lower left
		GIDEApp.SPRITE_BATCH.draw(tex, x, y, texSize, texSize, 8, 0, texSize, texSize, true, true);
		//upper left
		GIDEApp.SPRITE_BATCH.draw(tex, x, y + (height - texSize), texSize, texSize, 8, 0, texSize, texSize, true, false);
		//upper right
		GIDEApp.SPRITE_BATCH.draw(tex, x + (width - texSize),
				y + (height - texSize), texSize, texSize, 8, 0, texSize, texSize, false, false);
		//lower right
		GIDEApp.SPRITE_BATCH.draw(tex, x + (width - texSize), y, texSize, texSize, 8, 0, texSize, texSize, false, true);
	}

	public static void drawSquareEdges(Texture tex, float x, float y, float width, float height, int texSize) {
		//bottom
		GIDEApp.SPRITE_BATCH.draw(tex, x + texSize, y, (width - texSize * 2),
				texSize, 0, 0, 2, 8, false, true);
		//top
		GIDEApp.SPRITE_BATCH.draw(tex, x + texSize, y + (height - texSize),
				(width - texSize * 2), texSize, 0, 0, 2, 8, false, false);
		//left
		GIDEApp.SPRITE_BATCH.draw(tex, x, y + texSize, texSize,
				(height - texSize * 2), 0, 6, 8, 8, true, false);
		
		//right
		GIDEApp.SPRITE_BATCH.draw(tex, x + (width - texSize), y + texSize,
				texSize, (height - texSize * 2), 0, 6, 8, 8, false, false);
	}
}
