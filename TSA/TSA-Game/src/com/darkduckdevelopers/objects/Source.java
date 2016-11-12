package com.darkduckdevelopers.objects;

import org.lwjgl.openal.AL10;

/**
 * A class representing an audio source. This code will eventually be
 * implemented in a source component or something
 * 
 * @author Zachary
 */
public class Source {

	private int sourceID;

	public Source() {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1f);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1f);
	}

	/**
	 * Play a sound from the source
	 * 
	 * @param buffer
	 *            The id of the sound
	 */
	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	/**
	 * Delete this source
	 */
	public void delete() {
		AL10.alDeleteSources(sourceID);
	}

}
