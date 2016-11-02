package com.darkduckdevelopers.shapes;

public class ShapeTexture {

	private int textureID;
	private int numberOfRows = 1;
	
	public ShapeTexture(int texID) {
		this.textureID = texID;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getTextureID() {
		return textureID;
	}
	
}
