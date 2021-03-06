package dev.theavid.periculum.events;

/**
 * An option which causes a game ending if picked by the player.
 * 
 * @author TheAvidDev
 */
// 2020-06-05 TheAviDev - Created DeathOption class
public class DeathOption extends EventOption {
	private String deathMessage;
	private String deathIconFilename;

	/**
	 * Creates a regular Option with two extra parameters needed for a death screen.
	 * 
	 * @param deathMessage      the message displayed to the user on death
	 * @param deathIconFilename the name of the file housing the icon for the death
	 *                          screen
	 */
	public DeathOption(String name, float infectionRateEffect, float mentalStabilityEffect, String deathMessage,
			String deathIconFilename) {
		super(name, infectionRateEffect, mentalStabilityEffect);
		this.deathMessage = deathMessage;
		this.deathIconFilename = deathIconFilename;
	}

	public String getDeathMessage() {
		return deathMessage;
	}

	public String getDeathIconFilename() {
		return deathIconFilename;
	}
}
