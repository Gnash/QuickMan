package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;
import com.gnash.quickman.map.MapTile;

public class PinkyMover extends GhostMover {

	protected PinkyMover(Ghost owner, GameMap map) {
		super(owner, map);
	}

	@Override
	public MoveDirection decideDirection() {
		MapTile pTile = map.playerTile;
		int x = pTile.x;
		int y = pTile.y;
		switch (map.screen.playerLastMovement) {
		case DOWN:
			y -= 4;
			pTile = map.getTileByIndex(x, y);
			while (pTile == null || pTile.isBlocked()) {
				y++;
				pTile = map.getTileByIndex(x, y);
			}
			break;
		case LEFT:
			x -= 4;
			pTile = map.getTileByIndex(x, y);
			while (pTile == null || pTile.isBlocked()) {
				x++;
				pTile = map.getTileByIndex(x, y);
			}
			break;
		case NONE:
			break;
		case RIGHT:
			x += 4;
			pTile = map.getTileByIndex(x, y);
			while (pTile == null || pTile.isBlocked()) {
				x--;
				pTile = map.getTileByIndex(x, y);
			}
			break;
		case UP:
			y += 4;
			pTile = map.getTileByIndex(x, y);
			while (pTile == null || pTile.isBlocked()) {
				y--;
				pTile = map.getTileByIndex(x, y);
			}
			break;
		default:
			break;
		}
		MoveDirection result = map.getNextStepTowardsTile(owner.currentTile,
				pTile, owner.lastDirection);
		if (result == MoveDirection.NONE) {
			result = owner.lastDirection;
		}
		move(result);
		return result;
	}

}
