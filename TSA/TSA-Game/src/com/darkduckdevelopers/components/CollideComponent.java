package com.darkduckdevelopers.components;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

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
	public Vector2f size;
	public float gravity;
	public float verticalSpeed;
	public boolean isGrounded;
	public int colliderType;
	public int hitBy = -1;

	// 0 - static
	// 1 - player
	// 2 - moves player
	// 3 - player moves

	public CollideComponent(TransformComponent transform, int colliderType, float gravity, Vector2f size) {
		this.transform = transform;
		this.colliderType = colliderType;
		if (entities == null) {
			entities = new ArrayList<CollideComponent>();
		}
		entities.add(this);
		this.size = size;
		this.gravity = gravity;
	}

	@Override
	public void tick() {
		isGrounded = false;
		verticalSpeed += gravity * DisplayManager.getFrameTimeSeconds();
		transform.position.y += verticalSpeed * DisplayManager.getFrameTimeSeconds();
		for (CollideComponent collider : entities) {
			if (colliderType != collider.colliderType && collider.colliderType != 0) {
				float combinedSizeX = collider.size.x + size.x;
				float xOff = collider.transform.position.x - transform.position.x;
				if (Math.abs(xOff) <= combinedSizeX - 0.0001f) {
					float combinedSizeY = collider.size.y + size.y;
					float yOff = collider.transform.position.y - transform.position.y;
					if (Math.abs(yOff) <= combinedSizeY - 0.0001f) {
						collider.hit(colliderType);
						if (colliderType != 1 || collider.colliderType == 3) {
							if (Math.abs(yOff) < Math.abs(xOff) + 0.002f) {
								if (xOff > 0) {
									collider.transform.position.x = transform.position.x + combinedSizeX;
								} else {
									collider.transform.position.x = transform.position.x - combinedSizeX;
								}
							} else {
								if (yOff >= 0) {
									collider.transform.position.y = transform.position.y + combinedSizeY;
									if (collider.verticalSpeed < 0f) {
										collider.stopGravity();
									}
								} else {
									collider.transform.position.y = transform.position.y - combinedSizeY;
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
