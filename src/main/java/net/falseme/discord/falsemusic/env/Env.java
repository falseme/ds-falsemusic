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

	/**
	 * Load api keys
	 * 
	 * @return True if could load
	 * @throws Exception if couldn't load
	 */
	public static boolean load() throws Exception {

		System.out.println("LOADING ENV VARIABLES...");

		String[] keylist = new String[] { "DISCORD_TOKEN", "YOUTUBE_API_KEY", "YT_OAUTH_CLIENT", "YT_OAUTH_SECRET" };

		for (String s : keylist) {
			keys.put(s, System.getenv(s));
			if (keys.get(s) == null || keys.get(s).isEmpty())
				throw new Exception("COULDN'T LOAD ENV VAR: " + s);
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
