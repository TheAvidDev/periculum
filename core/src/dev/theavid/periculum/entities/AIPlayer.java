package dev.theavid.periculum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A player that isn't controlled with the keyboard, instead walking around
 * randomly.
 * 
 * @author TheAvidDev
 */
// 2020-06-13 TheAvidDev - Created autonomous player
public class AIPlayer extends Player {
	private EntityType[] playerTypes = { EntityType.PLAYER2, EntityType.PLAYER3 , EntityType.PLAYER4, EntityType.PLAYER5};

	private float movementCounter = 0;
	private float movementTime = 0;
	private final float MOVEMENT_TIME_MAXIMUM = 5f;
	private final float STALL_TIME_MAXIMUM = 5f;

	/**
	 * Creates a player with a random sprite (EntityType).
	 */
	public AIPlayer(float x, float y) {
		super(x, y);
		entityType = playerTypes[(int) (Math.random() * playerTypes.length)];
	}

	/**
	 * Sets a random direction and movement time to determine how long to walk and
	 * stall far, as well as which direction to do it in.
	 */
	@Override
	public void updateVelocity() {
		float dt = Gdx.graphics.getDeltaTime();
		if (movementCounter > 0) {
			switch (direction) {
			case 0:
				setYVel(getYVel() - MOVEMENT_VELOCITY * dt);
				break;
			case 1:
				setYVel(getYVel() + MOVEMENT_VELOCITY * dt);
				break;
			case 2:
				setXVel(getXVel() + MOVEMENT_VELOCITY * dt);
				break;
			case 3:
				setXVel(getXVel() - MOVEMENT_VELOCITY * dt);
				break;
			}
		}

		/**
		 * Update counters and direction if enough movement has occurred.
		 */
		movementCounter += dt;
		if (movementCounter > movementTime) {
			direction = (int) (Math.random() * 4);
			movementTime = (float) (Math.random() * MOVEMENT_TIME_MAXIMUM);
			movementCounter = (float) (-Math.random() * STALL_TIME_MAXIMUM);
		}
	}

	/**
	 * Don't draw any health bars.
	 */
	@Override
	public void additionalRender(OrthographicCamera camera) {
		return;
	}
}
