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
	 * Load api keys using main method args
	 * 
	 * @throws Exception if couldn't load
	 */
	public static void load(String... vars) throws Exception {

		System.out.println("LOADING ENV VARIABLES USING ARGS...\n");

		for (String s : vars) {
			String[] key_var = s.split("=");
			keys.put(key_var[0], key_var[1]);
			System.out.println(key_var[0] + " : " + key_var[1]);
		}

		String[] keylist = new String[] { "DISCORD_TOKEN", "YOUTUBE_API_KEY", "YT_OAUTH_CLIENT", "YT_OAUTH_SECRET" };

		for (String s : keylist) {
			if (keys.get(s) == null || keys.get(s).isEmpty())
				throw new Exception("COULDN'T LOAD ENV VAR: " + s);
		}

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
