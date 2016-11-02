package com.darkduckdevelopers.components;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.render.DisplayManager;

public class PlayerComponent extends BaseComponent {

	public TransformComponent transform;
	public float speed;
	
	public PlayerComponent(TransformComponent transform, float speed) {
		this.transform = transform;
		this.speed = speed;
	}
	
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
		float mouseX = (float) Mouse.getX() / (float) Display.getWidth();
		float mouseY = (float) Mouse.getY() / (float) Display.getHeight();
		mouseX = ((mouseX - 0.5f) * 2f);
		mouseY = ((mouseY - 0.5f) * 2f);
		transform.rotation = (float) Math.toDegrees(Math.atan(-mouseX / mouseY));
		if (mouseY < 0f) {
			transform.rotation += 180f;
		}
	}
	
}
