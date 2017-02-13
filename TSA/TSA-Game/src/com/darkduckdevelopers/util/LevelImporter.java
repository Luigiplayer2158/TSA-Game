package com.darkduckdevelopers.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

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
			Loader loader, Renderer renderer) {
		// Input to byte array
		InputStream is = Class.class.getResourceAsStream(levelFile);
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

			if (texture != -1 && property != -1) {
				int gridX = i % 1000;
				int gridY = i / 1000;
				Entity e = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(gridX / 5f, gridY / 5f), 0f, new Vector2f(
								0.2f, 0.2f));
				RenderComponent render = new RenderComponent(
						renderer,
						transform,
						new ShapeTexture(
								loader.loadTexture("aliss.png")),
						0, true);
				e.addComponent(render);
				entities.add(e);
				System.out.println(gridX + ": " + gridY);
			}

		}

	}

}
