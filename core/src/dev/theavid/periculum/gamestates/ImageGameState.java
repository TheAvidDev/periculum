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
 * A simple game scene with an image displayed on the center of the screen.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-13 TheAvidDev - Converted to more generic image game scene
// 2020-06-04 hirundinidae - Added new GameState methods 
// 2020-06-04 hirundinidae - Changed logo and company name png
// 2020-05-30 TheAvidDev - Created splash screen game state
public class ImageGameState extends GameState {

	private SpriteBatch batch;
	private Texture logo;
	private float scale;
	private GameState nextGameState;

	public ImageGameState(OrthographicCamera camera, String filename, float scale, GameState nextGameState) {
		super(camera);
		batch = new SpriteBatch();
		logo = new Texture("img/" + filename);
		this.scale = scale;
		this.nextGameState = nextGameState;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logo, -logo.getWidth() * scale, -logo.getHeight() * scale, logo.getWidth() * scale * 2,
				logo.getHeight() * scale * 2);
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
		return nextGameState;
	}

	@Override
	public void update() {
		/*
		 * We can't just do this once because if this game state gets passed as a next
		 * game state to an ImageGameState, the camera will get reset to a zoom of 1 in
		 * the dispose().
		 *
		 * TODO: The proper solution is creating a create() method.
		 */
		camera.zoom = 2f;
		camera.position.set(0, 0, 0);
		camera.update();
		return;
	}
}
