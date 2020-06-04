package dev.theavid.periculum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.gamestates.PlayingGameState;

/**
 * The main player class which the user has control over.
 * 
 * @author TheAvidDev
 */
// 2020-06-03 TheAvidDev - Switch to Entity superclass and create setters
// 2020-05-30 TheAvidDev - Add player animations
// 2020-05-29 TheAvidDev - Add player collision
// 2020-05-29 TheAvidDev - Fix player jittering by passing position as float
// 2020-05-27 TheAvidDev - Create basic player class with movement
public class Player extends Entity {
	private final int ANIMATION_SPEED = 4 * 4; // Multiple of 4
	private final float VELOCITY_MINIMUM = 0.1f;
	private final float VELOCITY_MAXIMUM = 10f;
	private final float VELOCITY_MULTIPLIER = 0.5f;
	private final float MOVEMENT_VELOCITY = 50f;

	private float xVel = 0;
	private float yVel = 0;
	private int animationFrame = 0;
	private double animationCounter = 0;
	private int direction = 0;

	public Player(EntityType entityType, float x, float y) {
		super(entityType, x, y);
	}

	/**
	 * Updates the player's position and velocities, decreasing the velocity
	 * gradually by multiplying it by VELOCITY_MULTIPLIER. Also add a minimum and
	 * maximum velocity for easier movement checking and better gameplay.
	 */
	@Override
	public void update() {
		/**
		 * We do the same rounding as we do in the camera to get rid of any jittering
		 * between the player and the level.
		 */
		x = Math.round(getNewX() * 10f) / 10f;
		y = Math.round(getNewY() * 10f) / 10f;
		setXVel(getXVel() * VELOCITY_MULTIPLIER);
		setYVel(getYVel() * VELOCITY_MULTIPLIER);

		/**
		 * Animation
		 */
		if (getXVel() == 0 && getYVel() == 0) {
			animationCounter = 0;
		} else {
			animationCounter += Math.abs(getXVel()) / 2 + Math.abs(getYVel()) / 2;
			animationCounter %= ANIMATION_SPEED;
		}
		animationFrame = (int) animationCounter / (ANIMATION_SPEED / 4);

		/**
		 * Movement control and limiting
		 */
		float dt = Gdx.graphics.getDeltaTime();
		if (KeyMap.DOWN.isPressed()) {
			setYVel(getYVel() - MOVEMENT_VELOCITY * dt);
			direction = 0;
		}
		if (KeyMap.UP.isPressed()) {
			setYVel(getYVel() + MOVEMENT_VELOCITY * dt);
			direction = 1;
		}
		if (KeyMap.RIGHT.isPressed()) {
			setXVel(getXVel() + MOVEMENT_VELOCITY * dt);
			direction = 2;
		}
		if (KeyMap.LEFT.isPressed()) {
			setXVel(getXVel() - MOVEMENT_VELOCITY * dt);
			direction = 3;
		}

		if (-VELOCITY_MINIMUM < getXVel() && getXVel() < VELOCITY_MINIMUM) {
			setXVel(0);
		}
		if (-VELOCITY_MINIMUM < getYVel() && getYVel() < VELOCITY_MINIMUM) {
			setYVel(0);
		}
		if (getYVel() > VELOCITY_MAXIMUM) {
			setYVel(VELOCITY_MAXIMUM);
		}
		if (getYVel() < -VELOCITY_MAXIMUM) {
			setYVel(-VELOCITY_MAXIMUM);
		}
		if (getXVel() > VELOCITY_MAXIMUM) {
			setXVel(VELOCITY_MAXIMUM);
		}
		if (getXVel() < -VELOCITY_MAXIMUM) {
			setXVel(-VELOCITY_MAXIMUM);
		}
	}

	/**
	 * Attempt to move the player up to xVel but stop if a collision is detected
	 * early.
	 * 
	 * @return the new x position for the player
	 */
	private double getNewX() {
		if (getXVel() < 0) {
			for (int i = 0; i >= (int) Math.floor(getXVel()); i--) {
				if (PlayingGameState.level.isColliding((int) x + i + 1, (int) y + 1, 12, 14)) {
					return (int) x + i;
				}
			}
			return (int) x + (int) Math.floor(getXVel());
		} else if (getXVel() > 0) {
			for (int i = 0; i <= (int) Math.ceil(getXVel()); i++) {
				if (PlayingGameState.level.isColliding((int) x + i + 3, (int) y + 1, 12, 14)) {
					return (int) x + i;
				}
			}
			return (int) x + (int) Math.ceil(getXVel());
		}
		return x;
	}

	/**
	 * Attempt to move the player up to yVel but stop if a collision is detected
	 * early.
	 * 
	 * @return the new y position for the player
	 */
	private double getNewY() {
		if (getYVel() < 0) {
			for (int i = 0; i >= (int) Math.floor(getYVel()); i--) {
				if (PlayingGameState.level.isColliding((int) x + 2, (int) y + i, 12, 14)) {
					return (int) y + i;
				}
			}
			return (int) y + (int) Math.floor(getYVel());
		} else if (getYVel() > 0) {
			for (int i = 0; i <= (int) Math.ceil(getYVel()); i++) {
				if (PlayingGameState.level.isColliding((int) x + 2, (int) y + i + 2, 12, 14)) {
					return (int) y + i;
				}
			}
			return (int) y + (int) Math.ceil(getYVel());
		}
		return y;
	}

	@Override
	public TextureRegion getTextureRegion() {
		return getTextureRegion(direction, animationFrame);
	}

	public float getXVel() {
		return xVel;
	}

	public void setXVel(float xVel) {
		this.xVel = xVel;
	}

	public float getYVel() {
		return yVel;
	}

	public void setYVel(float yVel) {
		this.yVel = yVel;
	}
}
