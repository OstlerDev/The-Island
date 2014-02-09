package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileDirt extends Tile {

	public TileDirt(int x, int y) {
		super("res/tiles/dirt.png", x, y);
		tileType = Tiles.DIRT;
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
