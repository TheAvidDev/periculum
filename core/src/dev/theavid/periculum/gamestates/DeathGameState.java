package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.events.DeathOption;

/**
 * The state where the player is notified of their failure.
 * 
 * @author hirundinidae
 *
 */
// 2020-06-05 hirunidnidae - Create basic outline of the death screen

public class DeathGameState extends GameState {

	private String deathMessage;
	private String deathIconFilename;
	private SpriteBatch batch = new SpriteBatch();
	private BitmapFont font = new BitmapFont();
	private Transition transition;

	public DeathGameState(OrthographicCamera camera, DeathOption deathOption) {
		super(camera);
		this.deathMessage = deathOption.getDeathMessage();
		this.deathIconFilename = deathOption.getDeathIconFilename();
		font.setColor(Color.WHITE);
	}

	@Override
	public void update() {
		camera.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();

	}

	@Override
	public boolean shouldTransition() {
		if (KeyMap.TRANSITION.isPressed()) {
			transition = Transition.RESTART;
			return true;
		}
		if (KeyMap.QUIT.isPressed()) {
			transition = Transition.QUIT;
			return true;
		}
		return false;
	}

	@Override
	public GameState getNextGameState() {
		if (transition == Transition.RESTART) {
			return new PlayingGameState(camera);
		}
		Gdx.app.exit();
		return new PlayingGameState(camera);
	}

	private enum Transition {
		QUIT, RESTART;
	}
}
