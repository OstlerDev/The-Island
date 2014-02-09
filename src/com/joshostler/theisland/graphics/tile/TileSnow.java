package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileSnow extends Tile {

	public TileSnow(int x, int y) {
		super("res/tiles/snow.png", x, y);
		tileType = Tiles.SNOW;
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
