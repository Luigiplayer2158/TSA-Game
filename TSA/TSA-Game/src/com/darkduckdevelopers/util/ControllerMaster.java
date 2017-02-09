package com.darkduckdevelopers.util;

import java.util.ArrayList;
import java.util.List;

import com.darkduckdevelopers.objects.Gamepad;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

/**
 * A class to manage gamepads
 * 
 * @author Zachary
 */
public class ControllerMaster {

	public static Gamepad[] gamepads;

	/**
	 * Get all connected gamepads and put them in the gamepad array
	 */
	public static void getControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		List<Controller> identifiedControllers = new ArrayList<Controller>();
		System.out.println();
		System.out.println("Controllers:");
		for (int i = 0; i < controllers.length; i++) {
			if (Boolean.parseBoolean(PropertiesFile.getProperty("controller_" + controllers[i].getName().toLowerCase() + "_usable"))) {
				identifiedControllers.add(controllers[i]);
				System.out.println(controllers[i].getName() + ": " + controllers[i].getType());
			}
		}
		System.out.println();
		gamepads = new Gamepad[identifiedControllers.size()];
		for (int i = 0; i < gamepads.length; i++) {
			gamepads[i] = new Gamepad(identifiedControllers.get(i));
		}
	}

	/**
	 * Poll all gamepads for input
	 */
	public static void tick() {
		for (Gamepad c : gamepads) {
			c.tick();
		}
	}

}
