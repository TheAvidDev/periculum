package dev.theavid.periculum.events;

/**
 * 
 * 
 * @author TheAvidDev
 */
// 2020-06-05 TheAvidDev - Created Option class
public class Option {
	private String name;
	private float infectionRateEffect;
	private float mentalStabilityEffect;

	/**
	 * Creates a new option for an Event.
	 * 
	 * @param name                  the name of this option
	 * @param infectionRateEffect   the effect this option has on infection risk
	 * @param mentalStabilityEffect the effect this option has on mental stability
	 */
	public Option(String name, float infectionRateEffect, float mentalStabilityEffect) {
		this.name = name;
		this.infectionRateEffect = infectionRateEffect;
		this.mentalStabilityEffect = mentalStabilityEffect;
	}

	public String getName() {
		return name;
	}

	public float getInfectionRateEffect() {
		return infectionRateEffect;
	}

	public float getMentalStabilityEffect() {
		return mentalStabilityEffect;
	}
}
