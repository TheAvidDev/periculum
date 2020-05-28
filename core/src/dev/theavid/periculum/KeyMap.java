package dev.theavid.periculum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * An enum to connect multiple keys to a descriptive control. This allows
 * mapping an action to any number of keys and makes it easier to check for
 * a control in the project.
 * 
 * @author TheAvidDev
 */
// 2020-05-28 TheAvidDev - Create basic directional keymaps
public enum KeyMap {
	UP (Keys.W, Keys.UP),
	DOWN (Keys.S, Keys.DOWN),
	LEFT (Keys.A, Keys.LEFT),
	RIGHT (Keys.D, Keys.RIGHT);
	
	private int[] keys;
	
	KeyMap(int... keys) {
		this.keys = keys;
	}
	
	public boolean isPressed() {
		for (int key : keys) {
			if (Gdx.input.isKeyPressed(key)) {
				return true;
			}
		}
		return false;
	}
}
