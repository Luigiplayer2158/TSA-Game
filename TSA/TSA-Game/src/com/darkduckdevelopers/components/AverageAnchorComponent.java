package com.darkduckdevelopers.components;

/**
 * A component that sets the position to the average of several positions
 * 
 * @author Zachary
 */
public class AverageAnchorComponent extends BaseComponent {

	TransformComponent transform;
	TransformComponent[] transforms;

	public AverageAnchorComponent(TransformComponent transform,
			TransformComponent... transforms) {
		this.transform = transform;
		this.transforms = transforms;
	}

	@Override
	public void tick() {
		// Bypass because averaging 1 value is a waste
		if (transforms.length == 1) {
			transform.position = transforms[0].position;
		} else {
			// Get the average of the anchors
			float xAvg = 0f;
			float yAvg = 0f;
			for (TransformComponent t : transforms) {
				xAvg += t.position.x;
				yAvg += t.position.y;
			}
			xAvg /= transforms.length;
			yAvg /= transforms.length;
			// Set the transform to the anchor average
			transform.position.x = xAvg;
			transform.position.y = yAvg;
		}
	}

}
