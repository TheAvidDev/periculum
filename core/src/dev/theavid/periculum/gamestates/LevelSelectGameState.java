package dev.theavid.periculum.gamestates;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import dev.theavid.periculum.KeyMap;
import dev.theavid.periculum.events.Event;

/**
 * The game state responsible for controlling what part of the game the player
 * can access, and a place for the player to exit the game from.
 * 
 * @author TheAvidDev
 */
// 2020-06-15 TheAvidDev - Created level select game state
public class LevelSelectGameState extends ChoosingGameState {
	public static boolean learning = true;

	public LevelSelectGameState(OrthographicCamera camera) {
		super(null, camera, Event.CHOICE, null, false, false);
		camera.zoom = 2f;
		camera.update();

		// Remove the option to select the main level
		if (learning) {
			entities = Arrays.copyOf(entities, 3);
		}

		/**
		 * Setup music.
		 */
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/loading.wav"));
		music.setLooping(true);
	}

	/**
	 * Modified to return either the instructions state or one of the playing game
	 * states.
	 */
	@Override
	public GameState getNextGameState() {
		if (chosenOption == event.getOptions()[0]) {
			return new ImageGameState(camera, "instructions1.png", 1 / 2f,
					new ImageGameState(camera, "instructions2.png", 1 / 2f,
							new ImageGameState(camera, "instructions3.png", 1 / 2f, new LevelSelectGameState(camera))));
		} else if (chosenOption == event.getOptions()[1]) {
			return new PlayingGameState(camera, true);
		} else {
			return new PlayingGameState(camera, false);
		}
	}

	/**
	 * Modified to remove the player health changes and added a check for the Quit
	 * key being pressed.
	 */
	@Override
	public void update() {
		camera.zoom = 2f;
		camera.update();
		if (chosenOption != null) {
			return;
		}
		if (KeyMap.QUIT.isPressed()) {
			Gdx.app.exit();
		}
		for (int i = 0; i < entities.length; i++) {
			if (entities[i].shouldKill()) {
				chosenOption = event.getOptions()[i - 1];
				break;
			}
		}
	}
}
