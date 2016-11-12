package com.darkduckdevelopers.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.Renderer;

/**
 * A class that generates text to render
 * 
 * @author Zachary
 */
public class Text {

	// TODO clean this up because component arch makes this weird

	private static ShapeTexture textTexture;
	private static Renderer renderer;

	/**
	 * Initialize the texture and the renderer
	 * 
	 * @param loader
	 *            The loader to use
	 * @param renderer
	 *            The renderer to create components for
	 */
	public static void initShape(Loader loader, Renderer renderer) {
		ShapeTexture texture = new ShapeTexture(
				loader.loadTexture(PropertiesFile.getProperty("file_font")));
		texture.setNumberOfRows(8);
		textTexture = texture;
		Text.renderer = renderer;
	}

	// TODO comment this behemoth
	public static List<Entity> getGlyphs(String text, float x, float y,
			float depth, float charSize, boolean useVM) {
		List<Entity> glyphs = new ArrayList<Entity>();
		String lowerCased = text.toLowerCase();
		char[] characters = lowerCased.toCharArray();
		float currentX = x;
		for (char c : characters) {
			if (c == ' ') {
				currentX += charSize * 2;
			} else {
				Entity toAdd = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(currentX, y), 0f, new Vector2f(charSize,
								charSize));
				RenderComponent render = new RenderComponent(renderer,
						transform, textTexture, getIndex(c), useVM);
				toAdd.addComponent(render);
				glyphs.add(toAdd);
				currentX += charSize * 2;
			}
		}
		return glyphs;
	}

	/**
	 * Get the texture atlas index of a character. By default it will be ?
	 * 
	 * @param c
	 *            The character to index
	 * @return The id of the index
	 */
	private static int getIndex(char c) {
		int index = 39;
		if (c == 'a') {
			index = 0;
		} else if (c == 'b') {
			index = 1;
		} else if (c == 'c') {
			index = 2;
		} else if (c == 'd') {
			index = 3;
		} else if (c == 'e') {
			index = 4;
		} else if (c == 'f') {
			index = 5;
		} else if (c == 'g') {
			index = 6;
		} else if (c == 'h') {
			index = 7;
		} else if (c == 'i') {
			index = 8;
		} else if (c == 'j') {
			index = 9;
		} else if (c == 'k') {
			index = 10;
		} else if (c == 'l') {
			index = 11;
		} else if (c == 'm') {
			index = 12;
		} else if (c == 'n') {
			index = 13;
		} else if (c == 'o') {
			index = 14;
		} else if (c == 'p') {
			index = 15;
		} else if (c == 'q') {
			index = 16;
		} else if (c == 'r') {
			index = 17;
		} else if (c == 's') {
			index = 18;
		} else if (c == 't') {
			index = 19;
		} else if (c == 'u') {
			index = 20;
		} else if (c == 'v') {
			index = 21;
		} else if (c == 'w') {
			index = 22;
		} else if (c == 'x') {
			index = 23;
		} else if (c == 'y') {
			index = 24;
		} else if (c == 'z') {
			index = 25;
		} else if (c == '1') {
			index = 26;
		} else if (c == '2') {
			index = 27;
		} else if (c == '3') {
			index = 28;
		} else if (c == '4') {
			index = 29;
		} else if (c == '5') {
			index = 30;
		} else if (c == '6') {
			index = 31;
		} else if (c == '7') {
			index = 32;
		} else if (c == '8') {
			index = 33;
		} else if (c == '9') {
			index = 34;
		} else if (c == '0') {
			index = 35;
		} else if (c == '-') {
			index = 36;
		} else if (c == ':') {
			index = 37;
		} else if (c == '!') {
			index = 38;
		} else if (c == '.') {
			index = 40;
		}
		return index;
	}
}
