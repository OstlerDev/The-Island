package com.joshostler.theisland.graphics.tile;

import com.joshostler.entity.Entity;

public class TileStone extends Tile {

	public TileStone(int x, int y) {
		super("stone.png", x, y);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
