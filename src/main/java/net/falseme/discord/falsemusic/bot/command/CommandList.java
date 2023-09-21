package net.falseme.discord.falsemusic.bot.command;

import java.util.List;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Some static methods to check status
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class CommandList {

	public static List<Command> loadCommands() {
		return null;
	}

	/**
	 * Used to check if the bot is able to execute some commands.
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

		if (!memberVoiceState.inAudioChannel()) {
			event.reply("Must be in a voice channel").queue();
			return false;
		}

		if (!botVoiceState.inAudioChannel()) {
			event.reply("Bot isn't in the voice channel. Use `/play` to let it join").queue();
			return false;
		}

		if (memberVoiceState.getChannel() != botVoiceState.getChannel()) {
			event.reply("Bot isn't in the same voice channel as you").queue();
			return false;
		}

		return true;

	}

	/**
	 * Used to check if the bot is able to play a song.
	 * 
	 * @param event The command interaction event.
	 * @return True if the bot is in the same voice channel or if it could join to
	 *         it. False if user isn't in a voice channel or bot is in another voice
	 *         channel.
	 */
	public static boolean ableToPlayCheck(SlashCommandInteractionEvent event) {

		Member member = event.getMember();

		if (!member.getVoiceState().inAudioChannel()) {
			event.reply("Must be in a Voice Channel").queue();
			return false;
		}

		Member botMember = event.getGuild().getSelfMember();
		GuildVoiceState botVoiceState = botMember.getVoiceState();

		if (!botVoiceState.inAudioChannel()) {
			event.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel()); // join
		} else if (botVoiceState.getChannel() != member.getVoiceState().getChannel()) {
			event.reply("Must be in the same channel as bot. Use `/leave` or move to the same channel").queue();
			return false;
		}

		return true;

	}

}
