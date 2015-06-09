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
import com.gnash.quickman.screens.game.GameScreen;

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
		List<GraphPair> availableNeighbors = new ArrayList<GraphPair>();
		if (tile == null) {
			return availableNeighbors;
		}
		int x = tile.x;
		int y = tile.y;
		addLeftNeighborIfAvailable(x, y, availableNeighbors);
		addRightNeighborIfAvailable(x, y, availableNeighbors);
		addUpperNeighborIfAvailable(x, y, availableNeighbors);
		addBottomNeighborIfAvailable(x, y, availableNeighbors);
		addTeleportNeighborsIfAvailable(tile, availableNeighbors);
		return availableNeighbors;
	}

	private void addTeleportNeighborsIfAvailable(MapTile tile,
			List<GraphPair> availableNeighbors) {
		if (tile.type == TileType.TP_LEFT) {
			availableNeighbors.add(new GraphPair(MoveDirection.LEFT, getTileByIndex(
					rightTeleporter.x - 1, rightTeleporter.y)));
		} else if (tile.type == TileType.TP_RIGHT) {
			availableNeighbors.add(new GraphPair(MoveDirection.RIGHT, getTileByIndex(
					leftTeleporter.x + 1, leftTeleporter.y)));
		}
	}

	private void addLeftNeighborIfAvailable(int x, int y, List<GraphPair> result) {
		MapTile left = getTileByIndex(x - 1, y);
		if (left != null && !left.isBlockedForGhosts()) {
			result.add(new GraphPair(MoveDirection.LEFT, left));
		}
	}

	private void addRightNeighborIfAvailable(int x, int y,
			List<GraphPair> availableNeighbors) {
		MapTile right = getTileByIndex(x + 1, y);
		if (right != null && !right.isBlockedForGhosts()) {
			availableNeighbors.add(new GraphPair(MoveDirection.RIGHT, right));
		}
	}

	private void addUpperNeighborIfAvailable(int x, int y,
			List<GraphPair> availableNeighbors) {
		MapTile up = getTileByIndex(x, y + 1);
		if (up != null && !up.isBlockedForGhosts()) {
			availableNeighbors.add(new GraphPair(MoveDirection.UP, up));
		}
	}

	private void addBottomNeighborIfAvailable(int x, int y,
			List<GraphPair> availableNeighbors) {
		MapTile down = getTileByIndex(x, y - 1);
		if (down != null && !down.isBlockedForGhosts()) {
			availableNeighbors.add(new GraphPair(MoveDirection.DOWN, down));
		}
	}

	public MoveDirection getNextStepTowardsTile(MapTile current, MapTile target,
			MoveDirection lastDirection) {
		MoveDirection result = DFS(current, target, lastDirection);
		return result;
	}

	private MoveDirection DFS(MapTile currentTile, MapTile targetTile,
			MoveDirection lastDirection) {
		Queue<GraphPair> queue = new LinkedList<GraphPair>();
		addReachableNeighborTilesToQueue(queue, currentTile, lastDirection);
		return findDirectionOnPathTowardsTile(currentTile, targetTile, queue);
	}

	private MoveDirection findDirectionOnPathTowardsTile(MapTile currentTile,
			MapTile targetTile, Queue<GraphPair> queue) {
		List<MapTile> visited = new ArrayList<MapTile>();
		do {
			GraphPair currentPair = queue.poll();
			currentTile = currentPair.tile;
			visited.add(currentTile);
			if (currentTile == targetTile) {
				return currentPair.dir;
			}
			List<GraphPair> possibleTargets = getAvailableNeighbors(currentTile);
			for (GraphPair pair : possibleTargets) {
				if (!visited.contains(pair.tile)) {
					queue.add(new GraphPair(currentPair.dir, pair.tile));
				}
			}
		} while (!queue.isEmpty());
		return MoveDirection.NONE;
	}

	private void addReachableNeighborTilesToQueue(Queue<GraphPair> queue,
			MapTile currentTile, MoveDirection lastDirection) {
		for (GraphPair currentPair : getAvailableNeighbors(currentTile)) {
			switch (lastDirection) {
			case DOWN:
				if (currentPair.dir != MoveDirection.UP)
					queue.add(currentPair);
				break;
			case LEFT:
				if (currentPair.dir != MoveDirection.RIGHT)
					queue.add(currentPair);
				break;
			case NONE:
				queue.add(currentPair);
				break;
			case RIGHT:
				if (currentPair.dir != MoveDirection.LEFT)
					queue.add(currentPair);
				break;
			case UP:
				if (currentPair.dir != MoveDirection.DOWN)
					queue.add(currentPair);
				break;
			default:
				break;
			}
		}
	}
}
