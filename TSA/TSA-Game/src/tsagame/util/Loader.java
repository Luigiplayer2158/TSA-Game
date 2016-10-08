package tsagame.util;

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

import tsagame.render.Quad;

/**
 * A class for loading data into the GPU.
 * 
 * @author Zachary
 */
public class Loader {

	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	/**
	 * Create references to a simple quad
	 */
	public void loadQuad() {
		int vaoID = createVAO();
		Quad.vaoID = vaoID;
		storeDataInAttributeList(0, Quad.defaultVertex, 2);
		unbindVAO();
	}

	/**
	 * Load a texture into the GPU and create a reference
	 * 
	 * @param fileName
	 *            The file name of the texture
	 * @return The texture reference
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG",
					Class.class.getResourceAsStream(fileName + ".png")); // Store
																			// the
																			// file's
																			// contents
																			// into
																			// the
																			// object
																			// 'texture'
		} catch (Exception e) {
			System.err.println("[GAME] Failed to load texture " + fileName);
		}
		int textureID = texture.getTextureID(); // Create the reference integer
		textures.add(textureID); // Add the texture to the clean up list
		return textureID;
	}

	/**
	 * Detach elements from the GPU
	 */
	public void cleanUp() {
		GL30.glDeleteVertexArrays(Quad.vaoID); // Delete the VAO used for all
												// quads
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo); // Delete the VBO at the index 'vbo'
		}
		for (int tex : textures) {
			GL11.glDeleteTextures(tex); // Delete the texture at the index 'tex'
		}
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); // Generate a new VAO and get the
												// index
		GL30.glBindVertexArray(vaoID); // Set the current VAO to 'vaoID'
		return vaoID;
	}

	private void storeDataInAttributeList(int index, float[] elements,
			int coordinateSize) {
		int vboID = GL15.glGenBuffers(); // Generate a new VBO and get the index
		vbos.add(vboID); // Add the VBO to the clean up list
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // Set the current VBO
														// to 'vboID'
		FloatBuffer buffer = storeDataInFloatBuffer(elements); // Create a float
																// buffer from
																// the array
																// 'elements'
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Store
																				// the
																				// float
																				// buffer
																				// into
																				// the
																				// VBO,
																				// never
																				// to
																				// be
																				// changed
		GL20.glVertexAttribPointer(index, coordinateSize, GL11.GL_FLOAT, false,
				0, 0); // Tell the VAO how many VBO values go towards a single
						// vertex
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind the VBO
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0); // Unbind the VAO
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); // Create
																			// a
																			// new
																			// float
																			// buffer
		buffer.put(data); // Store 'data' into the buffer
		buffer.flip(); // Flip the buffer from write mode to read mode
		return buffer;
	}

}
