package net.falseme.discord.falsemusic.ui.event;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * The MainView (FXML) Event Controller. <br>
 * MouseEvents, WindowEvents...
 * 
 * @author Falseme (Fabricio Tomás).
 */
public class MainViewController extends WindowController {

	private Scene nextScene;
	private String botColor;

	public Label windowLabel;
	public Button colorPickerButton;
	public TextField botToken;

	/**
	 * Creates an event controller for a given WindowStage and a Scene used to
	 * change it later.
	 */
	public MainViewController(Stage window, Scene nextScene) {
		super(window);
		this.nextScene = nextScene;
	}

	/**
	 * Initialize some MainView component properties.
	 */
	public void load() {

		Image image = new Image(getClass().getResourceAsStream("/images/icon.png"));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(32);
		imageView.setFitHeight(32);
		windowLabel.setGraphic(imageView);

		botColor = "#F36F38";
		colorPickerButton.setStyle("-fx-background-color:" + botColor);

	}

	/**
	 * Called when the "Run" button is pressed
	 */
	public void onRunButtonClicked() {

		String token = botToken.getText().trim();

		window.setScene(nextScene);
		window.setX(window.getX() - 50);
		window.setY(window.getY() - 50);
		window.sizeToScene();

//		BotEvent.onStart(token);

	}

}
