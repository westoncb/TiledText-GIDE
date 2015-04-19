package com.cyntaks.GDXGIDE;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cyntaks.GDXGIDE.input.DefaultInputTranslator;
import com.cyntaks.sgf.event.EventManager;

public class GIDEApp implements ApplicationListener{
	public static final boolean DEBUG = false;
	private long lastTime;
	
	private static boolean showFPS = false;
	private static Stack<Program> programs = new Stack<Program>();
	private static Stack<Program> programDrawList = new Stack<Program>();
	public static SpriteBatch SPRITE_BATCH;
	public static OrthographicCamera camera;
	
	private static float camX;
	private static float camY;

	public static float BACKGROUND_R = .1f;
	public static float BACKGROUND_G = .1f;
	public static float BACKGROUND_B = .1f;
	
	private static float clearR = BACKGROUND_R;
	private static float clearG = BACKGROUND_G;
	private static float clearB = BACKGROUND_B;
	
	private static Program initialProgram;
	private static Program loadingProgram;
	private static boolean loading;
	
	private static Queue<Program> executeProgramQueue = new LinkedList<Program>();
	private static Queue<Program> disposeProgramQueue = new LinkedList<Program>();
	private static Queue<Program> lowerProgramQueue = new LinkedList<Program>();
	private static Queue<Program> makeProgramTopQueue = new LinkedList<Program>();
	
	public void create() {
		lastTime = System.nanoTime();

		loadSystemResources();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camX = camera.position.x;
		camY = camera.position.y;

		//If we wanted to use multi-touch input, we'd need to supply a different translator here
		InputAbstractor.initialize(new DefaultInputTranslator());
		
		Gdx.input.setCursorCatched(true);
		
		initOpenGL();
		
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		SPRITE_BATCH = new SpriteBatch();
		
		executeProgram(initialProgram);
	}
	
	private static void loadSystemResources() {
		ResourceManager.loadTypeFace("droid", "droid");
		ResourceManager.queueSound("sounds/subtle-tech_rollover_01.wav", "rollover");
		ResourceManager.queueSound("sounds/subtle-tech_interface_50.wav", "abstract");
		ResourceManager.queueSound("sounds/subtle-tech_interface_02.wav", "insert");
		ResourceManager.queueTexture("images/newtest.png", "images/newtest.png", TextureFilter.Nearest);
		loading = true;
	}
	
	private void initOpenGL() {
		GL20 gl = Gdx.gl20;
		
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S,
				GL20.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T,
				GL20.GL_CLAMP_TO_EDGE);
	
