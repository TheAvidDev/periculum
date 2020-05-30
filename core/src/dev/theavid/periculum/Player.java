package dev.theavid.periculum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The main player class which the user has control over.
 * 
 * @author TheAvidDev
 */
// 2020-05-20 TheAvidDev - Added player collision
// 2020-05-29 TheAvidDev - Fix player jittering by passing position as float
// 2020-05-27 TheAvidDev - Created basic player class with movement
public class Player {
	private final double VELOCITY_MINIMUM = 0.1;
	private final double VELOCITY_MAXIMUM = 10;
	private final double VELOCITY_MULTIPLIER = 0.5;
	private final double MOVEMENT_VELOCITY = 50;

	private TextureRegion[][] textures;
	private double x = 0;
	private double y = 0;
	private double xVel = 0;
	private double yVel = 0;
	private int animationFrame = 0;
	private int direction = 0;

	// TODO: Switch to some entity enum
	public Player() {
		x = 1260;
		y = 1010;
		textures = TextureRegion.split(new Texture("entities/player.png"), 16, 16);
	}

	/**
	 * Updates the player's position and velocities, decreasing the velocity
	 * gradually by multiplying it by VELOCITY_MULTIPLIER. Also add a minimum and
	 * maximum velocity for easier movement checking and better gameplay.
	 */
	public void update() {
		x = Math.round(getNewX() * 10f) / 10f;
		y = Math.round(getNewY() * 10f) / 10f;
		xVel *= VELOCITY_MULTIPLIER;
		yVel *= VELOCITY_MULTIPLIER;

		float dt = Gdx.graphics.getDeltaTime();
		if (KeyMap.DOWN.isPressed()) { yVel -= MOVEMENT_VELOCITY * dt; direction = 0; }
		if (KeyMap.UP.isPressed()) { yVel += MOVEMENT_VELOCITY * dt; direction = 1; }
		if (KeyMap.RIGHT.isPressed()) { xVel += MOVEMENT_VELOCITY * dt; direction = 2; }
		if (KeyMap.LEFT.isPressed()) { xVel -= MOVEMENT_VELOCITY * dt; direction = 3; }

		if (-VELOCITY_MINIMUM < xVel && xVel < VELOCITY_MINIMUM) { xVel = 0; }
		if (-VELOCITY_MINIMUM < yVel && yVel < VELOCITY_MINIMUM) { yVel = 0; }
		if (yVel > VELOCITY_MAXIMUM) { yVel = VELOCITY_MAXIMUM; }
		if (yVel < -VELOCITY_MAXIMUM) { yVel = -VELOCITY_MAXIMUM; }
		if (xVel > VELOCITY_MAXIMUM) { xVel = VELOCITY_MAXIMUM; }
		if (xVel < -VELOCITY_MAXIMUM) { xVel = -VELOCITY_MAXIMUM; }
	}

	/**
	 * Attempt to move the player up to xVel but stop if a collision is detected
	 * early.
	 * 
	 * @return the new x position for the player
	 */
	private double getNewX() {
		if (xVel < 0) {
			for (int i = 0; i >= (int) Math.floor(xVel); i--) {
				/**
				 * x+i (without any +#) because -1 for trying to move one less x and +1 to
				 * center the 14 wide character. +1-1 cancel out.
				 */
				if (Periculum.level.isColliding((int) x + i, (int) y, 14, 16)) {
					return (int) x + i;
				}
			}
			return (int) x + (int) Math.floor(xVel);
		} else if (xVel > 0) {
			for (int i = 0; i <= (int) Math.ceil(xVel); i++) {
				/**
				 * x+2 because +1 for trying to move one more x and another +1 to center the 14
				 * wide character.
				 */
				if (Periculum.level.isColliding((int) x + i + 2, (int) y, 14, 16)) {
					return (int) x + i;
				}
			}
			return (int) x + (int) Math.ceil(xVel);
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
		if (yVel < 0) {
			for (int i = 0; i >= (int) Math.floor(yVel); i--) {
				if (Periculum.level.isColliding((int) x + 1, (int) y + i - 1, 14, 16)) {
					return (int) y + i;
				}
			}
			return (int) y + (int) Math.floor(yVel);
		} else if (yVel > 0) {
			for (int i = 0; i <= (int) Math.ceil(yVel); i++) {
				if (Periculum.level.isColliding((int) x + 1, (int) y + i + 1, 14, 16)) {
					return (int) y + i;
				}
			}
			return (int) y + (int) Math.ceil(yVel);
		}
		return y;
	}

	/**
	 * @return the appropriate texture region for the player's direction and
	 *         animation frame.
	 */
	public TextureRegion getTextureRegion() {
		return textures[direction][animationFrame];
	}

	public float getX() { return (float) x; }
	public float getY() { return (float) y; }
	public float getXVel() { return (float) xVel; }
	public float getYVel() { return (float) yVel; }

	public void dispose() { textures[0][0].getTexture().dispose(); }
}
