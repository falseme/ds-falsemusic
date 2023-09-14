package net.falseme.discord.falsemusic.bot.event;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.falseme.discord.falsemusic.bot.musicplayer.MusicManager;

/**
 * Manage differet events
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class EventListeners extends ListenerAdapter {

	/**
	 * Loads commands and inform in console
	 */
	@Override
	public void onReady(@NotNull ReadyEvent event) {

		MusicManager.load();

		System.out.println("-----------------------------------");
		System.out.println("BOT READY | Running as: " + event.getJDA().getSelfUser().getName());
		System.out.println("-----------------------------------");

	}

}
