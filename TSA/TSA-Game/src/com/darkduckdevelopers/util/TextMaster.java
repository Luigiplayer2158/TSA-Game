package com.darkduckdevelopers.util;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.Renderer;

/**
 * A class for generating text
 * 
 * @author Zachary
 */
public class TextMaster {

	private ShapeTexture fontTexture;
	private Renderer renderer;
	private EntityKiller killer;

	public TextMaster(Loader loader, Renderer renderer, EntityKiller killer, String propertiesIndex) {
		this.renderer = renderer;
		this.killer = killer;
		fontTexture = new ShapeTexture(loader.loadTexture(PropertiesFile
				.getProperty(propertiesIndex)));
		fontTexture.setNumberOfRows(8);
	}

	public void drawText(String text, float charSize, float x, float y,
			boolean useVM, int deathIndex, int lineLength, List<Entity> entities) {
		text = text.toLowerCase();
		char[] characters = text.toCharArray();
		float xPointer = x;
		float yPointer = y;
		int linePointer = 0;
		for (char c : characters) {
			if (c != ' ') {
				Entity toAdd = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(xPointer, yPointer), 0f, new Vector2f(
								charSize, charSize));
				RenderComponent render = new RenderComponent(renderer,
						transform, fontTexture, getIndex(c), useVM);
				killer.addToHitList(deathIndex, toAdd);
				toAdd.addComponent(render);
				entities.add(toAdd);
			}
			xPointer += charSize * 2f;
			linePointer++;
			if (linePointer >= lineLength) {
				yPointer -= charSize * 2f;
				xPointer = x;
			}
		}
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
