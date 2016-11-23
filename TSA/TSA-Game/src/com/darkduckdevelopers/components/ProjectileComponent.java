package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.DisplayManager;

/**
 * Component dictating the movement of a velocity-based object
 * 
 * @author Zachary
 */
public class ProjectileComponent extends BaseComponent {

	public TransformComponent transform;
	public CollideComponent collider;
	public float horizontalSpeed;

	public ProjectileComponent(TransformComponent transform,
			CollideComponent collider) {
		this.transform = transform;
		this.collider = collider;
	}

	public void launch(float velocityX, float velocityY) {
		this.horizontalSpeed = velocityX;
		collider.verticalSpeed = velocityY;
	}

	@Override
	public void tick() {
		transform.position.x += horizontalSpeed * DisplayManager.getFrameTimeSeconds();
		if (collider.hitBy != -1) {
			collider.hitBy = -1;
		}
	}

}
