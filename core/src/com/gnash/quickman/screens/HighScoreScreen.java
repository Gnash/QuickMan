package com.gnash.quickman.screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gnash.quickman.QuickMain;

public class HighScoreScreen implements Screen {

	private QuickMain game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	List<HighScoreEntry> entries;
	private Texture image = new Texture(
			Gdx.files.internal("sprites/highscore.png"));

	boolean enteringName;
	String currentName;
	int currentIndex;
	float counter;
	boolean showUnderscore;

	public HighScoreScreen(QuickMain game) {
		Gdx.input.setInputProcessor(new HighScoreInputHandler(this));
		this.game = game;
		this.enteringName = false;
		this.currentIndex = -1;
		this.counter = 0;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 32 * 27, 32 * 31 + 70);
		this.batch = new SpriteBatch();
		entries = new ArrayList<HighScoreEntry>();
		importHighScore();
	}

	public HighScoreScreen(QuickMain game, int points) {
		this(game);
		this.currentName = "";
		currentIndex = insertEntry(currentName, points);
		if (currentIndex >= 0)
			this.enteringName = true;
	}

	private void importHighScore() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					"highscore")));
			int count = 0;
			String name = "";
			int points = 0;
			for (String x = in.readLine(); x != null; x = in.readLine()) {
				if (count == 0) {
					name = x;
				} else if (count == 1) {
					points = Integer.valueOf(x);
					entries.add(new HighScoreEntry(name, points));
				}
				count = (count + 1) % 2;
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {

		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// tell the camera to update its matrices.
		camera.update();
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(image, camera.viewportWidth / 2 - image.getWidth() / 2,
				camera.viewportHeight - image.getHeight() - 35);
		if (enteringName) {
			counter += delta;
			if (counter > 1.0f) {
				showUnderscore = !showUnderscore;
				counter = counter % 1.0f;
			}
			HighScoreEntry e = entries.get(currentIndex);
			e.name = currentName;
		}

		for (int i = 0; i < entries.size() && i < 10; i++) {
			HighScoreEntry e = entries.get(i);
			game.font.setScale(3);
			if (currentIndex == i) {
				game.font.setColor(Color.RED);
				if (showUnderscore) {
					game.font.draw(batch, "_",
							50 + game.font.getBounds(e.name).width,
							800 - 80 * i);
				}
			} else {
				game.font.setColor(Color.WHITE);
			}
			game.font.draw(batch, e.name, 50, 800 - 80 * i);
		}
		for (int i = 0; i < entries.size() && i < 10; i++) {
			HighScoreEntry e = entries.get(i);
			game.font.setScale(3);
			if (currentIndex == i) {
				game.font.setColor(Color.RED);
			} else {
				game.font.setColor(Color.WHITE);
			}
			String entry = String.valueOf(e.points);
			game.font.draw(batch, entry,
					814 - game.font.getBounds(entry).width, 800 - 80 * i);
		}
		batch.end();
	}

	private int insertEntry(String name, int points) {
		HighScoreEntry e = new HighScoreEntry(name, points);
		int result = -1;
		for (int i = 0; i < entries.size(); i++) {
			HighScoreEntry temp = entries.get(i);
			if (e.points > temp.points) {
				entries.add(i, e);
				result = i;
				break;
			}
		}
		if (entries.size() < 10 && result < 0) {
			entries.add(e);
			result = entries.size() - 1;
		}
		while (entries.size() > 10) {
			entries.remove(10);
		}
		return result;

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

	public void goToMenu() {
		game.setScreen(new MenuScreen(game));
		dispose();
	}

	public void finishEntering() {
		enteringName = false;
		showUnderscore = false;
		try {
			File file = new File("highscore");

			PrintWriter writer = new PrintWriter(new FileWriter(file));

			for (int i = 0; i < entries.size(); i++) {
				HighScoreEntry e = entries.get(i);
				writer.println(e.name);
				writer.println(e.points);
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("OH GOD");
		}
	}
}
