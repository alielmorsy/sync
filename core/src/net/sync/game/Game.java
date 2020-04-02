/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sync.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import net.sync.game.database.DatabaseManager;
import net.sync.game.player.PlayerManager;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.ThemeManager;
import net.sync.game.song.SongManager;
import net.sync.game.ui.ScreenManager;
import net.sync.game.ui.screen.play.GameScreen;
import net.sync.game.util.Disposer;
import net.sync.game.util.concurrent.ExecutorManager;

public class Game implements ApplicationListener {
	public static String INPUT_PATH;

	public String tempFile;

	private static Game instance;
	private net.sync.game.Backend backend;
	private net.sync.game.GameSettings settings;
	private AssetManager assets;
	private ScreenManager screens;
	private net.sync.game.resource.ThemeManager themes;
	private SongManager songs;
	private net.sync.game.player.PlayerManager players;
	private ExecutorManager executors;
	private DatabaseManager database;
	private Disposer disposer;
	private FPSLogger fps;

	public Game(net.sync.game.Backend backend) {
		instance = this;
		this.backend = backend;
	}

	@Override
	public void create () {
		this.backend.initBackend();

		this.fps = new FPSLogger();
		this.disposer = new Disposer();
		this.settings = new net.sync.game.GameSettings();
		this.executors = new ExecutorManager();
		//if(Gdx.app.getType() != Application.ApplicationType.Android) { //TODO
			this.database = new DatabaseManager();
		//}
		this.assets = new AssetManager();
		this.themes = new net.sync.game.resource.ThemeManager();
		this.screens = new ScreenManager();
		this.songs = new SongManager();
		this.players = new net.sync.game.player.PlayerManager();

		//TODO test
		this.songs.index(Gdx.files.absolute("E:/Games/StepMania 5/Songs"));

		//Show test screen TODO
		if(Gdx.app.getType() != Application.ApplicationType.Android)
			this.screens.show(net.sync.game.ui.screen.play.GameScreen.instance());
	}

	@Override
	public void resize(int width, int height) {
		screens.resize(width, height);
	}

	@Override
	public void render () {
		//fps.log();

		//Update assets
		assets.update();

		//Perform rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screens.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume () {
		if(Gdx.app.getType() == Application.ApplicationType.Android) //TODO
			this.screens.show(GameScreen.instance());
	}

	@Override
	public void dispose () {
		disposer.dispose();
	}

	public AssetManager getAssets() {
		return assets;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public Backend getBackend() {
		return backend;
	}

	public ScreenManager getScreens() {
		return screens;
	}

	public ThemeManager getThemes() {
		return themes;
	}

	public SongManager getSongs() {
		return songs;
	}

	public PlayerManager getPlayers() {
		return players;
	}

	public ResourceProvider getResources() {
		//The theme is also the global resource provider
		return getThemes().getActiveTheme();
	}

	public ExecutorManager getExecutors() {
		return executors;
	}

	public DatabaseManager getDatabase() {
		return database;
	}

	public Disposer getDisposer() {
		return disposer;
	}

	public static Game instance() {
		return instance;
	}
}