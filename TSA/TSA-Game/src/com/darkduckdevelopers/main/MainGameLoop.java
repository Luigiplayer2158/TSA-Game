package com.darkduckdevelopers.main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.darkduckdevelopers.components.AutoTargetComponent;
import com.darkduckdevelopers.components.AverageAnchorComponent;
import com.darkduckdevelopers.components.CameraComponent;
import com.darkduckdevelopers.components.CollideComponent;
import com.darkduckdevelopers.components.FollowerComponent;
import com.darkduckdevelopers.components.MenuOptionScrollerComponent;
import com.darkduckdevelopers.components.MenuTargetableComponent;
import com.darkduckdevelopers.components.PlayerComponent;
import com.darkduckdevelopers.components.ProjectileComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.SpinComponent;
import com.darkduckdevelopers.components.TargetableComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import com.darkduckdevelopers.objects.ShapeTexture;
import com.darkduckdevelopers.render.DisplayManager;
import com.darkduckdevelopers.render.Renderer;
import com.darkduckdevelopers.shaders.Shader;
import com.darkduckdevelopers.util.AudioMaster;
import com.darkduckdevelopers.util.ControllerMaster;
import com.darkduckdevelopers.util.EntityKiller;
import com.darkduckdevelopers.util.LevelImporter;
import com.darkduckdevelopers.util.Loader;
import com.darkduckdevelopers.util.PropertiesFile;
import com.darkduckdevelopers.util.TextMaster;

/**
 * The main class of our game
 * 
 * @author Zachary
 */
public class MainGameLoop {

	private static Loader loader;
	private static Shader shader;
	private static Renderer renderer;
	private static EntityKiller killer;
	private static TextMaster textMaster;

	private static Entity splashScreen;
	private static List<Entity> temporaryGameEntities = new ArrayList<Entity>();
	private static List<Entity> permanantGameEntities = new ArrayList<Entity>();
	private static List<Entity> menuEntities = new ArrayList<Entity>();
	private static List<Entity> usefulEntities = new ArrayList<Entity>();

	private static int escapeKey;
	private static float unitSize;
	private static float gravity;
	private static float splashTime;
	
	public static int gameState;
	public static boolean debug;

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
		gameState = 2;

