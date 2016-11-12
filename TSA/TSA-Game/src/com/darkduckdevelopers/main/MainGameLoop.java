package com.darkduckdevelopers.main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.CameraComponent;
import com.darkduckdevelopers.components.CollideComponent;
import com.darkduckdevelopers.components.PlayerComponent;
import com.darkduckdevelopers.components.PositionalAnchorComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.util.AudioMaster;
import com.darkduckdevelopers.util.ControllerMaster;
import com.darkduckdevelopers.util.Loader;
import com.darkduckdevelopers.util.PropertiesFile;
import com.darkduckdevelopers.util.Text;

/**
 * The main class of our game
 * 
 * @author Zachary
 */
public class MainGameLoop {

	private static Loader loader;
	private static Shader shader;
	private static Renderer renderer;

	private static List<Entity> entities = new ArrayList<Entity>();

	private static int escapeKey;

	/**
	 * The entry point of execution
	 * 
	 * @param args
	 *            Runtime arguments passed by the cmd
	 */
	public static void main(String[] args) {
		init(); // Initialize everything that will be used in the game
		while (true) { // Note: Stop is called from within loop, so there is no
						// need to stop it
			loop(); // Move everything, process player input, etc
		}
	}

	/**
	 * Initialize the game and load the resources
	 */
	private static void init() {
		/**
		 * Create the gamepads and clear out values because they like to spike
		 */
		ControllerMaster.getControllers();
		ControllerMaster.tick();
		ControllerMaster.tick();

		/**
		 * Read the properties file and create vars from it
		 */
		PropertiesFile.readFile("properties.txt");
		int displayWidth = Integer.parseInt(PropertiesFile.getProperty("display_width"));
		int displayHeight = Integer.parseInt(PropertiesFile.getProperty("display_height"));
		String displayName = PropertiesFile.getProperty("display_name");
		escapeKey = Integer.parseInt(PropertiesFile.getProperty("key_escape"));
		String shaderVertexName = PropertiesFile.getProperty("game_normalVert");
		String shaderFragmentName = PropertiesFile.getProperty("game_normalFrag");

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
		Entity background = new Entity(); // Create a new background entity to
											// hold components
		TransformComponent backgroundTransform = new TransformComponent(new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f)); // Transformation
																															// info
		RenderComponent backgroundRender = new RenderComponent(renderer, backgroundTransform,
				new ShapeTexture(loader.loadTexture("background.png")), 0, false); // Render
																					// the
																					// entity
		background.addComponent(backgroundRender); // Add the component to the
													// ticking queue
		entities.add(background); // Add the entity to update queue

		Entity player = new Entity();
		TransformComponent playerTransform = new TransformComponent(new Vector2f(0f, 0f), 0f, new Vector2f(0.1f, 0.1f));
		RenderComponent playerRender = new RenderComponent(renderer, playerTransform,
				new ShapeTexture(loader.loadTexture("player.png")), 0, true);
		CollideComponent playerCollider = new CollideComponent(playerTransform, 1, -2f);
		PlayerComponent playerControl = new PlayerComponent(playerCollider, ControllerMaster.gamepads[0], 0.7f, 1f,
				0.1f);
		player.addComponent(playerRender);
		player.addComponent(playerControl);
		player.addComponent(playerCollider);
		entities.add(player);

		Entity camera = new Entity();
		TransformComponent cameraTransform = new TransformComponent(new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		CameraComponent cameraComp = new CameraComponent(cameraTransform, renderer); // Create
																						// camera
																						// component
		cameraComp.tick(); // Ticking to prevent initial NPE at prepare method
		camera.addComponent(cameraComp);
		entities.add(camera);
		PositionalAnchorComponent anchor = new PositionalAnchorComponent(playerTransform, cameraTransform); // Anchor
																											// camera
																											// to
																											// player
		camera.addComponent(anchor);

		ShapeTexture groundTexture = new ShapeTexture(loader.loadTexture("ground.png"));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				Entity ground = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(-0.9f + (i * 0.2f), -0.5f - j * 0.2f), 0f, new Vector2f(0.1f, 0.1f));
				RenderComponent render = new RenderComponent(renderer, transform, groundTexture, 0, true);
				ground.addComponent(render);
				if (j == 0) {
					CollideComponent collider = new CollideComponent(transform, 0, 0f);
					ground.addComponent(collider);
				}
				entities.add(ground);
			}
		}
		
		DisplayManager.update(); // Prevent time blinks
	}

	/**
	 * Tick all components and render the screen. This method shouldn't need to
	 * be changed
	 */
	private static void loop() {
		ControllerMaster.tick();
		renderer.prepare(); // Clear the screen
		for (Entity e : entities) { // Loop through all entities
			e.update(); // Update entity's components
		}
		if (Keyboard.isKeyDown(escapeKey) || Display.isCloseRequested()) { // Check
																			// if
																			// ESC
																			// is
																			// down
			stop(); // Stop the game
		}
		DisplayManager.update(); // Update the screen
	}

	/**
	 * Clean up graphics objects and formally stop the game
	 */
	public static void stop() {
		loader.cleanUp(); // Delete VAOs, VBOs, textures
		renderer.cleanUp(); // Delete shaders
		DisplayManager.cleanUp(); // Destroy the screen
		AudioMaster.cleanUp(); // Clean up audio
		System.exit(0); // Exit because it's technically still looping
	}

}
