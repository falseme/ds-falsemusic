package net.falseme.discord.falsemusic.bot.command;

import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Command Base Interface
 */
public interface Command {

	/**
	 * The command name. Used inside Discord for the execution. eg. /help
	 */
	public String getName();

	/**
	 * The command description. Shown in Discord as a help message for the user.
	 */
	public String getDescription();

	/**
	 * A list of params the command use. null in case no param is needed.
	 */
	public List<OptionData> getParams();

	/**
	 * The command execution. Activated by a Command Interaction.
	 * 
	 * @param event The command interaction event.
	 */
	public void execute(SlashCommandInteractionEvent event);

}
