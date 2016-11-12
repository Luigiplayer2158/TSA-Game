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
	public Controller controller;
	HashMap<String, Identifier> components;
	public float speed;
	public float turnSpeed;
	public float deadzone;

	public PlayerComponent(TransformComponent transform, Controller controller, float speed, float turnSpeed, float deadzone) {
		this.transform = transform;
		this.controller = controller;
		components = new HashMap<String, Identifier>();
		if (controller != null) { // A null controller is considered Keyboard + Mouse
			for (Component c : controller.getComponents()) {
				components.put(c.getName(), c.getIdentifier());
			}
		}
		this.speed = speed;
		this.turnSpeed = turnSpeed;
		this.deadzone = deadzone;
	}

	// Calculate the movement of the player
	// TODO Other components of a player
	// TODO Configurable inputs
	public void tick() {
		float dx = 0f;
		float dy = 0f;
		float dr = 0f;
		if (controller == null) {
			// If there is no controller, use keyboard input
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
		} else {
			// Use controller LStick to move
			if (Math.abs(controller.getComponent(components.get("X Axis")).getPollData()) > deadzone) {
				dx += controller.getComponent(components.get("X Axis")).getPollData() * speed
						* DisplayManager.getFrameTimeSeconds();
			}
			if (Math.abs(controller.getComponent(components.get("Y Axis")).getPollData()) > deadzone) {
			dy -= controller.getComponent(components.get("Y Axis")).getPollData() * speed
					* DisplayManager.getFrameTimeSeconds();
			}
			// RStick to turn
			if (Math.abs(controller.getComponent(components.get("Z Axis")).getPollData()) > deadzone) {
				dr -= controller.getComponent(components.get("Z Axis")).getPollData() * turnSpeed
						* DisplayManager.getFrameTimeSeconds();
			}
		}
		transform.rotation += dr;
		transform.position = new Vector2f(transform.position.x + dx, transform.position.y + dy);
	}

}
