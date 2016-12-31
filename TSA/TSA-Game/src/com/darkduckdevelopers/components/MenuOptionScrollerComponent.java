package com.darkduckdevelopers.components;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.main.MainGameLoop;
import com.darkduckdevelopers.util.PropertiesFile;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

/**
 * Auto target component with more menu-esque abilities
 * 
 * @author Zachary
 */
public class MenuOptionScrollerComponent extends BaseComponent {

	public static HashMap<TransformComponent, Integer> optionTargets;
	public HashMap<String, Identifier> components;
	public FollowerComponent selector;
	public Controller controller;
	public String controllerAimUD;
	public String controllerAimLR;
	public String controllerSelectForward;
	public String controllerSelectBackward;
	public boolean hasTargeted;
	public boolean hasSelected;
	public float dotThreshold;
	public float rangeThreshold;
	public float selfPrevention;
	public int index;
	public int upKey;
	public int rightKey;
	public int downKey;
	public int leftKey;
	public int selectKey;
	public int backKey;

	// A lot of this code can be seen in auto target component
	public MenuOptionScrollerComponent(FollowerComponent selector,
			Controller controller, int index) {
		if (optionTargets == null) {
			optionTargets = new HashMap<TransformComponent, Integer>();
		}
		this.selector = selector;
		this.controller = controller;
		this.index = index;
		this.dotThreshold = Float.parseFloat(PropertiesFile.getProperty("game_"
				+ index + "_aimDotTolerance"));
		this.rangeThreshold = Float.parseFloat(PropertiesFile
				.getProperty("game_" + index + "_aimRangeTolerance"));
		this.selfPrevention = Float.parseFloat(PropertiesFile
				.getProperty("game_" + index + "_aimSelfPrevention"));
		if (controller != null) {
			components = new HashMap<String, Identifier>();
			for (Component c : controller.getComponents()) {
				components.put(c.getName(), c.getIdentifier());
			}
			controllerAimUD = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_aimVert");
			controllerAimLR = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_aimHor");
			controllerSelectForward = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_select");
			controllerSelectBackward = PropertiesFile.getProperty("controller_"
					+ controller.getName().toLowerCase() + "_back");
		} else {
			upKey = Integer.parseInt(PropertiesFile.getProperty("key_upAim"));
			rightKey = Integer.parseInt(PropertiesFile
					.getProperty("key_rightAim"));
			downKey = Integer.parseInt(PropertiesFile
					.getProperty("key_downAim"));
			leftKey = Integer.parseInt(PropertiesFile
					.getProperty("key_leftAim"));
			selectKey = Integer.parseInt(PropertiesFile
					.getProperty("key_select"));
			backKey = Integer.parseInt(PropertiesFile.getProperty("key_back"));
		}
	}

	@Override
	public void tick() {
		Vector2f direction = new Vector2f();
		if (controller != null) {
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
		if (direction.x != 0f || direction.y != 0f) {
			if (!hasTargeted) {
				hasTargeted = true;
				move(direction);
			}
		} else {
			hasTargeted = false;
		}
		// Select or move back on the button index
		if (controller != null) {
			if (controller
					.getComponent(components.get(controllerSelectForward))
					.getPollData() > 0.5f) {
				if (!hasSelected) {
					hasSelected = true;
					select(optionTargets.get(selector.target));
				}
			} else if (controller.getComponent(
					components.get(controllerSelectBackward)).getPollData() > 0.5f) {
				if (!hasSelected) {
					hasSelected = true;
					back(optionTargets.get(selector.target));
				}
			} else {
				hasSelected = false;
			}
		} else {
			if (Keyboard.isKeyDown(selectKey)) {
				if (!hasSelected) {
					hasSelected = true;
					select(optionTargets.get(selector.target));
				}
			} else if (Keyboard.isKeyDown(backKey)) {
				if (!hasSelected) {
					hasSelected = true;
					back(optionTargets.get(selector.target));
				}
			} else {
				hasSelected = false;
			}
		}
	}

	private void select(int button) {
		if (button == 0) {
			MainGameLoop.gameState = 1;
		}
		if (button == 1) {
			MainGameLoop.stop();
		}
	}

	private void back(int button) {
		System.out.println("backed " + button);
	}

	private void move(Vector2f direction) {
		for (TransformComponent target : optionTargets.keySet()) {
			Vector2f offset = new Vector2f(target.position.x
					- selector.targeter.position.x, target.position.y
					- selector.targeter.position.y);
			Vector2f offsetNorm = new Vector2f(0f, 0f);
			Vector2f directionNorm = new Vector2f(0f, 0f);
			offset.normalise(offsetNorm);
			direction.normalise(directionNorm);
			float difference = Vector2f.dot(directionNorm, offsetNorm);
			float leastLength = rangeThreshold;
			TransformComponent leastLengthTransform = null;
			if (difference >= dotThreshold) {
				float length = offset.lengthSquared();
				if (length < leastLength && length > selfPrevention) {
					leastLength = length;
					leastLengthTransform = target;
				}
			}
			if (leastLengthTransform != null) {
				selector.retarget(leastLengthTransform);
			}
		}
	}

}
