package com.iem.game;

import static com.iem.utils.Utils.createButton;

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
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PreGameScreen extends ScreenAdapter {

    proyectoIEM game;
    private OrthographicCamera camera;

    FitViewport viewport;

    static final float BUTTON_WIDTH_PERCENT = 0.20f;

    int fontSize;

    //ANIMATION ATTRIBUTES
    Texture walkSheet;

    TextureRegion selectAnimation[] = new TextureRegion[28];

    Animation<TextureRegion> marioSelectAnimation;

    float stateTime;
    SpriteBatch batch;
    float posx, posy;

    //Camera
    private Stage stage;

    public PreGameScreen(proyectoIEM game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1090, camera);
    }

    @Override
    public void show() {
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
        float buttonWidth = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.10f;

        stage.addActor(createButton("Jugar", Gdx.graphics.getWidth() * .42f, Gdx.graphics.getHeight() * .15f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new GameScreen(game));
            }
        }));

        stage.addActor(createButton("Enrere", Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .85f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));

        walkSheet = game.personatge;
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
        batch = new SpriteBatch();
        stateTime = 0f;

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        TextureRegion selectTexture = marioSelectAnimation.getKeyFrame(stateTime,true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        game.batch.begin();
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.draw(selectTexture, Gdx.graphics.getWidth() * .45f, Gdx.graphics.getHeight() * .3f, 0, 0,
                selectTexture.getRegionWidth(),selectTexture.getRegionHeight(),10,10,0);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


}
