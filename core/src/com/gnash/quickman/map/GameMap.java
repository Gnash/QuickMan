package com.gnash.quickman.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gnash.quickman.FoodType;
import com.gnash.quickman.entities.MoveDirection;
import com.gnash.quickman.screens.GameScreen;

public class GameMap {
	public final int xStart;
	public final int yStart;
	public final int TILE_WIDTH = 32;
	public final int TILE_HEIGHT = 32;
	public int width;
	public int height;
	private MapTile[][] tiles;
	private Map<TileType, Texture> tileTextures;
	public GameScreen screen;
	private int dotCount;
	public MapTile playerTile;
	public MapTile homeTile;
	public MapTile rightTeleporter;
	public MapTile leftTeleporter;

	public GameMap(GameScreen screen, int[][] map, int xStart, int yStart) {
		this.dotCount = 0;
		generateMap(map);
		loadTextures();
		this.screen = screen;
		this.xStart = xStart;
		this.yStart = yStart;
		this.playerTile = getTileByIndex(xStart, yStart);
		this.homeTile = getTileByIndex(13, 16);
	}

	private void generateMap(int[][] map) {
		height = map.length;
		width = map[0].length;
		tiles = new MapTile[height][width];
		for (int y = 0; y < map.length; y++) {
			int[] row = map[y];
			for (int x = 0; x < row.length; x++) {
				if (row[x] == 1 || row[x] == 2) {
					dotCount++;
				}
				MapTile newTile = new MapTile(x - 1, height - 1 - y,
						TileType.values()[row[x]]);
				tiles[height - 1 - y][x] = newTile;
				if (newTile.type == TileType.TP_LEFT) {
					leftTeleporter = newTile;
				} else if (newTile.type == TileType.TP_RIGHT) {
					rightTeleporter = newTile;
				}
			}
		}
	}

