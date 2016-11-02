package com.darkduckdevelopers.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.util.Maths;

public class Renderer {

	public static int vaoID;
	public static final float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private Shader shader;
	private Matrix4f viewMatrix;

	public Renderer(Shader shader) {
		this.shader = shader;
		shader.start();
	}

	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.stop();
	}

	public void uploadViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public void render(RenderComponent render) {
		shader.start();
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(render.transform));
		shader.loadNumberOfRows(render.texture.getNumberOfRows());
		shader.loadOffset(render.getTextureXOffset(), render.getTextureYOffset());
		shader.loadUseVM(render.useVM);
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, render.texture.getTextureID());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
