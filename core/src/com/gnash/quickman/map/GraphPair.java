package com.gnash.quickman.map;

import com.gnash.quickman.entities.MoveDirection;

public class GraphPair {
	public MoveDirection dir;
	public MapTile tile;

	public GraphPair(MoveDirection dir, MapTile tile) {
		this.dir = dir;
		this.tile = tile;
	}
}
