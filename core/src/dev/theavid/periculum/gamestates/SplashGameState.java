package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.Periculum;

/**
 * A simple splash screen with an image displayed on the center of the screen.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-04 hirundinidae - Added new GameState methods 
// 2020-06-04 hirundinidae - Changed logo and company name png
// 2020-05-30 TheAvidDev - Created splash screen game state
public class SplashGameState extends GameState {
	private SpriteBatch batch;
	private Texture logo;

	public SplashGameState(OrthographicCamera camera) {
		super(camera);
		batch = new SpriteBatch();
		logo = new Texture("logo/logo_name.png");

		camera.zoom = 2f;
		camera.position.set(0, 0, 0);
		camera.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logo, -logo.getWidth() / 6, -logo.getHeight() / 6, logo.getWidth() / 3, logo.getHeight() / 3, 0, 0,
				logo.getWidth(), logo.getHeight(), false, false);
		Periculum.headerFont.draw(batch, "Press Space to continue.", -camera.viewportWidth / 2 + 20,
				-camera.viewportHeight / 2 + 16, camera.viewportWidth - 40, Align.center, true);
		batch.end();
	}

	@Override
	public void dispose() {
		camera.zoom = 1f;
		camera.update();
		batch.dispose();
		logo.dispose();
	}

	@Override
	public boolean shouldTransition() {
		return KeyMap.TRANSITION.isPressed(true);
	}

	@Override
	public GameState getNextGameState() {
		return new PlayingGameState(camera);
	}

	@Override
	public void update() {
		return;
	}
}
