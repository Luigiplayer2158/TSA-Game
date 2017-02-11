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

	public static void loadLevel(List<Entity> entities, String levelFile, Loader loader, Renderer renderer) {
		// Create a new reader
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream(levelFile));
			reader = new BufferedReader(isr);
		} catch (Exception e) {
			System.err.println("Level not found! " + levelFile);
			return;
		}

		// Read through file
		String line = "";
		try {
			line = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int positionIndex = 0;
		while (line != null) {
			byte[] bytes = line.getBytes();
			for (int i = 0; i < bytes.length; i += 2) {
				positionIndex += 1;
				if (bytes[i] != -1 && bytes[i + 1] != -1) {
					int gridX = positionIndex % 1000;
					int gridY = positionIndex / 1000;
					int property = (bytes[i] << 0) & 0x000000ff;
					int texId = (bytes[i + 1] << 0) & 0x000000ff;
					System.out.println(texId + ", " + property + ", [" + gridX
							+ ", " + gridY + "]");
					
				}
			}
			try {
				line = reader.readLine();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * KEY FOR ENTITY IDENTIFICATION
	 * 
	 * ID % 5 = PHYSICS TYPE ID / 5 = TEXTURE ID
	 */
	private static Entity addIdComponents(int id, Entity e, TransformComponent transform, Loader loader,
			Renderer renderer) {
		// Parse ID into texture and physics IDs
		int physicsType = id % 5;
		int textureId = id / 5;
		// Add physics component
		if (physicsType != 4) {
			CollideComponent collider = new CollideComponent(transform, physicsType,
					Float.parseFloat(PropertiesFile.getProperty("game_gravity")), transform.size);
			e.addComponent(collider);
		}
		// Add render component
		RenderComponent render = new RenderComponent(renderer, transform, performTextureLookup(textureId, loader), 0,
				true);
		e.addComponent(render);
		// Return to level parser
		return e;
	}

	private static ShapeTexture performTextureLookup(int texId, Loader loader) {
		// Check the hashmap for an existing texture
		if (textures.get(texId) != null) {
			return textures.get(texId);
		} else {
			// Lookup texture loation from properties file and create shape
			// texture object
			String propertiesFileKey = ("texture_" + texId);
			String textureLocation = PropertiesFile.getProperty(propertiesFileKey);
			int graphicalTextureId = loader.loadTexture(textureLocation);
			ShapeTexture texture = new ShapeTexture(graphicalTextureId);
			return texture;
		}
	}

}
