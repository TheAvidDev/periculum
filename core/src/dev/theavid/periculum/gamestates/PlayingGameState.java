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
import dev.theavid.periculum.entities.Notifier;
import dev.theavid.periculum.entities.Player;
import dev.theavid.periculum.events.Event;

/**
 * The main state in which the player plays with a level and moving camera.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-13 TheAvidDev - Added learning level distinction
// 2020-06-12 hirundinidae - Added Events in desired order to eventList
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
	public static ArrayList<Entity> entityList;
	public static ArrayList<FullEvent> eventList;
	public static Level level;

	private SpriteBatch batch = new SpriteBatch();
	private boolean makingChoice = false;
	private boolean learning;

	public PlayingGameState(OrthographicCamera camera, boolean learning) {
		super(camera);
		entityList = new ArrayList<Entity>();
		eventList = new ArrayList<FullEvent>();
		// The Player entity is always the first object in the entityList
		entityList.add(new Player(1260, 1010));
		level = new Level();
		debugger = new Debugger(getPlayer(), level, camera);
		this.learning = learning;

		if (this.learning) {
			// TODO: replace with learning events
			eventList.add(new FullEvent(Event.COMPLETE_ISOLATION, new Vector2(1351, 980)));
		} else {
			eventList.add(new FullEvent(Event.FRIDGE, new Vector2(1354, 949)));
			eventList.add(new FullEvent(Event.CASH, new Vector2(1351, 949)));
			eventList.add(new FullEvent(Event.MASK, new Vector2(1382, 883)));
			eventList.add(new FullEvent(Event.SNEEZE, new Vector2(1272, 776)));
			eventList.add(new FullEvent(Event.FLU, new Vector2(1079, 795)));
			eventList.add(new FullEvent(Event.DESK, new Vector2(1031, 882)));
			eventList.add(new FullEvent(Event.WASH_HANDS, new Vector2(915, 1034)));
			eventList.add(new FullEvent(Event.KEVIN, new Vector2(947, 886)));
			eventList.add(new FullEvent(Event.MOVIE, new Vector2(1141, 792)));
			eventList.add(new FullEvent(Event.DRINK, new Vector2(1321, 775)));
			eventList.add(new FullEvent(Event.CLOTHES, new Vector2(1385, 881)));
			eventList.add(new FullEvent(Event.SINK, new Vector2(1384, 1038)));
			eventList.add(new FullEvent(Event.COMPLETE_ISOLATION, new Vector2(1351, 980)));
			eventList.add(new FullEvent(Event.FRIEND, new Vector2(1291, 1018)));
		}

		// Kickstart the game by adding the first event
		entityList.add(new Notifier(eventList.get(0).getX(), eventList.get(0).getY()));
	}

	@Override
	public void update() {
		if (makingChoice) {
			return;
		}
		for (int i = entityList.size() - 1; i >= 0; i--) {
			entityList.get(i).update();
			if (entityList.get(i).shouldKill()) {
				/**
				 * If we find that a Notifier wants to be removed, we know that the player is
				 * attempting to make a choice. We also don't remove it so that we can hide it's
				 * removal during the scene switch.
				 */
				if (entityList.get(i) instanceof Notifier) {
					makingChoice = true;
					continue;
				}
				entityList.remove(i);
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

		for (Entity entity : entityList) {
			entity.additionalRender(camera);
		}
		debugger.render();
	}

	@Override
	public void dispose() {
		for (Entity entity : entityList) {
			entity.dispose();
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
	public boolean shouldTransition() {
		return makingChoice;
	}

	@Override
	public GameState getNextGameState() {
		makingChoice = false;
		Event currentEvent = eventList.get(0).getEvent();
		eventList.remove(0);

		/**
		 * This is quite aggresive and removes all Notifiers but there should really
		 * only be one, so cleaning up extra ones is fine.
		 */
		for (int i = entityList.size() - 1; i >= 0; i--) {
			if (entityList.get(i) instanceof Notifier) {
				entityList.remove(i);
			}
		}

		/**
		 * If this is not the last event, we will create a Notifier for the next event,
		 * otherwise doing nothing other than telling the ChoosingGameState that no
		 * future events exist so the next GameState should either be a win or loss.
		 */
		boolean isLastEvent = eventList.size() == 0;
		if (!isLastEvent) {
			entityList.add(new Notifier(eventList.get(0).getX(), eventList.get(0).getY()));
		}
		return new ChoosingGameState(this, camera, currentEvent, (Player) getPlayer(), isLastEvent, learning);
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
