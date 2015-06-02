package com.gnash.quickman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gnash.quickman.QuickMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 24 * 28;
		config.height = 24 * 31 + 70;
		new LwjglApplication(new QuickMain(), config);
	}
}
