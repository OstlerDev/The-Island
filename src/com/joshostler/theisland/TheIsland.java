package com.joshostler.theisland;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class TheIsland implements Runnable {
	
	public static String title = "The Island";
	
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	/*
	 * 0 = Menu
	 * 1 = Game
	 */
	private int screenType = 0;
	
	public TheIsland(){
		try {
			startGame();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void startGame() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(width*scale,height*scale));
		Display.create();
		Display.setTitle("The Island");
		
		//create renderer
		
		//load map with renderer
		
		run();
	}
	
	public void run() {
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
			//run renderers render method
			//this.render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				Display.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public synchronized void stop() {
		Display.destroy();
	}

	
	private void update() {
		//maybe create controller class controlled here
	}
}
