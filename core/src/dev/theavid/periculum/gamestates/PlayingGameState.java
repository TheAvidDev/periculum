package dev.theavid.periculum.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.theavid.periculum.Debugger;
import dev.theavid.periculum.Level;
import dev.theavid.periculum.entities.Entity;
import dev.theavid.periculum.entities.EntityType;
import dev.theavid.periculum.entities.Notifier;
import dev.theavid.periculum.entities.Player;
import dev.theavid.periculum.entities.Popup;
import dev.theavid.periculum.events.DeathOption;
import dev.theavid.periculum.events.Event;
import dev.theavid.periculum.events.Option;

/**
 * The main state in which the player plays with a level and moving camera.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-04 hirundinidae - Added new GameState methods
// 2020-06-04 TheAvidDev - Switched to entity list management system
// 2020-05-30 TheAvidDev - Switched to a game state
// 2020-05-29 TheAvidDev - Decrease occurrences of black lines between tiles
// 2020-05-29 TheAvidDev - Render background and foreground layers separately
// 2020-05-29 TheAvidDev - Allow window resizing and scale definitions
// 2020-05-28 hirundinidae - Create camera and its movement based on player
// 2020-05-28 TheAvidDev - Clean up code, removed libGDX defaults
// 2020-05-27 hirundinidae - Add level creation and rendering
public class PlayingGameState extends GameState {
	public static Debugger debugger;
	public static ArrayList<Entity> entityList = new ArrayList<Entity>();
	public static Level level;

	private SpriteBatch batch = new SpriteBatch();
	private ArrayList<FullEvent> eventList = new ArrayList<FullEvent>();
	private DeathOption deathOption;

	public PlayingGameState(OrthographicCamera camera) {
		super(camera);
		// The Player entity is always the first object in the entityList
		entityList.add(new Player(1260, 1010));
		level = new Level();
		debugger = new Debugger(getPlayer(), level, camera);

		eventList.add(new FullEvent(Event.COMPLETE_ISOLATION, new Vector2(1200, 1000)));
		eventList.add(new FullEvent(Event.COMPLETE_ISOLATION, new Vector2(1300, 1000)));

		// Kickstart the game by adding a first event
		entityList.add(new Notifier(eventList.get(0).getX(), eventList.get(0).getY()));
	}

	@Override
	public void update() {
		for (int i = entityList.size() - 1; i >= 0; i--) {
			entityList.get(i).update();
			if (entityList.get(i).shouldKill()) {
				Entity entity = entityList.get(i);
				entityList.remove(i);

				if (entity instanceof Notifier) {
					entityList.add(new Popup(eventList.get(0).getEvent()));
					eventList.remove(0);
					if (eventList.size() > 0) {
						entityList.add(new Notifier(eventList.get(0).getX(), eventList.get(0).getY()));
					}
					/**
					 * If there are no more events, we just won't do anything and wait for the game
					 * state to transition to a win or loss, depending on whether a DeathOption has
					 * been assigned.
					 */
				}
				if (entity instanceof Popup) {
					Option selectedOption = ((Popup) entity).getSelectedOption();
					// TODO: Change the player's healths
					if (selectedOption instanceof DeathOption) {
						deathOption = (DeathOption) selectedOption;
					}
				}

				entity = null;
			}
		}

		/**
		 * Rounding to tenth of a pixel removes extremely common black lines between
		 * tiles. However, this doesn't fix them on all resolutions.
		 */
		camera.position.set(Math.round(getPlayer().getX() * 10f) / 10f, Math.round(getPlayer().getY() * 10f) / 10f, 0);
		camera.update();
		debugger.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.506f, 0.984f, 0.294f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		level.renderBackground(camera);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = entityList.size() - 1; i >= 0; i--) {
			batch.draw(entityList.get(i).getTextureRegion(), entityList.get(i).getX(), entityList.get(i).getY());
		}
		batch.end();

		level.renderForeground(camera);
		debugger.render();
	}

	@Override
	public void dispose() {
		for (Entity entity : entityList) {
			entity.dispose();
		}
		for (EntityType entityType : EntityType.values()) {
			entityType.dispose();
		}
		batch.dispose();
		level.dispose();
	}

	/**
	 * The first entity in the entityList will always be the player, so this method
	 * is just a shortcut in case of any potential changes.
	 *
	 * @return the Player entity
	 */
	private Player getPlayer() {
		return (Player) entityList.get(0);
	}

	@Override
	public boolean shouldTransistion() {
		return deathOption != null || eventList.size() == 0;
	}

	@Override
	public GameState getNextGameState() {
		if (deathOption != null) {
			// TODO: return new GameOverState(deathOption...);
		}
		// TODO: return new WinState?
		return null;
	}

	class FullEvent {
		private Event event;
		private Vector2 location;

		public FullEvent(Event event, Vector2 location) {
			this.event = event;
			this.location = location;
		}

		public Event getEvent() {
			return event;
		}

		public float getX() {
			return location.x;
		}

		public float getY() {
			return location.y;
		}
	}
}
