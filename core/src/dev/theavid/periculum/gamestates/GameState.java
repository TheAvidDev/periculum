package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A generic game state which contains an orthographic camera.
 * 
 * @author TheAvidDev
 */
// 2020-05-30 TheAvidDev - Created abstract game state
public abstract class GameState {
	protected OrthographicCamera camera;

	public GameState(OrthographicCamera camera) {
		this.camera = camera;
	}

	/**
	 * Update any physics based calculations or general state of this GameState.
	 */
	public abstract void update();

	/**
	 * Draw any required objects of this GameState.
	 */
	public abstract void render();

	/**
	 * Disposes all resources associated with this GameState.
	 */
	public abstract void dispose();
}
