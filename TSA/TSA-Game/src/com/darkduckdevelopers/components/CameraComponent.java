package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.util.Maths;

public class CameraComponent extends BaseComponent {
	
	public TransformComponent transform;
	public Renderer renderer;
	
	public CameraComponent(TransformComponent transform, Renderer renderer) {
		this.transform = transform;
		this.renderer = renderer;
	}

	@Override
	public void tick() {
		renderer.uploadViewMatrix(Maths.createViewMatrix(transform));
	}

}
