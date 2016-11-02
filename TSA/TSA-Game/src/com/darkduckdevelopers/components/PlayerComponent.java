package com.darkduckdevelopers.components;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.render.DisplayManager;

/**
 * A component adding player controls
 * 
 * @author Zachary
 */
public class PlayerComponent extends BaseComponent {

	public TransformComponent transform;
	public float speed;
	
	public PlayerComponent(TransformComponent transform, float speed) {
		this.transform = transform;
		this.speed = speed;
	}
	
	// Calculate the movement of the player
	// TODO Other components of a player
	// TODO Variable key inputs
	public void tick() {
		float dx = 0f;
		float dy = 0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			dy += speed * DisplayManager.getFrameTimeSeconds();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			dy -= speed * DisplayManager.getFrameTimeSeconds();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			dx -= speed * DisplayManager.getFrameTimeSeconds();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			dx += speed * DisplayManager.getFrameTimeSeconds();
		}
		transform.position = new Vector2f(transform.position.x + dx, transform.position.y + dy);
	}
	
}
