package com.gnash.quickman.map;

import com.gnash.quickman.FoodType;

public class MapTile {
	public TileType type;
	public int x;
	public int y;

	public MapTile(int x, int y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public void free() {
		this.type = TileType.FREE;
	}

	public boolean isBlocked() {
		return type == TileType.BLOCKED || type == TileType.DOOR;
	}
	
	@Override
	public String toString() {
		return "MapTile [" + x + ", " + y + "]";
	}

	public boolean isBlockedForGhosts() {
		return type == TileType.BLOCKED;
	}

	FoodType enter() {
		FoodType result;
		switch (type) {
		case TP_LEFT:
			return FoodType.TP_LEFT;
		case TP_RIGHT:
			return FoodType.TP_RIGHT;
		case DOT:
			result = FoodType.DOT;
			break;
		case PILL:
			result = FoodType.PILL;
			break;
		case FREE:
		case BLOCKED:
		default:
			result = FoodType.NONE;
			break;
		}
		this.type = TileType.FREE;
		return result;
	}
	
	public int calculateManhattanDistance(MapTile other) {
		if (other == null) {
			return 0;
		}
		return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
	}
}
