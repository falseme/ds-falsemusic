package net.falseme.discord.falsemusic.bot;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.falseme.discord.falsemusic.bot.command.Command;
import net.falseme.discord.falsemusic.bot.command.MusicCommands;
import net.falseme.discord.falsemusic.bot.command.UserCommands;
import net.falseme.discord.falsemusic.bot.command.YoutubeCommands;
import net.falseme.discord.falsemusic.bot.event.CommandListener;
import net.falseme.discord.falsemusic.bot.event.EventListeners;
import net.falseme.discord.falsemusic.env.Env;

/**
 * Main app class
 * 
 * @author Falseme (Fabricio Tomás)
 */
@SpringBootApplication
public class Main {

	/**
	 * Main app method <br>
	 * Builds the bot and loads settings, commands and listeners
	 */
	public static void main(String[] args) {

		SpringApplication.run(Main.class, args);

		try {
			Env.load();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		JDA jdabot = JDABuilder.createDefault(Env.get("DISCORD_TOKEN")).build();

		jdabot.getPresence().setActivity(Activity.listening("10 hours of absolute silence (the original)"));

		jdabot.addEventListener(new EventListeners());
		List<Command> commandList = MusicCommands.loadCommands();
		commandList.addAll(UserCommands.loadCommands());
		commandList.addAll(YoutubeCommands.loadCommands());
		for (Command c : commandList) {
			System.out.println(c.getName());
		}
		jdabot.addEventListener(new CommandListener(commandList));

		try {
			jdabot.awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
