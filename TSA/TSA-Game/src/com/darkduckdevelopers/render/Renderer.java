package com.darkduckdevelopers.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.util.Maths;

/**
 * A class to render entities
 * 
 * @author Zachary
 */
public class Renderer {

	public static int vaoID;
	public static final float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private Shader shader;
	private Matrix4f viewMatrix;

	public Renderer(Shader shader) {
		this.shader = shader;
		shader.start();
	}

	/**
	 * Clear the screen and update the view matrix
	 */
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // Clear the color FBO
		GL11.glClearColor(0, 0, 0, 1); // Set all color to black
		shader.start(); // Start the shader program
		shader.loadViewMatrix(viewMatrix); // Load the view matrix uniform
		shader.stop(); // Stop the shader program
	}

	/**
	 * Send the view matrix to the renderer
	 * 
	 * @param viewMatrix
	 *            The view matrix to upload
	 */
	public void uploadViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	/**
	 * Render an entity
	 * 
	 * @param render
	 *            The render component of the entity
	 */
	public void render(RenderComponent render) {
		shader.start(); // Start the shader program
		shader.loadTransformationMatrix(Maths
				.createTransformationMatrix(render.transform)); // Load the
																// transformation
																// uniform
		shader.loadNumberOfRows(render.texture.getNumberOfRows()); // Load the
																	// number of
																	// rows in
																	// the atlas
		shader.loadOffset(render.getTextureXOffset(), // Load the atlas offset
				render.getTextureYOffset());
		shader.loadUseVM(render.useVM); // Load whether or not to use the view
										// matrix
		GL30.glBindVertexArray(vaoID); // Bind the global VAO
		GL20.glEnableVertexAttribArray(0); // Enable the VBO with id 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0); // Active texture in slot 0
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, render.texture.getTextureID()); // Bind
																				// texture
																				// to
																				// slot
																				// 0
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4); // Draw the quad using
															// shader program
		GL20.glDisableVertexAttribArray(0); // Unbind VBO
		GL30.glBindVertexArray(0); // Unbind VAO
		shader.stop(); // Stop the shader program
	}

	/**
	 * Clean up the shader
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

}
