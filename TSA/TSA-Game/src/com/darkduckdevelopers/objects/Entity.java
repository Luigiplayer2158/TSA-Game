package com.darkduckdevelopers.objects;

import java.util.ArrayList;
import java.util.List;

import com.darkduckdevelopers.components.BaseComponent;

public class Entity {
	
	private List<BaseComponent> components = new ArrayList<BaseComponent>();

	public void update() {
		for (BaseComponent comp : components) {
			comp.tick();
		}
	}
	
	public void addComponent(BaseComponent comp) {
		components.add(comp);
	}
	
}
