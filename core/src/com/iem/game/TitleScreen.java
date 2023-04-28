package com.iem.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.iem.utils.GifDecoder;

import org.json.JSONException;

import java.util.Vector;

public class TitleScreen extends ScreenAdapter {

    proyectoIEM game;
    Stage stage;

    TextButton.TextButtonStyle textButtonStyle;
    TextButton introduirNom, triarPersonatge, triarCicle, iniciarJocStandalone, iniciarJocMultijugador, ranking;
    float elapsed;

    public TitleScreen(proyectoIEM game) {
        this.game = game;
    }


    @Override
    public void show(){
        stage = new Stage();
        game.camera = new OrthographicCamera();

        // Button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;

        // Button Nom
        introduirNom=new TextButton("Introduir nom",textButtonStyle);
        introduirNom.setPosition(1000,1000);
        introduirNom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.setScreen(new InputScreen(game));
            }
        });
        stage.addActor(introduirNom);

        // Button Personatge
        triarPersonatge=new TextButton("Triar personatge",textButtonStyle);
        triarPersonatge.setPosition(900,800);
        triarPersonatge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(triarPersonatge);

        // Button Cicle
        triarCicle=new TextButton("Triar cicle",textButtonStyle);
        triarCicle.setPosition(1050,600);
        triarCicle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.setScreen(new CiclesScreen(game));
            }
        });
        stage.addActor(triarCicle);

        // Iniciar Joc Standalone
        iniciarJocStandalone=new TextButton("Standalone",textButtonStyle);
        iniciarJocStandalone.setPosition(1000,400);
        iniciarJocStandalone.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {

            }
        });
        stage.addActor(iniciarJocStandalone);

        iniciarJocMultijugador=new TextButton("Multijugador",textButtonStyle);
        iniciarJocMultijugador.setPosition(1000,200);
        iniciarJocMultijugador.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {

            }
        });
        stage.addActor(iniciarJocMultijugador);

        ranking=new TextButton("Ranking",textButtonStyle);
        ranking.setPosition(1050,0);
        ranking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                try {
                    game.setScreen(new RankingScreen(game));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.addActor(ranking);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();

        game.batch.begin();
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")), 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.title.draw(game.batch, "IPOP GAME",Gdx.graphics.getWidth() * .30f, Gdx.graphics.getHeight() * .90f);

        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}