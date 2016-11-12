package com.darkduckdevelopers.components;

import java.util.ArrayList;
import java.util.List;

import com.darkduckdevelopers.render.DisplayManager;

/**
 * Collision component for projectiles, squares, etc
 * 
 * @author Zachary
 */
public class CollideComponent extends BaseComponent {

	public static List<CollideComponent> entities;
	public TransformComponent transform;
	public float size;
	public float gravity;
	public float verticalSpeed;
	public int colliderType;
	public boolean isGrounded;

	public CollideComponent(TransformComponent transform, int colliderType, float gravity) {
		this.transform = transform;
		this.colliderType = colliderType;
		if (entities == null) {
			entities = new ArrayList<CollideComponent>();
		}
		entities.add(this);
		if (transform.size.x > transform.size.y) {
			size = transform.size.x;
		} else {
			size = transform.size.y;
		}
		this.gravity = gravity;
	}

	@Override
	public void tick() {
		isGrounded = false;
		verticalSpeed += gravity * DisplayManager.getFrameTimeSeconds();
		transform.position.y += verticalSpeed * DisplayManager.getFrameTimeSeconds();
		for (CollideComponent collider : entities) {
			if (collider != this && collider.colliderType != 0) { // Don't
																	// collide
																	// with
																	// self,
																	// thats
																	// stupid
				float xOff = collider.transform.position.x - transform.position.x;
				if (Math.abs(xOff) <= collider.size + size) {
					float yOff = collider.transform.position.y - transform.position.y;
					if (Math.abs(yOff) <= (collider.size + size)) {
						if (Math.abs(yOff) > Math.abs(xOff) - 0.05f) {
							if (yOff > 0) {
								collider.transform.position.y = transform.position.y + (collider.size + size);
								collider.stopGravity();
							} else {
								collider.transform.position.y = transform.position.y - (collider.size + size);
							}
						} else {
							if (xOff > 0) {
								collider.transform.position.x = transform.position.x + (collider.size + size);
							} else {
								collider.transform.position.x = transform.position.x - (collider.size + size);
							}
						}
					}
				}
			}
		}
	}
	
	public void stopGravity() {
		verticalSpeed = 0f;
		isGrounded = true;
	}

}
