package com.darkduckdevelopers.components;

/**
 * Tag an entity as targetable for the menu target component
 * 
 * @author Zachary
 */
public class MenuTargetableComponent extends BaseComponent {

	public TransformComponent transform;
	public boolean isIndexed;
	public int index;
	public int buttonId;
	
	public MenuTargetableComponent(TransformComponent transform, int index, int buttonId) {
		this.transform = transform;
		this.index = index;
		this.buttonId = buttonId;
		isIndexed = false;
	}
	
	@Override
	public void tick() {
		if (!isIndexed) {
			MenuOptionScrollerComponent.optionTargets.put(transform, buttonId);
			isIndexed = true;
		}
	}
	
}
