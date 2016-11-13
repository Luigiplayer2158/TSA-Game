package com.darkduckdevelopers.components;

/**
 * A component for telling when an entity is ticked
 * 
 * @author Zachary
 */
public class DebugComponent extends BaseComponent {

	@Override
	public void tick() {
		System.out.println("Debug component ticked");
	}

}
