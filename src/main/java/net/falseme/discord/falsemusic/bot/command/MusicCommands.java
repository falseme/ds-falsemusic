package net.falseme.discord.falsemusic.bot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.falseme.discord.falsemusic.bot.event.EventListeners;
import net.falseme.discord.falsemusic.bot.musicplayer.AudioPlayList;
import net.falseme.discord.falsemusic.bot.musicplayer.MusicManager;
import net.falseme.discord.falsemusic.bot.youtube.YoutubeRequest;
import net.falseme.discord.falsemusic.bot.youtube.YoutubeResponse;

/**
 * Load and Manage all the music related commands using subclasses
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class MusicCommands {

	/**
	 * Loads the command list given by subclasses
	 * 
	 * @return A linkedlist containing every command so as to be loaded later.
	 * 
	 * @see EventListeners
	 */
	public static List<Command> loadCommands() {

		List<Command> commands = new LinkedList<>();

		commands.add(new Play());
		commands.add(new Stop());
		commands.add(new Skip());
		commands.add(new Leave());

		return commands;

	}

	/**
	 * Play song command.<br>
	 * /play [url] <br>
	 * It uses the MusicManager to play a song for a specific Guild.
	 * 
	 * @author Falseme (Fabricio Tomás)
	 * 
	 * @see MusicManager
	 */
	public static class Play implements Command {

		@Override
		public String getName() {
			return "play";
		}

		@Override
		public String getDescription() {
			return "play a song by the given url";
		}

		@Override
		public List<OptionData> getParams() {
			List<OptionData> params = new ArrayList<>();
			params.add(new OptionData(OptionType.STRING, "song",
					"The song url or the number if you made a search before", true));
			return params;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			if (!CommandList.ableToPlayCheck(event))
				return;

			event.deferReply().queue();

			// SONG URL OR NUMBER
			String url = event.getOption("song").getAsString();
			String author = null, songName = null, thumbnail = null;

			// CHECK IF IS A NUMBER AND REPRODUCE THE NUMBER OF THE SONG SEARCHED
			int songIndex = IsANumber.check(url);
			// keep in mind that user will use '1' as the first number.
			// always use '-1' when getting listed data
			if (songIndex > 0 && songIndex <= YoutubeRequest.MAX_VID_SEARCH) {

				YoutubeResponse response = YoutubeRequest.get(event.getGuild());
				if (response == null) {
					event.getHook().sendMessage("Please use `/search` before").queue();
					return;
				}
				url = response.getUrl(songIndex - 1);
				author = response.getAuthor(songIndex - 1);
				songName = response.getSongName(songIndex - 1);
				thumbnail = response.getThumbnail(songIndex - 1);

			} else if (songIndex > YoutubeRequest.MAX_VID_SEARCH) {

				event.getHook().sendMessage(
						String.format("Please only use numbers between %d and %d", 1, YoutubeRequest.MAX_VID_SEARCH))
						.queue();

			} else if (url == null || url.isEmpty() || !url.startsWith("https://www.youtube.com/watch?v=")) {
				// no need to check songIndex. It is '-1'

				event.getHook().sendMessage("Please enter a valid URL").queue();
				return;

			}

			// PLAY THE SONG USING THE URL
			MusicManager.play(event.getGuild(), url);

			// EMBED MESSAGE
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(new Color(0xfe6906));

			boolean isplaylistempty = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList().getQueue()
					.isEmpty();
			boolean songplaying = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList().getAudioPlayer()
					.getPlayingTrack() != null;

			String fieldTitle = (!isplaylistempty || songplaying) ? "Song added to current playlist!" : "Playing song!";
			String fieldDesc = (songName == null) ? String.format("`url: %s`", url)
					: String.format("%s: %s", author, songName);

			eb.addField(fieldTitle, fieldDesc, false); // title & songName/url
			if (author != null)
				eb.addField(author, url, false); // author & url

			// CHECK NULL THUMBNAIL URL
			int index = url.indexOf("?v=") + 3;
			if (thumbnail == null)
				thumbnail = "https://i.ytimg.com/vi/" + url.substring(index) + "/default.jpg";
			eb.setThumbnail(thumbnail); // thumbnail image

			event.getHook().sendMessageEmbeds(eb.build()).queue();

		}

	}

	/**
	 * Stop song command.<br>
	 * /stop <br>
	 * It stops the whole song playlist on a specific Guild.
	 * 
	 * @author Falseme (Fabricio Tomás)
	 * 
	 * @see AudioPlaylist
	 */
	public static class Stop implements Command {

		@Override
		public String getName() {
			return "stop";
		}

		@Override
		public String getDescription() {
			return "Stop the current song";
		}

		@Override
		public List<OptionData> getParams() {
			return null;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			if (!CommandList.sameVoiceChannelCheck(event))
				return;

			AudioPlayList audioPlayList = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList();
			audioPlayList.getQueue().clear();
			audioPlayList.getAudioPlayer().stopTrack();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(new Color(0xfe6906));
			eb.addField("Stopped music!", "The whole playlist was ereased", false);

			event.reply("").addEmbeds(eb.build()).queue();

		}

	}

	/**
	 * Skip song command.<br>
	 * /skip <br>
	 * Skips the current song and let the next one play.
	 * 
	 * @author Falseme (Fabricio Tomás)
	 * 
	 * @see AudioPlaylist
	 */
	public static class Skip implements Command {

		@Override
		public String getName() {
			return "skip";
		}

		@Override
		public String getDescription() {
			return "Skips the current song and plays the next one";
		}

		@Override
		public List<OptionData> getParams() {
			return null;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			if (!CommandList.sameVoiceChannelCheck(event))
				return;

			MusicManager musicManager = MusicManager.getMusicManager(event.getGuild());
			boolean playlistempty = musicManager.getAudioPlayList().getQueue().isEmpty();
			musicManager.getAudioPlayList().getAudioPlayer().stopTrack();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(new Color(0xfe6906));
			if (playlistempty)
				eb.addField("Song Skipped!", "The playlist is empty. Use `/play` to add a new song", false);
			else
				eb.addField("Song Skipped!", "Playing the next one...", false);

			event.reply("").addEmbeds(eb.build()).queue();

		}

	}

	/**
	 * Leave song command.<br>
	 * /leave <br>
	 * Leaves the voice channel and clears the playlist queue.
	 * 
	 * @author Falseme (Fabricio Tomás)
	 * 
	 * @see AudioPlaylist
	 */
	public static class Leave implements Command {

		@Override
		public String getName() {
			return "leave";
		}

		@Override
		public String getDescription() {
			return "leave the voice channel";
		}

		@Override
		public List<OptionData> getParams() {
			return null;
		}

		@Override
		public void execute(SlashCommandInteractionEvent event) {

			if (!CommandList.sameVoiceChannelCheck(event))
				return;

			AudioPlayList audioPlayList = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList();
			audioPlayList.getQueue().clear();
			audioPlayList.getAudioPlayer().stopTrack();

			event.getGuild().getAudioManager().closeAudioConnection();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(new Color(0xfe6906));
			eb.addField("Left voice channel!", "", false);

			event.reply("").addEmbeds(eb.build()).queue();

		}

	}

}

class IsANumber {

	public static int check(String s) {
		try {
			int n = Integer.valueOf(s);
			return n;
		} catch (Exception ex) {
			return -1;
		}
	}

}