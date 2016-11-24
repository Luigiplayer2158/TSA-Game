package com.darkduckdevelopers.util;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.Renderer;

/**
 * Load levels with the snap of a finger
 * 
 * @author Zachary
 */
public class LevelLoader {

	public static HashMap<Integer, ShapeTexture> textures = new HashMap<Integer, ShapeTexture>();

	public static void loadLevel(List<Entity> levelEntities, String levelPath,
			Loader loader, Renderer renderer) {
		String line = "weeearrrtuuuyeeepooolaaa";
		// Split line every 12 characters
		for (int i = 0; i < line.length() / 12; i++) {
			String blockData = line.substring(i * 12, i * 12 + 12);
			int[] convertedData = new int[3];
			for (int j = 0; j < 3; j++) {
				byte[] bytes = blockData.substring(j * 4, j * 4 + 4).getBytes();
				convertedData[j] = (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) // Bytes
						// to
						// int
						| ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
			}
			float xPos = Float.intBitsToFloat(convertedData[0]); // Int to float
																	// by binary
			float yPos = Float.intBitsToFloat(convertedData[1]);
			int id = convertedData[2];
			levelEntities.add(createEntity(xPos, yPos, id, loader, renderer));
		}
	}

	private static Entity createEntity(float x, float y, int id, Loader loader, Renderer renderer) {
		Entity toAdd = new Entity();
		TransformComponent transform = new TransformComponent(new Vector2f(x, y), 0f, new Vector2f(0.1f, 0.1f));
		ShapeTexture toUse;
		if (textures.get(id) != null) {
			toUse = textures.get(id);
		} else {
			String textureFile = null;
			switch(Math.abs(id)) {
			case 1: {
				textureFile = "ground.png";
			}
			default: {
				textureFile = "background.png";
			}
			}
			toUse = new ShapeTexture(loader.loadTexture(textureFile));
		}
		RenderComponent render = new RenderComponent(renderer, transform, toUse, 0, true);
		toAdd.addComponent(render);
		return toAdd;
	}
}
