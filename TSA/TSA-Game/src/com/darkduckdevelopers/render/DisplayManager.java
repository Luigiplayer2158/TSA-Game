package com.darkduckdevelopers.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static long previousTime;
	private static float delta;

	public static void createDisplay(int width, int height, String name) {
		try {
			ContextAttribs attribs = new ContextAttribs(3, 2)
					.withForwardCompatible(true).withProfileCore(true);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(name);
			Display.create(new PixelFormat(), attribs);
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void update() {
		Display.update();
		Display.sync(60);
		long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		if (previousTime == 0) {
			previousTime = currentTime;
		}
		delta = (currentTime - previousTime) / 1000f;
		previousTime = currentTime;
	}

	public static void cleanUp() {
		Display.destroy();
	}

}
