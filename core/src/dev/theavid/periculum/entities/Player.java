package dev.theavid.periculum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.gamestates.PlayingGameState;

/**
 * The main player class which the user has control over.
 * 
 * @author TheAvidDev
 */
// 2020-06-13 TheAvidDev - Added health bars and changed health denominator
// 2020-06-11 TheAvidDev - Add player health alterations
// 2020-06-03 TheAvidDev - Switch to Entity superclass and create setters
// 2020-05-30 TheAvidDev - Add player animations
// 2020-05-29 TheAvidDev - Add player collision
// 2020-05-29 TheAvidDev - Fix player jittering by passing position as float
// 2020-05-27 TheAvidDev - Create basic player class with movement
public class Player extends Entity {
	protected final int ANIMATION_SPEED = 4 * 4; // Multiple of 4
	protected final float VELOCITY_MINIMUM = 0.1f;
	protected final float VELOCITY_MAXIMUM = 10f;
	protected final float VELOCITY_MULTIPLIER = 0.5f;
	protected final float MOVEMENT_VELOCITY = 50f;
	private final float HEALTH_DENOMINATOR = 7;

	protected int animationFrame = 0;
	protected double animationCounter = 0;
	protected int direction = 0;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private float xVel = 0;
	private float yVel = 0;
	private float infectionRisk = 0;
	private float mentalStability = 1;

	public Player(float x, float y) {
		this(EntityType.PLAYER, x, y);
	}

	/**
	 * Allow creation of a player with a non-default sprite (EntityType).
	 */
	public Player(EntityType playerType, float x, float y) {
		super(playerType, x, y);
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

		updateVelocity();

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
	 * Movement control and limiting.
	 */
	protected void updateVelocity() {
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
	}

	/**
	 * Does the additional rendering required for the health bars.
	 */
	@Override
	public void additionalRender(OrthographicCamera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		float barX = getX() - camera.viewportWidth / 2 + 5;
		float baseY = getY() - camera.viewportHeight / 2 + 5;

		/**
		 * Render the health bar box and bars.
		 *
		 * TODO: Don't hard code colors or dimensions.
		 */
		shapeRenderer.setColor(0.698f, 0.686f, 0.651f, 1f);
		shapeRenderer.rect(barX - 3, baseY - 3, 46, 18);
		shapeRenderer.setColor(0.255f, 0.275f, 0.325f, 1f);
		shapeRenderer.rect(barX - 2, baseY - 2, 44, 16);
		shapeRenderer.setColor(0.773f, 0.180f, 0.361f, 1f);
		shapeRenderer.rect(barX, baseY + 7, 40 * infectionRisk, 5);
		shapeRenderer.setColor(0.122f, 0.565f, 0.737f, 1f);
		shapeRenderer.rect(barX, baseY, 40 * mentalStability, 5);
		shapeRenderer.end();
	}

	/**
	 * Cleanup the shape renderer we used for additional rendering.
	 */
	public void dispose() {
		super.dispose();
		shapeRenderer.dispose();
	}

	/**
	 * Alter the player's infection risk.
	 */
	public void changeInfectionRisk(float deltaInfectionRisk) {
		infectionRisk = Math.max(0, infectionRisk + deltaInfectionRisk / HEALTH_DENOMINATOR);
	}

	/**
	 * Alter the player's mental stability.
	 */
	public void changeMentalStability(float deltaMentalStability) {
		mentalStability = Math.min(1, mentalStability + deltaMentalStability / HEALTH_DENOMINATOR);
	}

	/**
	 * Determine whether the player should die because their mental stability has
	 * reached 0 or if their infection risk has reached 1.
	 */
	@Override
	public boolean shouldKill() {
		return infectionRisk >= 1 || mentalStability <= 0;
	}

	/**
	 * Attempt to move the player up to xVel but stop if a collision is detected
	 * early.
	 * 
	 * @return the new x position for the player
	 */
	protected double getNewX() {
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
	protected double getNewY() {
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
