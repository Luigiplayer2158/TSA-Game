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
	public PlayerComponent player;
	public float size;
	public float gravity;
	public float verticalSpeed;
	public boolean isGrounded;
	public int colliderType;
	// 0 - ground
	// 1 - player
	// 2 - other

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
	
	public CollideComponent(PlayerComponent player, float gravity) {
		this.transform = player.transform;
		this.colliderType = 1;
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
			if (colliderType != collider.colliderType && collider.colliderType != 0) { // Don't
																	// collide
																	// with
																	// self,
																	// thats
																	// stupid
				float xOff = collider.transform.position.x - transform.position.x;
				float combinedSize = collider.size + size;
				if (Math.abs(xOff) <= combinedSize) {
					float yOff = collider.transform.position.y - transform.position.y;
					if (Math.abs(yOff) <= combinedSize) {
						if (Math.abs(yOff) > Math.abs(xOff) - 0.01f) {
							if (yOff > 0) {
								collider.transform.position.y = transform.position.y + combinedSize;
								collider.stopGravity();
							} else {
								collider.transform.position.y = transform.position.y - combinedSize;
							}
						} else {
							if (xOff > 0) {
								collider.transform.position.x = transform.position.x + combinedSize;
							} else {
								collider.transform.position.x = transform.position.x - combinedSize;
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
