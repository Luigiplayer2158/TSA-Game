package com.darkduckdevelopers.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.CameraComponent;
import com.darkduckdevelopers.components.PlayerComponent;
import com.darkduckdevelopers.components.PositionalAnchorComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.render.Loader;
import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.shapes.ShapeTexture;
import com.darkduckdevelopers.util.AudioMaster;
import com.darkduckdevelopers.util.PropertiesFile;
import com.darkduckdevelopers.util.Text;

public class MainGameLoop {

	private static Loader loader;
	private static Shader shader;
	private static Renderer renderer;
	private static Random rand;

	private static List<Entity> entities = new ArrayList<Entity>();

	private static int escapeKey;
	private static boolean debugMode;
	private static short state;

	public static void main(String[] args) {
		init(); // Initialize everything that will be used in the game
		while (!Display.isCloseRequested()) {
			loop(); // Move everything, process player input, etc
		}
		stop(); // Clean up
	}

	private static void init() {
		rand = new Random(); // Initialize the random for future use

		/**
		 * Read the properties file and create vars from it
		 */
		PropertiesFile.readFile("properties");
		int displayWidth = Integer.parseInt(PropertiesFile
				.getProperty("display_width"));
		int displayHeight = Integer.parseInt(PropertiesFile
				.getProperty("display_height"));
		String displayName = PropertiesFile.getProperty("display_name");
		escapeKey = Integer.parseInt(PropertiesFile.getProperty("key_escape"));
		debugMode = Boolean.parseBoolean(PropertiesFile
				.getProperty("game_debug"));
		String shaderVertexName = PropertiesFile.getProperty("game_normalVert");
		String shaderFragmentName = PropertiesFile
				.getProperty("game_normalFrag");

		DisplayManager.createDisplay(displayWidth, displayHeight, displayName); // Create
																				// the
																				// display
		shader = new Shader(shaderVertexName, shaderFragmentName); // Initialize
																	// shader
		loader = new Loader(); // Initialize loader
		renderer = new Renderer(shader); // Initialize renderer
		System.out.print("[INFO] "); // Console formatting stuff
		Text.initShape(loader, renderer); // Initialize the data in the text
											// renderer
		loader.loadToVAO(Renderer.positions); // Load the quad VAO that will be
												// used for everything

		/**
		 * The following code is all just entity creation. Unique components are
		 * commented on their first appearance.
		 */
		Entity player = new Entity(); // Create a new player entity
		TransformComponent playerTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f)); // Info on
																	// transformation
		RenderComponent playerRender = new RenderComponent(renderer,
				playerTransform, new ShapeTexture(
						loader.loadTexture("player.png")), 0, true); // Component
																		// to
																		// render
																		// the
																		// player
		player.addComponent(playerRender); // Add component. Not adding
											// transform because it's just data
		entities.add(player); // Add player to the entity list so its components
								// are ticked

		Entity camera = new Entity();
		TransformComponent cameraTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		CameraComponent cameraComp = new CameraComponent(cameraTransform,
				renderer); // Create camera component
		cameraComp.tick(); // Ticking to prevent NPE at prepare method
		camera.addComponent(cameraComp);
		entities.add(camera);
		PositionalAnchorComponent anchor = new PositionalAnchorComponent(
				playerTransform, cameraTransform); // Anchor camera to player
		camera.addComponent(anchor);
	}

	private static void loop() {
		renderer.prepare(); // Clear the screen
		for (Entity e : entities) { // Loop through all entities
			e.update(); // Update entity's components
		}
		if (Keyboard.isKeyDown(escapeKey)) { // Check if ESC is down
			stop(); // Stop the game
		}
		DisplayManager.update(); // Update the screen
	}

	public static void stop() {
		loader.cleanUp(); // Delete VAOs, VBOs, textures
		renderer.cleanUp(); // Delete shaders
		DisplayManager.cleanUp(); // Destroy the screen
		AudioMaster.cleanUp(); // Clean up audio
		System.exit(0); // Exit because it's technically still looping
	}

}
