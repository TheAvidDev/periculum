package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.theavid.periculum.KeyMap;

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
	private SpriteBatch spriteBatch;
	private Texture logo;

	public SplashGameState(OrthographicCamera camera) {
		super(camera);
		spriteBatch = new SpriteBatch();
		logo = new Texture("logo/logo_name.png");
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
		spriteBatch.draw(logo, -logo.getWidth() / 12, -logo.getHeight() / 12, logo.getWidth() / 6, logo.getHeight() / 6,
				0, 0, logo.getWidth(), logo.getHeight(), false, false);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
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

}
