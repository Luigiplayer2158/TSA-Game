package com.darkduckdevelopers.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.darkduckdevelopers.main.MainGameLoop;

/**
 * A class providing basic shader functionality
 * 
 * @author Zachary
 */
public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER); // Compile
																		// vertex
																		// shader
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER); // Compile
																				// fragment
																				// shader
		programID = GL20.glCreateProgram(); // Create a new shader program
		GL20.glAttachShader(programID, vertexShaderID); // Attach the vertex
														// shader
		GL20.glAttachShader(programID, fragmentShaderID); // Attach the fragment
															// shader
		bindAttributes(); // Bind the VBOs of the shader
		GL20.glLinkProgram(programID); // Link the program to the shader
		GL20.glValidateProgram(programID); // Validate the program
		getAllUniformLocations(); // Get the uniforms of the shader
	}

	// See Shader
	protected abstract void getAllUniformLocations();

	/**
	 * Get the uniform location of a given variable name
	 * 
	 * @param uniformName
	 *            The name of the uniform variable
	 * @return The integer location of the uniform
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	/*
	 * All of the below methods deal with loading Java data into shader uniforms
	 * at a given location
	 */

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadVector2(int location, Vector2f value) {
		GL20.glUniform2f(location, value.x, value.y);
	}

	protected void loadVector3(int location, Vector3f value) {
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}

	// Note that GLSL does not have boolean values so a 0 or 1 float is used
	// with an if statement
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	protected void loadInt(int location_horizontalTexture, int texID) {
		GL20.glUniform1i(location_horizontalTexture, texID);
	}

	// Matrices must be stored in a float buffer to be loaded
	protected void loadMatrix(int location, Matrix4f value) {
		value.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);

	}

	/**
	 * Start the shader program
	 */
	public void start() {
		GL20.glUseProgram(programID);
	}

	/**
	 * Don't use a shader program
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}

	/**
	 * Delete elements from VRAM
	 */
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID); // Detach vShader
		GL20.glDetachShader(programID, fragmentShaderID); // Detach fShader
		GL20.glDeleteShader(vertexShaderID); // Delete vShader
		GL20.glDeleteShader(fragmentShaderID); // Delete fShader
		GL20.glDeleteProgram(programID); // Delete shader program
	}

	/**
	 * Bind all of the VBOs the shader uses
	 */
	protected abstract void bindAttributes();

	/**
	 * Tell the shader to use a certain VBO with a variable name
	 * 
	 * @param attribute
	 *            The VBO id
	 * @param variableName
	 *            The name to use in the shader
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	/**
	 * Compile the GLSL shader from a text file
	 * 
	 * @param file
	 *            The path to the shader file
	 * @param type
	 *            The type of shader
	 * @return The id of the shader
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			InputStream in = MainGameLoop.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n"); // Write a Java line
														// from the
														// shader's line
			}
			reader.close(); // Clean up BufferedReader
		} catch (IOException e) {
			System.err.println("[ERROR] Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type); // Create a new shader in VRAM
		GL20.glShaderSource(shaderID, shaderSource); // Load the shader into
														// VRAM
		GL20.glCompileShader(shaderID); // Compile the shader into GPU code
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("[ERROR] Could not compile shader."
					+ GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		return shaderID;
	}

}
