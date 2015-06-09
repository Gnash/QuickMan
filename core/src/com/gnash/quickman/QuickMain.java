package com.gnash.quickman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.gnash.quickman.screens.game.GameScreen;
import com.gnash.quickman.screens.menu.MenuScreen;

public class QuickMain extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	private Music music; 
	
	@Override
	public void create () {
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/electricfinale.mp3"));
		music.setLooping(true);
		music.setVolume(0.7f);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 12;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		font = generator.generateFont(param);
		generator.dispose();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		if (screen.getClass() == GameScreen.class) {
			music.stop();
		} else if (!music.isPlaying()) {
			music.play();
		}
	}
}
