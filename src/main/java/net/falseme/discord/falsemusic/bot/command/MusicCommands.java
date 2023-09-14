package net.falseme.discord.falsemusic.bot.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

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

		return commands;

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

		String url = event.getOption("url").getAsString();
		MusicManager.play(event.getGuild(), url);
		event.reply("play: " + url).queue();

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

		Member member = event.getMember();
		GuildVoiceState memberVoiceState = member.getVoiceState();

		Member botMember = event.getGuild().getSelfMember();
		GuildVoiceState botVoiceState = botMember.getVoiceState();

		if (!memberVoiceState.inAudioChannel() || !botVoiceState.inAudioChannel()
				|| memberVoiceState.getChannel() != botVoiceState.getChannel()) {

			event.reply("I won't tell you why it doesn't work").queue();
			return;

		}

		AudioPlayList audioPlayList = MusicManager.getMusicManager(event.getGuild()).getAudioPlayList();
		audioPlayList.getQueue().clear();
		audioPlayList.getAudioPlayer().stopTrack();
		event.reply("Stop").queue();

	}

}
