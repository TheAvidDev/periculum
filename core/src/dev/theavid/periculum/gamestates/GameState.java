package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A generic game state which contains an orthographic camera.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-04 hirundinidae - Added methods shouldTransistion() and getNextGameState()
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

	/**
	 * 
	 * @return boolean value indicating if the a transition should occur.
	 */
	public abstract boolean shouldTransition();

	/**
	 * 
	 * @return next GameState
	 */
	public abstract GameState getNextGameState();
}
