package com.darkduckdevelopers.components;

import java.util.HashMap;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

import org.lwjgl.input.Keyboard;

import com.darkduckdevelopers.main.MainGameLoop;
import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.util.PropertiesFile;

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
	public int leftKey;
	public int rightKey;
	public int jumpKey;
	public String controllerLR;
	public String controllerJump;
	
	public PlayerComponent(CollideComponent collider, Controller controller, float speed, float jumpSpeed) {
		this.transform = collider.transform;
		this.collider = collider;
		this.controller = controller;
		components = new HashMap<String, Identifier>();
		if (controller != null) { // A null controller is considered Keyboard + Mouse
			for (Component c : controller.getComponents()) {
				components.put(c.getName(), c.getIdentifier());
			}
			deadzone = Float.parseFloat(PropertiesFile.getProperty("controller_deadzone"));
			controllerLR = PropertiesFile.getProperty("controller_leftHor");
			controllerJump = PropertiesFile.getProperty("controller_jump");
		} else {
			leftKey = Integer.parseInt(PropertiesFile.getProperty("key_left"));
			rightKey = Integer.parseInt(PropertiesFile.getProperty("key_right"));
			jumpKey = Integer.parseInt(PropertiesFile.getProperty("key_jump"));
		}
		this.speed = speed;
		this.jumpSpeed = jumpSpeed;
	}

	// Calculate the movement of the player
	// TODO Other components of a player
	// TODO Configurable inputs
	public void tick() {
		float dx = 0f;
		if (controller == null) {
			// If there is no controller, use keyboard input
			if (Keyboard.isKeyDown(leftKey)) {
				dx -= speed * DisplayManager.getFrameTimeSeconds();
			}
			if (Keyboard.isKeyDown(rightKey)) {
				dx += speed * DisplayManager.getFrameTimeSeconds();
			}
			if (Keyboard.isKeyDown(jumpKey) && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		} else {
			// Use controller LStick to move
			if (Math.abs(controller.getComponent(components.get(controllerLR)).getPollData()) > deadzone) {
				dx += controller.getComponent(components.get(controllerLR)).getPollData() * speed
						* DisplayManager.getFrameTimeSeconds();
			}
			if (controller.getComponent(components.get(controllerJump)).getPollData() > 0.5f && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		}
		transform.position.x += dx;
	}

}
