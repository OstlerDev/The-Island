package com.joshostler.theisland.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.joshostler.maps.Map;

public class GameWindow {
	
	private GameWindowCallback callback;
  
	private boolean gameRunning = true;
 
	private int width;
	private int height;
	public int offsetX = 200;
	public int offsetY = 200;

	private TextureLoader textureLoader;
  
	private String title = "The Island";
	
	public Map gameMap;
	
	public GameWindow() {
		try {
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	TextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	public void setTitle(String title) {
	    //this.title = title;
	    if(Display.isCreated()) {
	    	Display.setTitle(title);
	    }
	}

	public void setResolution(int x, int y) {
		width = x;
		height = y;
	}
	
	private boolean setDisplayMode() {
		try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(
					width, height, -1, -1, -1, -1, 60, 60);

			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width=" + width,
					"height=" + height,
					"freq=" + 60,
					"bpp="
							+ org.lwjgl.opengl.Display.getDisplayMode()
									.getBitsPerPixel() });

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Unable to enter fullscreen, continuing in windowed mode");
		}

		return false;
	}
	
	public void startRendering() throws LWJGLException {
		setDisplayMode();
		
		// grab the mouse, dont want that hideous cursor when we're playing!
		Mouse.setGrabbed(true);
  
		// enable textures since we're going to use these for our sprites
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// disable the OpenGL depth test since we're rendering 2D graphics
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GL11.glOrtho(0, width, height, 0, -1, 1);
		
		textureLoader = new TextureLoader();
		
		if(callback != null) {
			callback.initialise();
		}
		    
		gameLoop();
	}

	public void setGameWindowCallback(GameWindowCallback callback) {
		this.callback = callback;
	}
  
	private void gameLoop() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		gameMap = new Map();
		
		while (gameRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				
				// clear screen
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
					offsetX-=4;
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
					offsetX+=4;
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))
					offsetY-=4;
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
					offsetY+=4;
				GL11.glTranslatef(-offsetX, -offsetY, 0);
				gameMap.render();
				
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				
				// let subsystem paint
				if (callback != null) {
					callback.frameRendering();
				}
				
				// update window contents
				Display.update();
				
				updates++;
				delta--;
			}
			
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			
			
			if(Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				gameRunning = false;
				Display.destroy();
				callback.windowClosed();
			}
		}
	}

	public void destroy() {
		Display.destroy();
	}  
}