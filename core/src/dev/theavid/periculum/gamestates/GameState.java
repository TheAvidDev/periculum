package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A generic game state which contains an orthographic camera.
 * 
 * @author TheAvidDev
 */
// 2020-05-30 TheAvidDev - Created abstract game state
public abstract class GameState {
	OrthographicCamera camera;

	public GameState(OrthographicCamera camera) {
		this.camera = camera;
	}

	public abstract void update();

	public abstract void render();

	public abstract void dispose();
}
