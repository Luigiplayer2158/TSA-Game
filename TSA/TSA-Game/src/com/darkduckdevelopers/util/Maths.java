package com.darkduckdevelopers.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.darkduckdevelopers.components.TransformComponent;

public class Maths {
	
	private static int cachedDisplayWidth;
	private static int cachedDisplayHeight;
	private static Vector3f cachedAspect;

	public static Matrix4f createTransformationMatrix(TransformComponent transform) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(new Vector3f(transform.position.x, transform.position.y, 0f), matrix, matrix);
		Matrix4f.scale(new Vector3f(transform.size.x, transform.size.y, 0), matrix, matrix);
		if (Display.getWidth() != cachedDisplayWidth && Display.getHeight() != cachedDisplayHeight) {
			float ratio = ((float) Display.getWidth()) / ((float) Display.getHeight());
			cachedAspect = new Vector3f(1f, ratio, 0f);
		}
		Matrix4f.scale(cachedAspect, matrix, matrix);
		Matrix4f.rotate((float) (Math.toRadians(transform.rotation)), new Vector3f(0, 0, 1), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(TransformComponent transform) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.translate(new Vector3f(-transform.position.x, -transform.position.y, 0f), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) (Math.toRadians(transform.rotation)), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Matrix4f.scale(new Vector3f(transform.size.x, transform.size.y, 0), viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
}
