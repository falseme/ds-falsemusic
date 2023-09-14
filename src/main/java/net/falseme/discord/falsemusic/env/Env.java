package net.falseme.discord.falsemusic.env;

import java.util.HashMap;
import java.util.Map;

/*
 * Load & retrieve environment variables
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class Env {

	private static Map<String, String> keys = new HashMap<>();

	public static boolean load() {

		System.out.println("LOADING ENV VARIABLES...");

		String[] keylist = new String[] { "DISCORD_TOKEN" };

		for (String s : keylist) {
			keys.put(s, System.getenv(s));
			if (keys.get(s) == null || keys.get(s).isEmpty())
				assert false : "Could not load env variables";
		}

		return true;

	}

	/*
	 * Get an environment variable by the given key.
	 * 
	 * @param key
	 * 
	 * @return the environment variable
	 */
	public static String get(String key) {

		if (keys.containsKey(key))
			return keys.get(key);
		else
			return null;

	}

}
