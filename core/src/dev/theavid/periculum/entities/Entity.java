package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An entity is any object that can be drawn and has some position in game but
 * is not part of the background tiles. This include objects like the player,
 * notifiers, and other characters.
 *
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-03 TheAvidDev - Add methods to help with drawing and interface with
//                         the EntityType
// 2020-05-29 hirundinidae - Created Entitiy class
public class Entity {
	protected float x;
	protected float y;
	private EntityType entityType;

	public Entity(EntityType entityType, float x, float y) {
		this.entityType = entityType;
		this.x = x;
		this.y = y;
	}

	/**
	 * Updates physics based properties of an Entity.
	 */
	public void update() {
		return;
	}

	/**
	 * Disposes all resources associated with this Entity.
	 */
	public void dispose() {
		entityType.dispose();
	}

	/**
	 * Returns the TextureRegion which will be drawn by a Spritebatch. This method
	 * must be implemented for rendering.
	 *
	 * @return the TextureRegion to render
	 */
	public TextureRegion getTextureRegion() {
		return entityType.getTextureRegion();
	}

	/**
	 * A helper method which passes execution to this Entity's EntityType's
	 * TextureRegion getter.
	 */
	public TextureRegion getTextureRegion(int row, int column) {
		return entityType.getTextureRegion(row, column);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public EntityType getEntityType() {
		return entityType;
	}
}
