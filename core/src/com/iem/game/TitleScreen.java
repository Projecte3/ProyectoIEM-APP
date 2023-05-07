package com.iem.game;

import static com.iem.utils.Utils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.Utils;

public class TitleScreen extends ScreenAdapter {

    OrthographicCamera camera;
    FitViewport viewport;

    proyectoIEM game;
    Stage stage;
    float elapsed;
    Button escollirAlies, escollirCicle, standalone, multijugador, ranking;

    static final float BUTTON_WIDTH_PERCENT = 0.22f;
    int fontSize;
    public TitleScreen(proyectoIEM game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1070, camera);
    }

    @Override
    public void show(){
        stage = new Stage();

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                break;
            case Desktop:
                fontSize = 30;
                break;
        }

        // Calcula el ancho y la altura de los botones en función de la pantalla
        float buttonWidth = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.12f;


        // BOTONES
        escollirAlies = createButton("Escull alies", Gdx.graphics.getWidth() * .375f, Gdx.graphics.getHeight() * .675f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new InputScreen(game));
            }
        });
        stage.addActor(escollirAlies);

        escollirCicle = createButton("Triar cicle", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .525f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new FamiliesScreen(game));
            }
        });
        stage.addActor(escollirCicle);

        escollirCicle = createButton("Triar\n personatge", Gdx.graphics.getWidth() * .50f, Gdx.graphics.getHeight() * .525f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new SelectionCharacterScreen(game));
            }
        });
        stage.addActor(escollirCicle);

        standalone = createButton("Standalone", Gdx.graphics.getWidth() * .375f, Gdx.graphics.getHeight() * .375f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new PreGameScreen(game));
            }
        });
        stage.addActor(standalone);

        multijugador = createButton("Multijugador", Gdx.graphics.getWidth() * .375f, Gdx.graphics.getHeight() * .225f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
            }
        });
        stage.addActor(multijugador);

        ranking = createButton("Ranking", Gdx.graphics.getWidth() * .375f, Gdx.graphics.getHeight() * .075f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new RankingScreen(game));
            }
        });
        stage.addActor(ranking);

        stage.addActor(Utils.createLabel("Personatge", Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .12f, fontSize));
        stage.addActor(Utils.createLabel("Alies: " + game.alies, Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .08f, fontSize));
        stage.addActor(Utils.createLabel("Cicle: " + game.cicle, Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .04f, fontSize));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/title.png")),Gdx.graphics.getWidth()* .20f,Gdx.graphics.getHeight() * .83f,Gdx.graphics.getWidth() *.6f, Gdx.graphics.getHeight() * .15f);

        if(game.alies.equals("")){
            standalone.setTouchable(Touchable.disabled);
            multijugador.setTouchable(Touchable.disabled);
            game.batch.draw(new Texture(Gdx.files.internal("UI/cross.png")),Gdx.graphics.getWidth() * .005f, Gdx.graphics.getHeight() * .03f,Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .10f);
        } else {
            game.batch.draw(new Texture(Gdx.files.internal("UI/check.png")),Gdx.graphics.getWidth()* .005f,Gdx.graphics.getHeight() * .03f,Gdx.graphics.getWidth() * .05f, Gdx.graphics.getHeight() * .10f);
        }

        if(game.cicle.equals("")){
            standalone.setTouchable(Touchable.disabled);
            multijugador.setTouchable(Touchable.disabled);
            game.batch.draw(new Texture(Gdx.files.internal("UI/cross.png")),Gdx.graphics.getWidth() * .005f, Gdx.graphics.getHeight() * -.01f,Gdx.graphics.getWidth() *.05f, Gdx.graphics.getHeight() * .10f);
        } else {
            game.batch.draw(new Texture(Gdx.files.internal("UI/check.png")),Gdx.graphics.getWidth()* .005f,Gdx.graphics.getHeight() * -.01f,Gdx.graphics.getWidth() *.05f, Gdx.graphics.getHeight() * .10f);
        }

        if(game.personatge == null){
            standalone.setTouchable(Touchable.disabled);
            multijugador.setTouchable(Touchable.disabled);
            game.batch.draw(new Texture(Gdx.files.internal("UI/cross.png")),Gdx.graphics.getWidth() * .005f, Gdx.graphics.getHeight() * .07f,Gdx.graphics.getWidth() *.05f, Gdx.graphics.getHeight() * .10f);
        } else {
            game.batch.draw(new Texture(Gdx.files.internal("UI/check.png")),Gdx.graphics.getWidth()* .005f,Gdx.graphics.getHeight() * .07f,Gdx.graphics.getWidth() *.05f, Gdx.graphics.getHeight() * .10f);
        }

        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }


}