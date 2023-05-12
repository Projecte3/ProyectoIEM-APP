package com.iem.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.czyzby.websocket.CommonWebSockets;
import com.iem.game.proyectoIEM;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("ProyectoIEM-APP");
		config.setResizable(false);
		config.setWindowedMode(1280, 720);
		CommonWebSockets.initiate();
		new Lwjgl3Application(new proyectoIEM(), config);
	}
}
