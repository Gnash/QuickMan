package com.gnash.quickman.screens.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.gnash.quickman.FoodType;
import com.gnash.quickman.QuickMain;
import com.gnash.quickman.entities.Ghost;
import com.gnash.quickman.entities.GhostType;
import com.gnash.quickman.entities.MoveDirection;
import com.gnash.quickman.map.GameMap;
import com.gnash.quickman.screens.highscore.HighScoreScreen;

public class GameScreen implements Screen {
	private static final float playerSpeed = 200;

	private static final int xStart = 13;

	private static final int yStart = 7;

	private static final int LIVE_WIDTH = 32;

	private static final float PILL_DURATION = 10;

	// currently unused layout
	private int[][] defaultMapLayout2 = { { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
			{ 3, 0, 1, 1, 1, 1, 1, 1, 1, 3 }, { 3, 1, 3, 3, 3, 3, 3, 3, 1, 3, 3 },
			{ 3, 1, 1, 1, 1, 1, 1, 1, 1, 3 }, { 3, 1, 3, 2, 3, 3, 2, 3, 1, 3, 3 },
			{ 3, 1, 3, 1, 3, 3, 1, 3, 1, 3 }, { 3, 1, 3, 1, 1, 1, 1, 3, 1, 3, 3 },
			{ 3, 1, 3, 3, 3, 3, 3, 3, 1, 3 }, { 3, 1, 3, 3, 3, 3, 3, 3, 1, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 } };

	private int[][] defaultMapLayout = {
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 2, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 2, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 4, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 0, 0, 0, 0, 0, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 5, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 3, 0, 0, 0, 0, 0, 3, 1, 3, 3, 1, 1, 1,
					1, 1, 1, 1, 6 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 0, 0, 0, 0, 0, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					3, 3, 3, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 2, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 3, 3,
					1, 1, 2, 3, 3 },
			{ 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					1, 3, 3, 3, 3 },
			{ 3, 3, 3, 3, 1, 3, 3, 1, 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 1, 3, 3, 1, 3, 3,
					1, 3, 3, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 3, 3, 3, 3, 3, 3, 3, 3,
					3, 3, 1, 3, 3 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 3, 3 },
			{ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
					3, 3, 3, 3, 3 } };

	private QuickMain game;
	public GameMap map;
	private OrthographicCamera camera;
	public SpriteBatch batch;

	public MoveDirection playerMovement = MoveDirection.NONE;
	boolean keepMoving = false;
	public MoveDirection playerLastMovement = MoveDirection.RIGHT;
	private Rectangle player;
	private Texture playerTexture = new Texture(
			Gdx.files.internal("sprites/PacRight1.png"));
	private HashMap<MoveDirection, Texture> playerTexturesClosed;
	private int points;
	private int lives;

	private final Sound dotSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/dotPickup.wav"));
	private final Sound pillSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/pillPickup.wav"));
	private final Sound deathSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/death.wav"));
	private final Sound beepSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/beep1.wav"));
	private final Sound startSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/beep2.wav"));
	private final Sound eatSound = Gdx.audio.newSound(Gdx.files
			.internal("sounds/eatGhost.wav"));

	private float playerTargetX;
	private float playerTargetY;

	private int dotPoints;
	private int pillPoints;
	private int ghostPoints;

	private boolean mouthOpen = false;

	private Array<Ghost> ghosts;
	public Ghost blinky;
	public Ghost pinky;
	public Ghost inky;
	public Ghost clyde;

	private HashMap<MoveDirection, Texture> playerTexturesOpen;

	private boolean playerDead = false;

	public MoveDirection nextMovement = MoveDirection.RIGHT;

	private int countdown;

	private int level;

	private float frightTimer;

	public GameScreen(QuickMain game) {
		this.game = game;
		Gdx.input.setInputProcessor(new GameInputHandler(this));
		game.font.setScale(3);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 32 * 27, 32 * 31 + 70);
		this.batch = new SpriteBatch();

		// Set starting points and the worth of the different pickups
		level = 0;
		lives = 3;
		points = 0;
		dotPoints = 50;
		pillPoints = 125;
		ghostPoints = 200;

