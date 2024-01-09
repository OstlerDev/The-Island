package com.joshostler;

import com.joshostler.theisland.TheIsland;

public class Main {

	/**
	 * @param args list of string arguments, not used during main invocation on this program
	 */
	public static void main(String[] args) {
		try {
		TheIsland game = new TheIsland();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(1);//Null pointer exception specific exit
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(2); //All other exceptions
		}
		//exit naturally with process
	}
}
