package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.Vector;

public class TitleScreen extends ScreenAdapter {

    proyectoIEM game;
    private Vector backgroundSprite;

    public TitleScreen(proyectoIEM game) {
        this.game = game;
    }

    @Override
    public void show(){
        game.background = new Texture(Gdx.files.internal("background.png"));
        game.backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    game.setScreen(new GameScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // game.backgroundSprite.draw(game.batch); // Dibuja el fondo
        game.font.draw(game.batch, "IPOP GAME", Gdx.graphics.getWidth() * .10f, Gdx.graphics.getHeight() * .50f);
        game.font.draw(game.batch, "Press space to play", Gdx.graphics.getWidth() * .10f, Gdx.graphics.getHeight() * .25f);
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}