		// Load the player textures into the movement direction - dependant
		// HashMap
		playerTexturesClosed = new HashMap<MoveDirection, Texture>();
		playerTexturesClosed.put(MoveDirection.UP,
				new Texture(Gdx.files.internal("sprites/PacUp1.png")));
		playerTexturesClosed.put(MoveDirection.DOWN,
				new Texture(Gdx.files.internal("sprites/PacDown1.png")));
		playerTexturesClosed.put(MoveDirection.LEFT,
				new Texture(Gdx.files.internal("sprites/PacLeft1.png")));
		playerTexturesClosed.put(MoveDirection.RIGHT,
				new Texture(Gdx.files.internal("sprites/PacRight1.png")));

		playerTexturesOpen = new HashMap<MoveDirection, Texture>();
		playerTexturesOpen.put(MoveDirection.UP,
				new Texture(Gdx.files.internal("sprites/PacUp2.png")));
		playerTexturesOpen.put(MoveDirection.DOWN,
				new Texture(Gdx.files.internal("sprites/PacDown2.png")));
		playerTexturesOpen.put(MoveDirection.LEFT,
				new Texture(Gdx.files.internal("sprites/PacLeft2.png")));
		playerTexturesOpen.put(MoveDirection.RIGHT,
				new Texture(Gdx.files.internal("sprites/PacRight2.png")));

		playerTexture = playerTexturesClosed.get(MoveDirection.RIGHT);

		// Make PacMan eat!

		Timer.schedule(new Task() {
			@Override
			public void run() {
				mouthOpen = !mouthOpen;
			}
		}, 0.2f, 0.2f);

