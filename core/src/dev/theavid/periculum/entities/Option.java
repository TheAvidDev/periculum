package dev.theavid.periculum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import dev.theavid.periculum.gamestates.ChoosingGameState;

/**
 * An option entity that a player interacts with for use in event choices.
 * 
 * @author TheAvidDev
 *
 */
//2020-06-11 TheAvidDev - Added option selection
public class Option extends Entity {
	private OrthographicCamera camera;
	private boolean isHovering = false;

	public Option(OrthographicCamera camera, float x, float y) {
		super(EntityType.OPTION, x, y);
		this.camera = camera;
	}

	public TextureRegion getTextureRegion() {
		// Get mouse position.
		Vector3 mousePos = new Vector3();
		mousePos.x = Gdx.input.getX();
		mousePos.y = Gdx.input.getY();
		mousePos.z = 0;
		camera.unproject(mousePos);

		// Check if mouse is within bounds of option.
		if (mousePos.x > getX() && mousePos.y > getY()
				&& mousePos.x < getX() + getEntityType().getWidth() * ChoosingGameState.UI_SCALE
				&& mousePos.y < getY() + getEntityType().getHeight() * ChoosingGameState.UI_SCALE) {
			isHovering = true;
		} else {
			isHovering = false;
		}
		return getTextureRegion(isHovering ? 1 : 0, 0);
	}

	@Override
	public boolean shouldKill() {
		return isHovering && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}
}