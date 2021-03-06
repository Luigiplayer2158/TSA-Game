package com.darkduckdevelopers.util;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.darkduckdevelopers.main.MainGameLoop;
import com.darkduckdevelopers.render.Renderer;

/**
 * A class for loading items into the GPU
 * 
 * @author Zachary
 */
public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	/**
	 * Load a texture into the GPU
	 * 
	 * @param tex
	 *            The texture file name
	 * @return The id of the texture in VRAM
	 */
	public int loadTexture(String tex) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", MainGameLoop.class
					.getResourceAsStream("/com/darkduckdevelopers/res/" + tex));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		} catch (Exception e) {
			System.err.println("[ERROR] Failed to load texture!");
			e.printStackTrace();
		}
		int texID = texture.getTextureID();
		textures.add(texID);
		return texID;
	}

	/**
	 * Load positions into a vertex array object
	 * 
	 * @param positions
	 *            The positions to load into the VAO
	 */
	public void loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		Renderer.vaoID = vaoID; // Set the global VAO
	}

	/**
	 * Delete all items from VRAM
	 */
	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}

	/**
	 * Create a new VAO in VRAM
	 * 
	 * @return The id of the VAO
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID); // Bind the VAO
		return vaoID;
	}

	/**
	 * Store data into a VBO in VRAM
	 * 
	 * @param attribNumber
	 *            The VBO id
	 * @param coordinateSize
	 *            The number of values per vertex
	 * @param data
	 *            The data to store into a VBO
	 */
	private void storeDataInAttributeList(int attribNumber, int coordinateSize,
			float[] data) {
		int vboID = GL15.glGenBuffers(); // Create a new VBO
		vbos.add(vboID); // Add to the cleanup list
		// Store data into buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNumber, coordinateSize, GL11.GL_FLOAT,
				false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind VBO
	}

	/**
	 * Unbind a VAO
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	/**
	 * Store an array into a float buffer
	 * 
	 * @param data
	 *            The array to transform
	 * @return The float buffer for a VBO
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
