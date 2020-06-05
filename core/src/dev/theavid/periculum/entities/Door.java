package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.theavid.periculum.gamestates.PlayingGameState;

/**
 * An animated door entity which animates when other entities are near it.
 * 
 * @author TheAvidDev
 */
// 2020-06-04 TheAvidDev - Create door class with animations
public class Door extends Entity {
	private final double MIN_DISTANCE = Math.pow(20, 2);
	private double animationFrame = 0;

	public Door(float x, float y) {
		super(EntityType.DOOR, x, y);
	}

	@Override
	public void update() {
		for (Entity entity : PlayingGameState.entityList) {
			if (entity.getEntityType() == EntityType.DOOR) {
				continue;
			}

			/**
			 * As long as there is any entity near the door, switch to the next frame once.
			 */
			if (Math.pow(entity.getX() - getX(), 2) + Math.pow(entity.getY() - getY(), 2) < MIN_DISTANCE) {
				animationFrame = Math.min(animationFrame + 0.5, 2);
				return;
			}
		}
		animationFrame = Math.max(animationFrame - 0.5, 0);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return getTextureRegion(0, (int) animationFrame);
	}
}
