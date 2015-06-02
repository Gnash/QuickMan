package com.gnash.quickman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gnash.quickman.QuickMain;

public class MenuScreen implements Screen {

	private QuickMain game;
	private int currentItem;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private int itemCount = 3;
	private Texture image = new Texture(Gdx.files.internal("sprites/menuMan.png"));

	public MenuScreen(QuickMain game) {
		Gdx.input.setInputProcessor(new MenuInputHandler(this));
		this.game = game;
		this.batch = new SpriteBatch();
		currentItem = 0;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 32 * 27, 32 * 31 + 70);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// tell the camera to update its matrices.
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		game.font.setScale(4);
		batch.begin();
		
		batch.draw(image, 7, 32 * 31 + 70 - 650);
		
		
		if (currentItem == 0) {
			game.font.setColor(Color.RED);
		}
		float width = game.font.getBounds("Start").width;
		game.font.draw(batch, "Start", camera.viewportWidth / 2 - width / 2,
				camera.viewportHeight / 2 - 180);
		game.font.setColor(Color.WHITE);
		
		if (currentItem == 1) {
			game.font.setColor(Color.RED);
		}
		width = game.font.getBounds("Highscore").width;
		game.font.draw(batch, "Highscore", camera.viewportWidth / 2 - width / 2,
				camera.viewportHeight / 2 - 260);
		game.font.setColor(Color.WHITE);
		
		if (currentItem == 2) {
			game.font.setColor(Color.RED);
		}
		width = game.font.getBounds("Beenden").width;
		game.font.draw(batch, "Beenden", camera.viewportWidth / 2 - width / 2,
				camera.viewportHeight / 2 - 340);
		game.font.setColor(Color.WHITE);
		
		
		batch.end();
		// reset font color
	}

	public void activateCurrentOption() {
		if (currentItem == 0) {
			startGame();
		} else if (currentItem == 1) {
			showHighscore();
		} else if (currentItem == 2) {
			Gdx.app.exit();
		}
	}

	private void showHighscore() {
		game.setScreen(new HighScoreScreen(game));
		dispose();
	}

	public void startGame() {
		game.setScreen(new GameScreen(game));
		dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void up() {
		currentItem = (currentItem + itemCount - 1) % itemCount;
	}

	public void down() {
		currentItem = (currentItem + 1) % itemCount;
	}

}
