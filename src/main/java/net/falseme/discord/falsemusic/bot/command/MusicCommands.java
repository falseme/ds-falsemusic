package net.falseme.discord.falsemusic.bot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.falseme.discord.falsemusic.bot.event.EventListeners;
import net.falseme.discord.falsemusic.bot.musicplayer.AudioPlayList;
import net.falseme.discord.falsemusic.bot.musicplayer.MusicManager;

/**
 * Load and Manage all the music related commands
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
	 * Extra method used to check some conditions before executing a command.
	 * 
	 * @param event The command interaction event
	 * @return True if User and Bot are connected to the same voice channel. False
	 *         if one of them is not connected to a voice channel or it's on another
	 *         one.
	 */
	public static boolean sameVoiceChannelCheck(SlashCommandInteractionEvent event) {

		Member member = event.getMember();
		GuildVoiceState memberVoiceState = member.getVoiceState();

		Member botMember = event.getGuild().getSelfMember();
		GuildVoiceState botVoiceState = botMember.getVoiceState();

		if (!memberVoiceState.inAudioChannel() || !botVoiceState.inAudioChannel()
				|| memberVoiceState.getChannel() != botVoiceState.getChannel()) {

			return false;

		}

		return true;

	}

}

/**
 * Play song command.<br>
 * /play [url] <br>
 * It uses the MusicManager to play a song for a specific Guild.
 * 
 * @author Falseme (Fabricio Tomás)
 * 
 * @see MusicManager.play
 */
class Play implements Command {

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
		params.add(new OptionData(OptionType.STRING, "url", "The song url", true));
		return params;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {

		Member member = event.getMember();

		if (!member.getVoiceState().inAudioChannel()) {

			event.reply("Must be in a Voice Channel").queue();
			return;

		}

		Member botMember = event.getGuild().getSelfMember();
		GuildVoiceState botVoiceState = botMember.getVoiceState();

		if (!botVoiceState.inAudioChannel()) {

			event.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel()); // join

		} else if (botVoiceState.getChannel() != member.getVoiceState().getChannel()) {

			event.reply("Must be in the same channel as bot. You can disconnect it or move to the same channel")
					.queue();
			return;

		}

		event.deferReply().queue();

		String url = event.getOption("url").getAsString();
		MusicManager.play(event.getGuild(), url);

		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(0xfe6906));

		boolean isplaylistempty = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList().getQueue()
				.isEmpty();
		boolean songplaying = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList().getAudioPlayer()
				.getPlayingTrack() != null;
		if (!isplaylistempty || songplaying)
			eb.addField("Song added to current playlist!", "`url: " + url + "`", false);
		else
			eb.addField("Playing song!", "`url: " + url + "`", false);

		int index = url.indexOf("?v=") + 3;
		eb.setThumbnail("https://i.ytimg.com/vi/" + url.substring(index) + "/default.jpg");

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
class Stop implements Command {

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

		if (!MusicCommands.sameVoiceChannelCheck(event)) {
			event.reply("I won't tell you why it doesn't work >:(").queue();
			return;
		}

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
class Skip implements Command {

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

		if (!MusicCommands.sameVoiceChannelCheck(event)) {
			event.reply("I won't tell you why it doesn't work >:(").queue();
			return;
		}

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
class Leave implements Command {

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

		if (!MusicCommands.sameVoiceChannelCheck(event)) {
			event.reply("I won't tell you why it doesn't work >:(").queue();
			return;
		}

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