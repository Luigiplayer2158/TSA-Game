package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.DisplayManager;

/**
 * A component to spin an entity
 * 
 * @author Zachary
 */
public class SpinComponent extends BaseComponent {

	public TransformComponent transform;
	public float spinSpeed;
	
	public SpinComponent(TransformComponent transform, float spinSpeed) {
		this.transform = transform;
		this.spinSpeed = spinSpeed;
	}
	
	@Override
	public void tick() {
		// Spin
		transform.rotation += spinSpeed * DisplayManager.getFrameTimeSeconds();
	}

}
