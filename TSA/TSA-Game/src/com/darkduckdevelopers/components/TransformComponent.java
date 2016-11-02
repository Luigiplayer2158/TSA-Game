package com.darkduckdevelopers.components;

import org.lwjgl.util.vector.Vector2f;

public class TransformComponent extends BaseComponent {
	
	public Vector2f position;
	public Vector2f size;
	public float rotation;

	public TransformComponent(Vector2f position, float rotation, Vector2f size) {
		this.position = position;
		this.rotation = rotation;
		this.size = size;
	}
	
	@Override
	public void tick() {
	}

}
