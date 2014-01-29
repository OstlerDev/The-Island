package com.joshostler.theisland.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.joshostler.theisland.TheIsland;

public class Sprite {
	
	private Image image;
	private Texture texture;
	
	private int width;
	private int height;
	
	 public Sprite (GameWindow gameWindow, String ref){
		 try {
			 texture = gameWindow.getTextureLoader().getTexture("stone.png");
			 
			 width = texture.getImageWidth();
			 height = texture.getImageHeight();
		 } catch (IOException e){
			 System.err.println("Unable to Load Texture: " + ref);
			 System.exit(0);
		 }
	 }

	public int getWidth(){
		 return texture.getImageWidth();
	 }
	 public int getHeight(){
		 return texture.getImageHeight();
	 }
	 
	 public void draw(int x, int y){
		// store the current model matrix
		 System.out.println("Drawing!");
			GL11.glPushMatrix();
			
			// bind to the appropriate texture for this sprite
			System.out.println("Binding");
			texture.bind();
			System.out.println("Done Binding!");
			
			// translate to the right location and prepare to draw
			GL11.glTranslatef(x, y, 0);		
	    	GL11.glColor3f(1,1,1);
			
			// draw a quad textured to match the sprite
	    	GL11.glBegin(GL11.GL_QUADS);
			{
		      GL11.glTexCoord2f(0, 0);
		      GL11.glVertex2f(0, 0);
		      GL11.glTexCoord2f(0, texture.getHeight());
		      GL11.glVertex2f(0, height);
		      GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
		      GL11.glVertex2f(width,height);
		      GL11.glTexCoord2f(texture.getWidth(), 0);
		      GL11.glVertex2f(width,0);
			}
			GL11.glEnd();
			
			// restore the model view matrix to prevent contamination
			GL11.glPopMatrix();
	 }
}
