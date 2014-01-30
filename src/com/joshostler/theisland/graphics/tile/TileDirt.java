package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileDirt extends Tile {

	public TileDirt(int x, int y) {
		super("dirt.png", x, y);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
