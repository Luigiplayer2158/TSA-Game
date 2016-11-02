package com.darkduckdevelopers.components;

import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.shapes.ShapeTexture;

public class RenderComponent extends BaseComponent {

	public Renderer renderer;
	public TransformComponent transform;
	public ShapeTexture texture;
	public int index;
	public boolean useVM;

	public RenderComponent(Renderer renderer, TransformComponent transform,
			ShapeTexture texture, int index, boolean useVM) {
		this.renderer = renderer;
		this.transform = transform;
		this.texture = texture;
		this.index = index;
		this.useVM = useVM;
	}

	@Override
	public void tick() {
		renderer.render(this);
	}
	
	public float getTextureXOffset() {
		int column = index % texture.getNumberOfRows();
		return (float) column / (float) texture.getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = index / texture.getNumberOfRows();
		return (float) row / (float) texture.getNumberOfRows();
	}

}
