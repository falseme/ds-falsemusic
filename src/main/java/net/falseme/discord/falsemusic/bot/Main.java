package net.falseme.discord.falsemusic.bot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.falseme.discord.falsemusic.env.Env;
import net.falseme.discord.falsemusic.ui.event.ListenViewController;
import net.falseme.discord.falsemusic.ui.event.MainViewController;

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

		try {
			Env.load();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		launch(args);

	}

	/**
	 * Starts the JavaFX Application
	 */
	@Override
	public void start(Stage stage) throws Exception {

		stage.initStyle(StageStyle.UNDECORATED);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/listenView.fxml"));
		ListenViewController listenController = new ListenViewController(stage);
		loader.setController(listenController);
		Parent sceneRoot = loader.load();
		Scene scene = new Scene(sceneRoot);

		loader = new FXMLLoader(getClass().getResource("/scenes/mainView.fxml"));
		MainViewController controller = new MainViewController(stage, scene);
		loader.setController(controller);
		Parent root = loader.load();
		stage.setScene(new Scene(root));
		stage.sizeToScene();
		stage.show();
		controller.load();

	}

}
