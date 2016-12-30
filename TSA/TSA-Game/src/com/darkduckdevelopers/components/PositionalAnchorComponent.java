package com.darkduckdevelopers.components;


/**
 * A component that anchors a transform to another transform
 * 
 * @author Zachary
 */
public class PositionalAnchorComponent extends BaseComponent {

	public TransformComponent anchor;
	public TransformComponent object;
	public float offsetX;
	public float offsetY;

	public PositionalAnchorComponent(TransformComponent anchor,
			TransformComponent object) {
		this.anchor = anchor;
		this.object = object;
		// Calculate the relative position of the object to the anchor
		this.offsetX = object.position.x - anchor.position.x;
		this.offsetY = object.position.y - anchor.position.y;
	}

	public void tick() {
		// Move the object to its relative position
		object.position.x = anchor.position.x + offsetX;
		object.position.y = anchor.position.y + offsetY;
	}

}
