package com.darkduckdevelopers.main;

import com.darkduckdevelopers.util.AudioMaster;
import com.darkduckdevelopers.util.Source;

public class SoundTesting {

	public static void main(String[] args) {
		AudioMaster.init();
		int shoot = AudioMaster
				.loadSound("/com/darkduckdevelopers/res/shoot.wav");
		int reload = AudioMaster
				.loadSound("/com/darkduckdevelopers/res/reload.wav");
		Source[] sources = new Source[6];
		for (int i = 0; i < sources.length; i++) {
			sources[i] = new Source();
		}
		while (true) {
			for (int i = 0; i < sources.length; i++) {
				if (i < 0) {
					sources[i].play(shoot);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					sources[i].play(shoot);
					try {
						Thread.sleep(112);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			sources[0].play(reload);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
