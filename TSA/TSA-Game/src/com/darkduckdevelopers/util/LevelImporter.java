package com.darkduckdevelopers.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
			Loader loader, Renderer renderer) {
		// Create a new reader
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(
					Class.class.getResourceAsStream(levelFile));
			reader = new BufferedReader(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Read through file
		String line = "";
		while (line != null) {
			try {
				line = reader.readLine();
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Create a 9 x N 2D array and store tile information
			char[] string_chars = line.toCharArray();
			byte[][] bytes = new byte[string_chars.length / 9][9];
			for (int i = 0; i < (string_chars.length / 9); i++) {
				byte[] tile = bytes[i];
				// X bits to Java
				float x;
				int x_as_int = (tile[0] & 0xFF) | ((tile[1] & 0xFF) << 8)
						| ((tile[2] & 0xFF) << 16) | ((tile[3] & 0xFF) << 24);
				x = Float.intBitsToFloat(x_as_int);
				// Y bits to Java
				float y;
				int y_as_int = (tile[4] & 0xFF) | ((tile[5] & 0xFF) << 8)
						| ((tile[6] & 0xFF) << 16) | ((tile[7] & 0xFF) << 24);
				y = Float.intBitsToFloat(y_as_int);
				// ID bits to Java
				int id = tile[8] & 0xFF;

				// Create the entity
				Entity e = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(0.2f * x, 0.2f * y), 0f, new Vector2f(
								0.1f, 0.1f));
				
				// Add specific ID components
				e = addIdComponents(id, e, transform, loader, renderer);

				// Send to entity list
				entities.add(e);
			}
		}
	}

	/**
	 * KEY FOR ENTITY IDENTIFICATION
	 * 
	 * ID % 5 = PHYSICS TYPE ID / 5 = TEXTURE ID
	 */
	private static Entity addIdComponents(int id, Entity e, TransformComponent transform, Loader loader, Renderer renderer) {
		// Parse ID into texture and physics IDs
		int physicsType = id % 5;
		int textureId = id / 5;
		// Add physics component
		if (physicsType != 4) {
			CollideComponent collider = new CollideComponent(transform, physicsType, Float.parseFloat(PropertiesFile.getProperty("game_gravity")));
			e.addComponent(collider);
		}
		// Add render component
		RenderComponent render = new RenderComponent(renderer, transform, performTextureLookup(textureId, loader), 0, true);
		e.addComponent(render);
		// Return to level parser
		return e;
	}

	private static ShapeTexture performTextureLookup(int texId, Loader loader) {
		// Check the hashmap for an existing texture
		if (textures.get(texId) != null) {
			return textures.get(texId);
		} else {
			// Lookup texture loation from properties file and create shape texture object
			String propertiesFileKey = ("texture_" + texId);
			String textureLocation = PropertiesFile.getProperty(propertiesFileKey);
			int graphicalTextureId = loader.loadTexture(textureLocation);
			ShapeTexture texture = new ShapeTexture(graphicalTextureId);
			return texture;
		}
	}

}
