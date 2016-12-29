package com.darkduckdevelopers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.darkduckdevelopers.objects.Entity;

/**
 * A utility allowing remote death
 * 
 * @author Zachary
 */
public class EntityKiller {

	private HashMap<Integer, List<Entity>> entities = new HashMap<Integer, List<Entity>>();

	// Add an entity to be killable
	public void addToHitList(int index, Entity entity) {
		List<Entity> group = entities.get(index); // Get the current group at
													// the index
		if (group != null) {
			group.add(entity); // Add to group if non null
			entities.put(index, group);
		} else {
			group = new ArrayList<Entity>(); // Create new group if null
			entities.put(index, group);
		}
	}

	public void genocide(int index) {
		for (Entity e : entities.get(index)) {
			e.kill(); // These entities have lives too :(
		}
	}

}
