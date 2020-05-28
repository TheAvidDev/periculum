package dev.theavid.periculum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The main player class which the user has control over.
 * 
 * @author TheAvidDev
 */
// 2020-05-27 TheAvidDev - Created basic player class with movement
public class Player {	
	private final double VELOCITY_MINIMUM = 3000000;
	private final double VELOCITY_MAXIMUM = 0.1;
	private final double VELOCITY_MULTIPLIER = 0.9;
	private final double MOVEMENT_VELOCITY = 150;
	
	private TextureRegion[][] textures;
	private double x = 0;
	private double y = 0;
	private double xVel = 0;
	private double yVel = 0;
	private int animationFrame = 0;
	private int direction = 0;
	
	// TODO: Switch to some entity enum
	public Player() {
		textures = TextureRegion.split(new Texture("entities/player.png"), 16, 16);
	}
	
	/**
	 * Updates the player's position and velocities, decreasing the velocity
	 * gradually by multiplying it by VELOCITY_MULTIPLIER. Also add a minimum
	 * and maximum velocity for easier movement checking and better gameplay.
	 */
	public void update() {
		x += xVel;
		y += yVel;
		xVel *= VELOCITY_MULTIPLIER;
		yVel *= VELOCITY_MULTIPLIER;
		
		float dt = Gdx.graphics.getDeltaTime();
		// TODO: Make this cleaner with an InputManager class.
		if (Gdx.input.isKeyPressed(Input.Keys.S)) { yVel -= MOVEMENT_VELOCITY * dt; direction = 0; }
		if (Gdx.input.isKeyPressed(Input.Keys.W)) { yVel += MOVEMENT_VELOCITY * dt; direction = 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.D)) { xVel += MOVEMENT_VELOCITY * dt; direction = 2; }
		if (Gdx.input.isKeyPressed(Input.Keys.A)) { xVel -= MOVEMENT_VELOCITY * dt; direction = 3; }

		if (-VELOCITY_MINIMUM < xVel && xVel < VELOCITY_MINIMUM) { xVel = 0; }
		if (-VELOCITY_MINIMUM < yVel && yVel < VELOCITY_MINIMUM) { yVel = 0; }
		if (yVel > VELOCITY_MAXIMUM) { yVel = VELOCITY_MAXIMUM; }
		if (yVel < -VELOCITY_MAXIMUM) { yVel = -VELOCITY_MAXIMUM; }
		if (xVel > VELOCITY_MAXIMUM) { xVel = VELOCITY_MAXIMUM; }
		if (xVel < -VELOCITY_MAXIMUM) { xVel = -VELOCITY_MAXIMUM; }
	}
	
	/**
	 * @return the appropriate texture region for the player's direction and
	 * animation frame.
	 */
	public TextureRegion getTextureRegion() {
		return textures[direction][animationFrame];
	}
		
	/**
	 * While the {@link SpriteBatch} can use floats to draw a texture, we'll
	 * cast it to an integer to align it to full pixels.
	 */
	public int getX() { return (int) x; }
	public int getY() { return (int) y; }
	
	public void dispose() { textures[0][0].getTexture().dispose(); }
}

