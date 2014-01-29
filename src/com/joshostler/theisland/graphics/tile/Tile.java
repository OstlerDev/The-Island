/**
 * 
 */
package com.joshostler.theisland.graphics.tile;

import java.awt.Rectangle;

import com.joshostler.entity.Entity;
import com.joshostler.theisland.TheIsland;
import com.joshostler.theisland.graphics.ResourceHandler;
import com.joshostler.theisland.graphics.Sprite;

public abstract class Tile {
	
	protected double x;
	protected double y;
	
	protected Sprite sprite;
	
	private Rectangle tile = new Rectangle();
	private Rectangle player = new Rectangle();
	
	public Tile(String ref, int x, int y) {
		this.sprite = new Sprite(ResourceHandler.get().getGameWindow(), ref);
		this.x = x;
		this.y = y;
	}
	
	public void draw(){
		sprite.draw((int)x, (int)y);
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public boolean collidesWith(Tile other){
		tile.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		player.setBounds((int) other.x,(int) other.y, other.sprite.getWidth(), other.sprite.getHeight());
		
		return tile.intersects(player);
	}
	
	public abstract void collidedWith(Entity other);
}
