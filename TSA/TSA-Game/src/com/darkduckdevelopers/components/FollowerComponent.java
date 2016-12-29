package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.DisplayManager;

/**
 * A component that follows another entity and can move
 * 
 * @author Zachary
 */
public class FollowerComponent extends BaseComponent {

	public TransformComponent targeter;
	public TransformComponent target;
	private float distance;

	/**
	 * Create a component to follow another entity
	 * 
	 * @param targeter
	 *            The entity to move
	 * @param target
	 *            The entity to move towards
	 */
	public FollowerComponent(TransformComponent targeter,
			TransformComponent target) {
		this.targeter = targeter;
		this.target = target;
	}

	@Override
	public void tick() {
		// Change distance to target
		float delta = 0f;
		delta = 1 - (distance * 0.3f);
		if (distance != 0f) {
			distance -= delta * DisplayManager.getFrameTimeSeconds();
			if (distance <= 0.2f) {
				distance = 0;
			}
		}
		// Calculate movement required
		float dx = target.position.x - targeter.position.x;
		float dy = target.position.y - targeter.position.y;
		// Move entity
		targeter.position.x += dx * delta;
		targeter.position.y += dy * delta;
	}
	
	// Set a new target
	public void retarget(TransformComponent transform) {
		this.target = transform;
	}

}
