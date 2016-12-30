package com.darkduckdevelopers.components;


import org.lwjgl.util.vector.Vector2f;

/**
 * This isn't actually a component. It stores positional data of an entity for
 * use by other components.
 * 
 * @author Zachary
 */
public class TransformComponent {

	public Vector2f position;
	public Vector2f size;
	public float rotation;

	public TransformComponent(Vector2f position, float rotation, Vector2f size) {
		this.position = position;
		this.rotation = rotation;
		this.size = size;
	}

}
