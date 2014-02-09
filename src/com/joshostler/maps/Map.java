package com.joshostler.maps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.joshostler.theisland.graphics.ResourceHandler;
import com.joshostler.theisland.graphics.tile.Tile;
import com.joshostler.theisland.graphics.tile.Tile.Tiles;
import com.joshostler.theisland.graphics.tile.TileDirt;
import com.joshostler.theisland.graphics.tile.TileGrass;
import com.joshostler.theisland.graphics.tile.TileSand;
import com.joshostler.theisland.graphics.tile.TileSnow;
import com.joshostler.theisland.graphics.tile.TileStone;
import com.joshostler.theisland.graphics.tile.TileWater;

public class Map {

	float[][] mapFloats;
	Tile[][] mapTiles;
	int width;
	int height;

	public Map() {
		width = 250;
		height = 250;

		mapTiles = new Tile[width][height];

		float[][] baseNoise = generateWhiteNoise(width, height);
		mapFloats = GeneratePerlinNoise(baseNoise, 6);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// System.out.println("Current Height: " + j +
				// "; Current Width: " + i + "; Float at that location: " +
				// mapFloats[i][j]);
				// System.out.println("Assigning Tile a value!");
				if (mapFloats[i][j] <= 0.2) {
					mapTiles[i][j] = new TileWater(i * 16, j * 16);
				} else if (mapFloats[i][j] > 0.2 && mapFloats[i][j] <= 0.3) {
					mapTiles[i][j] = new TileSand(i * 16, j * 16);
				} else if (mapFloats[i][j] > 0.3 && mapFloats[i][j] <= 0.6) {
					mapTiles[i][j] = new TileGrass(i * 16, j * 16);
				} else if (mapFloats[i][j] > 0.6 && mapFloats[i][j] <= 0.7) {
					mapTiles[i][j] = new TileDirt(i * 16, j * 16);
				} else if (mapFloats[i][j] > 0.7 && mapFloats[i][j] <= 0.85) {
					mapTiles[i][j] = new TileStone(i * 16, j * 16);
				} else {
					mapTiles[i][j] = new TileSnow(i * 16, j * 16);
				}
			}
		}

		saveMapToFile();
	}

	public float[][] generateWhiteNoise(int width, int height) {
		Random random = new Random(0); // Seed to 0 for testing
		float[][] noise = new float[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noise[i][j] = (float) random.nextDouble() % 1;
			}
		}

		return noise;
	}

	float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	float[][] GenerateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][] smoothNoise = new float[width][height];

		int samplePeriod = 1 << octave; // calculates 2 ^ k
		float sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < width; i++) {
			// calculate the horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
			float horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < height; j++) {
				// calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap
																		// around
				float vertical_blend = (j - sample_j0) * sampleFrequency;

				// blend the top two corners
				float top = Interpolate(baseNoise[sample_i0][sample_j0],
						baseNoise[sample_i1][sample_j0], horizontal_blend);

				// blend the bottom two corners
				float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
						baseNoise[sample_i1][sample_j1], horizontal_blend);

				// final blend
				smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
																// arrays
																// containing

		float persistance = 0.5f;

		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		// blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		// normalisation
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}

	public void saveMapToFile() {
		File file = new File("map.data");
		try {
			if (file.exists())
				file.delete();

			file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < mapTiles.length; i++) {
				for (int j = 0; j < mapTiles[0].length; j++) {
					if (mapTiles[i][j].getTileType() == Tiles.DIRT)
						bw.write("1");
					else if (mapTiles[i][j].getTileType() == Tiles.DIRT)
						bw.write("2");
					else if (mapTiles[i][j].getTileType() == Tiles.GRASS)
						bw.write("3");
					else if (mapTiles[i][j].getTileType() == Tiles.SAND)
						bw.write("4");
					else if (mapTiles[i][j].getTileType() == Tiles.SNOW)
						bw.write("5");
					else if (mapTiles[i][j].getTileType() == Tiles.STONE)
						bw.write("6");
					else
						bw.write("7");
				}
				bw.newLine();
			}
			bw.close();

			System.out.println("Map save created!");

		} catch (IOException e) {
			System.out.println("ERROR: Map save Error! Check map IO!");
		}

	}

	public Tile[][] getMapTiles() {
		return mapTiles;
	}

	void drawDot(float x, float y) {
		GL11.glColor3f(0f, 0f, 1f);

		for (int k = 0; k < 5; k++) {
			for (int l = 0; l < 5; l++) {
				GL11.glBegin(GL11.GL_POINTS);
				GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
				GL11.glVertex2f(x + k, y + l);
				GL11.glEnd();
			}
		}
	}

	public void render() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (mapTiles[i][j] != null) {
					int offsetX = ResourceHandler.get().getGameWindow().offsetX;
					int x = mapTiles[i][j].getX();
					int offsetY = ResourceHandler.get().getGameWindow().offsetY;
					int y = mapTiles[i][j].getY();

					int screenWidth = ResourceHandler.get().getGameWindow()
							.getWidth();
					int screenHeight = ResourceHandler.get().getGameWindow()
							.getHeight();

					if (x + 17 > offsetX && x - 1 < screenWidth + offsetX) {
						if (y + 17 > offsetY && y - 1 < screenHeight + offsetY) {
							mapTiles[i][j].draw();
						}
					}
				}
			}
		}
		for (int i = 0; i < width - 1; i++) {
			for (int j = 0; j < height - 1; j++) {
				if (i > 0 && j > 0) {
					// Checks up and to the left for a corner, then
					// draws a dot.
					if (mapTiles[i - 1][j].getTileType() != mapTiles[i][j]
							.getTileType()
							&& mapTiles[i][j - 1].getTileType() != mapTiles[i][j]
									.getTileType()) {
						drawDot(i * 16, j * 16);
					}
					// Checks up and to the right for a corner, then
					// draws a dot.

					if (mapTiles[i + 1][j].getTileType() != mapTiles[i][j]
							.getTileType()
							&& mapTiles[i][j - 1].getTileType() != mapTiles[i][j]
									.getTileType()) {
						drawDot(i * 16 + 13, j * 16);
					}

					// Checks down and to the left for a corner, then
					// draws a dot.

					if (mapTiles[i - 1][j].getTileType() != mapTiles[i][j]
							.getTileType()
							&& mapTiles[i][j + 1].getTileType() != mapTiles[i][j]
									.getTileType()) {
						drawDot(i * 16, j * 16 + 13);
					}

					// Checks down and to the right for a corner, then
					// draws a dot.

					if (mapTiles[i + 1][j].getTileType() != mapTiles[i][j]
							.getTileType()
							&& mapTiles[i][j + 1].getTileType() != mapTiles[i][j]
									.getTileType()) {
						drawDot(i * 16 + 13, j * 16 + 13);
					}

				}
			}
		}
	}
}
