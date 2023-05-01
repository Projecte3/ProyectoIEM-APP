package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.iem.utils.GifDecoder;

public class EndScreen extends ScreenAdapter {

    proyectoIEM game;
    float elapsed;

    public EndScreen(proyectoIEM game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean keyDown(int keyCode) {

                if (keyCode == Input.Keys.ENTER) {
                    game.setScreen(new TitleScreen(game));
                }

                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("gameoverBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.font.draw(game.batch, "GAME OVER",Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .70f);
        game.font.draw(game.batch, "Pulsa espacio\n para empezar", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .40f);
        game.batch.end();

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
