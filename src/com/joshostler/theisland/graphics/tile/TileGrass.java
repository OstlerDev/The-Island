package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileGrass extends Tile {

	public TileGrass(int x, int y) {
		super("res/tiles/grass.png", x, y);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
