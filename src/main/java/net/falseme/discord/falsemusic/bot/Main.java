package net.falseme.discord.falsemusic.bot;

import net.falseme.discord.falsemusic.bot.event.BotEvent;
import net.falseme.discord.falsemusic.env.Env;

/**
 * Main app class
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class Main {

	/**
	 * Main app method <br>
	 * Builds the bot and loads settings, commands and listeners
	 */
	public static void main(String[] args) {

		try {
			Env.load(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		try {
			BotEvent.launch(Env.get("DISCORD_TOKEN"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
