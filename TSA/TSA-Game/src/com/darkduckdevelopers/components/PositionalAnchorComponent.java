package com.darkduckdevelopers.components;

public class PositionalAnchorComponent extends BaseComponent {

	public TransformComponent anchor;
	public TransformComponent object;
	public float offsetX;
	public float offsetY;
	
	public PositionalAnchorComponent(TransformComponent anchor, TransformComponent object) {
		this.anchor = anchor;
		this.object = object;
		this.offsetX = object.position.x - anchor.position.x;
		this.offsetY = object.position.y - anchor.position.y;
	}
	
	public void tick() {
		object.position.x = anchor.position.x + offsetX;
		object.position.y = anchor.position.y + offsetY;
	}
	
}
