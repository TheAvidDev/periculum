package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import dev.theavid.periculum.entities.EntityType;
import dev.theavid.periculum.gamestates.GameState;
import dev.theavid.periculum.gamestates.ImageGameState;
import dev.theavid.periculum.gamestates.PlayingGameState;

/**
 * Main game class which handles game states and the transitions between them.
 *
 * @author hirundinidae
 * @author TheAvidDev
 */
// 2020-06-13 TheAvidDev - Made sure to dispose of game states when switching
// 2020-06-04 hirundinidae - Updated some aspects of game switching 
// 2020-06-04 hirundinidae - Updated comment for game state transitions  
// 2020-05-30 TheAvidDev - Switched to a game state based approach
public class Periculum extends ApplicationAdapter {
	private final float TRANSITION_SPEED = 1.25f; // relative to 1 second
	private final int TILE_WIDTH = 16;
	private final int VIEWPORT_WIDTH = 16;

	public static BitmapFont headerFont;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private GameState currentGameState;
	private float transitionCounter = -1;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 320, 320 * (4 / 3));
		shapeRenderer = new ShapeRenderer();
		currentGameState = new ImageGameState(camera, "logo_name.png", 1 / 6f,
				new ImageGameState(camera, "instructions1.png", 1 / 2f,
					new ImageGameState(camera, "instructions2.png", 1 / 2f,
						new ImageGameState(camera, "instructions3.png", 1 / 2f,
							new PlayingGameState(camera, true)))));
		currentGameState.playMusic();

		/**
		 * Create main font of default 16 point size.
		 */
		FileHandle fontFile = Gdx.files.internal("unifont.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		headerFont = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render() {
		if (KeyMap.DEBUG_SKIP_TUTORIAL.isPressed()) {
			transitionCounter = -1;
			currentGameState.dispose();
			currentGameState = new PlayingGameState(camera, false);
		}

		currentGameState.update();
		currentGameState.render();

		/**
		 * Update the transition counter and transition to the playing game state if the
		 * SPACE key is pressed.
		 */
		if (transitionCounter != 0 || currentGameState.shouldTransition()) {
			transitionCounter += Gdx.graphics.getDeltaTime() * TRANSITION_SPEED;
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, Math.abs(transitionCounter));
			shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();
			currentGameState.setMusicVolume(1-Math.abs(transitionCounter));
			Gdx.gl.glDisable(GL20.GL_BLEND);
			if (transitionCounter - Gdx.graphics.getDeltaTime() * TRANSITION_SPEED < 0 && transitionCounter > 0) {
				transitionCounter = 0;
			}
		}
		if (transitionCounter >= 1) {
			transitionCounter = -1;
			/**
			 * The PlayingGameState is one that can get transitioned back to, so we delegate
			 * the responsibility of cleaning it up to whatever may transition back to it.
			 * This should be changed if more states get transitioned in and out of.
			 */
			if (!(currentGameState instanceof PlayingGameState)) {
				currentGameState.dispose();
				currentGameState.stopMusic();
			}

			currentGameState = currentGameState.getNextGameState();
			currentGameState.playMusic();
		}
	}

	@Override
	public void dispose() {
		currentGameState.dispose();
		headerFont.dispose();
		for (EntityType entityType : EntityType.values()) {
			entityType.dispose();
		}
	}

	/**
	 * This method resizes the camera's viewport while maintaining scale to allow
	 * any window size. It will base the scale off of the shortest side length, so
	 * ultra wide screens won't be unnecessarily zoomed in.
	 */
	@Override
	public void resize(int width, int height) {
		if ((float) width / (float) height > (float) height / (float) width) {
			float scale = (float) width / (float) height;
			camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH * scale;
			camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH;
		} else {
			float scale = (float) height / (float) width;
			camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH;
			camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH * scale;
		}
		camera.update();
	}
}
