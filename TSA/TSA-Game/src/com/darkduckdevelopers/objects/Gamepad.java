package com.darkduckdevelopers.objects;

import java.util.HashMap;

import com.darkduckdevelopers.util.PropertiesFile;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier;

/**
 * An interface allowing controller input to be easier to access
 * 
 * @author Zachary
 */
public class Gamepad {

	private HashMap<String, Identifier> components;
	private Controller controller;
	private float buttonMax;
	private float stickMax;
	public boolean vertInvert;

	public Gamepad(Controller ctrl) {
		this.controller = ctrl;
		this.buttonMax = Float.parseFloat(PropertiesFile
				.getProperty("controller_" + controller.getName().toLowerCase()
						+ "_buttonMax"));
		this.stickMax = Float.parseFloat(PropertiesFile
				.getProperty("controller_" + controller.getName().toLowerCase()
						+ "_stickMax"));
		this.vertInvert = Boolean.parseBoolean(PropertiesFile
				.getProperty("controller_" + controller.getName().toLowerCase()
						+ "_vertInvert"));
		// Index all components
		components = new HashMap<String, Identifier>();
		for (Component comp : controller.getComponents()) {
			components.put(comp.getName(), comp.getIdentifier());
		}
	}

	// Poll the controller for new input info
	public void tick() {
		controller.poll();
	}

	// Get the value stored in a controller component
	public float getInput(String identifier, boolean isButton) {
		if (isButton) {
			return controller.getComponent(components.get(identifier))
					.getPollData() / buttonMax;
		} else {
			return controller.getComponent(components.get(identifier))
					.getPollData() / stickMax;
		}
	}

	public String getName() {
		return controller.getName();
	}

}
