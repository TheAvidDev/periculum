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
	PLAYER("player.png", false, 16, 16),
	DOOR("door.png", false, 16, 11),
	POPUP("popup.png", false, 64, 48),
	OPTION("option.png", false, 58, 12);

	private final String BASE_DIR = "entities/";

	private final boolean collidable;
	private final int width;
	private final int height;
	private TextureRegion[][] textureRegions;

	private EntityType(String filename, boolean collidable, int width, int height) {
		this.collidable = collidable;
		this.width = width;
		this.height = height;
		textureRegions = TextureRegion.split(new Texture(BASE_DIR + filename), width, height);
	}

	/**
	 * Gets the TextureRegion of the original Texture based on the row and column.
	 * This is best used for animated textures.
	 * 
	 * @param row    which row of the Texture to get
	 * @param column which column of the Texture to get
	 * @return the TextureRegion based on the row and column
	 */
	public TextureRegion getTextureRegion(int row, int column) {
		return textureRegions[row][column];
	}

	/**
	 * A shortcut for getting the first TextureRegion of the base texture. Best used
	 * for static textures without animations.
	 *
	 * @return the first TextureRegion
	 */
	public TextureRegion getTextureRegion() {
		return textureRegions[0][0];
	}

	/**
	 * Disposes all resources associated with this EntityType.
	 */
	public void dispose() {
		textureRegions[0][0].getTexture().dispose();
	}

	public boolean getCollidable() {
		return collidable;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
