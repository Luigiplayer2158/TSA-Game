package tsagame.main;

import org.lwjgl.opengl.Display;

import tsagame.render.DisplayManager;

/**
 * The main class for our game.
 * 
 * @author Zachary
 */
public class MainGameLoop {

	/**
	 * The main method. This is the entry point of code execution.
	 */
	public static void main(String[] args) {
		/** INITIALIZATION **/
		DisplayManager.createDisplay(); // Create the display

		/** LOOP **/
		while (!Display.isCloseRequested()) { // Stop looping if the display is
												// closed
			DisplayManager.update(); // Update the display
		}

		/** CLEAN UP **/
		DisplayManager.closeDisplay(); // Close display
	}

}
