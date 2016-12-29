package com.darkduckdevelopers.objects;

import java.util.ArrayList;
import java.util.List;

import com.darkduckdevelopers.components.BaseComponent;

/**
 * A class that holds components
 * 
 * @author Zachary
 */
public class Entity {

	private List<BaseComponent> components = new ArrayList<BaseComponent>();
	private boolean killed;

	/**
	 * Update all components of this entity
	 */
	public boolean update() {
		boolean active = false;
		for (BaseComponent comp : components) {
			comp.tick();
			active = true;
		}
		return active;
	}

	// Kill an entity, it will be collected
	public void kill() {
		components.clear();
		killed = true;
	}

	/**
	 * Add a component to this entity
	 * 
	 * @param comp
	 *            The component to add
	 */
	public void addComponent(BaseComponent comp) {
		if (!killed) { // Nothing can save the dead entities
			components.add(comp);
		}
	}

}
