package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class SelectionCharacterScreen extends ScreenAdapter {

    proyectoIEM game;

    //ANIMATION ATTRIBUTES
    Texture walkSheet;

    TextureRegion selectAnimation[] = new TextureRegion[28];

    Animation<TextureRegion> marioSelectAnimation;

    float stateTime;
    SpriteBatch batch;
    float posx, posy;

    Rectangle up, down, left, right, fire;
    final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
    float lastSend = 0f;

    //Camera
    private OrthographicCamera camera;
    private Stage stage;

    public SelectionCharacterScreen(proyectoIEM game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        walkSheet = new Texture(Gdx.files.internal("mario.png"));
        posx = 750;
        posy = 450;

        //Select Screen Animation
        selectAnimation[0] = new TextureRegion(walkSheet,117,123,20,39);
        selectAnimation[1] = new TextureRegion(walkSheet,143,123,20,39);

        selectAnimation[2] = new TextureRegion(walkSheet,4,122,20,39);
        selectAnimation[3] = new TextureRegion(walkSheet,31,122,20,39);
        selectAnimation[4] = new TextureRegion(walkSheet,62,122,20,39);
        selectAnimation[5] = new TextureRegion(walkSheet,90,122,20,39);

        selectAnimation[6] = new TextureRegion(walkSheet,21,0,20,39);
        selectAnimation[7] = new TextureRegion(walkSheet,42,0,20,39);
        selectAnimation[8] = new TextureRegion(walkSheet,64,0,20,39);
        selectAnimation[9] = new TextureRegion(walkSheet,85,0,20,39);

        selectAnimation[10] = new TextureRegion(walkSheet,4,41,20,39);
        selectAnimation[11] = new TextureRegion(walkSheet,24,41,20,39);
        selectAnimation[12] = new TextureRegion(walkSheet,45,41,20,39);
        selectAnimation[13] = new TextureRegion(walkSheet,65,41,20,39);
        selectAnimation[14] = new TextureRegion(walkSheet,86,41,20,39);

        selectAnimation[15] = new TextureRegion(walkSheet,88,161,20,39);
        selectAnimation[16] = new TextureRegion(walkSheet,61,161,20,39);
        selectAnimation[17] = new TextureRegion(walkSheet,30,161,20,39);
        selectAnimation[18] = new TextureRegion(walkSheet,2,161,20,39);

        selectAnimation[19] = new TextureRegion(walkSheet,110,0,20,39);
        selectAnimation[20] = new TextureRegion(walkSheet,131,0,20,39);
        selectAnimation[21] = new TextureRegion(walkSheet,154,0,20,39);
        selectAnimation[22] = new TextureRegion(walkSheet,175,0,20,39);

        selectAnimation[23] = new TextureRegion(walkSheet,4,81,20,39);
        selectAnimation[24] = new TextureRegion(walkSheet,25,81,20,39);
        selectAnimation[25] = new TextureRegion(walkSheet,45,81,20,39);
        selectAnimation[26] = new TextureRegion(walkSheet,67,81,20,39);
        selectAnimation[27] = new TextureRegion(walkSheet,88,81,20,39);

        //Select Screen Animation
        marioSelectAnimation = new Animation<TextureRegion>(0.25f,selectAnimation);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 2900, 1480);
        batch = new SpriteBatch();
        stateTime = 0f;

        stage.addActor(createButton("Jugar", Gdx.graphics.getWidth() * .42f, Gdx.graphics.getHeight() * .15f, 500, 150, 50,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new GameScreen(game));
            }
        }));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        TextureRegion selectTexture = marioSelectAnimation.getKeyFrame(stateTime,true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        camera.update();

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")), 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/seleccionPersonajeTitle.png")),Gdx.graphics.getWidth() * .15f, Gdx.graphics.getHeight() * .85f,2000, 200);

        game.batch.draw(selectTexture, Gdx.graphics.getWidth() * .45f, Gdx.graphics.getHeight() * .3f, 0, 0,
                selectTexture.getRegionWidth(),selectTexture.getRegionHeight(),20,20,0);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public Button createButton(String labelStr, float x, float y, float width, float height, int size, ClickListener listener) {
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
