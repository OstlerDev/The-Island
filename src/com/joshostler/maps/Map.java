package com.joshostler.maps;

import java.util.Random;

import com.joshostler.theisland.graphics.ResourceHandler;
import com.joshostler.theisland.graphics.tile.Tile;
import com.joshostler.theisland.graphics.tile.TileDirt;
import com.joshostler.theisland.graphics.tile.TileStone;

public class Map {
	
	float[][] mapFloats;
	Tile[][] mapTiles;
	int width;
	int height;

	public Map(){
		width = 500;
		height = 500;
		
		mapTiles = new Tile[width][height];
		
		float[][] baseNoise = generateWhiteNoise(width, height);
		mapFloats = GeneratePerlinNoise(baseNoise, 4);
		
		for(int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				System.out.println("Current Height: " + j + "; Current Width: " + i + "; Float at that location: " + mapFloats[i][j]);
				System.out.println("Assigning Tile a value!");
				if (mapFloats[i][j] < 0.5){
					mapTiles[i][j] = new TileDirt(i*16,j*16);
				} else {
					mapTiles[i][j] = new TileStone(i*16,j*16);
				}
			}
		}
	}
	
	public float[][] generateWhiteNoise(int width, int height)
	{
	    Random random = new Random(); //Seed to 0 for testing
	    float[][] noise = new float[width][height];
	 
	    for (int i = 0; i < width; i++)
	    {
	        for (int j = 0; j < height; j++)
	        {
	            noise[i][j] = (float)random.nextDouble() % 1;
	        }
	    }
	 
	    return noise;
	}
	
	float Interpolate(float x0, float x1, float alpha)
	{
	   return x0 * (1 - alpha) + alpha * x1;
	}
	
	float[][] GenerateSmoothNoise(float[][] baseNoise, int octave)
	{
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][] smoothNoise = new float[width][height];
	 
	   int samplePeriod = 1 << octave; // calculates 2 ^ k
	   float sampleFrequency = 1.0f / samplePeriod;
	 
	   for (int i = 0; i < width; i++)
	   {
	      //calculate the horizontal sampling indices
	      int sample_i0 = (i / samplePeriod) * samplePeriod;
	      int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
	      float horizontal_blend = (i - sample_i0) * sampleFrequency;
	 
	      for (int j = 0; j < height; j++)
	      {
	         //calculate the vertical sampling indices
	         int sample_j0 = (j / samplePeriod) * samplePeriod;
	         int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
	         float vertical_blend = (j - sample_j0) * sampleFrequency;
	 
	         //blend the top two corners
	         float top = Interpolate(baseNoise[sample_i0][sample_j0],
	            baseNoise[sample_i1][sample_j0], horizontal_blend);
	 
	         //blend the bottom two corners
	         float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
	            baseNoise[sample_i1][sample_j1], horizontal_blend);
	 
	         //final blend
	         smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
	      }
	   }
	 
	   return smoothNoise;
	}
	
	float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount)
	{
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing
	 
	   float persistance = 0.5f;
	 
	   //generate smooth noise
	   for (int i = 0; i < octaveCount; i++)
	   {
	       smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
	   }
	 
	    float[][] perlinNoise = new float[width][height];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for (int octave = octaveCount - 1; octave >= 0; octave--)
	    {
	       amplitude *= persistance;
	       totalAmplitude += amplitude;
	 
	       for (int i = 0; i < width; i++)
	       {
	          for (int j = 0; j < height; j++)
	          {
	             perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //normalisation
	   for (int i = 0; i < width; i++)
	   {
	      for (int j = 0; j < height; j++)
	      {
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	public Tile[][] getMapTiles(){
		return mapTiles;
	}
	
	public void render(){
		for(int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				if (mapTiles[i][j] != null){
					int offsetX = ResourceHandler.get().getGameWindow().offsetX;
					int x = mapTiles[i][j].getX();
					int offsetY = ResourceHandler.get().getGameWindow().offsetY;
					int y = mapTiles[i][j].getY();
					if (x+17 > offsetX && x-1 < 1200+offsetX)
						if (y+17 > offsetY && y-1 < 700+offsetY)
							mapTiles[i][j].draw();
				}
			}
		}
	}
}
