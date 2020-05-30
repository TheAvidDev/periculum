package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A simple splash screen with an image displayed on the center of the screen.
 * 
 * @author TheAvidDev
 */
// 2020-05-30 TheAvidDev - Created splash screen game state
public class SplashGameState extends GameState {
	private SpriteBatch spriteBatch;
	private Texture logo;

	public SplashGameState(OrthographicCamera camera) {
		super(camera);
		spriteBatch = new SpriteBatch();
		logo = new Texture("logo/logo.png");
	}

	@Override
	public void update() {
		camera.position.set(0, 0, 0);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(
				logo,
				-logo.getWidth()/16,
				-logo.getHeight()/16,
				logo.getWidth()/8,
				logo.getHeight()/8,
				0, 0,
				logo.getWidth(),
				logo.getHeight(), false, false
		);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		logo.dispose();
	}
}
