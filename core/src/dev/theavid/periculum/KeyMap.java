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
// 2020-05-28 TheAvidDev - Add option to require control key to be pressed
// 2020-05-28 TheAvidDev - Create basic directional keymaps
public enum KeyMap {
	UP (Keys.W, Keys.UP),
	DOWN (Keys.S, Keys.DOWN),
	LEFT (Keys.A, Keys.LEFT),
	RIGHT (Keys.D, Keys.RIGHT),
	CONTROL (Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT),
	DEBUG (true, Keys.D);
	
	private int[] keys;
	private boolean ctrl;

	KeyMap(int... keys) {
		this(false, keys);
	}
	
	KeyMap(boolean ctrl, int... keys) {
		this.ctrl = ctrl;
		this.keys = keys;
	}
	
	/**
	 * Check if any key in a key map is pressed. It also has functionality to
	 * check if either of the control (ctrl) keys are pressed, presuming the
	 * key map requires it for activation.
	 *
	 * @return whether any key in the key map is pressed
	 */
	public boolean isPressed(boolean justPressed) {
		if (ctrl && !KeyMap.CONTROL.isPressed()) {
			return false;
		}
		for (int key : keys) {
			if ((justPressed && Gdx.input.isKeyJustPressed(key))
					|| (!justPressed && Gdx.input.isKeyPressed(key))) {
				return true;
			}
		}
		return false;
	}

	public boolean isPressed() { return isPressed(false); }
}
