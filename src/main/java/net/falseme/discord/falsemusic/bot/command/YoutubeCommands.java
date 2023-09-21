package net.falseme.discord.falsemusic.bot.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.falseme.discord.falsemusic.bot.event.EventListeners;
import net.falseme.discord.falsemusic.bot.youtube.YoutubeRequest;
import net.falseme.discord.falsemusic.bot.youtube.YoutubeResponse;

/**
 * Load and Manage all the youtube related commands using subclasses
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class YoutubeCommands {

	/**
	 * Loads the command list given by subclasses
	 * 
	 * @return A linkedlist containing every command so as to be loaded later.
	 * 
	 * @see EventListeners
	 */
	public static List<Command> loadCommands() {

		List<Command> commands = new LinkedList<>();

		Search search = new Search();
		commands.add(search);

		return commands;

	}

	/**
	 * Search song command.<br>
	 * /search [song name] <br>
	 * It uses the youtube data api to search a song.
	 * 
	 * @author Falseme (Fabricio Tomás)
	 * 
	 * @see YoutubeRequest
	 */
	public static class Search implements Command {

		@Override
		public String getName() {
			return "search";
		}

		@Override
		public String getDescription() {
			return "Search for youtube videos";
		}

		@Override
		public List<OptionData> getParams() {
			List<OptionData> params = new ArrayList<>();
			params.add(new OptionData(OptionType.STRING, "search", "The video you want to search", true));
			return params;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			event.deferReply().queue();

			try {

				String searchQuery = event.getOption("search").getAsString();
				YoutubeResponse youtubeResponse = YoutubeRequest.executeQuery(searchQuery, event.getGuild());

				event.getHook().sendMessageEmbeds(youtubeResponse.getEmbedMessages()).queue();

			} catch (IOException e) {
				e.printStackTrace();
				event.getHook().sendMessage("Exception while searching for a youtube video. " + e.getMessage()).queue();
			}

		}

	}

}
