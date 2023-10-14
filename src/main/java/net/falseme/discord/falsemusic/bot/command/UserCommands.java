package net.falseme.discord.falsemusic.bot.command;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.falseme.discord.falsemusic.bot.event.EventListeners;

/**
 * Load and Manage User utilities commands
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class UserCommands {

	/**
	 * Loads the command list given by subclasses
	 * 
	 * @return A linkedlist containing every command so as to be loaded later.
	 * 
	 * @see EventListeners
	 */
	public static List<Command> loadCommands() {

		List<Command> commands = new LinkedList<>();

		commands.add(new Help());

		return commands;

	}

	/**
	 * Help user command.<br>
	 * /help <br>
	 * Shows the command list and other usefull information
	 * 
	 * @author Falseme (Fabricio Tomás)
	 */
	public static class Help implements Command {

		@Override
		public String getName() {
			return "help";
		}

		@Override
		public String getDescription() {
			return "show help";
		}

		@Override
		public List<OptionData> getParams() {
			return null;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(new Color(0x0084ff));
			eb.setTitle("User Help");
			eb.setDescription("Command List");
			eb.addField("► `/search [name]`", "*Search on Youtube for song/video by a given name*", false);
			eb.addField("► `/play [song]`", "*Play a song by a given _url_ or _number_*", false);
			eb.addField("► `/stop`", "*Stop the current song and empty the playlist*", false);
			eb.addField("► `/skip`", "*Skip the current song*", false);
			eb.addField("► `/leave`", "*Leave the voice channel & delete the current playlist*", false);
			eb.addField("", "", false);
			eb.addField("Support", "You can support this bot hosting on `Kofi` ||https://ko-fi.com/falseme||", false);

			event.replyEmbeds(eb.build()).queue();

		}

	}

}
