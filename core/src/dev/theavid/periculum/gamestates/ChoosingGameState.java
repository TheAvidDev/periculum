package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import dev.theavid.periculum.Periculum;
import dev.theavid.periculum.entities.Entity;
import dev.theavid.periculum.entities.EntityType;
import dev.theavid.periculum.entities.Option;
import dev.theavid.periculum.entities.Player;
import dev.theavid.periculum.events.DeathOption;
import dev.theavid.periculum.events.Event;
import dev.theavid.periculum.events.EventOption;

/**
 * The game state in which the user is choosing an option from an event.
 * 
 * @author TheAvidDev
 */
// 2020-06-11 TheAvidDev - Added option selection
// 2020-06-06 TheAvidDev - Create game state
public class ChoosingGameState extends GameState {
	public final static float UI_SCALE = 8;
	private final float UI_PROMPT_OFFSET_Y = 20;
	private final float UI_PROMPT_OFFSET_X = 40;
	private final float UI_OPTION_OFFSET_X = 40;

	private GameState originalState;
	private Event event;
	private Player player;
	private boolean lastEvent;

	private EventOption chosenOption;
	private Entity[] entities;

	private SpriteBatch batch = new SpriteBatch();

	public ChoosingGameState(GameState originalState, OrthographicCamera camera, Event event, Player player,
			boolean lastEvent) {
		super(camera);
		this.originalState = originalState;
		this.event = event;
		this.player = player;
		this.lastEvent = lastEvent;

		/**
		 * Zoom out the camera for better font rendering.
		 */
		camera.position.set(0, 0, 0);
		camera.zoom = 2f;
		camera.update();

		/**
		 * Create the background popup and the popups for options.
		 */
		entities = new Entity[event.getOptions().length + 1];
		entities[0] = new Entity(EntityType.POPUP, 0, 0);
		for (int i = 0; i < event.getOptions().length; i++) {
			entities[i + 1] = new Option(camera,
					(-EntityType.POPUP.getWidth() / 2
							+ (EntityType.POPUP.getWidth() - EntityType.OPTION.getWidth()) / 2) * UI_SCALE,
					(((EntityType.OPTION.getHeight() + 1) * -i) + 3) * UI_SCALE);
		}
	}

	/**
	 * Check whether the player has clicked on an option and if so, update the
	 * player data accordingly.
	 */
	@Override
	public void update() {
		if (chosenOption != null) {
			return;
		}
		for (int i = 0; i < entities.length; i++) {
			if (entities[i].shouldKill()) {
				chosenOption = event.getOptions()[i - 1];
				player.changeInfectionRisk(chosenOption.getInfectionRateEffect());
				player.changeMentalStability(chosenOption.getMentalStabilityEffect());
				break;
			}
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
		float width = entityType.getWidth() * UI_SCALE;
		float height = entityType.getHeight() * UI_SCALE;
		batch.draw(entities[0].getTextureRegion(), x, y, width, height);
		Periculum.headerFont.setColor(Color.WHITE);

		/**
		 * Draw the prompt.
		 */
		x = UI_PROMPT_OFFSET_X - entityType.getWidth() * UI_SCALE / 2;
		y = -UI_PROMPT_OFFSET_Y + entityType.getHeight() * UI_SCALE / 2;
		width = entityType.getWidth() * UI_SCALE - 2 * UI_PROMPT_OFFSET_X;
		Periculum.headerFont.draw(batch, event.getPrompt(), x, y, width, Align.center, true);

		/**
		 * Format the option entity and text.
		 */
		for (int i = 1; i < entities.length; i++) {
			entityType = entities[i].getEntityType();
			width = entityType.getWidth() * UI_SCALE;
			height = entityType.getHeight() * UI_SCALE;
			batch.draw(entities[i].getTextureRegion(), entities[i].getX(), entities[i].getY(), width, height);

			x = entities[i].getX() + UI_OPTION_OFFSET_X;
			y = entities[i].getY() + entityType.getHeight() * UI_SCALE - entityType.getHeight() * UI_SCALE / 4;
			width = entityType.getWidth() * UI_SCALE - 2 * UI_OPTION_OFFSET_X;
			Periculum.headerFont.draw(batch, event.getOptions()[i - 1].getName(), x, y, width, Align.center, true);
		}
		batch.end();
	}

	@Override
	public void dispose() {
		camera.zoom = 1f;
		camera.update();
		batch.dispose();
		for (Entity entity : entities) {
			entity.dispose();
		}
	}

	@Override
	public boolean shouldTransition() {
		return chosenOption != null;
	}

	/**
	 * Determine whether the player continues playing, dies from selecting an option
	 * that instantly kills, dies from selecting an option that brings their health
	 * to a loss, or wins by surviving with health.
	 */
	@Override
	public GameState getNextGameState() {
		if (chosenOption instanceof DeathOption) {
			return new EndGameState(camera, ((DeathOption) chosenOption));
		} else {
			if (player.shouldKill()) {
				return new EndGameState(camera, true);
			} else if (lastEvent) {
				return new EndGameState(camera, false);
			}
		}
		return originalState;
	}
}
