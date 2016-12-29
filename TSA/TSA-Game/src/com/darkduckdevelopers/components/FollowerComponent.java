package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.util.PropertiesFile;

/**
 * A component that follows another entity and can move
 * 
 * @author Zachary
 */
public class FollowerComponent extends BaseComponent {

	public TransformComponent targeter;
	public TransformComponent target;
	public float distance;
	public float lockSpeed;
	public float lockThreshold;

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
		distance = 1f;
		lockSpeed = Float.parseFloat(PropertiesFile.getProperty("game_reticleSpeed"));
		lockThreshold = Float.parseFloat(PropertiesFile.getProperty("game_reticleLock"));
	}

	@Override
	public void tick() {
		// Change distance to target
		float delta = 0f;
		delta = 1f - (distance * lockSpeed);
		if (distance != 0f) {
			distance -= delta * DisplayManager.getFrameTimeSeconds();
			if (distance <= lockThreshold) {
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
		distance = 1f;
	}

}
