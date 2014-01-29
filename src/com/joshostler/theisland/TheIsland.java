package com.joshostler.theisland;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import com.joshostler.theisland.graphics.GameWindow;
import com.joshostler.theisland.graphics.GameWindowCallback;
import com.joshostler.theisland.graphics.ResourceHandler;
import com.joshostler.theisland.graphics.TextureLoader;
import com.joshostler.theisland.graphics.tile.Tile;
import com.joshostler.theisland.graphics.tile.TileStone;

public class TheIsland implements Runnable, GameWindowCallback {
	
	private TheIsland game = this;
	
	private static String title = "The Island";
	
	private GameWindowCallback callback;
	private GameWindow window;
	
	private static int width = 300;
	private static int height = width / 16 * 9;
	private static int scale = 3;
	
	private boolean running = false;
	
	private TextureLoader textureLoader;
	private ArrayList entities = new ArrayList();
	private ArrayList tiles = new ArrayList();
	
	TileStone tile;
	
	/*
	 * 0 = Menu
	 * 1 = Game
	 */
	private int screenType = 1;
	
	public TheIsland(){
		try {
			startGame();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void startGame() throws LWJGLException {
		
		window = ResourceHandler.get().getGameWindow();
		window.setResolution(width*scale, height*scale);
		window.setGameWindowCallback(this);
		window.setTitle("Does this even work?");
		
		window.startRendering();
	
		run();
	}
	
	public void run() {
		running = true;
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				this.update();
				updates++;
				delta--;
			}
			
			this.render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				window.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	private void render() {
		
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		// let subsystem paint
		if (callback != null) {
			callback.frameRendering();
		}
		
		tile = new TileStone(0,0);
		tiles.add(tile);
		
		window.setTitle("Swag");
		
		for(int i = 0; i < tiles.size(); i++){
			Tile t = (Tile) tiles.get(i);
			t.draw();
		}
		// update window contents
		Display.update();
	}

	public synchronized void stop() {
		try {
		ResourceHandler.get().getGameWindow().destroy();
		callback.windowClosed();
		} catch (IllegalStateException e) {
			System.out.println(e);
		}
		System.exit(1);
	}

	
	private void update() {
		//maybe create controller class controlled here
		Display.update();
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			stop();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_B)){
			System.out.println("Key B has been pressed");
		}
	}

	@Override
	public void initialise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameRendering() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed() {
		// TODO Auto-generated method stub
		
	}
}
