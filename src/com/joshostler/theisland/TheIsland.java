package com.joshostler.theisland;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import com.joshostler.theisland.graphics.GameWindow;
import com.joshostler.theisland.graphics.GameWindowCallback;
import com.joshostler.theisland.graphics.ResourceHandler;
import com.joshostler.theisland.graphics.tile.TileStone;

public class TheIsland implements Runnable, GameWindowCallback {
	
	private static String title = "The Island";
	
	private GameWindowCallback callback;
	private GameWindow window;
	
	private static int width = 300;
	private static int height = width / 16 * 9;
	private static int scale = 3;
	
	TileStone tile;
	
	/*
	 * 0 = Menu
	 * 1 = Game
	 */
	private enum State {INTRO, LOADING, MAIN_MENU, GAME};
	private State state = State.GAME;
	
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
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		width = gd.getDisplayMode().getWidth();
		height = gd.getDisplayMode().getHeight();
		
		window.setResolution(width, height);
		window.setGameWindowCallback(this);
		window.setTitle(title);
		
		window.startRendering();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
