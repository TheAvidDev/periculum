package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class.
 *
 * @author hirundinidae
 * @author TheAvidDev
 */
// 2020-05-29 TheAvidDev - Decrease occurrences of black lines between tiles
// 2020-05-29 TheAvidDev - Render background and foreground layers separately
// 2020-05-29 TheAvidDev - Allow window resizing and scale definitions
// 2020-05-28 hirundinidae - Create camera and its movement based on player
// 2020-05-28 TheAvidDev - Clean up code, removed libGDX defaults
// 2020-05-27 hirundinidae - Add level creation and rendering
public class Periculum extends ApplicationAdapter {
	private final int TILE_WIDTH = 16;
	private final int VIEWPORT_WIDTH = 16;

	public static Debugger debugger;
	public static Level level;

	SpriteBatch batch;
	Player player;
	OrthographicCamera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		player = new Player();
		level = new Level();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 320, 320 * (4 / 3));
		level.create();
		debugger = new Debugger(player, level, camera);
	}

	@Override
	public void render() {
		// Updating
		player.update();
		/**
		 * Rounding to tenth of a pixel removes extremely common black lines between
		 * tiles. However, this doesn't fix them on all resolutions.
		 */
		camera.position.set(Math.round(player.getX() * 10f) / 10f, Math.round(player.getY() * 10f) / 10f, 0);
		camera.update();
		debugger.update();

		// Drawing
		Gdx.gl.glClearColor(0.506f, 0.984f, 0.294f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		level.renderBackground(camera);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(player.getTextureRegion(), player.getX(), player.getY());
		batch.end();

		level.renderForeground(camera);

		debugger.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
		level.dispose();
	}

	/**
	 * This method resizes the camera's viewport while maintaining scale to allow
	 * any window size. It will base the scale off of the shortest side length, so
	 * ultra wide screens won't be unnecessarily zoomed in.
	 */
	@Override
	public void resize(int width, int height) {
		if ((float) width / (float) height > (float) height / (float) width) {
			float scale = (float) width / (float) height;
			camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH * scale;
			camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH;
		} else {
			float scale = (float) height / (float) width;
			camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH;
			camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH * scale;
		}
		camera.update();
	}
}
