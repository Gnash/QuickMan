package com.gnash.quickman.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class HighScoreInputHandler implements InputProcessor {

	private HighScoreScreen screen;

	public HighScoreInputHandler(HighScoreScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (screen.enteringName) {
			if (keycode == Input.Keys.ENTER) {
				screen.finishEntering();
				return true;
			} else if (keycode == Input.Keys.BACKSPACE
					&& screen.currentName.length() > 0) {
				System.out.print(screen.currentName + ": ");
				screen.currentName = screen.currentName.substring(0,
						screen.currentName.length() - 1);
			}
		} else {
			screen.goToMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if ((Character.isAlphabetic(character) || Character.isDigit(character))
				&& screen.enteringName && screen.currentName.length() < 15) {
			screen.currentName += character;
		}
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
