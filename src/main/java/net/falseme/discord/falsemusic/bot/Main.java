package net.falseme.discord.falsemusic.bot;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import net.falseme.discord.falsemusic.ui.MainViewController;

/**
 * Main app class
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class Main extends Application {

	/**
	 * Main app method <br>
	 * Builds the bot and loads settings, commands and listeners
	 */
	public static void main(String[] args) {

		launch(args);

		boolean test = true;
		if (test)
			return;

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
	 * Starts the JavaFX Application
	 */
	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/mainView.fxml"));
		MainViewController controller = new MainViewController(stage);
		loader.setController(controller);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		controller.load();

	}

}
