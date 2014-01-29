package com.joshostler;

import com.joshostler.theisland.TheIsland;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		TheIsland game = new TheIsland();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
