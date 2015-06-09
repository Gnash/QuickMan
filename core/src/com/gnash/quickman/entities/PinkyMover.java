package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;
import com.gnash.quickman.map.MapTile;

public class PinkyMover extends GhostMover {

	protected PinkyMover(Ghost owner, GameMap map) {
		super(owner, map);
	}

	@Override
	public MoveDirection decideDirection() {
		MapTile playerTile = map.playerTile;
		int x = playerTile.x;
		int y = playerTile.y;
		MapTile targetTile = playerTile;
		switch (map.screen.playerLastMovement) {
		case DOWN:
			y -= 4;
			targetTile = map.getTileByIndex(x, y);
			while (targetTile == null || targetTile.isBlocked()) {
				y++;
				targetTile = map.getTileByIndex(x, y);
			}
			break;
		case LEFT:
			x -= 4;
			targetTile = map.getTileByIndex(x, y);
			while (targetTile == null || targetTile.isBlocked()) {
				x++;
				targetTile = map.getTileByIndex(x, y);
			}
			break;
		case RIGHT:
			x += 4;
			targetTile = map.getTileByIndex(x, y);
			while (targetTile == null || targetTile.isBlocked()) {
				x--;
				targetTile = map.getTileByIndex(x, y);
			}
			break;
		case UP:
			y += 4;
			targetTile = map.getTileByIndex(x, y);
			while (targetTile == null || targetTile.isBlocked()) {
				y--;
				targetTile = map.getTileByIndex(x, y);
			}
			break;
		case NONE:
			break;
		default:
			break;
		}
		MoveDirection result = map.getNextStepTowardsTile(owner.currentTile,
				targetTile, owner.lastDirection);
		if (result == MoveDirection.NONE) {
			result = owner.lastDirection;
		}
		move(result);
		return result;
	}
}
