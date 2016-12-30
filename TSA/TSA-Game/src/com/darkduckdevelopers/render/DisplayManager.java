package com.darkduckdevelopers.render;

import java.awt.Dimension;
import java.awt.Toolkit;

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
	private static int width;
	private static int height;

	/**
	 * Create a new LWJGL display
	 * 
	 * @param width
	 *            The width of the display when windowed
	 * @param height
	 *            The height of the display when windowed
	 * @param name
	 *            The title of the display
	 * @param fullscreen
	 *            Whether or not the display is fullscreen
	 */
	public static void createDisplay(int width, int height, String name,
			boolean fullscreen) {
		try {
			ContextAttribs attribs = new ContextAttribs(3, 2)
					.withForwardCompatible(true).withProfileCore(true); // Set
																		// OpenGL
																		// version
			DisplayManager.width = width;
			DisplayManager.height = height;
			setFullscreen(fullscreen);
			Display.setTitle(name); // Set display title
			Display.create(new PixelFormat(), attribs); // Create the
																	// display
			Display.setVSyncEnabled(true); // Enable VSync
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		previousTime = Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	/**
	 * Set whether or not the display is fullscreen
	 * 
	 * @param fullscreen
	 *            To fullscreen or not to fullscreen
	 * @throws LWJGLException
	 *             Exception from display modes
	 */
	public static void setFullscreen(boolean fullscreen) throws LWJGLException {
		if (!fullscreen) {
			Display.setDisplayMode(new DisplayMode(width, height)); // Non-fullscreen
		} else {
			DisplayMode[] displayModes = Display.getAvailableDisplayModes(); // Get
																				// display
																				// modes
																				// possible
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Get
																				// monitor
																				// size
			for (DisplayMode dm : displayModes) { // Cycle through modes
				if (screenSize.height == dm.getHeight()
						&& screenSize.width == dm.getWidth()) {
					Display.setDisplayMode(dm); // Set fullscreen mode
					Display.setFullscreen(true); // Tell the OS to remove
													// borders
				}
			}
		}
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
