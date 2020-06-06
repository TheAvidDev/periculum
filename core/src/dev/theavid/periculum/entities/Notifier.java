package dev.theavid.periculum.entities;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.gamestates.PlayingGameState;

public class Notifier extends Entity {
	private final double MIN_DISTANCE = Math.pow(20, 2);

	public Notifier(float x, float y) {
		super(EntityType.NOTIFIER, x, y);
	}

	@Override
	public boolean shouldKill() {
		for (Entity entity : PlayingGameState.entityList) {
			if (entity.getEntityType() == EntityType.PLAYER && KeyMap.TRANSITION.isPressed()
					&& Math.pow(entity.getX() - getX(), 2) + Math.pow(entity.getY() - getY(), 2) < MIN_DISTANCE) {
				return true;
			}
		}
		return false;
	}
}
