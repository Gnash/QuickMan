package com.gnash.quickman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.gnash.quickman.map.MapTile;
import com.gnash.quickman.screens.GameScreen;

public class Ghost {
	private static final Texture scaredTexture = new Texture(Gdx.files.internal("sprites/ScaredGhost1.png"));
	private static final Texture eatenTexture = new Texture(Gdx.files.internal("sprites/EatenGhost1.png"));
	public static final int NORMAL_SPEED = 180;
	public static final int SCARED_SPEED = 120;
	public MapTile scatterTile;
	public Rectangle rect;
	private MoveDirection direction;
	public MoveDirection lastDirection;
	public Texture texture;
	public int speed = NORMAL_SPEED;
	private float targetY;
	private float targetX;
	private GameScreen screen;
	private GhostMover mover;
	public boolean eaten;
	public boolean scared;
	MapTile currentTile;

	public Ghost(GameScreen screen, int x, int y, GhostType type) {
		currentTile = screen.map.getTileByPixel(x, y);
		rect = new Rectangle(x, y, screen.map.TILE_WIDTH,
				screen.map.TILE_HEIGHT);
		this.screen = screen;
		this.eaten = false;
		this.scared = false;
		switch (type) {
		case BLINKY:
			texture = new Texture(Gdx.files.internal("sprites/RedGhost1.png"));
			mover = new BlinkyMover(this, screen.map);
			scatterTile = screen.map.getTileByIndex(25, 29);
			break;
		case PINKY:
			texture = new Texture(
					Gdx.files.internal("sprites/PurpleGhost1.png"));
			mover = new PinkyMover(this, screen.map);
			scatterTile = screen.map.getTileByIndex(1, 29);
			break;
		case INKY:	
			texture = new Texture(Gdx.files.internal("sprites/GreenGhost1.png"));
			mover = new InkyMover(this, screen.map);
			scatterTile = screen.map.getTileByIndex(25, 1);
			break;
		case CLYDE:
			texture = new Texture(
					Gdx.files.internal("sprites/OrangeGhost1.png"));
			mover = new ClydeMover(this, screen.map);
			scatterTile = screen.map.getTileByIndex(1, 1);
			break;
		default:
			break;
		}
		direction = MoveDirection.NONE;
		lastDirection = MoveDirection.NONE;
	}

	public void render() {
	}

	private void handleMovement(float delta) {
		switch (direction) {
		case DOWN:
			rect.y -= speed * delta;
			if (rect.y <= targetY) {
				rect.y = targetY;
				direction = MoveDirection.NONE;
			}
			break;
		case LEFT:
			rect.x -= speed * delta;
			if (rect.x <= targetX) {
				rect.x = targetX;
				direction = MoveDirection.NONE;
			}
			break;
		case RIGHT:
			rect.x += speed * delta;
			if (rect.x >= targetX) {
				rect.x = targetX;
				direction = MoveDirection.NONE;
			}
			break;
		case UP:
			rect.y += speed * delta;
			if (rect.y >= targetY) {
				rect.y = targetY;
				direction = MoveDirection.NONE;
			}
			break;
		case NONE:
			break;
		default:
			break;
		}
		if (direction == MoveDirection.NONE) {
			currentTile = screen.map.getTileByPixel(rect.x, rect.y);
			if (currentTile == null) {
				System.out.println(rect.x);
			}
			if (currentTile == screen.map.homeTile) {
				this.eaten = false;
				this.scared = false;
				this.speed = NORMAL_SPEED;
			} else if (currentTile == screen.map.rightTeleporter) {
				currentTile = screen.map.leftTeleporter;
				rect.x = screen.map.leftTeleporter.x * screen.map.TILE_WIDTH;
			} else if (currentTile == screen.map.leftTeleporter) {
				currentTile = screen.map.rightTeleporter;
				rect.x = screen.map.rightTeleporter.x * screen.map.TILE_WIDTH;
			}
		}
	}

	public void setTarget(float x, float y) {
		targetX = x;
		targetY = y;
	}

	public void update(float delta) {
		if (direction == MoveDirection.NONE) {
			if (eaten) {
				direction = screen.map.getNextStepTowardsTile(currentTile, screen.map.homeTile, lastDirection);
				mover.move(direction);
			}
			else if (scared) {
				direction = screen.map.getNextStepTowardsTile(currentTile, scatterTile, lastDirection);
				mover.move(direction);
			} else {
				direction = mover.decideDirection();
			}
			lastDirection = direction;
		}
		handleMovement(delta);
	}

	public Texture getTexture() {
		if (eaten) {
			return eatenTexture;
		} else if (scared) {
			return scaredTexture;
		} else {
			return texture;
		}
	}
}
