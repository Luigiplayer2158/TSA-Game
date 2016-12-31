package com.darkduckdevelopers.util;

import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

/**
 * A class to manage gamepads
 * 
 * @author Zachary
 */
public class ControllerMaster {

	public static Controller[] gamepads;

	/**
	 * Get all connected gamepads and put them in the gamepad array
	 */
	public static void getControllers() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		List<Controller> identifiedControllers = new ArrayList<Controller>();
		System.out.println();
		System.out.println("Controllers:");
		for (int i = 0; i < controllers.length; i++) {
			if (controllers[i].getType().equals(Type.GAMEPAD)) {
				identifiedControllers.add(controllers[i]);
				System.out.println(controllers[i].getName());
			}
		}
		System.out.println();
		gamepads = new Controller[identifiedControllers.size()];
		for (int i = 0; i < gamepads.length; i++) {
			gamepads[i] = identifiedControllers.get(i);
		}
	}

	/**
	 * Poll all gamepads for input
	 */
	public static void tick() {
		for (Controller c : gamepads) {
			c.poll();
		}
	}

}
