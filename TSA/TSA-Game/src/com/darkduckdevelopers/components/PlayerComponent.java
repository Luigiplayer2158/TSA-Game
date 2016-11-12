package com.darkduckdevelopers.components;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.render.DisplayManager;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier;

/**
 * A component adding player controls
 * 
 * @author Zachary
 */
public class PlayerComponent extends BaseComponent {

	public TransformComponent transform;
	public CollideComponent collider;
	public Controller controller;
	public HashMap<String, Identifier> components;
	public float speed;
	public float jumpSpeed;
	public float deadzone;
	
	public PlayerComponent(CollideComponent collider, Controller controller, float speed, float jumpSpeed, float deadzone) {
		this.transform = collider.transform;
		this.collider = collider;
		this.controller = controller;
		components = new HashMap<String, Identifier>();
		if (controller != null) { // A null controller is considered Keyboard + Mouse
			for (Component c : controller.getComponents()) {
				components.put(c.getName(), c.getIdentifier());
			}
		}
		this.speed = speed;
		this.jumpSpeed = jumpSpeed;
		this.deadzone = deadzone;
	}

	// Calculate the movement of the player
	// TODO Other components of a player
	// TODO Configurable inputs
	public void tick() {
		float dx = 0f;
		if (controller == null) {
			// If there is no controller, use keyboard input
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				dx -= speed * DisplayManager.getFrameTimeSeconds();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				dx += speed * DisplayManager.getFrameTimeSeconds();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		} else {
			// Use controller LStick to move
			if (Math.abs(controller.getComponent(components.get("X Axis")).getPollData()) > deadzone) {
				dx += controller.getComponent(components.get("X Axis")).getPollData() * speed
						* DisplayManager.getFrameTimeSeconds();
			}
			if (controller.getComponent(components.get("Button 1")).getPollData() > 0.5f && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		}
		transform.position.x += dx;
	}

}
