package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.theavid.periculum.Debugger;
import dev.theavid.periculum.Level;
import dev.theavid.periculum.entities.Player;

/**
 * The main state in which the player plays with a level and moving camera.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-05-30 TheAvidDev - Switched to a game state
// 2020-05-29 TheAvidDev - Decrease occurrences of black lines between tiles
// 2020-05-29 TheAvidDev - Render background and foreground layers separately
// 2020-05-29 TheAvidDev - Allow window resizing and scale definitions
// 2020-05-28 hirundinidae - Create camera and its movement based on player
// 2020-05-28 TheAvidDev - Clean up code, removed libGDX defaults
// 2020-05-27 hirundinidae - Add level creation and rendering
public class PlayingGameState extends GameState {
	public static Debugger debugger;
	public static Level level;

	SpriteBatch batch;
	Player player;

	public PlayingGameState(OrthographicCamera camera) {
		super(camera);
		batch = new SpriteBatch();
		player = new Player();
		level = new Level();

		level.create();
		debugger = new Debugger(player, level, camera);
	}

	@Override
	public void update() {
		player.update();
		/**
		 * Rounding to tenth of a pixel removes extremely common black lines between
		 * tiles. However, this doesn't fix them on all resolutions.
		 */
		camera.position.set(Math.round(player.getX() * 10f) / 10f, Math.round(player.getY() * 10f) / 10f, 0);
		camera.update();
		debugger.update();

	}

	@Override
	public void render() {
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
}
