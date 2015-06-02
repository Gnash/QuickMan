package com.gnash.quickman.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.gnash.quickman.entities.MoveDirection;

public class GameInputHandler implements InputProcessor {

	private GameScreen gameScreen;

	public GameInputHandler(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (gameScreen.isPlayerMoving()) {
			// currently moving, so queue the current movement
			switch (keycode) {
			case Input.Keys.UP:
					gameScreen.nextMovement = MoveDirection.UP;				
				return true;
			case Input.Keys.DOWN:
					gameScreen.nextMovement = MoveDirection.DOWN;
				return true;
			case Input.Keys.RIGHT:
					gameScreen.nextMovement = MoveDirection.RIGHT;
				return true;
			case Input.Keys.LEFT:
					gameScreen.nextMovement = MoveDirection.LEFT;
				return true;
			}
			return false;
		}
		// Not moving, so pick a new direction!
		switch (keycode) {
		case Input.Keys.UP:
			gameScreen.moveUp();
			gameScreen.nextMovement = MoveDirection.UP;
			return true;
		case Input.Keys.DOWN:
			gameScreen.moveDown();
			gameScreen.nextMovement = MoveDirection.DOWN;
			return true;
		case Input.Keys.RIGHT:
			gameScreen.moveRight();
			gameScreen.nextMovement = MoveDirection.RIGHT;
			return true;
		case Input.Keys.LEFT:
			gameScreen.moveLeft();
			gameScreen.nextMovement = MoveDirection.LEFT;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
//			switch (keycode) {
//			case Input.Keys.UP:
//				if (gameScreen.playerMovement == MoveDirection.UP) {
//					gameScreen.keepMoving = false;
//				} else if (gameScreen.nextMovement == MoveDirection.UP) {
//					gameScreen.nextMovement = MoveDirection.NONE;
//				}
//				return true;
//			case Input.Keys.DOWN:
//				if (gameScreen.playerMovement == MoveDirection.DOWN) {
//					gameScreen.keepMoving = false;
//				} else if (gameScreen.nextMovement == MoveDirection.DOWN) {
//					gameScreen.nextMovement = MoveDirection.NONE;
//				}
//				return true;
//			case Input.Keys.RIGHT:
//				if (gameScreen.playerMovement == MoveDirection.RIGHT) {
//					gameScreen.keepMoving = false;
//				} else if (gameScreen.nextMovement == MoveDirection.RIGHT) {
//					gameScreen.nextMovement = MoveDirection.NONE;
//				}
//				return true;
//			case Input.Keys.LEFT:
//				if (gameScreen.playerMovement == MoveDirection.LEFT) {
//					gameScreen.keepMoving = false;
//				} else if (gameScreen.nextMovement == MoveDirection.LEFT) {
//					gameScreen.nextMovement = MoveDirection.NONE;
//				}
//				return true;
//			}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
