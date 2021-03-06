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
		if (levelFile.startsWith("+")) {
			is = Class.class.getResourceAsStream(levelFile.substring(1));
		} else {
			try {
				is = new FileInputStream(levelFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] bytes = new byte[256 * 256];
		try {
			is.read(bytes);
		} catch (IOException e) {
			System.err.println("There was a problem reading " + levelFile
					+ ". It's possible the file doesn't exist.");
			e.printStackTrace();
		}

		// Create entity for 4 bytes
		for (int i = 0; i < bytes.length / 4; i++) {
			int texture = bytes[i * 4 + 2];
			if (texture != 0) {
				int gridX = bytes[i * 4];
				int gridY = bytes[i * 4 + 1];
				int property = bytes[i * 4 + 3];
				texture = textureLookup(texture, loader);
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
	private static int textureLookup(int id, Loader loader) {
		ShapeTexture texture = textures.get(id);
		if (texture == null) {
			String textureFile = PropertiesFile.getProperty("texture_" + id);
			if (textureFile != null) {
				texture = new ShapeTexture(loader.loadTexture(textureFile));
				textures.put(id, texture);
			} else {
				// Load a null texture if the texture doesn't exit
				id = -1;
				if (textures.get(-1) == null) {
					texture = new ShapeTexture(loader.loadTexture(PropertiesFile.getProperty("texture_null")));
					textures.put(-1, texture);
					System.out.println(texture);
				}
			}
		}
		return id;
	}

}
