package tsagame.render;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * A class for interacting with the display. WARNING! Intense LWJGL code below!
 * 
 * @author Zachary
 */
public class DisplayManager {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	public static final int MAX_FPS = 90;
	public static float frameTime;
	private static long previousTime;

	/**
	 * Create the display
	 */
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true).withProfileCore(true); // Set the OpenGL version to use
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); // Set the width and height of the display
			Display.create(new PixelFormat(), attribs); // Create the OpenGL display
		} catch (Exception e) {
			System.err.println("Failed to create display");
		}
		previousTime = System.currentTimeMillis(); // Prevent teleporting on initialization
		GL11.glViewport(0, 0, WIDTH, HEIGHT); // Tell OpenGL the part of the screen to use
	}

	/**
	 * Update the display
	 */
	public static void update() {
		Display.update(); // Draw to the screen
		Display.sync(MAX_FPS); // Cap the game speed
		frameTime = frameTimeSeconds(); // Calculate previous frame time
	}

	/**
	 * Close the display
	 */
	public static void closeDisplay() {
		Display.destroy(); // Close the display and delete associated assets
	}

	/**
	 * Calculate the frame time in seconds
	 * 
	 * @return The frame time
	 */
	private static float frameTimeSeconds() {
		long currentTime = System.currentTimeMillis();
		float difference = currentTime - previousTime;
		previousTime = currentTime;
		difference /= 1000f;
		return difference;
	}

}
