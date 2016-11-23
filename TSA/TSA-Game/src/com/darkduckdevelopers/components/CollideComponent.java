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
	public int hitBy = -1;

	// 0 - static
	// 1 - player
	// 2 - moves player
	// 3 - player moves

	public CollideComponent(TransformComponent transform, int colliderType,
			float gravity) {
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
		transform.position.y += verticalSpeed
				* DisplayManager.getFrameTimeSeconds();
		for (CollideComponent collider : entities) {
			if (colliderType != collider.colliderType
					&& collider.colliderType != 0) {
				float xOff = collider.transform.position.x
						- transform.position.x;
				float combinedSize = collider.size + size;
				if (Math.abs(xOff) <= combinedSize) {
					float yOff = collider.transform.position.y
							- transform.position.y;
					if (Math.abs(yOff) <= combinedSize) {
						collider.hit(colliderType);
						if (colliderType != 1 || collider.colliderType == 3) {
							if (Math.abs(yOff) > Math.abs(xOff) - 0.01f) {
								if (yOff > 0) {
									collider.transform.position.y = transform.position.y
											+ combinedSize;
									if (collider.verticalSpeed < 0f) {
										collider.stopGravity();
									}
								} else {
									collider.transform.position.y = transform.position.y
											- combinedSize;
								}
							} else {
								if (xOff > 0) {
									collider.transform.position.x = transform.position.x
											+ combinedSize;
								} else {
									collider.transform.position.x = transform.position.x
											- combinedSize;
								}
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

	public void hit(int hitter) {
		hitBy = hitter;
	}

}
