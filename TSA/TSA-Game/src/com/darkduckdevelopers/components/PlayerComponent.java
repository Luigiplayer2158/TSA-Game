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
	public RenderComponent render;
	public Gamepad controller;

	public float speed;
	public float jumpSpeed;
	public float deadzone;
	public float animCurrentTime;
	public float animSpeed = 0.15f;
	public int animation;
	public int animationFrame;
	public int leftKey;
	public int rightKey;
	public int jumpKey;
	public String controllerLR;
	public String controllerJump;

	public PlayerComponent(CollideComponent collider, Gamepad controller, RenderComponent playerRender) {
		this.transform = collider.transform;
		this.collider = collider;
		this.controller = controller;
		this.render = playerRender;
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
		animation = 0;
		if (controller == null) {
			// If there is no controller, use keyboard input
			if (Keyboard.isKeyDown(leftKey)) {
				dx -= speed * DisplayManager.getFrameTimeSeconds();
				animation--;
			}
			if (Keyboard.isKeyDown(rightKey)) {
				dx += speed * DisplayManager.getFrameTimeSeconds();
				animation++;
			}
			if (Keyboard.isKeyDown(jumpKey) && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		} else {
			// Use controller LStick to move
			float leftRightInput = controller.getInput(controllerLR, false);
			if (Math.abs(leftRightInput) > deadzone) {
				dx += leftRightInput * speed * DisplayManager.getFrameTimeSeconds();
				if (dx > 0) {
					animation++;
				} else {
					animation--;
				}
			}
			if (controller.getInput(controllerJump, true) > 0.5f && collider.isGrounded) {
				collider.verticalSpeed = jumpSpeed;
			}
		}
		// Figure out animation frames
		if (animation == 0) {
			// If there is no animation we need to reset everything to be ready
			animCurrentTime = 0;
			animationFrame = 0;
			render.index = 0;
		} else {
			// If there is an animation add to the animation timer
			animCurrentTime += DisplayManager.getFrameTimeSeconds();
			if (animation == 1) {
				// For every time the animation time is complete
				if (animCurrentTime >= animSpeed) {
					animCurrentTime %= animSpeed;
					// Increase animation frame
					animationFrame++;
					// Loop back at the end
					if (animationFrame > 3) {
						animationFrame = 0;
					}
					// If the player is airborne then choose an appropriate frame
					if (!collider.isGrounded) {
						animationFrame = 1;
					}
					// Calculate atlas index of frame (standard)
					render.index = animationFrame * 4 + 2;
				}
			} else if (animation == -1) {
				if (animCurrentTime >= animSpeed) {
					animCurrentTime %= animSpeed;
					animationFrame++;
					if (animationFrame > 3) {
						animationFrame = 0;
					}
					// If the player is airborne then choose an appropriate frame
					if (!collider.isGrounded) {
						animationFrame = 1;
					}
					render.index = animationFrame * 4 + 3;
				}
			}
		}
		// Move the player
		transform.position.x += dx;
	}

}
