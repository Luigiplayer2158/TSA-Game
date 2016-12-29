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
import com.darkduckdevelopers.components.ProjectileComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.util.AudioMaster;
import com.darkduckdevelopers.util.ControllerMaster;
import com.darkduckdevelopers.util.LevelImporter;
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

	private static List<Entity> temporaryGameEntities = new ArrayList<Entity>();
	private static List<Entity> permanantGameEntities = new ArrayList<Entity>();
	private static List<Entity> menuEntities = new ArrayList<Entity>();

	private static int gameState;
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
		gameState = 0;

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
		int displayWidth = Integer.parseInt(PropertiesFile
				.getProperty("display_width"));
		int displayHeight = Integer.parseInt(PropertiesFile
				.getProperty("display_height"));
		String displayName = PropertiesFile.getProperty("display_name");
		escapeKey = Integer.parseInt(PropertiesFile.getProperty("key_escape"));
		String shaderVertexName = PropertiesFile.getProperty("file_normalVert");
		String shaderFragmentName = PropertiesFile
				.getProperty("file_normalFrag");

		DisplayManager.createDisplay(displayWidth, displayHeight, displayName); // Create
																				// the
																				// display
		shader = new Shader(shaderVertexName, shaderFragmentName); // Initialize
																	// shader
		loader = new Loader(); // Initialize loader
		renderer = new Renderer(shader); // Initialize renderer
		Text.initShape(loader, renderer); // Initialize the data in the text
											// renderer
		loader.loadToVAO(Renderer.positions); // Load the quad VAO that will be
												// used for everything
		initPermanantEntities(permanantGameEntities);
		initTemporaryEntities(temporaryGameEntities);
		initMenuEntities(menuEntities);
		DisplayManager.update();
	}

	/**
	 * Tick all components and render the screen. This method shouldn't need to
	 * be changed
	 */
	private static void loop() {
		ControllerMaster.tick();
		renderer.prepare(); // Clear the screen
		if (gameState == 1) {
			for (Entity e : temporaryGameEntities) { // Loop through all
														// level-specific
														// entities
				e.update(); // Update entity's components
			}
			for (Entity e : permanantGameEntities) { // Things like the player
				e.update();
			}
		}
		if (gameState == 0) {
			for (Entity e : menuEntities) { // Menu screen stuff
				e.update();
			}
		}
		if (Keyboard.isKeyDown(escapeKey) || Display.isCloseRequested()) {
			stop(); // Stop the game
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
			gameState = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			gameState = 1;
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

	// //////////////////////////////////////////
	// WARNING: IMMENSE ENTITY CREATION BELOW //
	// //////////////////////////////////////////

	private static void initPermanantEntities(List<Entity> entities) {
		/* Player entity */
		TransformComponent playerTransform = null;
		for (int i = 0; i < ControllerMaster.gamepads.length + 1; i++) {
			Entity player = new Entity();
			playerTransform = new TransformComponent(new Vector2f(0f, 0f), 0f,
					new Vector2f(0.1f, 0.1f));
			RenderComponent playerRender = new RenderComponent(renderer,
					playerTransform, new ShapeTexture(
							loader.loadTexture("player.png")), 0, true);
			CollideComponent playerCollider = new CollideComponent(
					playerTransform, 1, -2f);
			PlayerComponent playerControl;
			if (i > 0) {
				playerControl = new PlayerComponent(playerCollider,
						ControllerMaster.gamepads[i - 1], 0.7f, 1f);
			} else {
				playerControl = new PlayerComponent(playerCollider, null, 0.7f,
						1f);
			}
			player.addComponent(playerRender);
			player.addComponent(playerControl);
			player.addComponent(playerCollider);
			permanantGameEntities.add(player);
		}

		/* Camera entity */
		Entity camera = new Entity();
		TransformComponent cameraTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		CameraComponent cameraComp = new CameraComponent(cameraTransform,
				renderer);
		cameraComp.tick();
		camera.addComponent(cameraComp);
		permanantGameEntities.add(camera);
		PositionalAnchorComponent anchor = new PositionalAnchorComponent(
				playerTransform, cameraTransform);
		camera.addComponent(anchor);
	}

	private static void initTemporaryEntities(List<Entity> entities) {
		LevelImporter.loadLevel(entities, "/com/darkduckdevelopers/res/test.txt", loader, renderer);
		/* Background entity */
		Entity background = new Entity();
		TransformComponent backgroundTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		RenderComponent backgroundRender = new RenderComponent(renderer,
				backgroundTransform, new ShapeTexture(
						loader.loadTexture("background.png")), 0, false);
		background.addComponent(backgroundRender);
		temporaryGameEntities.add(background);
		menuEntities.add(background);
		/* Terrain entities */
		ShapeTexture groundTexture = new ShapeTexture(
				loader.loadTexture("ground.png"));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				Entity ground = new Entity();
				TransformComponent transform = new TransformComponent(
						new Vector2f(-0.9f + (i * 0.2f), -0.9f - j * 0.2f), 0f,
						new Vector2f(0.1f, 0.1f));
				RenderComponent render = new RenderComponent(renderer,
						transform, groundTexture, 0, true);
				ground.addComponent(render);
				if (j == 0) {
					CollideComponent collider = new CollideComponent(transform,
							0, 0f);
					ground.addComponent(collider);
				}
				temporaryGameEntities.add(ground);
			}
		}
		Entity testProjectile = new Entity();
		TransformComponent projectileTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(0.05f, 0.05f));
		RenderComponent projectileRender = new RenderComponent(renderer,
				projectileTransform, new ShapeTexture(
						loader.loadTexture("sprites.png")), 0, true);
		CollideComponent projectileCollide = new CollideComponent(
				projectileTransform, 2, -2f);
		ProjectileComponent projectile = new ProjectileComponent(
				projectileTransform, projectileCollide);
		testProjectile.addComponent(projectileRender);
		testProjectile.addComponent(projectileCollide);
		testProjectile.addComponent(projectile);
		temporaryGameEntities.add(testProjectile);
	}

	private static void initMenuEntities(List<Entity> entities) {
		/* Title entity */
		Entity title = new Entity();
		TransformComponent titleTransform = new TransformComponent(
				new Vector2f(0f, 0.5f), 0f, new Vector2f(0.8f, 0.8f));
		RenderComponent titleRender = new RenderComponent(renderer,
				titleTransform, new ShapeTexture(
						loader.loadTexture("title.png")), 0, false);
		title.addComponent(titleRender);
		menuEntities.add(title);
	}

}
