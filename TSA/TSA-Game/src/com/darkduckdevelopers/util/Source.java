package com.darkduckdevelopers.util;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceID;
	
	public Source() {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1f);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1f);
	}
	
	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}
	
	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
	
}
