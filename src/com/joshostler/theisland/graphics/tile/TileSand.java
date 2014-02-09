package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileSand extends Tile {

	public TileSand(int x, int y) {
		super("res/tiles/sand.png", x, y);
		tileType = Tiles.SAND;
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
