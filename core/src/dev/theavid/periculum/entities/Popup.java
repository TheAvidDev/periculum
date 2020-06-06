package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.theavid.periculum.events.Event;
import dev.theavid.periculum.events.Option;

// TODO: Add comments
public class Popup extends Entity {

	private Event event;

	public Popup(Event event) {
		super(EntityType.POPUP, 0, 0);
		this.event = event;
	}

	public TextureRegion getTextureRegion() {
		// TODO: Draw the prompt and it's options
		// Even better, make a separate ComplexEntity that has a draw method with some
		// fancy~ stuff
		return super.getTextureRegion();
	}

	public Option getSelectedOption() {
		// TODO: Return selected option
		return null;
	}

	public boolean shouldKill() {
		// TODO: Detect click here
		return false;
	}
}
