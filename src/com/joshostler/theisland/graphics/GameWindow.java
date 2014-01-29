package com.joshostler.theisland.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameWindow {
	
	private GameWindowCallback callback;
  
	private boolean gameRunning = true;
  
	private int width;
  
	private int height;

	private TextureLoader textureLoader;
  
	private String title;
	
	public GameWindow() {
	}
	
	TextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	public void setTitle(String title) {
	    this.title = title;
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
	
	public void startRendering() {
		try {                
			setDisplayMode();
			Display.create();
			
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
		} catch (LWJGLException le) {
			callback.windowClosed();
		}
    
		gameLoop();
	}

	public void setGameWindowCallback(GameWindowCallback callback) {
		this.callback = callback;
	}
  
	private void gameLoop() {
		while (gameRunning) {
			// clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			// let subsystem paint
			if (callback != null) {
				callback.frameRendering();
			}
			
			// update window contents
			Display.update();
			
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