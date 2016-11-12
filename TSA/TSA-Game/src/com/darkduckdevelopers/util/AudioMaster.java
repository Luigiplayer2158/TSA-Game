package com.darkduckdevelopers.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

/**
 * The god of all audio handling
 * 
 * @author Zachary
 */
public class AudioMaster {

	private static List<Integer> buffers = new ArrayList<Integer>();

	/**
	 * Initialize the audio system
	 */
	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load a wav sound file
	 * 
	 * @param file
	 *            The file path to load
	 * @return The integer id of the sound
	 */
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(Class.class
				.getResourceAsStream(file));
		AL10.alBufferData(buffer, waveFile.format, waveFile.data,
				waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	/**
	 * Delete all audio buffers
	 */
	public static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}

}
