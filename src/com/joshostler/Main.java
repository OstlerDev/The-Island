package com.joshostler;

import com.joshostler.theisland.TheIsland;
import java.lang.Exception;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args.length > 0)
				throw new Exception();
			TheIsland theisland = new TheIsland();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(1);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(2);
		}
	}
}
