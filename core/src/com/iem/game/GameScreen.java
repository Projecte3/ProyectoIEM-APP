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
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter {

    proyectoIEM game;
    OrthographicCamera camera;

    //ANIMATION ATTRIBUTES
    Texture walkSheet, background;
    TextureRegion IDLEFrameDown[] = new TextureRegion[1];
    TextureRegion IDLEFrameUp[] = new TextureRegion[1];
    TextureRegion IDLEFrameX[] = new TextureRegion[4];

    TextureRegion walkFrame[] = new TextureRegion[4];
    TextureRegion walkFrameUP[] = new TextureRegion[5];
    TextureRegion walkFrameDown[] = new TextureRegion[5];

    Animation<TextureRegion> IDLEMarioDown, IDLEMarioUP,IDLEMarioX, walkMario, walkMarioUP, walkMarioDown;

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
        background = new Texture(Gdx.files.internal("Grass_Sample.png"));
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
        walkFrameUP[1] = new TextureRegion(walkSheet,24,41,20,39);
        walkFrameUP[2] = new TextureRegion(walkSheet,45,41,20,39);
        walkFrameUP[3] = new TextureRegion(walkSheet,65,41,20,39);
        walkFrameUP[4] = new TextureRegion(walkSheet,86,41,20,39);

        //Walk Frame DOWN
        walkFrameDown[0] = new TextureRegion(walkSheet,4,81,20,39);
        walkFrameDown[1] = new TextureRegion(walkSheet,25,81,20,39);
        walkFrameDown[2] = new TextureRegion(walkSheet,45,81,20,39);
        walkFrameDown[3] = new TextureRegion(walkSheet,67,81,20,39);
        walkFrameDown[4] = new TextureRegion(walkSheet,88,81,20,39);

        //IDLE ANIMATIONS
        IDLEMarioDown = new Animation<TextureRegion>(0.25f,IDLEFrameDown);
        IDLEMarioUP = new Animation<TextureRegion>(0.25f,IDLEFrameUp);
        IDLEMarioX = new Animation<TextureRegion>(0.25f,IDLEFrameX);

        //WALK ANIMATIONS
        walkMario = new Animation<TextureRegion>(0.25f,walkFrame);
        walkMarioUP = new Animation<TextureRegion>(0.25f,walkFrameUP);
        walkMarioDown = new Animation<TextureRegion>(0.25f,walkFrameDown);

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
        TextureRegion IDLEDown = IDLEMarioDown.getKeyFrame(stateTime,true);
        TextureRegion walkFrameX = walkMario.getKeyFrame(stateTime, true);
        TextureRegion walkUP = walkMarioUP.getKeyFrame(stateTime, true);
        TextureRegion walkDown = walkMarioDown.getKeyFrame(stateTime, true);


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(background, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        int direction = virtual_joystick_control();

        switch (direction){
            //IDLE ANIMATION
            case 0:
                batch.begin();
                IDLEMarioDown = new Animation<TextureRegion>(0.25f,IDLEFrameDown);
                batch.draw(IDLEDown, posx, posy, 0, 0,
                        IDLEDown.getRegionWidth(),IDLEDown.getRegionHeight(),5,5,0);
                batch.end();
                break;
            //GO DOWN ANIMATION
            case 1:
                IDLEMarioDown = new Animation<TextureRegion>(0.25f, walkFrameUP);
                batch.begin();
                posy += 20;
                batch.draw(walkUP, posx, posy,0, 0,
                        walkUP.getRegionWidth(),walkUP.getRegionHeight(),5,5,0);
                batch.end();
                break;
            //GO UP ANIMATION
            case 2:
                IDLEMarioDown = new Animation<TextureRegion>(0.25f, walkFrameDown);
                batch.begin();
                posy -= 20;
                batch.draw(walkDown, posx, posy,0, 0,
                        walkDown.getRegionWidth(),walkDown.getRegionHeight(),5,5,0);
                batch.end();
                break;
            //GO LEFT ANIMATION
            case 3:
                IDLEMarioDown = new Animation<TextureRegion>(0.25f, walkFrame);
                batch.begin();
                posx -= 20;
                batch.draw(walkFrameX, posx, posy,0, 0,
                        walkFrameX.getRegionWidth(),walkFrameX.getRegionHeight(),-5,5,0);
                batch.end();
                break;
            //GO RIGHT ANIMATION
            case 4:
                IDLEMarioDown = new Animation<TextureRegion>(0.25f, walkFrame);
                batch.begin();
                posx += 20;
                batch.draw(walkFrameX, posx, posy,0, 0,
                        walkFrameX.getRegionWidth(),walkFrameX.getRegionHeight(),5,5,0);
                batch.end();
                break;
        }

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    protected int virtual_joystick_control() {
        for(int i=0;i<10;i++)
            if (Gdx.input.isTouched(i)) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touchPos);
                if (up.contains(touchPos.x, touchPos.y)) {
                    return UP;
                } else if (down.contains(touchPos.x, touchPos.y)) {
                    return DOWN;
                } else if (left.contains(touchPos.x, touchPos.y)) {
                    return LEFT;
                } else if (right.contains(touchPos.x, touchPos.y)) {
                    return RIGHT;
                }
            }
        return IDLE;
    }
}
