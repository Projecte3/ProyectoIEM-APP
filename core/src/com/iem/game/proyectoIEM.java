package com.iem.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class proyectoIEM extends Game {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;

	BitmapFont font, title;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter titleParameter, fontParameter;
	Music menuMusic;
	Sound sound;
	Sound goodItem;
	Sound badItem;

	static final float BUTTON_WIDTH_PERCENT = 0.20f;
	static final float TEXTFIELD_WIDTH_PERCENT = 0.40f;


	String alies = "";
	String cicle = "";
	Texture personatge;


	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/mario-coin.mp3"));
		goodItem = Gdx.audio.newSound(Gdx.files.internal("sounds/itemCorrecte.mp3"));
		badItem = Gdx.audio.newSound(Gdx.files.internal("sounds/itemIncorrecte.mp3"));

		font = new BitmapFont();
		title = new BitmapFont();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
		titleParameter = new FreeTypeFontParameter();
		titleParameter.size = 14;
		titleParameter.borderWidth = 2f;
		titleParameter.color = Color.BLACK;
		titleParameter.borderColor = Color.WHITE;

		fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 10;
		fontParameter.borderWidth = 1f;
		fontParameter.color = Color.BLACK;
		fontParameter.borderColor = Color.WHITE;

		title = generator.generateFont(titleParameter);
		font = generator.generateFont(fontParameter);

		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/backgroundMusic.ogg"));
		menuMusic.setLooping(true);
		menuMusic.play();

		title.getData().setScale(14);
		font.getData().setScale(7);
		setScreen(new TitleScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		title.dispose();
		font.dispose();
	}
}
