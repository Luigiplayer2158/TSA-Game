package com.darkduckdevelopers.components;



/**
 * Tag an entity as targetable for the auto target component
 * 
 * @author Zachary
 */
public class TargetableComponent extends BaseComponent {

	public TransformComponent transform;
	public boolean isIndexed;
	
	public TargetableComponent(TransformComponent transform) {
		this.transform = transform;
		isIndexed = false;
	}
	
	@Override
	public void tick() {
		if (!isIndexed) {
			AutoTargetComponent.targetableEntities.add(this);
			isIndexed = true;
		}
	}
	
}
