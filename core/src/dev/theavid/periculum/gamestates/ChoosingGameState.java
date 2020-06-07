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
	private final float UI_PROMPT_OFFSET_Y = 20;
	private final float UI_PROMPT_OFFSET_X = 40;
	private final float UI_OPTION_OFFSET_X = 40;
	private final float UI_OPTION_TEXT_OFFSET_X = 40;

	private GameState originalState;
	private Event event;
	private boolean lastEvent;
	private Entity[] entities;
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
		entities = new Entity[event.getOptions().length + 1];
		entities[0] = new Entity(EntityType.POPUP, 0, 0);
		for (int i = 0; i < event.getOptions().length; i++) {
			entities[i + 1] = new Entity(EntityType.POPUP, 10, 10 * i);
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
		 * Draw the background popup.
		 */
		EntityType entityType = entities[0].getEntityType();
		float x = -entityType.getWidth() * UI_SCALE / 2;
		float y = -entityType.getHeight() * UI_SCALE / 2;
		float width = -x * 2;
		float height = -y * 2;
		batch.draw(entities[0].getTextureRegion(), x, y, width, height);
		Periculum.headerFont.setColor(Color.WHITE);

		/**
		 * Draw the prompt with the following format:
		 *  _________________
		 * |                 | <- I_PROMPT_OFFSET_Y
		 * |----  prompt ----|
		 * |                 |
		 * |                 |
		 * |_________________|
		 * |----|       |----|
		 *   /\           /\
		 *  UI_PROMPT_OFFSET_X
		 */
		x = UI_PROMPT_OFFSET_X - entityType.getWidth() * UI_SCALE / 2;
		y = -UI_PROMPT_OFFSET_Y + entityType.getHeight() * UI_SCALE / 2;
		width = entityType.getWidth() * UI_SCALE - 2 * UI_PROMPT_OFFSET_X;
		Periculum.headerFont.draw(batch, event.getPrompt(), x, y, width, Align.center, true);

		/**
		 * Format the option entity and text with the following format:
		 *
		 *  ________________________
		 * |                        |
		 * |         prompt         |
		 * |   __________________   |
		 * |--|-----option 1-----|--| <- UI_OPTION_OFFSET_X
		 * |   __________________   |
		 * |--|-----option 2-----|--| <- UI_OPTION_OFFSET_X
		 * |                        |
		 * |                        |
		 * |________________________|
		 * |--|-----|      |-----|--| <- UI_OPTION_OFFSET_X
		 *       /\          /\
		 *   UI_OPTION_TEXT_OFFSET_X
		 */
		for (int i = 1; i < entities.length; i++) {
			entityType = entities[i].getEntityType();
			x = -entityType.getWidth() / 2 * UI_SCALE + UI_OPTION_OFFSET_X;
			y = entityType.getHeight() / 2 * UI_SCALE - (entityType.getHeight() * UI_SCALE / 4) * (i + 1);
			width = entityType.getWidth() * UI_SCALE - 2 * UI_OPTION_OFFSET_X;
			height = entityType.getHeight() * UI_SCALE / 4 - 20;
			batch.draw(entities[i].getTextureRegion(), x, y, width, height);

			x = -entityType.getWidth() / 2 * UI_SCALE + UI_OPTION_TEXT_OFFSET_X;
			y = entityType.getHeight() / 2 * UI_SCALE - (entityType.getHeight() * UI_SCALE / 4) * i - height / 2
					- Periculum.headerFont.getLineHeight() / 2;
			width = entityType.getWidth() * UI_SCALE - 2 * UI_OPTION_TEXT_OFFSET_X;
			Periculum.headerFont.draw(batch, event.getOptions()[i - 1].getName(), x, y, width, Align.center, true);
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		entities[0].dispose();
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
			// TODO: return new DeathGameState(...);
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
