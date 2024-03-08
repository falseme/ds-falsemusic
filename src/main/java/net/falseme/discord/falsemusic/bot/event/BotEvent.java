package net.falseme.discord.falsemusic.bot.event;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.falseme.discord.falsemusic.bot.command.Command;
import net.falseme.discord.falsemusic.bot.command.MusicCommands;
import net.falseme.discord.falsemusic.bot.command.UserCommands;
import net.falseme.discord.falsemusic.bot.command.YoutubeCommands;
import net.falseme.discord.falsemusic.bot.musicplayer.MusicManager;

/**
 * Manage all the Bot Events
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class BotEvent extends ListenerAdapter {

	/*
	 * Starts the bot, set pressence, event listeners and chat commands
	 */
	public static void onStart(@NotNull String token) {

		JDA jdabot = JDABuilder.createDefault(token).build();

		jdabot.getPresence().setActivity(Activity.listening("10 hours of absolute silence"));

		jdabot.addEventListener(new BotEvent());
		List<Command> commandList = MusicCommands.loadCommands();
		commandList.addAll(UserCommands.loadCommands());
		commandList.addAll(YoutubeCommands.loadCommands());
		for (Command c : commandList) {
			System.out.println("Loaded Command: " + c.getName());
		}
		jdabot.addEventListener(new CommandListener(commandList));

		try {
			jdabot.awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Informs the bot is ready and loads the Music Manager
	 */
	@Override
	public void onReady(@NotNull ReadyEvent event) {

		MusicManager.load();

		System.out.println("-----------------------------------");
		System.out.println("BOT READY | Running as: " + event.getJDA().getSelfUser().getName());
		System.out.println("-----------------------------------");

	}

}
