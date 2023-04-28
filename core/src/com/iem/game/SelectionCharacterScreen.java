package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    public SelectionCharacterScreen(proyectoIEM game) {
        this.game = game;
    }

    @Override
    public void show() {
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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        camera.update();

        selectAnimation();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    //Select animation renderization
    public void selectAnimation(){
        TextureRegion selectTexture = marioSelectAnimation.getKeyFrame(stateTime,true);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(selectTexture, posx, posy, 0, 0,
                selectTexture.getRegionWidth(),selectTexture.getRegionHeight(),10,10,0);
        batch.end();
    }
}
