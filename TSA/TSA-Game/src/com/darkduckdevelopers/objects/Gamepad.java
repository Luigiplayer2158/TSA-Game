package com.darkduckdevelopers.objects;

import java.util.HashMap;

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

	public Gamepad(Controller ctrl) {
		this.controller = ctrl;
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
	public float getInput(String identifier) {
		return controller.getComponent(components.get(identifier)).getPollData();
	}

	public String getName() {
		return controller.getName();
	}

}
