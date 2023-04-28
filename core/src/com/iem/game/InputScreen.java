package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.iem.utils.GifDecoder;

import java.util.Random;

import jdk.internal.net.http.common.Log;

public class InputScreen extends ScreenAdapter {

    proyectoIEM game;
    TextButton.TextButtonStyle textButtonStyle;
    TextButton button;

    TextField.TextFieldStyle textFieldStyle;
    TextField inputNom;

    Stage stage;
    float elapsed;

    String[] arrayAlies = {"Mario", "Luigi", "Princesa Peach", "Yoshi", "Toad", "Bowser", "Wario", "Donkey Kong", "Daisy", "Birdo", "Waluigi", "Goomba", "Koopa Troopa", "Boo", "Bullet Bill", "Chain Chomp", "King Boo", "Petey Piranha", "Piranha Plant", "Shy Guy", "Dry Bones", "Hammer Bro", "Lakitu", "Monty Mole", "Blooper", "Bob-omb", "Thwomp", "Whomp", "Rosalina", "Captain Toad", "Pauline", "Cappy", "Poochy"};

    public InputScreen(proyectoIEM game) {
        this.game = game;
    }


    @Override
    public void show(){
        stage = new Stage();

        game.camera = new OrthographicCamera();
        game.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // TextField
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = game.font;
        textFieldStyle.fontColor = Color.BLACK;

        inputNom = new TextField("Nom", textFieldStyle);
        inputNom.setPosition(1100,500);
        inputNom.addListener(new InputListener() {
            public boolean keyTyped (InputEvent event, char character) {
                game.alies = String.valueOf(character);
                return true;
            }
        });

        // Button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.fontColor = Color.BLACK;

        button=new TextButton("ENTER",textButtonStyle);
        button.setPosition(1100,300);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                if(game.alies == null){
                    game.alies = generarAlias();
                    game.setScreen(new TitleScreen(game));
                } else {
                    game.setScreen(new TitleScreen(game));
                }

            }
        });

        stage.addActor(button);
        stage.addActor(inputNom);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();

        game.batch.begin();

        game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.title.draw(game.batch, "Introduce tu nombre:",Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .70f);
        game.batch.end();
        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public String generarAlias() {
        Random rand = new Random();
        int indice = rand.nextInt(arrayAlies.length);
        return arrayAlies[indice];
    }
}
