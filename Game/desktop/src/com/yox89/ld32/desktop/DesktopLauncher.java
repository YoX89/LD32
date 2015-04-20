package com.yox89.ld32.desktop;

import box2dLight.RayHandler;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yox89.ld32.Gajm;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 800;
		config.title = "Mirror Mirror";
		new LwjglApplication(new Gajm(), config);
	}
}
