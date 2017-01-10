package com.darkduckdevelopers.components;


import org.lwjgl.input.Keyboard;

import com.darkduckdevelopers.objects.Gamepad;
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
	public Gamepad controller;

	public float speed;
	public float jumpSpeed;
	public float deadzone;
	public int leftKey;
	public int rightKey;
	public int jumpKey;
	public String controllerLR;
	public String controllerJump;

	public PlayerComponent(CollideComponent collider, Gamepad controller) {
		this.transform = collider.transform;
		this.collider = collider;
		this.controller = controller;
		if (controller != null) { // A null controller is considered Keyboard +
									// Mouse
			// Index all controller inputs for easy querying

			// Get variables used to query the controller
			// The controller name is used because sometimes the different
			// controllers have different inputs, as seen in AutoTargetComponent
			deadzone = Float.parseFloat(
					PropertiesFile.getProperty("controller_" + controller.getName().toLowerCase() + "_deadzone"));
			controllerLR = PropertiesFile.getProperty("controller_" + controller.getName().toLowerCase() + "_leftHor");
			controllerJump = PropertiesFile.getProperty("controller_" + controller.getName().toLowerCase() + "_jump");
		} else {
			// Get variables used to query the keyboard
			leftKey = Integer.parseInt(PropertiesFile.getProperty("key_leftMove"));
			rightKey = Integer.parseInt(PropertiesFile.getProperty("key_rightMove"));
			jumpKey = Integer.parseInt(PropertiesFile.getProperty("key_jump"));
		}
		this.speed = Float.parseFloat(PropertiesFile.getProperty("game_playerSpeed"));
		this.jumpSpeed = Float.parseFloat(PropertiesFile.getProperty("game_playerJump"));
	}

	// Calculate the movement of the player
	// TODO Other components of a player
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
			float leftRightInput = controller.getInput(controllerLR, false);
			if (Math.abs(leftRightInput) > deadzone) {
				dx += leftRightInput * speed * DisplayManager.getFrameTimeSeconds();
			}
			if (controller.getInput(controllerJump, true) > 0.5f && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		}
		transform.position.x += dx;
	}

}