		// Create the level content
		nextLevel();

	}

	private void createGhosts() {
		ghosts = new Array<Ghost>();
		blinky = new Ghost(this, map.TILE_WIDTH * 13, map.TILE_HEIGHT * 19,
				GhostType.BLINKY);
		ghosts.add(blinky);

		pinky = new Ghost(this, map.TILE_WIDTH * 13, map.TILE_HEIGHT * 17,
				GhostType.PINKY);
		ghosts.add(pinky);

		inky = new Ghost(this, map.TILE_WIDTH * 13, map.TILE_HEIGHT * 16,
				GhostType.INKY);
		ghosts.add(inky);

		clyde = new Ghost(this, map.TILE_WIDTH * 13, map.TILE_HEIGHT * 15,
				GhostType.CLYDE);
		ghosts.add(clyde);
	}

	@Override
	public void render(float delta) {
		initializeRenderingProcess();
		if (!playerDead && countdown <= 0) {
			updateGameLogic(delta);
		}
		drawGame();
	}

	private void updateGameLogic(float delta) {
		updateFrightTimer(delta);
		updatePlayerTexture(playerLastMovement);
		handlePlayerMovement(delta);
		for (Ghost g : ghosts) {
			g.update(delta);
		}
		checkForCollisions();
	}

	private void initializeRenderingProcess() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	private void drawGame() {
		map.render();
		batch.begin();
		drawPoints();
		drawLives();
		batch.draw(playerTexture, player.x, player.y);
		for (Ghost g : ghosts) {
			batch.draw(g.getTexture(), g.rect.x, g.rect.y, g.rect.width,
					g.rect.height);
		}
		drawCountdownIfNecessary();
		batch.end();
	}

	private void drawCountdownIfNecessary() {
		if (countdown == 4) {
			game.font.draw(batch, "Level " + level, camera.viewportWidth / 2 - 90,
					camera.viewportHeight / 2 + 20);
		} else if (countdown > 0) {
			game.font.draw(batch, String.valueOf(countdown),
					camera.viewportWidth / 2 - 10, camera.viewportHeight / 2 + 20);
		} else if (countdown == 0) {
			game.font.draw(batch, "GO!", camera.viewportWidth / 2 - 35,
					camera.viewportHeight / 2 + 20);
		}
	}

	private void updateFrightTimer(float delta) {
		if (frightTimer > 0) {
			frightTimer -= delta;
			if (frightTimer <= 0) {
				unscareGhosts();
				frightTimer = 0;
			}
		}
	}

	private void unscareGhosts() {
		for (Ghost g : ghosts) {
			g.scared = false;
			if (!g.eaten)
				g.speed = Ghost.NORMAL_SPEED;
		}
	}

	private void drawLives() {
		game.font.draw(batch, "Leben: ", camera.viewportWidth - 3 * LIVE_WIDTH
				- 180 - 32, 1050);
		for (int i = 0; i < lives; i++) {
			batch.draw(playerTexturesOpen.get(MoveDirection.RIGHT),
					camera.viewportWidth - (3 - i) * LIVE_WIDTH, 1015);
		}
	}

	private void checkForCollisions() {
		for (Ghost g : ghosts) {
			if (g.rect.overlaps(player) && !g.eaten) {
				if (g.scared) {
					g.scared = false;
					g.eaten = true;
					points += ghostPoints;
					eatSound.play();
				} else {
					deathSound.play();
					playerDead = true;
					startRespawnTimer();
				}
			}
		}
	}

	private void startRespawnTimer() {
		Timer.schedule(new Task() {

			@Override
			public void run() {
				playerDead = false;
				lives--;
				if (lives > 0) {
					resetCharacters();
				} else {
					game.setScreen(new HighScoreScreen(game, points));
					dispose();
				}
			}

		}, 1.5f);
	}

	private void drawPoints() {
		game.font.draw(batch, "Punkte: " + points, 30, 1050);
	}

	private void handlePlayerMovement(float delta) {
		if (isPlayerMoving()) {
			FoodType food = FoodType.NONE;
			switch (playerMovement) {
			case UP:
				food = handlePlayerMovementUp(delta);
				break;
			case DOWN:
				food = handlePlayerMovementDown(delta);
				break;
			case LEFT:
				food = handlePlayerMovementLeft(delta);
				break;
			case RIGHT:
				food = handlePlayerMovementRight(delta);
				break;
			case NONE:
				break;
			default:
				break;
			}
			handlePickup(food);
		}
	}

	private FoodType handlePlayerMovementUp(float delta) {
		FoodType food = FoodType.NONE;
		player.y += playerSpeed * delta;
		if (player.y >= playerTargetY) {
			player.y = playerTargetY;
			food = map.enterTile(player.x, player.y);
			playerMovement = MoveDirection.NONE;
			if (!map.isBlocked(nextMovement, player.x, player.y)) {
				movePlayer(nextMovement);
			} else {
				movePlayerUpOneTile();
			}
		}
		return food;
	}

	private FoodType handlePlayerMovementDown(float delta) {
		FoodType food = FoodType.NONE;
		player.y -= playerSpeed * delta;
		if (player.y <= playerTargetY) {
			player.y = playerTargetY;
			food = map.enterTile(player.x, player.y);
			playerMovement = MoveDirection.NONE;
			if (!map.isBlocked(nextMovement, player.x, player.y)) {
				movePlayer(nextMovement);
			} else {
				movePlayerDownOneTile();
			}
		}
		return food;
	}

	private FoodType handlePlayerMovementLeft(float delta) {
		FoodType food = FoodType.NONE;
		player.x -= playerSpeed * delta;
		if (player.x <= playerTargetX) {
			player.x = playerTargetX;
			food = map.enterTile(player.x, player.y);
			playerMovement = MoveDirection.NONE;
			if (food == FoodType.TP_LEFT) {
				player.x = map.playerTile.x * map.TILE_WIDTH;
				player.y = map.playerTile.y * map.TILE_HEIGHT;
				movePlayerLeftOneTile();
			} else if (!map.isBlocked(nextMovement, player.x, player.y)) {
				movePlayer(nextMovement);
			} else {
				movePlayerLeftOneTile();
			}
		}
		return food;
	}

	private FoodType handlePlayerMovementRight(float delta) {
		FoodType food = FoodType.NONE;
		player.x += playerSpeed * delta;
		if (player.x >= playerTargetX) {
			player.x = playerTargetX;
			food = map.enterTile(player.x, player.y);
			playerMovement = MoveDirection.NONE;
			if (food == FoodType.TP_RIGHT) {
				player.x = map.playerTile.x * map.TILE_WIDTH;
				player.y = map.playerTile.y * map.TILE_HEIGHT;
				movePlayerRightOneTile();
			} else if (!map.isBlocked(nextMovement, player.x, player.y)) {
				movePlayer(nextMovement);
			} else {
				movePlayerRightOneTile();
			}
		}
		return food;
	}

	private void scareGhosts() {
		frightTimer = PILL_DURATION;
		for (Ghost g : ghosts) {
			g.scare();
		}

	}

	private void movePlayer(MoveDirection dir) {
		switch (dir) {
		case DOWN:
			movePlayerDownOneTile();
			break;
		case LEFT:
			movePlayerLeftOneTile();
			break;
		case NONE:
			break;
		case RIGHT:
			movePlayerRightOneTile();
			break;
		case UP:
			movePlayerUpOneTile();
			break;
		default:
			break;
		}
	}

	private void updatePlayerTexture(MoveDirection direction) {
		if (mouthOpen) {
			playerTexture = playerTexturesOpen.get(direction);
		} else {
			playerTexture = playerTexturesClosed.get(direction);
		}
	}

	private void handlePickup(FoodType food) {
		switch (food) {
		case DOT:
			dotSound.play();
			points += dotPoints;
			break;
		case PILL:
			pillSound.play(0.5f);
			points += pillPoints;
			scareGhosts();
			break;
		case NONE:
			break;
		default:
			break;

		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isPlayerMoving() {
		return playerMovement != MoveDirection.NONE;
	}

	public boolean movePlayerLeftOneTile() {
		if (!map.getTileByPixel(player.x - 1, player.y).isBlocked()) {
			playerMovement = MoveDirection.LEFT;
			playerLastMovement = playerMovement;
			playerTargetX = player.x - map.TILE_WIDTH;
			playerTargetY = player.y;
			return true;
		}
		return false;
	}

	public boolean movePlayerRightOneTile() {
		if (player.x <= map.getPixelWidth()
				&& !map.getTileByPixel(player.x + map.TILE_WIDTH, player.y).isBlocked()) {
			playerMovement = MoveDirection.RIGHT;
			playerLastMovement = playerMovement;
			playerTargetX = player.x + map.TILE_WIDTH;
			playerTargetY = player.y;
			return true;
		}
		return false;
	}

	public boolean movePlayerDownOneTile() {
		if (player.y > 0 && !map.getTileByPixel(player.x, player.y - 1).isBlocked()) {
			playerMovement = MoveDirection.DOWN;
			playerLastMovement = playerMovement;
			playerTargetY = player.y - map.TILE_HEIGHT;
			playerTargetX = player.x;
			return true;
		}
		return false;
	}

	public boolean movePlayerUpOneTile() {
		if (player.x <= map.getPixelHeight()
				&& !map.getTileByPixel(player.x, player.y + map.TILE_HEIGHT)
						.isBlocked()) {
			playerMovement = MoveDirection.UP;
			playerLastMovement = playerMovement;
			playerTargetY = player.y + map.TILE_HEIGHT;
			playerTargetX = player.x;
			return true;
		}
		return false;
	}

	public void nextLevel() {
		level++;
		map = new GameMap(this, defaultMapLayout, xStart, yStart);
		resetCharacters();

	}

	public void finishLevel() {
		nextLevel();
	}

	private void resetCharacters() {
		player = new Rectangle(map.TILE_WIDTH * map.xStart, map.TILE_HEIGHT
				* map.yStart, map.TILE_WIDTH, map.TILE_HEIGHT);
		playerMovement = MoveDirection.NONE;
		nextMovement = MoveDirection.RIGHT;
		keepMoving = false;
		playerLastMovement = MoveDirection.RIGHT;
		updatePlayerTexture(playerLastMovement);
		createGhosts();
		countdown = 5;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				countdown--;
				if (countdown == 4) {

				} else if (countdown > 0) {
					beepSound.play();
				} else if (countdown == 0) {
					startSound.play();
				}
			}

		}, 0, 1, 5);
	}
}
