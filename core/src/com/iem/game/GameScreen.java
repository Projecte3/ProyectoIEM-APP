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

public class GameScreen extends ScreenAdapter {

    proyectoIEM game;
    OrthographicCamera camera;

    //ANIMATION ATTRIBUTES
    Texture walkSheet;
    TextureRegion IDLEFrameDown[] = new TextureRegion[1];
    TextureRegion IDLEFrameUp[] = new TextureRegion[1];
    TextureRegion IDLEFrameX[] = new TextureRegion[4];

    TextureRegion walkFrame[] = new TextureRegion[4];
    TextureRegion walkFrameUP[] = new TextureRegion[5];
    TextureRegion walkFrameDown[] = new TextureRegion[5];

    TextureRegion selectAnimation[] = new TextureRegion[28];

    Animation<TextureRegion> IDLEMarioDown, IDLEMarioUP,IDLEMarioX, walkMario, walkMarioUP, walkMarioDown,marioSelectAnimation;

    float stateTime;
    SpriteBatch batch;
    float posx, posy;

    Rectangle up, down, left, right, fire;
    final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
    float lastSend = 0f;

    //Camera

    public GameScreen(proyectoIEM game) {
        this.game = game;
    }

    @Override
    public void show() {
        walkSheet = new Texture(Gdx.files.internal("mario.png"));
        posx = 750;
        posy = 450;

        //IDLE FRAME DOWN
        IDLEFrameDown[0] = new TextureRegion(walkSheet,117,123,20,39);

        //IDLE FRAME UP
        IDLEFrameUp[0] = new TextureRegion(walkSheet,143,123,20,39);

        //IDLE FRAME HORIZONTALY
        IDLEFrameX[0] = new TextureRegion(walkSheet,4,122,20,39);
        IDLEFrameX[1] = new TextureRegion(walkSheet,31,122,20,39);
        IDLEFrameX[2] = new TextureRegion(walkSheet,62,122,20,39);
        IDLEFrameX[3] = new TextureRegion(walkSheet,90,122,20,39);

        //Walk FRAME X
        walkFrame[0] = new TextureRegion(walkSheet,21,0,20,39);
        walkFrame[1] = new TextureRegion(walkSheet,42,0,20,39);
        walkFrame[2] = new TextureRegion(walkSheet,64,0,20,39);
        walkFrame[3] = new TextureRegion(walkSheet,85,0,20,39);

        //Walk Frame UP
        walkFrameUP[0] = new TextureRegion(walkSheet,4,41,20,39);
        walkFrameUP[1] = new TextureRegion(walkSheet,24,41,20,29);
        walkFrameUP[2] = new TextureRegion(walkSheet,45,41,20,29);
        walkFrameUP[3] = new TextureRegion(walkSheet,65,41,20,29);
        walkFrameUP[4] = new TextureRegion(walkSheet,86,41,20,29);

        //Walk Frame DOWN
        walkFrameDown[0] = new TextureRegion(walkSheet,4,81,20,39);
        walkFrameDown[1] = new TextureRegion(walkSheet,25,81,20,39);
        walkFrameDown[2] = new TextureRegion(walkSheet,45,81,20,39);
        walkFrameDown[3] = new TextureRegion(walkSheet,67,81,20,39);
        walkFrameDown[4] = new TextureRegion(walkSheet,88,81,20,39);

        //Select Screen Animation
        marioSelectAnimation = new Animation<TextureRegion>(0.25f,selectAnimation);

        //IDLE ANIMATIONS
        IDLEMarioDown = new Animation<TextureRegion>(0.55f,IDLEFrameDown);
        IDLEMarioUP = new Animation<TextureRegion>(0.55f,IDLEFrameUp);
        IDLEMarioX = new Animation<TextureRegion>(0.55f,IDLEFrameX);

        //WALK ANIMATIONS
        walkMario = new Animation<TextureRegion>(0.55f,walkFrame);
        walkMarioUP = new Animation<TextureRegion>(0.55f,walkFrameUP);
        walkMarioDown = new Animation<TextureRegion>(0.55f,walkFrameDown);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        stateTime = 0f;

        up = new Rectangle(0, 1480*2/3, 2900, 1480/3);
        down = new Rectangle(0, 0, 2900, 1480/3);
        left = new Rectangle(0, 0, 2900/3,1480);
        right = new Rectangle(2900*2/3, 0, 2900/3, 1480);
    }

    @Override
    public void render(float delta) {
        TextureRegion selectTexture = marioSelectAnimation.getKeyFrame(stateTime,true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(selectTexture, posx, posy, 0, 0, selectTexture.getRegionWidth(),selectTexture.getRegionHeight(),10,10,0);
        batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
