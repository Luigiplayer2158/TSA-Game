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

	/**
	 * Update all components of this entity
	 */
	public void update() {
		for (BaseComponent comp : components) {
			comp.tick();
		}
	}

	/**
	 * Add a component to this entity
	 * 
	 * @param comp
	 *            The component to add
	 */
	public void addComponent(BaseComponent comp) {
		components.add(comp);
	}

}
