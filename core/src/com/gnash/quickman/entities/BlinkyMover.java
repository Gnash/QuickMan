package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;

public class BlinkyMover extends GhostMover {

	public BlinkyMover(Ghost owner, GameMap map) {
		super(owner, map);
	}

	@Override
	public MoveDirection decideDirection() {
		MoveDirection result = map.getNextStepTowardsTile(owner.currentTile, map.playerTile, owner.lastDirection);
		move(result);
		return result;
	}
}
