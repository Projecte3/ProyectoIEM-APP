package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import org.json.JSONException;


public class TitleScreen extends ScreenAdapter {

    proyectoIEM game;
    Stage stage;
    float elapsed;
    Button escollirAlies, escollirCicle, standalone, multijugador, ranking;

    public TitleScreen(proyectoIEM game) {
        this.game = game;
    }


    @Override
    public void show(){
        stage = new Stage();
        escollirAlies = createButton("Escull alies", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .65f, 850, 200, 60,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new InputScreen(game));
            }
        });
        stage.addActor(escollirAlies);

        escollirCicle = createButton("Triar cicle", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .50f, 850, 200, 60,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new FamiliesScreen(game));
            }
        });
        stage.addActor(escollirCicle);

        standalone = createButton("Standalone", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .35f, 850, 200, 60,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new SelectionCharacterScreen(game));
            }
        });
        stage.addActor(standalone);

        multijugador = createButton("Multijugador", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .20f, 850, 200, 60,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
            }
        });
        stage.addActor(multijugador);

        ranking = createButton("Ranking", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .05f, 850, 200, 60,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new RankingScreen(game));
            }
        });
        stage.addActor(ranking);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")), 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/title.png")),Gdx.graphics.getWidth()* .28f,Gdx.graphics.getHeight() * .82f,1200, 200);
        if(game.alies == null || game.cicle == null){
            standalone.setDisabled(true);
            multijugador.setDisabled(true);

            standalone.setTouchable(Touchable.disabled);
            multijugador.setTouchable(Touchable.disabled);
        }
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public Button createButton(String labelStr, float x, float y, float width, float height, int size,ClickListener listener) {
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/8-BIT WONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = generator.generateFont(parameter);
        skin.add("default", labelStyle);

        // Crear el estilo del botón
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/button_up.png"))));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/button_down.png"))));

        // Crear el botón con el estilo y las dimensiones especificadas
        Button button = new Button(buttonStyle);
        button.setPosition(x, y);
        button.setWidth(width);
        button.setHeight(height);

        // Crear la etiqueta con el texto especificado y el estilo por defecto
        Label label = new Label(labelStr, skin);
        label.setAlignment(Align.center); // centrar el texto en la etiqueta

        // Agregar la etiqueta al botón
        button.add(label);

        // Agregar el listener al botón
        button.addListener(listener);

        // Agregar el botón al escenario
        stage.addActor(button);

        // Devolver el botón creado
        return button;
    }

}