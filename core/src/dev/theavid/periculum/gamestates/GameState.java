package dev.theavid.periculum.gamestates;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A generic game state which contains an orthographic camera.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-13 TheAvidDev - Added music
// 2020-06-04 hirundinidae - Added methods shouldTransistion() and getNextGameState()
// 2020-05-30 TheAvidDev - Created abstract game state
public abstract class GameState {
	protected OrthographicCamera camera;
	protected Music music;

	public GameState(OrthographicCamera camera) {
		this.camera = camera;
	}

	/**
	 * Update any physics based calculations or general state of this GameState.
	 */
	public abstract void update();

	/**
	 * Draw any required objects of this GameState.
	 */
	public abstract void render();

	/**
	 * Disposes all resources associated with this GameState.
	 */
	public abstract void dispose();

	/**
	 * Whether this GameState should cease to exist and transition to another
	 * GameState.
	 * 
	 * @return boolean value indicating if the a transition should occur.
	 */
	public abstract boolean shouldTransition();

	/**
	 * Get the next game state to transition to. This function will be called
	 * exactly once and only if shouldTransition returned true. Therefore, it should
	 * return a new instance of a GameState, preventing a situation where more than
	 * one GameState is currently initialized.
	 * 
	 * @return next GameState to transition to
	 */
	public abstract GameState getNextGameState();

	public void setMusicVolume(float volume) {
		if (music != null) {
			/**
			 * LibGDX will crash spectacularly when trying to play music that has a negative
			 * volume. Why it doesn't crash on getting set in the first place is beyond me.
			 */
			music.setVolume(Math.max(0, volume));
		}
	}

	/**
	 * Start playing the music from the start or from previous pause point.
	 */
	public void playMusic() {
		if (music != null && !music.isPlaying()) {
			music.play();
		}
	}

	/**
	 * Pause the music so it can be resumed later.
	 */
	public void pauseMusic() {
		if (music != null) {
			music.pause();
		}
	}

	/**
	 * Stop and dispose of the music.
	 */
	public void stopMusic() {
		if (music != null) {
			music.stop();
			music.dispose();
		}
	}
}
