package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;

public abstract class GhostMover {
	protected Ghost owner;
	protected GameMap map;

	protected GhostMover(Ghost owner, GameMap map) {
		this.owner = owner;
		this.map = map;
	}

	public abstract MoveDirection decideDirection();

	protected void move(MoveDirection dir) {
		switch (dir) {
		case DOWN:
			owner.setTarget(owner.rect.x, owner.rect.y - map.TILE_HEIGHT);
			break;
		case LEFT:
			owner.setTarget(owner.rect.x - map.TILE_WIDTH, owner.rect.y);
			break;
		case NONE:
			break;
		case RIGHT:
			owner.setTarget(owner.rect.x + map.TILE_WIDTH, owner.rect.y);
			break;
		case UP:
			owner.setTarget(owner.rect.x, owner.rect.y + map.TILE_HEIGHT);
			break;
		default:
			break;
		}
	}
}
