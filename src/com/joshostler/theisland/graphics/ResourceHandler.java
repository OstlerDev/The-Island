package com.joshostler.theisland.graphics;

public class ResourceHandler {
	
	private static final ResourceHandler one = new ResourceHandler();
	
	public static ResourceHandler get(){
		return one;
	}
	
	private GameWindow window;
	
	private ResourceHandler(){}
	
	public GameWindow getGameWindow(){
		if (window == null){
			window = new GameWindow();
		}
		return window;
	}
	
	public Sprite getSprite(String ref){
		if (window == null){
			throw new RuntimeException("Attempting to retrieve sprite before game window was created");
		}
		return new Sprite (window, ref);
	}
}
