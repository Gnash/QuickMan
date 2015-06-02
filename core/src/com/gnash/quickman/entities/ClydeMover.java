package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;

public class ClydeMover extends GhostMover {

	protected ClydeMover(Ghost owner, GameMap map) {
		super(owner, map);
	}

	@Override
	public MoveDirection decideDirection() {
		MoveDirection result = MoveDirection.NONE;
		int dist = owner.currentTile.calculateManhattanDistance(map.playerTile);
		if (dist >= 8) {
			result = map.getNextStepTowardsTile(owner.currentTile, map.playerTile, owner.lastDirection);
		} else {
			result = map.getNextStepTowardsTile(owner.currentTile, owner.scatterTile, owner.lastDirection);
		}
		
		move(result);
		return result;
	}

}
