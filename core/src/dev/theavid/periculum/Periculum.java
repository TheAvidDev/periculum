package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class 
 * 
 * @author hirundinidae
 */
// 2020-05-27 hirundinidae - Called Level.java methods
public class Periculum extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Player player;
	Level level;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		player = new Player();
		level = new Level();
		level.create();
	}

	@Override
	public void render() {
		player.update();

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		level.render();
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(player.getTextureRegion(), player.getX(), player.getY());
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		player.dispose();
		level.dispose();
	}
}
