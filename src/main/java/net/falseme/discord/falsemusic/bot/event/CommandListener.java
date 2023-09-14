package net.falseme.discord.falsemusic.bot.event;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.falseme.discord.falsemusic.bot.command.Command;
import net.falseme.discord.falsemusic.bot.command.MusicCommands;

/**
 * Listens to different command interactions
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class CommandListener extends ListenerAdapter {

	private List<Command> commands = new ArrayList<>();

	/**
	 * Buils a listener by a given command list.
	 * 
	 * @param commands The list of commands to be loaded.
	 * @see MusicCommands
	 */
	public CommandListener(List<Command> commands) {
		this.commands.addAll(commands);
	}

	/**
	 * Loads the whole command list on every guild
	 * 
	 * @param event an OnReady event
	 */
	public void loadCommandsOnReady(ReadyEvent event) {

		List<Guild> guilds = event.getJDA().getGuilds();

		for (Guild guild : guilds) {
			for (Command command : commands) {

				if (command.getParams() == null)
					guild.upsertCommand(command.getName(), command.getDescription()).queue();
				else
					guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getParams())
							.queue();

			}
		}

	}

	/**
	 * Executes a command after an interaction event
	 * 
	 * @param event The interaction event
	 */
	public void executeCommandOnInteraction(SlashCommandInteractionEvent event) {

		for (Command command : commands) {

			if (command.getName().equals(event.getName())) {
				command.execute(event);
				return;
			}

		}

	}

	/**
	 * Adds a command to the command list
	 * 
	 * @param command the command instance
	 */
	public void addCommand(Command command) {
		commands.add(command);
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		loadCommandsOnReady(event);
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		executeCommandOnInteraction(event);
	}

}