	public void render() {
		screen.batch.begin();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width - 2; x++) {
				MapTile t = getTileByIndex(x, y);
				if (t == null) {
					System.out.println(x + ", " + y);
				}
				screen.batch.draw(tileTextures.get(t.type), x * TILE_WIDTH, y
						* TILE_HEIGHT);
			}
		}
		screen.batch.end();
	}

	private void loadTextures() {
		tileTextures = new HashMap<TileType, Texture>();
		tileTextures.put(TileType.FREE,
				new Texture(Gdx.files.internal("sprites/free.png")));
		tileTextures.put(TileType.TP_LEFT,
				new Texture(Gdx.files.internal("sprites/free.png")));
		tileTextures.put(TileType.TP_RIGHT,
				new Texture(Gdx.files.internal("sprites/free.png")));
		tileTextures.put(TileType.DOT,
				new Texture(Gdx.files.internal("sprites/dot.png")));
		tileTextures.put(TileType.PILL,
				new Texture(Gdx.files.internal("sprites/pill.png")));
		tileTextures.put(TileType.BLOCKED,
				new Texture(Gdx.files.internal("sprites/blocked.png")));
		tileTextures.put(TileType.DOOR,
				new Texture(Gdx.files.internal("sprites/door.png")));
	}

	public MapTile getTileByIndex(int x, int y) {
		// increase x by one because of the invisible row at x == 0
		x++;
		if (y >= 0 && y < tiles.length && x >= 0 && x < tiles[0].length) {
			return tiles[y][x];
		} else {
			return null;
		}
	}

	public MapTile getTileByPixel(float x, float y) {
		int xIndex = (int) Math.floor(x / TILE_WIDTH);
		int yIndex = (int) Math.floor(y / TILE_HEIGHT);
		return getTileByIndex(xIndex, yIndex);
	}

	public float getPixelWidth() {
		return width * TILE_WIDTH;
	}

	public float getPixelHeight() {
		return height * TILE_HEIGHT;
	}

	public boolean isBlocked(MoveDirection dir, float x, float y) {
		MapTile currentTile = getTileByPixel(x, y);
		if (currentTile != null) {
			MapTile tile;
			switch (dir) {
			case DOWN:
				tile = getTileByPixel(x, y - TILE_HEIGHT);
				break;
			case LEFT:
				tile = getTileByPixel(x - TILE_WIDTH, y);
				break;
			case RIGHT:
				tile = getTileByPixel(x + TILE_WIDTH, y);
				break;
			case UP:
				tile = getTileByPixel(x, y + TILE_HEIGHT);
				break;
			case NONE:
				tile = currentTile;
				break;
			default:
				tile = null;
				break;
			}
			return tile == null || tile.isBlocked();
		}
		return true;
	}

	public FoodType enterTile(float xPixel, float yPixel) {
		MapTile tile = getTileByPixel(xPixel, yPixel);
		if (tile != null) {
			FoodType result = tile.enter();
			playerTile = tile;
			if (result == FoodType.DOT || result == FoodType.PILL) {
				dotCount--;
				if (dotCount <= 0) {
					screen.finishLevel();
				} else if (dotCount == 20) {
					screen.blinky.speed = 200;
				} else if (dotCount == 10) {
					screen.blinky.speed = 220;
				}
			} else if (result == FoodType.TP_LEFT) {
				playerTile = rightTeleporter;
			} else if (result == FoodType.TP_RIGHT) {
				playerTile = leftTeleporter;
			}
			return result;
		} else {
			return null;
		}

	}

	public List<GraphPair> getAvailableNeighbors(MapTile tile) {
		ArrayList<GraphPair> result = new ArrayList<GraphPair>();
		if (tile == null) {
			return result;
		}
		int x = tile.x;
		int y = tile.y;
		MapTile left = getTileByIndex(x - 1, y);
		MapTile right = getTileByIndex(x + 1, y);
		MapTile up = getTileByIndex(x, y + 1);
		MapTile down = getTileByIndex(x, y - 1);
		if (left != null && !left.isBlockedForGhosts()) {
			result.add(new GraphPair(MoveDirection.LEFT, left));
		}
		if (right != null && !right.isBlockedForGhosts()) {
			result.add(new GraphPair(MoveDirection.RIGHT, right));
		}
		if (up != null && !up.isBlockedForGhosts()) {
			result.add(new GraphPair(MoveDirection.UP, up));
		}
		if (down != null && !down.isBlockedForGhosts()) {
			result.add(new GraphPair(MoveDirection.DOWN, down));
		}
		if (tile.type == TileType.TP_LEFT) {
			result.add(new GraphPair(MoveDirection.LEFT, getTileByIndex(rightTeleporter.x - 1, rightTeleporter.y)));
		} else if (tile.type == TileType.TP_RIGHT) {
			result.add(new GraphPair(MoveDirection.RIGHT, getTileByIndex(leftTeleporter.x + 1, leftTeleporter.y)));
						
		}
		return result;
	}

	public MoveDirection getNextStepTowardsTile(MapTile current,
			MapTile target, MoveDirection lastDirection) {
		MoveDirection result = DFS(current, target, lastDirection);
		return result;
	}

	private MoveDirection DFS(MapTile current, MapTile target,
			MoveDirection lastDirection) {
		Queue<GraphPair> queue = new LinkedList<GraphPair>();
		List<MapTile> visited = new ArrayList<MapTile>();
		GraphPair currentPair;
		// if (current == target) {
		// return MoveDirection.NONE;
		// }
		// visited.add(current);
		for (GraphPair x : getAvailableNeighbors(current)) {
			switch (lastDirection) {
			case DOWN:
				if (x.dir != MoveDirection.UP)
					queue.add(x);
				break;
			case LEFT:
				if (x.dir != MoveDirection.RIGHT)
					queue.add(x);
				break;
			case NONE:
				queue.add(x);
				break;
			case RIGHT:
				if (x.dir != MoveDirection.LEFT)
					queue.add(x);
				break;
			case UP:
				if (x.dir != MoveDirection.DOWN)
					queue.add(x);
				break;
			default:
				break;

			}
		}
		MapTile start = current;
		do {
			currentPair = queue.poll();
			if (currentPair == null) {
				System.out.println(start + ", " + target);
			}
			current = currentPair.tile;
			visited.add(current);
			if (current == target) {
				return currentPair.dir;
			}
			List<GraphPair> possibleTargets = getAvailableNeighbors(current);
			for (GraphPair pair : possibleTargets) {
				if (!visited.contains(pair.tile)) {
					queue.add(new GraphPair(currentPair.dir, pair.tile));
				}
			}
		} while (!queue.isEmpty());
		return MoveDirection.NONE;
	}
}
