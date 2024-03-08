package net.falseme.discord.falsemusic.ui.event;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowController {

	protected Stage window;
	protected double windowX, windowY;

	/**
	 * Creates a WindowController for a given WindowStage.
	 * 
	 * @param window The javafx window stage
	 */
	public WindowController(Stage window) {
		
		this.window = window;
	
	}

	/**
	 * Called when the close button is pressed
	 */
	public void onWindowClose() {
		window.close();
	}

	/**
	 * Called when the minimize button is pressed
	 */
	public void onWindowMinimize() {
		window.setIconified(true);
	}

	/**
	 * Called the mouse button is pressed on the window bar
	 */
	public void onWindowBarPressed(MouseEvent event) {

		windowX = event.getSceneX();
		windowY = event.getSceneY();

	}

	/**
	 * Called when mouse is dragged on the window bar. Used to move it.
	 */
	public void onWindowBarDragged(MouseEvent event) {

		if (event.getButton() != MouseButton.PRIMARY)
			return;

		window.setX(event.getScreenX() - windowX);
		window.setY(event.getScreenY() - windowY);

	}

}
