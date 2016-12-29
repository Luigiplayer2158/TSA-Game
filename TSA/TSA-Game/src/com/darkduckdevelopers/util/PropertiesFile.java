package com.darkduckdevelopers.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.darkduckdevelopers.main.MainGameLoop;

/**
 * A class representing a properties file
 * 
 * @author Zachary
 */
public class PropertiesFile {

	// /////////////////////////////////////////////////////////////////////////
	// SEE THE PROPERTIES FILE USED IN /com/darkduckdevelopers/res/properties //
	// /////////////////////////////////////////////////////////////////////////

	private static HashMap<String, String> properties = new HashMap<String, String>();

	/**
	 * Read a properties file and store everything in the index
	 * 
	 * @param fileName
	 *            The properties file name
	 */
	public static void readFile(String fileName) {
		InputStream inputStream = MainGameLoop.class
				.getResourceAsStream("/com/darkduckdevelopers/res/" + fileName);
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isr); // Create a reader
		String currentLine;
		try {
			while ((currentLine = reader.readLine()) != null) {
				if (!currentLine.startsWith("//")
						&& !currentLine.trim().equals("")) {
					String[] splitLine = currentLine.split("="); // Split the
																	// line
																	// at the =
					properties.put(splitLine[0], splitLine[1]); // Index the
																// property
				}
			}
		} catch (IOException e) {
			System.err
					.println("[ERROR] There was a problem with reading the properties file!");
			e.printStackTrace();
		}
	}

	/**
	 * Search the index for a property
	 * 
	 * @param propertyName
	 *            The property title
	 * @return The string value of the property
	 */
	public static String getProperty(String propertyName) {
		return properties.get(propertyName);
	}

}
