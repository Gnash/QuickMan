package com.gnash.quickman.entities;

import com.gnash.quickman.map.GameMap;

public class InkyMover extends GhostMover {

	protected InkyMover(Ghost owner, GameMap map) {
		super(owner, map);
	}

	@Override
	public MoveDirection decideDirection() {
		MoveDirection result = map.getNextStepTowardsTile(owner.currentTile,
				map.screen.blinky.currentTile, owner.lastDirection);
		move(result);
		return result;

	}

}
