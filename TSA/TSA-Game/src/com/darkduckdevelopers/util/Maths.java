package com.darkduckdevelopers.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.darkduckdevelopers.components.TransformComponent;

/**
 * A class to do math not covered by LWJGL or Java's math operations
 * 
 * @author Zachary
 */
public class Maths {

	private static int cachedDisplayWidth;
	private static int cachedDisplayHeight;
	private static Vector3f cachedAspect;

	/**
	 * Create the transformation matrix for an entity
	 * 
	 * @param transform
	 *            The transform component to read from
	 * @return The matrix transformation
	 */
	public static Matrix4f createTransformationMatrix(
			TransformComponent transform) {
		aspectScale();
		Matrix4f matrix = new Matrix4f(); // Create new matrix
		matrix.setIdentity(); // Set matrix to diagonal 1s and all 0s
		// The operations are in the opposite of what would seem like correct
		// order because the shader will be calculating the transformation in
		// reverse
		Matrix4f.scale(cachedAspect, matrix, matrix);
		Matrix4f.translate(new Vector3f(transform.position.x,
				transform.position.y, 0f), matrix, matrix);
		Matrix4f.rotate((float) (Math.toRadians(transform.rotation)),
				new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(transform.size.x, transform.size.y, 0),
				matrix, matrix);
		return matrix;
	}

	/**
	 * Create a view matrix for a camera
	 * 
	 * @param transform
	 *            The camera's position
	 * @return The camera view matrix
	 */
	public static Matrix4f createViewMatrix(TransformComponent transform) {
		// See transformation matrix
		aspectScale();
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.translate(new Vector3f(-transform.position.x,
				-transform.position.y * cachedAspect.y, 0f), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) (Math.toRadians(transform.rotation)),
				new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Matrix4f.scale(new Vector3f(transform.size.x, transform.size.y, 0),
				viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	/**
	 * Create the aspect ratio of the display
	 */
	public static void aspectScale() {
		if (Display.getWidth() != cachedDisplayWidth
				&& Display.getHeight() != cachedDisplayHeight) {
			float ratio = ((float) Display.getWidth())
					/ ((float) Display.getHeight());
			cachedAspect = new Vector3f(1f, ratio, 0f);
		}
	}

}
