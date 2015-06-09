package com.gnash.quickman.screens.game;

import java.util.Arrays;
import java.util.List;

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
			queueMovement(keycode);
		} else {
			changeDirection(keycode);
		}
		return isValidInput(keycode);
	}

	private void queueMovement(int keycode) {
		switch (keycode) {
		case Input.Keys.UP:
			gameScreen.nextMovement = MoveDirection.UP;
			break;
		case Input.Keys.DOWN:
			gameScreen.nextMovement = MoveDirection.DOWN;
			break;
		case Input.Keys.RIGHT:
			gameScreen.nextMovement = MoveDirection.RIGHT;
			break;
		case Input.Keys.LEFT:
			gameScreen.nextMovement = MoveDirection.LEFT;
			break;
		}

	}

	private void changeDirection(int keycode) {
		switch (keycode) {
		case Input.Keys.UP:
			gameScreen.movePlayerUpOneTile();
			gameScreen.nextMovement = MoveDirection.UP;
			break;
		case Input.Keys.DOWN:
			gameScreen.movePlayerDownOneTile();
			gameScreen.nextMovement = MoveDirection.DOWN;
			break;
		case Input.Keys.RIGHT:
			gameScreen.movePlayerRightOneTile();
			gameScreen.nextMovement = MoveDirection.RIGHT;
			break;
		case Input.Keys.LEFT:
			gameScreen.movePlayerLeftOneTile();
			gameScreen.nextMovement = MoveDirection.LEFT;
			break;
		}
	}

	private boolean isValidInput(int keycode) {
		List<Integer> validKeycodes = Arrays.asList(Input.Keys.UP, Input.Keys.DOWN,
				Input.Keys.LEFT, Input.Keys.RIGHT);
		return validKeycodes.contains(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
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
