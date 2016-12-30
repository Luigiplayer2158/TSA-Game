package com.darkduckdevelopers.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.util.PropertiesFile;

/**
 * A component allowing aimbot
 * 
 * @author Zachary
 */
public class AutoTargetComponent extends BaseComponent {

	public static List<TargetableComponent> targetableEntities;

	public FollowerComponent reticle;
	public Controller controller;
	public HashMap<String, Identifier> components;
	public boolean hasTargeted;
	public float dotThreshold;
	public float rangeThreshold;
	public float selfPrevention;

	public String controllerAimLR;
	public String controllerAimUD;
	public int upKey;
	public int rightKey;
	public int downKey;
	public int leftKey;

	public AutoTargetComponent(FollowerComponent reticle, PlayerComponent player) {
		targetableEntities = new ArrayList<TargetableComponent>();
		this.reticle = reticle;
		this.controller = player.controller;
		this.dotThreshold = Float.parseFloat(PropertiesFile.getProperty("game_aimDotTolerance"));
		this.rangeThreshold = Float.parseFloat(PropertiesFile.getProperty("game_aimRangeTolerance"));
		this.selfPrevention = Float.parseFloat(PropertiesFile.getProperty("game_aimSelfPrevention"));

		// See player component for controller vs keyboard handling
		if (controller != null) {
			for (Component c : controller.getComponents()) {
				components.put(c.getName(), c.getIdentifier());
			}
			controllerAimUD = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_aimVert");
			controllerAimLR = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_aimHor");
		} else {
			upKey = Integer.parseInt(PropertiesFile.getProperty("key_upAim"));
			rightKey = Integer.parseInt(PropertiesFile
					.getProperty("key_rightAim"));
			downKey = Integer.parseInt(PropertiesFile
					.getProperty("key_downAim"));
			leftKey = Integer.parseInt(PropertiesFile
					.getProperty("key_leftAim"));
		}
	}

	@Override
	public void tick() {
		Vector2f direction = new Vector2f();
		if (controller != null) {
			// Controller inputs can be directly put in
			direction.x = controller.getComponent(
					components.get(controllerAimLR)).getPollData();
			direction.y = controller.getComponent(
					components.get(controllerAimUD)).getPollData();
		} else {
			if (Keyboard.isKeyDown(downKey)) {
				direction.y -= 1f;
			}
			if (Keyboard.isKeyDown(upKey)) {
				direction.y += 1f;
			}
			if (Keyboard.isKeyDown(leftKey)) {
				direction.x -= 1f;
			}
			if (Keyboard.isKeyDown(rightKey)) {
				direction.x += 1f;
			}
		}
		// Check if the player has already moved the reticle in the given
		// stroke, we dont want the reticle rapidly switching
		if (direction.x != 0f || direction.y != 0f) {
			if (!hasTargeted) {
				hasTargeted = true;
				// Set the new target based on 2 phase algorithms
				reticle.retarget(select(prune(direction, reticle.targeter.position), reticle.targeter.position));
			}
		} else {
			hasTargeted = false;
		}
	}

	// Remove all far away entities that wont be targeted
	private List<TargetableComponent> prune(Vector2f direction, 
			Vector2f position) {
		List<TargetableComponent> prunedList = new ArrayList<TargetableComponent>();
		for (TargetableComponent target : targetableEntities) {
			Vector2f targetPos = target.transform.position; // Get position of all targets
			float xOff = targetPos.x - position.x;
			float yOff = targetPos.y - position.y;
			// Narrow to nearby only
			if (Math.abs(xOff) < rangeThreshold && Math.abs(yOff) < rangeThreshold) {
				// Narrow to one or two quadrants
				if ((xOff <= 0 && direction.x <= 0) || (xOff >= 0 && direction.x >= 0)) {
					if ((yOff <= 0 && direction.y <= 0) || (yOff >= 0 && direction.y >= 0)) {
						// Check parallelity
						Vector2f offset = new Vector2f(xOff, yOff);
						offset.normalise(offset);
						direction.normalise(direction);
						float difference = Vector2f.dot(direction, offset);
						// Add the entity if it within the threshold
						if (difference >= dotThreshold) {
							prunedList.add(target);
						}
					}
				}
			}
		}
		return prunedList;
	}

	private TransformComponent select(List<TargetableComponent> targets, Vector2f position) {
		float leastLength = rangeThreshold;
		TransformComponent leastLengthTransform = null;
		for (TargetableComponent target : targets) {
			// Check length of offset
			Vector2f targetPosition = target.transform.position;
			Vector2f offset = new Vector2f(targetPosition.x - position.x, targetPosition.y - position.y);
			// Check least length and set transform
			float length = offset.lengthSquared();
			if (length < leastLength && length > selfPrevention) {
				leastLength = length;
				leastLengthTransform = target.transform;
			}
		}
		return leastLengthTransform;
	}

}
