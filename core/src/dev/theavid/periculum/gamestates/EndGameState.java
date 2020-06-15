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
 */
// 2020-06-13 TheAvidDev - Added win game state, converting to a generic EndGameState
// 2020-06-13 TheAvidDev - Add icon and message rendering
// 2020-06-05 hirunidnidae - Create basic outline of the death screen
public class EndGameState extends GameState {
	private final String CONTINUE_STRING = "To restart, press Space. To exit press Q.";
	private final float ICON_SCALE = 3;

	private String message;
	private Texture icon;
	private SpriteBatch batch = new SpriteBatch();
	private Transition transition;
	private boolean loss;
	private boolean learning;

	/**
	 * Setup the message and icon for a "default" death caused by reaching 0 mental
	 * stability or reaching the maximum infection risk.
	 */
	public EndGameState(OrthographicCamera camera, boolean loss, boolean learning) {
		super(camera);
		if (loss) {
			message = "You died! Either your infection risk was too high or your mental stability dropped to 0.";
			icon = new Texture("events/skull.png");
			music = Gdx.audio.newMusic(Gdx.files.internal("audio/loss.mp3"));
		} else {
			message = "Congratulations! You win!\nYou passed all of the challenges that came your way, making choices that lead you to success.";
			icon = new Texture("events/trophy.png");
			music = Gdx.audio.newMusic(Gdx.files.internal("audio/win.mp3"));
		}
		music.setLooping(true);
		this.loss = loss;
		this.learning = learning;
		camera.zoom = 2f;
		camera.update();
	}

	/**
	 * Setup the message and icon from the event that caused their immediate death.
	 */
	public EndGameState(OrthographicCamera camera, DeathOption deathOption, boolean learning) {
		super(camera);
		message = deathOption.getDeathMessage();
		icon = new Texture("events/" + deathOption.getDeathIconFilename());
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/loss.mp3"));
		music.setLooping(true);
		this.loss = true;
		this.learning = learning;
		camera.zoom = 2f;
		camera.update();
	}

	/**
	 * Render the icon, message, and continuation part to inform the player of their
	 * demise or win.
	 */
	@Override
	public void render() {
		/**
		 * Clear the screen.
		 */
		if (loss) {
			Gdx.gl.glClearColor(0.082f, 0.129f, 0.173f, 1f);
			Periculum.headerFont.setColor(Color.WHITE);
		} else {
			Gdx.gl.glClearColor(0.886f, 0.875f, 0.831f, 1f);
			Periculum.headerFont.setColor(Color.BLACK);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**
		 * Draw icon, message, and continuation prompt.
		 * 
		 * TODO: Don't hard code these values.
		 */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(icon, -icon.getWidth() * ICON_SCALE, -icon.getHeight() * ICON_SCALE,
				icon.getWidth() * ICON_SCALE * 2, icon.getHeight() * ICON_SCALE * 2);
		Periculum.headerFont.draw(batch, message, -camera.viewportWidth / 2 + 20, camera.viewportHeight / 2 - 10,
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
		icon.dispose();
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
			return new PlayingGameState(camera, learning);
		}
		return new LevelSelectGameState(camera);
	}

	private enum Transition {
		QUIT, RESTART;
	}

	@Override
	public void update() {
		return;
	}
}
