package net.falseme.discord.falsemusic.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainViewController {

	private Stage window;
	private double windowX, windowY;

	public Label windowLabel;
	public Button colorPickerButton;
	private String botColor;

	public MainViewController(Stage window) {

		this.window = window;

	}

	public void onWindowClose() {
		window.close();
	}

	public void onWindowMinimize() {
		window.setIconified(true);
	}

	public void onWindowBarPressed(MouseEvent event) {

		windowX = event.getSceneX();
		windowY = event.getSceneY();

	}

	public void onWindowBarDragged(MouseEvent event) {

		if (event.getButton() != MouseButton.PRIMARY)
			return;

		window.setX(event.getScreenX() - windowX);
		window.setY(event.getScreenY() - windowY);

	}

	public void load() {

		Image image = new Image(getClass().getResourceAsStream("/images/icon.png"));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(32);
		imageView.setFitHeight(32);
		windowLabel.setGraphic(imageView);

		botColor = "#F36F38";
		System.out.println(colorPickerButton);
		colorPickerButton.setStyle("-fx-background-color:" + botColor);
//		colorPickerButton.setBackground(new Background(
//				new BackgroundFill(Paint.valueOf(botColor), new CornerRadii(0), colorPickerButton.getInsets())));

	}

}
