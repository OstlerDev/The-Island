package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileWater extends Tile {

	public TileWater(int x, int y) {
		super("res/tiles/water.png", x, y);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
