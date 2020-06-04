package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An enum for non static entities.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-03 TheAvidDev - Added more getter methods and loaded textures
// 2020-05-29 hirundinidae - Created enum
public enum EntityType {
	NOTIFIER("notifier.png", false, 16, 16),
	PLAYER("player.png", false, 16, 16);

	private final String BASE_DIR = "entities/";

	private final boolean collidable;
	private final int width;
	private final int height;
	private TextureRegion[][] textureRegions;

	EntityType(String filename, boolean collidable, int width, int height) {
		this.collidable = collidable;
		this.width = width;
		this.height = height;
		textureRegions = TextureRegion.split(new Texture(BASE_DIR + filename), width, height);
	}

	public TextureRegion getTextureRegion(int row, int column) {
		return textureRegions[row][column];
	}

	public TextureRegion getTextureRegion() {
		return textureRegions[0][0];
	}

	public void dispose() {
		textureRegions[0][0].getTexture().dispose();
	}

	public boolean getCollidable() {
		return collidable;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
