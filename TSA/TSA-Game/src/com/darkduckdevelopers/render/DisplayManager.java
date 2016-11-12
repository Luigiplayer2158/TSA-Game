package com.darkduckdevelopers.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * An interface to the LWJGL display
 * 
 * @author Zachary
 */
public class DisplayManager {

	private static long previousTime;
	private static float delta;

	/**
	 * Create a new LWJGL display
	 * 
	 * @param width
	 *            The width of the display
	 * @param height
	 *            The height of the display
	 * @param name
	 *            The title of the display
	 */
	public static void createDisplay(int width, int height, String name) {
		try {
			ContextAttribs attribs = new ContextAttribs(3, 2)
					.withForwardCompatible(true).withProfileCore(true); // Set
																		// OpenGL
																		// version
			Display.setDisplayMode(new DisplayMode(width, height)); // Set
																	// display
																	// dimensions
			Display.setTitle(name); // Set display title
			Display.create(new PixelFormat(), attribs); // Create the display
			Display.setVSyncEnabled(true); // Enable VSync
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		previousTime = Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	/**
	 * Get the previous frame time in seconds for timed movement
	 * 
	 * @return The previous frame time
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}

	/**
	 * Update the display, cap the framerate, and calculate the frame time
	 */
	public static void update() {
		Display.update(); // Refresh the display
		Display.sync(60); // Cap the framerate at 60 FPS
		// Calculate the frame time
		long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		delta = (currentTime - previousTime) / 1000f;
		previousTime = currentTime;
	}

	/**
	 * Destroy the display
	 */
	public static void cleanUp() {
		Display.destroy();
	}

}
