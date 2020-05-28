package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class.
 *
 * @author hirundinidae
 * @author TheAvidDev
 */
// 2020-05-28 TheAvidDev - cleaned up code, removed libGDX defaults
// 2020-05-27 hirundinidae - added level creation and rendering
public class Periculum extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	Level level;

	@Override
	public void create() {
		batch = new SpriteBatch();
		player = new Player();
		level = new Level();
		level.create();
	}

	@Override
	public void render() {
		player.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		level.render();
		batch.begin();
		batch.draw(player.getTextureRegion(), player.getX(), player.getY());
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
		level.dispose();
	}
}