		/**
		 * Read the properties file
		 */
		PropertiesFile.readFile("properties.txt");
		// Init variables
		int displayWidth = Integer.parseInt(PropertiesFile
				.getProperty("display_width"));
		int displayHeight = Integer.parseInt(PropertiesFile
				.getProperty("display_height"));
		String displayName = PropertiesFile.getProperty("display_name");
		escapeKey = Integer.parseInt(PropertiesFile.getProperty("key_escape"));
		String shaderVertexName = PropertiesFile.getProperty("file_normalVert");
		String shaderFragmentName = PropertiesFile
				.getProperty("file_normalFrag");
		// Global variables
		unitSize = Float
				.parseFloat(PropertiesFile.getProperty("game_unitSize"));
		gravity = Float.parseFloat(PropertiesFile.getProperty("game_gravity"));
		debug = Boolean.parseBoolean(PropertiesFile.getProperty("game_debug"));
		// Splash screen
		// TODO splash screen
		splashTime = Float.parseFloat(PropertiesFile.getProperty("game_splashTime"));
		splashScreen = new Entity();
		TransformComponent splashTransform = new TransformComponent(new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));

		/**
		 * Create the gamepads and clear out values because they like to spike
		 */
		ControllerMaster.getControllers();
		ControllerMaster.tick();
		ControllerMaster.tick();

		DisplayManager.createDisplay(displayWidth, displayHeight, displayName,
				false); // Create the display
		shader = new Shader(shaderVertexName, shaderFragmentName); // Initialize
																	// shader
		loader = new Loader(); // Initialize loader
		renderer = new Renderer(shader); // Initialize renderer
		killer = new EntityKiller();
		textMaster = new TextMaster(loader, renderer, killer, "file_blackFont"); // Initialize
																					// the
		// data in the
		// text
		// renderer
		loader.loadToVAO(Renderer.positions); // Load the quad VAO that will be
												// used for everything
		initPermanantEntities();
		initTemporaryEntities();
		initMenuEntities();
		DisplayManager.update();
		
	}

	/**
	 * Tick all components and render the screen. This method shouldn't need to
	 * be changed
	 */
	private static void loop() {
		ControllerMaster.tick();
		renderer.prepare(); // Clear the screen
		if (gameState == 0) {
			// A splash screen takes up time, but it will be more impressive
			splashScreen.update();
			splashTime += DisplayManager.getFrameTimeSeconds();
		} else if (gameState == 1) {
			for (Entity e : temporaryGameEntities) { // Loop through all
														// level-specific
														// entities
				if (e.update()) { // Update entity's components
					usefulEntities.add(e);
				}
			}
			temporaryGameEntities.clear();
			temporaryGameEntities.addAll(usefulEntities); // Garbage collection
			usefulEntities.clear();
			for (Entity e : permanantGameEntities) { // Things like the player
				if (e.update()) {
					usefulEntities.add(e);
				}
			}
			permanantGameEntities.clear();
			permanantGameEntities.addAll(usefulEntities);
			usefulEntities.clear();
		} else if (gameState == 2) {
			for (Entity e : menuEntities) { // Menu screen stuff
				if (e.update()) {
					usefulEntities.add(e);
				}
			}
			menuEntities.clear();
			menuEntities.addAll(usefulEntities);
			usefulEntities.clear();
		}
		if (Keyboard.isKeyDown(escapeKey)) {
			gameState = 2;
		}
		if (Display.isCloseRequested()) {
			stop();
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

	// /////////////////////////////////////////
	// WARNING: IMMENSE ENTITY CREATION BELOW //
	// /////////////////////////////////////////

	private static void initPermanantEntities() {
		/* Player entity */
		TransformComponent[] playerTransforms = new TransformComponent[ControllerMaster.gamepads.length + 1];
		for (int i = 0; i < ControllerMaster.gamepads.length + 1; i++) {
			String playerType = "aliss";
			Entity player = new Entity();
			TransformComponent playerTransform = new TransformComponent(
					new Vector2f(0f, 0f), 0f, new Vector2f(unitSize * 2, unitSize * 2));
			playerTransforms[i] = playerTransform;
			ShapeTexture playerTexture = new ShapeTexture(loader.loadTexture(playerType + ".png"));
			playerTexture.setNumberOfRows(4);
			RenderComponent playerRender = new RenderComponent(renderer,
					playerTransform, playerTexture, 0, true);
			float playerHitboxSizeX = Float.parseFloat(PropertiesFile.getProperty("game_" + playerType + "_playerHitboxX"));
			float playerHitboxSizeY = Float.parseFloat(PropertiesFile.getProperty("game_" + playerType + "_playerHitboxY"));
			CollideComponent playerCollider = new CollideComponent(
					playerTransform, 1, -2f, new Vector2f(playerHitboxSizeX * unitSize, playerHitboxSizeY * unitSize));
			PlayerComponent playerControl;
			if (i > 0) {
				playerControl = new PlayerComponent(playerCollider,
						ControllerMaster.gamepads[i - 1], playerRender);
			} else {
				playerControl = new PlayerComponent(playerCollider, null, playerRender);
			}
			TargetableComponent playerTargetable = new TargetableComponent(
					playerTransform, 1);
			player.addComponent(playerRender);
			player.addComponent(playerControl);
			player.addComponent(playerCollider);
			player.addComponent(playerTargetable);
			permanantGameEntities.add(player);
			// Reticle for player
			Entity reticle = new Entity();
			TransformComponent reticleTransform = new TransformComponent(
					new Vector2f(0f, 0f), 0f, new Vector2f(unitSize, unitSize));
			RenderComponent reticleRender = new RenderComponent(renderer,
					reticleTransform, new ShapeTexture(
							loader.loadTexture("reticle.png")), 0, true);
			FollowerComponent reticleFollower = new FollowerComponent(
					reticleTransform, playerTransform);
			AutoTargetComponent reticleControl = new AutoTargetComponent(
					reticleFollower, playerControl.controller, 1);
			SpinComponent reticleSpin = new SpinComponent(reticleTransform,
					360f);
			reticle.addComponent(reticleRender);
			reticle.addComponent(reticleFollower);
			reticle.addComponent(reticleControl);
			reticle.addComponent(reticleSpin);
			permanantGameEntities.add(reticle);
		}

		/* Camera entity */
		Entity gameCamera = new Entity();
		TransformComponent cameraTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		CameraComponent cameraComp = new CameraComponent(cameraTransform,
				renderer);
		cameraComp.tick();
		gameCamera.addComponent(cameraComp);
		permanantGameEntities.add(gameCamera);
		AverageAnchorComponent cameraAnchor = new AverageAnchorComponent(
				cameraTransform, playerTransforms);
		gameCamera.addComponent(cameraAnchor);
		// Try text
		textMaster.drawText("about time i changed this", unitSize / 5f, 0f, -0.5f, true,
				-1, 100, permanantGameEntities);
	}

	private static void initTemporaryEntities() {
		//TODO make level importer more integrated
				LevelImporter.loadLevel(temporaryGameEntities, "/com/darkduckdevelopers/res/testlevel.txt", loader, renderer);
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
						new Vector2f((-unitSize * 9) + (i * unitSize * 2),
								(-unitSize * 9) - (j * unitSize * 2)), 0f,
						new Vector2f(unitSize, unitSize));
				RenderComponent render = new RenderComponent(renderer,
						transform, groundTexture, 0, true);
				ground.addComponent(render);
				if (j == 0) {
					CollideComponent collider = new CollideComponent(transform,
							0, 0f, transform.size);
					ground.addComponent(collider);
				}
				TargetableComponent targetable = new TargetableComponent(
						transform, 1);
				ground.addComponent(targetable);
				temporaryGameEntities.add(ground);
			}
		}
		/* Projectile */
		Entity testProjectile = new Entity();
		TransformComponent projectileTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(unitSize / 2f,
						unitSize / 2f));
		RenderComponent projectileRender = new RenderComponent(renderer,
				projectileTransform, new ShapeTexture(
						loader.loadTexture("fireball.png")), 0, true);
		CollideComponent projectileCollide = new CollideComponent(
				projectileTransform, 2, gravity, new Vector2f(unitSize / 2f, unitSize / 2f));
		ProjectileComponent projectile = new ProjectileComponent(
				projectileTransform, projectileCollide);
		TargetableComponent projectileTargetable = new TargetableComponent(
				projectileTransform, 1);
		testProjectile.addComponent(projectileRender);
		testProjectile.addComponent(projectileCollide);
		testProjectile.addComponent(projectile);
		testProjectile.addComponent(projectileTargetable);
		temporaryGameEntities.add(testProjectile);
	}

	private static void initMenuEntities() {
		/* Menu screen transformations */
		Entity mainScreen = new Entity();
		TransformComponent mainScreenTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		TargetableComponent mainScreenTargetable = new TargetableComponent(
				mainScreenTransform, 0);
		mainScreen.addComponent(mainScreenTargetable);
		menuEntities.add(mainScreen);

		Entity optionsScreen = new Entity();
		TransformComponent optionsScreenTransform = new TransformComponent(
				new Vector2f(-2f, 0f), 0f, new Vector2f(1f, 1f));
		TargetableComponent optionsScreenTargetable = new TargetableComponent(
				optionsScreenTransform, 0);
		optionsScreen.addComponent(optionsScreenTargetable);
		menuEntities.add(optionsScreen);

		/* Camera entity */
		Entity menuCamera = new Entity();
		TransformComponent cameraTransform = new TransformComponent(
				new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f));
		CameraComponent cameraComp = new CameraComponent(cameraTransform,
				renderer);
		FollowerComponent cameraFollower = new FollowerComponent(
				cameraTransform, mainScreenTransform);
		AutoTargetComponent cameraControl = new AutoTargetComponent(
				cameraFollower, null, 0);
		menuCamera.addComponent(cameraComp);
		menuCamera.addComponent(cameraFollower);
		menuCamera.addComponent(cameraControl);
		menuEntities.add(menuCamera);

		/* Main menu screen */
		Entity title = new Entity();
		TransformComponent titleTransform = new TransformComponent(
				new Vector2f(0f, 0.5f), 0f, new Vector2f(unitSize * 8f,
						unitSize * 8f));
		RenderComponent titleRender = new RenderComponent(renderer,
				titleTransform, new ShapeTexture(
						loader.loadTexture("title.png")), 0, true);
		title.addComponent(titleRender);
		menuEntities.add(title);

		// Main menu buttons
		Entity playButtonTarget = new Entity();
		TransformComponent playButtonTargetTransform = new TransformComponent(
				new Vector2f(-unitSize * 2f, unitSize), 0f,
				new Vector2f(1f, 1f));
		MenuTargetableComponent playButtonTargetable = new MenuTargetableComponent(
				playButtonTargetTransform, 2, 0);
		playButtonTarget.addComponent(playButtonTargetable);
		menuEntities.add(playButtonTarget);

		textMaster.drawText("PLAY", unitSize / 2f, -unitSize / 2f, unitSize,
				true, -1, 100, menuEntities);

		Entity quitButtonTarget = new Entity();
		TransformComponent quitButtonTargetTransform = new TransformComponent(
				new Vector2f(-unitSize * 2f, 0f), 0f, new Vector2f(1f, 1f));
		MenuTargetableComponent quitButtonTargetable = new MenuTargetableComponent(
				quitButtonTargetTransform, 2, 1);
		quitButtonTarget.addComponent(quitButtonTargetable);
		menuEntities.add(quitButtonTarget);

		textMaster.drawText("QUIT", unitSize / 2f, -unitSize / 2f, 0f, true,
				-1, 100, menuEntities);

		// Main menu targeter
		Entity buttonTargeter = new Entity();
		TransformComponent buttonTargeterTransform = new TransformComponent(
				new Vector2f(-unitSize * 2f, unitSize), 0f, new Vector2f(
						unitSize / 2f, unitSize / 2f));
		FollowerComponent buttonTargeterFollower = new FollowerComponent(
				buttonTargeterTransform, playButtonTargetTransform);
		MenuOptionScrollerComponent buttonTargeterAimer = new MenuOptionScrollerComponent(
				buttonTargeterFollower, null, 2);
		RenderComponent buttonTargeterRender = new RenderComponent(renderer,
				buttonTargeterTransform, new ShapeTexture(
						loader.loadTexture("fireball.png")), 0, true);
		buttonTargeter.addComponent(buttonTargeterFollower);
		buttonTargeter.addComponent(buttonTargeterAimer);
		buttonTargeter.addComponent(buttonTargeterRender);
		menuEntities.add(buttonTargeter);

		/* Options menu screen */
		Entity optionsTitle = new Entity();
		TransformComponent optionsTitleTransform = new TransformComponent(
				new Vector2f(-2f, 0.5f), 0f, new Vector2f(unitSize * 8f,
						unitSize * 8f));
		RenderComponent optionsTitleRender = new RenderComponent(renderer,
				optionsTitleTransform, new ShapeTexture(
						loader.loadTexture("title.png")), 0, true);
		optionsTitle.addComponent(optionsTitleRender);
		menuEntities.add(optionsTitle);
	}

}
