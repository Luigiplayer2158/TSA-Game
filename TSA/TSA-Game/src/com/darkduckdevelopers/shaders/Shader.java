package com.darkduckdevelopers.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

/**
 * The main shader for rendering entities
 * 
 * @author Zachary
 */
public class Shader extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_useVM;
	private int location_numberOfRows;
	private int location_offset;

	public Shader(String vertex, String fragment) {
		super(vertex, fragment);
	}

	// See ShaderProgram
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	/**
	 * Get the integer location of all uniform variables
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_useVM = super.getUniformLocation("useVM");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}

	/*
	 * All of the below methods should be self explanatory. They load a Java
	 * value into the shader via uniform variables.
	 */

	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	public void loadOffset(float x, float y) {
		super.loadVector2(location_offset, new Vector2f(x, y));
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void loadUseVM(boolean useVM) {
		super.loadBoolean(location_useVM, useVM);
	}

}
