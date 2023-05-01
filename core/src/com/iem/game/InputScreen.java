package com.iem.game;

import static com.iem.utils.Utils.createButton;
import static com.iem.utils.Utils.createTextField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.GifDecoder;

import java.util.Random;

public class InputScreen extends ScreenAdapter {

    proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;


    int fontSize;

    TextField inputNom;
    Stage stage;
    float elapsed;

    String[] arrayAlies = {"Mario", "Luigi", "Princesa Peach", "Yoshi", "Toad", "Bowser", "Wario", "Donkey Kong", "Daisy", "Birdo", "Waluigi", "Goomba", "Koopa Troopa", "Boo", "Bullet Bill", "Chain Chomp", "King Boo", "Petey Piranha", "Piranha Plant", "Shy Guy", "Dry Bones", "Hammer Bro", "Lakitu", "Monty Mole", "Blooper", "Bob-omb", "Thwomp", "Whomp", "Rosalina", "Captain Toad", "Pauline", "Cappy", "Poochy"};

    public InputScreen(proyectoIEM game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1080, camera);
    }


    @Override
    public void show(){
        stage = new Stage();

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                break;
            case Desktop:
                fontSize = 40;
                break;
        }

        // Calcula el ancho y la altura de los botones en función de la pantalla
        float buttonWidth = Gdx.graphics.getWidth() * proyectoIEM.BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.12f;

        float textfieldWidth = Gdx.graphics.getWidth() * proyectoIEM.TEXTFIELD_WIDTH_PERCENT;
        float textfieldHeight = Gdx.graphics.getHeight() * 0.20f;

        inputNom = createTextField(stage,"Introdueix un alies", Gdx.graphics.getWidth() * 0.30f, Gdx.graphics.getHeight() * .40f, textfieldWidth, textfieldHeight, fontSize, new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return true;
            }
        });
        stage.addActor(inputNom);

        stage.addActor(createButton("Introduir", Gdx.graphics.getWidth() * .40f, Gdx.graphics.getHeight() * .15f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                if(inputNom.getText().equals("")){
                    game.alies = generarAlias() + Math.round(Math.random() * 3);
                } else {
                    game.alies = inputNom.getText();
                }
                game.setScreen(new TitleScreen(game));
            }
        }));

        stage.addActor(createButton("Enrere", Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .85f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
