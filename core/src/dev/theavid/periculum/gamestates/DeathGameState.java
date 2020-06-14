package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.Periculum;
import dev.theavid.periculum.events.DeathOption;

/**
 * The state where the player is notified of their failure.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 *
 */
// 2020-06-13 TheAvidDev - Add icon and message rendering
// 2020-06-05 hirunidnidae - Create basic outline of the death screen

public class DeathGameState extends GameState {
	private final String CONTINUE_STRING = "To restart, press Space. To exit press Q.";
	private final float ICON_SCALE = 3;

	private String deathMessage;
	private Texture deathIcon;
	private SpriteBatch batch = new SpriteBatch();
	private Transition transition;

	/**
	 * Setup the message and icon for a "default" death caused by reaching 0 mental
	 * stability or reaching the maximum infection risk.
	 */
	public DeathGameState(OrthographicCamera camera) {
		super(camera);
		this.deathMessage = "You died! Either your infection risk was too high or your mental stability dropped to 0.";
		this.deathIcon = new Texture("events/skull.png");
	}

	/**
	 * Setup the message and icon from the event that caused their immediate death.
	 */
	public DeathGameState(OrthographicCamera camera, DeathOption deathOption) {
		super(camera);
		this.deathMessage = deathOption.getDeathMessage();
		this.deathIcon = new Texture("events/" + deathOption.getDeathIconFilename());
	}

	@Override
	public void update() {
		camera.update();
	}

	/**
	 * Render the icon, message, and continuation part to inform the player of their
	 * demise.
	 */
	@Override
	public void render() {
		Periculum.headerFont.setColor(Color.WHITE);
		camera.zoom = 2f;

		/**
		 * Clear the screen.
		 */
		Gdx.gl.glClearColor(0.082f, 0.129f, 0.173f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**
		 * Draw icon, message, and continuation prompt.
		 * 
		 * TODO: Don't hard code these values.
		 */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(deathIcon, -deathIcon.getWidth() * ICON_SCALE, -deathIcon.getHeight() * ICON_SCALE,
				deathIcon.getWidth() * ICON_SCALE * 2, deathIcon.getHeight() * ICON_SCALE * 2);
		Periculum.headerFont.draw(batch, deathMessage, -camera.viewportWidth / 2 + 20, camera.viewportHeight / 2 - 10,
				camera.viewportWidth - 40, Align.center, true);
		Periculum.headerFont.draw(batch, CONTINUE_STRING, -camera.viewportWidth / 2 + 20,
				-camera.viewportHeight / 2 + 16, camera.viewportWidth - 40, Align.center, true);
		batch.end();
	}

	@Override
	public void dispose() {
		camera.zoom = 1f;
		camera.update();
		batch.dispose();
		deathIcon.dispose();
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
