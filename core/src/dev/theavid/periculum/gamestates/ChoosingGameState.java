package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.Periculum;
import dev.theavid.periculum.entities.Entity;
import dev.theavid.periculum.entities.EntityType;
import dev.theavid.periculum.entities.Player;
import dev.theavid.periculum.events.Event;

/**
 * The game state in which the user is choosing an option from an event.
 * 
 * @author TheAvidDev
 */
// 2020-06-06 TheAvidDev - Create game state
public class ChoosingGameState extends GameState {
	private final float UI_SCALE = 8;
	private final float CAMERA_ZOOM = 2f;

	private GameState originalState;
	private Event event;
	private boolean lastEvent;
	private Entity[] popups;
	private Outcome outcome;

	private SpriteBatch batch = new SpriteBatch();

	public ChoosingGameState(GameState originalState, OrthographicCamera camera, Event event, Player player,
			boolean lastEvent) {
		super(camera);
		this.originalState = originalState;
		this.event = event;
		this.lastEvent = lastEvent;

		/**
		 * Zoom out the camera for better font rendering.
		 */
		camera.position.set(0, 0, 0);
		camera.zoom = CAMERA_ZOOM;
		camera.update();

		/**
		 * Create the background popup and the popups for options.
		 */
		popups = new Entity[event.getOptions().length + 1];
		popups[0] = new Entity(EntityType.POPUP, 0, 0);
		for (int i = 0; i < event.getOptions().length; i++) {
			popups[i + 1] = new Entity(EntityType.POPUP, 10, 10 * i);
		}
	}

	@Override
	public void update() {
		// TODO: Make this update based on option pressed.
		if (KeyMap.TRANSITION.isPressed(true)) {
			outcome = Outcome.CONTINUE;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.082f, 0.129f, 0.173f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		/**
		 * This is bad. Really bad. Horrifically horrible and bad. Terrible. But it
		 * works **for now**. This has to be fixed. TODO: Fix this mess of """code"""
		 */
		batch.draw(popups[0].getTextureRegion(), -popups[0].getEntityType().getWidth() * UI_SCALE / 2,
				-popups[0].getEntityType().getHeight() * UI_SCALE / 2, popups[0].getEntityType().getWidth() * UI_SCALE,
				popups[0].getEntityType().getHeight() * UI_SCALE);
		Periculum.headerFont.setColor(Color.WHITE);

		Periculum.headerFont.draw(batch, event.getPrompt(), 40 - popups[0].getEntityType().getWidth() * UI_SCALE / 2,
				-20 + popups[0].getEntityType().getHeight() * UI_SCALE / 2,
				popups[0].getEntityType().getWidth() * UI_SCALE - 80, Align.center, true);
		for (int i = 1; i < popups.length; i++) {
			batch.draw(popups[i].getTextureRegion(), -popups[i].getEntityType().getWidth() * UI_SCALE / 2 + 40,
					popups[i].getEntityType().getHeight() * UI_SCALE / 2
							- (popups[i].getEntityType().getHeight() * UI_SCALE / 4) * i,
					popups[i].getEntityType().getWidth() * UI_SCALE - 80,
					-popups[i].getEntityType().getHeight() * UI_SCALE / 4 + 20);
			Periculum.headerFont.draw(batch, event.getOptions()[i - 1].getName(),
					-popups[i].getEntityType().getWidth() * UI_SCALE / 2 + 40,
					popups[i].getEntityType().getHeight() * UI_SCALE / 2
							- (popups[i].getEntityType().getHeight() * UI_SCALE / 4) * i
							+ (-popups[i].getEntityType().getHeight() * UI_SCALE / 4 + 20) / 2
							+ Periculum.headerFont.getLineHeight() / 2,
					popups[i].getEntityType().getWidth() * UI_SCALE - 80, Align.center, true);
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		popups[0].dispose();
	}

	@Override
	public boolean shouldTransition() {
		return outcome != null;
	}

	@Override
	public GameState getNextGameState() {
		/**
		 * Reset the zoom back to that of the rest of the game.
		 */
		camera.zoom = 1f;
		camera.update();

		if (outcome == Outcome.WIN) {
			// TODO: return new WinGameState();
		}
		if (outcome == Outcome.LOSS) {
			// TODO: return new LossGameState();
		}
		return originalState;
	}

	/**
	 * A simple enum to determine what the next GameScene should be.
	 */
	private enum Outcome {
		CONTINUE, WIN, LOSS;
	}
}
