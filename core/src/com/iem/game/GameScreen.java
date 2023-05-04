package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.APIPost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    // Obtener las dimensiones de la pantalla del dispositivo
    Graphics graphics = Gdx.graphics;
    float screenWidth = graphics.getWidth();
    float screenHeight = graphics.getHeight();
    ArrayList<String> totems = new ArrayList<>();
    Random random = new Random();

    proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;

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
    int spriteSizeX, spriteSizeY;
    SpriteBatch batch;
    float posx, posy;

    Rectangle up, down, left, right;
    final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
    float lastSend = 0f;

    //Camera
    BitmapFont font = new BitmapFont();
    float scrollPosition = 0f;
    int currentTextIndex = 0;
    float timer = 0;
    int currentPosition = 0;
    float delay = 0.1f;
    float scrollTimer = 0;
    int maxVisibleChars = 20;
    float scrollSpeed = 50;

    float randomNumx = random.nextFloat() * 1000;
    float randomNumy = random.nextFloat() * 500;
    public GameScreen(proyectoIEM game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1080, camera);
    }

    @Override
    public void show() {

        switch (Gdx.app.getType()){
            case Android:
                spriteSizeX = 5;
                spriteSizeY = 5;
                break;
            case Desktop:
                spriteSizeX = 2;
                spriteSizeY = 2;
                break;
        }

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
        IDLEMarioDown = new Animation<>(0.25f, IDLEFrameDown);
        IDLEMarioUP = new Animation<>(0.25f, IDLEFrameUp);
        IDLEMarioX = new Animation<>(0.25f, IDLEFrameX);

        //WALK ANIMATIONS
        walkMario = new Animation<>(0.25f, walkFrame);
        walkMarioUP = new Animation<>(0.25f, walkFrameUP);
        walkMarioDown = new Animation<>(0.25f, walkFrameDown);

        batch = new SpriteBatch();
        stateTime = 0f;

        // Crear los rectángulos ajustados a la pantalla del dispositivo
        up = new Rectangle(0, 0, screenWidth, screenHeight/3);
        down = new Rectangle(0, screenHeight * 2/3, screenWidth, screenHeight);
        left = new Rectangle(0, 0, screenWidth/3, screenHeight);
        right = new Rectangle(screenWidth * 2/3, 0, screenWidth, screenHeight);

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean keyDown(int keyCode) {
            if (keyCode == Input.Keys.ESCAPE) {
                game.setScreen(new TitleScreen(game));
            }
            return true;
            }
        });

        getTotems();
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
        batch.draw(background, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        int direction = virtual_joystick_control();

        switch (direction) {
            //IDLE ANIMATION
            case 0:
                batch.begin();
                IDLEMarioDown = new Animation<>(0.25f, IDLEFrameDown);
                batch.draw(IDLEDown, posx, posy, 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;
            //GO UP ANIMATION
            case 1:
                IDLEMarioDown = new Animation<>(0.25f, walkFrameUP);
                batch.begin();
                posy += 20;
                batch.draw(walkUP, posx, posy, 0, 0,
                        walkUP.getRegionWidth(), walkUP.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;
            //GO DOWN ANIMATION
            case 2:
                IDLEMarioDown = new Animation<>(0.25f, walkFrameDown);
                batch.begin();
                posy -= 20;
                batch.draw(walkDown, posx, posy, 0, 0,
                        walkDown.getRegionWidth(), walkDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;
            //GO LEFT ANIMATION
            case 3:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                batch.begin();
                posx -= 20;
                batch.draw(walkFrameX, posx, posy, 0 + walkFrameX.getRegionWidth(), 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), -spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;
            //GO RIGHT ANIMATION
            case 4:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                batch.begin();
                posx += 20;
                batch.draw(walkFrameX, posx, posy, 0, 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;
        }
        //TODO funcion generar otros textos en otras coordenadas
        String currentText = totems.get(currentTextIndex);
        float scrollAmount = scrollSpeed * scrollTimer;
        int visibleChars = Math.min(maxVisibleChars, currentText.length() - currentPosition);
        String visibleText = currentText.substring(currentPosition, currentPosition + visibleChars);
        batch.begin();
        font.draw(batch, visibleText, randomNumx / 2f, randomNumy / 2f);
        batch.end();

        timer += delta;
        if (timer >= delay) {
            currentPosition++;
            if (currentPosition >= currentText.length()) {
                currentPosition = visibleChars - 1; // Reset to last visible character position
            }
            timer = 0;
        }
        scrollTimer += delta;
        if (scrollAmount >= font.getSpaceXadvance() * visibleChars) {
            scrollTimer = 0;
        }
        float speed = 10f;
        scrollPosition += speed * delta;
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
                //camera.unproject(touchPos);
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

    public ArrayList<String> getTotems(){
        JSONObject obj = new JSONObject();
        obj.put("cicleNom",game.cicle);
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_totems",obj);
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONArray cicles = objResponse.getJSONArray("result");

            for (int i = 0; i < cicles.length(); i++) {
                JSONObject totem = cicles.getJSONObject(i);
                String totemStr = totem.getString("nom");
                totems.add(totemStr);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return totems;
    }

}
