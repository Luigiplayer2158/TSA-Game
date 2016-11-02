package com.darkduckdevelopers.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.darkduckdevelopers.main.MainGameLoop;

public class PropertiesFile {

	private static HashMap<String, String> properties = new HashMap<String, String>();

	public static void readFile(String fileName) {
		InputStream inputStream = MainGameLoop.class
				.getResourceAsStream("/com/darkduckdevelopers/res/" + fileName);
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isr);
		String currentLine;
		try {
			while ((currentLine = reader.readLine()) != null) {
				String[] splitLine = currentLine.split("=");
				properties.put(splitLine[0], splitLine[1]);
			}
		} catch (IOException e) {
			System.err
					.println("[ERROR] There was a problem with reading the properties file!");
			e.printStackTrace();
		}
	}

	public static String getProperty(String propertyName) {
		return properties.get(propertyName);
	}

}
