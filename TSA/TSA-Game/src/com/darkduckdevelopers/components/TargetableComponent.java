package com.darkduckdevelopers.components;



/**
 * Tag an entity as targetable for the auto target component
 * 
 * @author Zachary
 */
public class TargetableComponent extends BaseComponent {

	public TransformComponent transform;
	public boolean isIndexed;
	public int index;
	
	public TargetableComponent(TransformComponent transform, int index) {
		this.transform = transform;
		this.index = index;
		isIndexed = false;
	}
	
	@Override
	public void tick() {
		if (!isIndexed) {
			AutoTargetComponent.targetableEntities.get(index).add(this);
			isIndexed = true;
		}
	}
	
}