		/*gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER,
				gl.GL_LINEAR);
		gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER,
				gl.GL_LINEAR);*/
	}
	
	public static void disposeAllPrograms() {
		for (Program program : programs) {
			disposeProgram(program);
		}
	}
	
	public static void shutDown() {
		Gdx.app.exit();
	}

	public void dispose() {
		shutDown();
	}

	public void pause() {
		
	}

	public void render() {
		long elapsedTime = System.nanoTime() - lastTime;
		lastTime = System.nanoTime();
		float elapsedTimeSecs = elapsedTime/1000000000f;
		float targetElapsedTime = (1000000000f/60)/1000000000f; //can't use 60 directly, as here
		float scale = elapsedTimeSecs/targetElapsedTime;

		update(scale);
		
		//use scale here for consistent timing across machines, targetElapsedTime for smoothness on one machine.
		EventManager.getInstance().updateOccurred(targetElapsedTime);

		if(!changesQueued())
			draw();
	}
	
	private boolean changesQueued() {
		if(!executeProgramQueue.isEmpty())
			return true;
		if(!disposeProgramQueue.isEmpty())
			return true;
		if(!lowerProgramQueue.isEmpty())
			return true;
		if(!makeProgramTopQueue.isEmpty())
			return true;
		
		else return false;
	}
	
	private void update(float delta) {
		InputAbstractor.update(delta);
		
		carryOutQueuedChanges();
		
		for (Program prog : programs) {
			prog.auxUpdate(delta);
		}
	}
	
	private void carryOutQueuedChanges() {
		//Add programs
		Iterator<Program> iter = executeProgramQueue.iterator();
		while(iter.hasNext())
			programs.push(iter.next());
		executeProgramQueue.clear();
		
		//Dispose programs
		iter = disposeProgramQueue.iterator();
		while(iter.hasNext())
			programs.remove(iter.next());
		disposeProgramQueue.clear();
		
		//Lower programs
		iter = lowerProgramQueue.iterator();
		while(iter.hasNext()) {
			Program next = iter.next();
			if(programs.peek() == next) {
				programs.remove(next);
				int index = programs.size()-2;
				if(index > -1)
					programs.insertElementAt(next, index);
				else
					programs.push(next);
			}
		}
		lowerProgramQueue.clear();
		
		//Make programs top
		iter = makeProgramTopQueue.iterator();
		while(iter.hasNext()) {
			Program next = iter.next();
			programs.remove(next);
			programs.push(next);
		}
		makeProgramTopQueue.clear();
	}

	public void resize(int arg0, int arg1) {
		
	}

	public void resume() {
		
	}
	
	public static void setCameraPosition(float x, float y) {
		camX = x;
		camY = y;
	}

	public void draw() {
		GL20 gl = Gdx.graphics.getGL20();	
		gl.glClearColor(BACKGROUND_R, BACKGROUND_G, BACKGROUND_B, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        prepareCameraForDraw();
        
        SPRITE_BATCH.begin();

        normalDraw();
        
        if(!ResourceManager.areResourcesQueued()) {
			if(loading)
				loadComplete(loadingProgram);
		} else {
			if(!loadingProgram.isNoLoadScreen()) {
				loadingProgram.getLoadScreen().draw(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				ResourceManager.loadNextQueuedResource();
			} else {
				ResourceManager.loadQueuedResources();
				loadComplete(loadingProgram);
			}
		}

		SPRITE_BATCH.end();
	}
	
	private static void normalDraw() {
		programDrawList.clear();
		programDrawList.setSize(programs.size());
		Collections.copy(programDrawList, programs);
		Collections.sort(programDrawList); //sort by z-order of their root windows
		
		for (Program prog : programDrawList) {
			if(prog.isInitialized()) {
				prog.drawBackground();
				GIDEApp.SPRITE_BATCH.flush();
				prog.auxDraw();
				prog.drawOverlay();
			}
		}
		
		if(showFPS) {
			//fontCache.setColor(Color.BLACK);
			//fontCache.setText("FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10);
			//fontCache.draw(SPRITE_BATCH);
		}
		Gdx.graphics.setTitle("GIDE - Gestural IDE | FPS: " + Gdx.graphics.getFramesPerSecond());
	}
	
	private static void prepareCameraForDraw() {
		//this may now be broken since camera isn't actually being used atm and I've cut a lot out of
		//this class. In particular this partly used to work through camResetX/Y, and now it will 
		//probably translate out of control.
		camera.position.x = camX;
        camera.position.y = camY;
        camera.update();
        SPRITE_BATCH.setProjectionMatrix(camera.projection);
		SPRITE_BATCH.setTransformMatrix(camera.view);
	}
	
	public static void executeProgram(Program program) {
		if(!executeProgramQueue.contains(program)) {
			executeProgramQueue.add(program);
			
			loading = true;
			loadingProgram = program;
			program.getLoadScreen().load();
			program.getLoadScreen().init();
			program.auxLoad();
		}
	}
	
	public static void executeHoverProgram(Program program) {
		executeProgram(program);
		lowerProgramFromTop(program);
	}
	
	public static void loadComplete(Program program) {
		loading = false;
		program.auxInit();
		program.getRootWindow().getInTransition().begin();
	}
	
	public static void makeProgramTop(Program newTop) {
		if(programs.contains(newTop)) {
			makeProgramTopQueue.add(newTop);
		} else { //move it up in the execute-queue instead
			Stack<Program> reAdd = new Stack<Program>();
			int size = executeProgramQueue.size();
			while (size >= 0) {
				Program program = executeProgramQueue.remove();
				if(program != newTop)
					reAdd.push(program);
				size--;
			}
			executeProgramQueue.add(newTop);
			size = reAdd.size();
			while(size >= 0) {
				executeProgramQueue.add(reAdd.pop());
				size--;
			}
		}
			
	}
	
	public static void lowerProgramFromTop(Program program) {
		lowerProgramQueue.add(program);
	}
	
	public static void disposeProgram(Program program) {
		if(!disposeProgramQueue.contains(program)) {
			program.auxDispose();
			disposeProgramQueue.add(program);
		}
	}
	
	public static void setBackgroundColor(float red, float green, float blue) {
		clearR = red;
		clearG = green;
		clearB = blue;
	}
	
	public static float getBgRed() {
		return clearR;
	}
	
	public static float getBgGreen() {
		return clearG;
	}
	
	public static float getBgBlue() {
		return clearB;
	}
	
	public static float getCamX() {
		return camX;
	}
	
	public static float getCamY() {
		return camY;
	}
	
	public static Stack<Program> getPrograms() {
		return programs;
	}
	
	public static void setInitialProgram(Program program) {
		initialProgram = program;
	}
}
