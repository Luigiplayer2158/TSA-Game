package com.darkduckdevelopers.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.CollideComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.Renderer;

/**
 * A class that can import a level file
 * 
 * @author Ethan
 * @author Zachary
 */
public class LevelImporter {

	private static HashMap<Integer, ShapeTexture> textures = new HashMap<Integer, ShapeTexture>();

	public static void loadLevel(List<Entity> entities, String levelFile,
			Loader loader, Renderer renderer, float unitSize, float gravity) {
		// Input to byte array
		levelFile = levelFile.replace('\\', '/');
		System.out.println(levelFile);
		InputStream is = null;
		try {
			is = new FileInputStream(levelFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = new byte[4000004];
		try {
			is.read(bytes);
		} catch (IOException e) {
			System.err.println("There was a problem reading " + levelFile
					+ ". It's possible the file doesn't exist.");
			e.printStackTrace();
		}

		// Create entity for 4 bytes
		for (int i = 0; i < bytes.length / 4; i++) {
			int property = (bytes[i * 4]);
			int texture = (bytes[i * 4 + 1]);
			texture = texture | (bytes[i * 4 + 2] << 8);
			texture = texture | (bytes[i * 4 + 3] << 16);

			if (texture != -1 && property != -1 && i != 0) {
				textureLookup(texture, loader);
				int gridX = (i - 900) / 1000;
				int gridY = (i - 900) % 1000;
				Entity e = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(gridX * unitSize * 2, gridY * unitSize * 2),
						0f, new Vector2f(unitSize, unitSize));
				RenderComponent render = new RenderComponent(renderer,
						transform, textures.get(texture), 0, true);
				if (property < 4) {
					CollideComponent collider = new CollideComponent(transform,
							property, gravity, new Vector2f(unitSize, unitSize));
					if (property == 0) {
						collider.gravity = 0f;
					}
					e.addComponent(collider);
				}
				e.addComponent(render);
				entities.add(e);
			}

		}
	}

	// Lookup a texture, if it isn't there, put one there
	private static void textureLookup(int id, Loader loader) {
		ShapeTexture texture = textures.get(id);
		if (texture == null) {
			String textureFile = PropertiesFile.getProperty("texture_" + id);
			texture = new ShapeTexture(loader.loadTexture(textureFile));
			textures.put(id, texture);
		}
	}

}
