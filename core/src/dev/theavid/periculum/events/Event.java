package dev.theavid.periculum.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * An event that can occur in the game and requires the user to make a decision
 * which affects their infection risk and mental stability.
 * 
 * @author TheAvidDev
 */
// 2020-06-16 hirundinidae - Added learning events 
// 2020-06-12 hirundinidae - Added events 
// 2020-06-05 TheAvidDev - Created event enum
public enum Event {
	COMPLETE_ISOLATION("isolate.txt"),
	CASH("cash.txt"),
	CLOTHES("clothes.txt"),
	COUGH("cough.txt"),
	DESK("desk.txt"),
	DRINK("drink.txt"),
	FLU("flu.txt"),
	FRIDGE("fridge.txt"),
	FRIEND("friend.txt"),
	KEVIN("kevin.txt"),
	MASK("mask.txt"),
	MOVIE("movie.txt"),
	SINK("sink.txt"),
	SNEEZE("sneeze.txt"),	
	WASH_HANDS("washhands.txt"),
	DORM("dorm.txt"),
	WASHROOM("washroom.txt"),
	POND("pond.txt"),
	SCHOOL("school.txt"),
	CONFIRM("confirm.txt");

	private String prompt;
	private EventOption[] options;

	private Event(String filename) {
		/**
		 * Attempt to open the file in the first place.
		 */
		FileHandle handle = Gdx.files.local("events/" + filename);
		String[] file = handle.readString().split("\n");

		/**
		 * Read the prompt of this Event.
		 */
		prompt = file[0];

		/**
		 * Get the number of options that this Event has.
		 */
		int length;
		try {
			length = Integer.parseInt(file[1].trim());
		} catch (NumberFormatException e) {
			System.out.println(
					"Second line of Event definition file '" + filename + "' must be the integer number of choices.");
			e.printStackTrace();
			Gdx.app.exit();
			return;
		}

		/**
		 * Read in all Options or subclasses thereof. For a generic {@link Option}, the
		 * format is as follows:
		 * 
		 * <pre>
		 * String | Float | Float
		 * </pre>
		 * 
		 * The first String is the name of this option. The first float is the effect
		 * this option has on infection risk. The second float is the effect this option
		 * has on mental stability. Any amount of leading and trailing whitespace is
		 * allowed.
		 * 
		 * The DeathOption has the following format:
		 * 
		 * <pre>
		 * String | Float | Float | String | String
		 * </pre>
		 * 
		 * The first three options are the same, with the second String being the death
		 * message and the third String being the filename to read the death icon from.
		 * `events/' will be prepended to this filename. Once again, Any amount of
		 * leading and trailing whitespace is allowed.
		 */
		options = new EventOption[length];
		for (int i = 0; i < length; i++) {
			String[] tokens;
			tokens = file[i + 2].trim().split("\\|");

			/**
			 * Create the new Option depending on token length.
			 */
			if (tokens.length == 3) {
				options[i] = new EventOption(tokens[0], Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
			} else if (tokens.length == 5) {
				options[i] = new DeathOption(tokens[0], Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						tokens[3], tokens[4]);
			} else {
				System.out.println("Invalid token length for Event definition file '" + filename + "' option " + i);
				Gdx.app.exit();
				return;
			}
		}
	}

	public String getPrompt() {
		return prompt;
	}

	public EventOption[] getOptions() {
		return options;
	}
}
