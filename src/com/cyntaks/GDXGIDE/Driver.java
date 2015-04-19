package com.cyntaks.GDXGIDE;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cyntaks.GDXGIDE.programs.CodeEditor;

public class Driver {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		//1024x600 or 1600x900
		config.width = 1024;
		config.height = 600;
		config.title = "GIDE - Gestural IDE";
		config.vSyncEnabled = true;
		//need a way of giving a target framerate (see GideApp->render())
		CodeEditor editor = new CodeEditor();
		GIDEApp.setInitialProgram(editor);
		new LwjglApplication(new GIDEApp(), config);
	}
}
