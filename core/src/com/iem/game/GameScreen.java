package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.APIPost;
import com.iem.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    ArrayList<String> goodTotems = new ArrayList<>();
    ArrayList<String> badTotems = new ArrayList<>();
    ArrayList<Vector2> goodTotemPositions = new ArrayList<>();
    ArrayList<Vector2> badTotemPositions = new ArrayList<>();

    int itemsCorrectes;
    int itemsIncorrectes;
    float timer;

    proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;

    //ANIMATION ATTRIBUTES
    Texture walkSheet, background, egg;
    TextureRegion IDLEFrameDown[] = new TextureRegion[1];
    TextureRegion IDLEFrameUp[] = new TextureRegion[1];
    TextureRegion IDLEFrameX[] = new TextureRegion[4];

    TextureRegion walkFrame[] = new TextureRegion[4];
    TextureRegion walkFrameUP[] = new TextureRegion[5];
    TextureRegion walkFrameDown[] = new TextureRegion[5];

    TextureRegion eggRegion[] = new TextureRegion[4];

    Animation<TextureRegion> IDLEMarioDown, IDLEMarioUP,IDLEMarioX, walkMario, walkMarioUP, walkMarioDown, eggAnimation;

    float stateTime;
    int spriteSizeX, spriteSizeY;
    SpriteBatch batch;
    float posx, posy;

    Rectangle playerRect;

    Rectangle up, down, left, right;
    final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;

    //Camera
    BitmapFont font;
    float scrollPosition = 0f;
    int currentPosition = 0;
    float delay = 0.7f;
    float scrollTimer = 0;
    int maxVisibleChars = 20;
    float scrollSpeed = 0.5f;

    Stage stage;
    Label totemsCorrectesLabel;
    Label totemsIncorrectesLabel;
    Label timerLabel;

    int fontSize;

    public GameScreen(proyectoIEM game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1080, camera);
    }

    @Override
    public void show() {
        stage = new Stage();
        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                spriteSizeX = 5;
                spriteSizeY = 5;
                font = Utils.createFont(35);
                break;
            case Desktop:
                fontSize = 20;
                spriteSizeX = 2;
                spriteSizeY = 2;
                font = Utils.createFont(20);
                break;
        }

        walkSheet = new Texture(Gdx.files.internal("mario.png"));
        background = new Texture(Gdx.files.internal("Grass_Sample.png"));
        egg = new Texture(Gdx.files.internal("eggs.png"));
        posx = 750;
        posy = 450;
        playerRect = new Rectangle();

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

        //EGG
        eggRegion[0] = new TextureRegion(egg,4,4,20,20);
        eggRegion[1] = new TextureRegion(egg,23,4,20,20);
        eggRegion[2] = new TextureRegion(egg,42,4,20,20);
        eggRegion[3] = new TextureRegion(egg,61,4,20,20);

        eggAnimation = new Animation<>(0.55f, eggRegion);

        batch = new SpriteBatch();
        stateTime = 0f;

        // Crear los rectángulos ajustados a la pantalla del dispositivo
        up = new Rectangle(0, 0, screenWidth, screenHeight/3);
        down = new Rectangle(0, screenHeight * 2/3, screenWidth, screenHeight);
        left = new Rectangle(0, 0, screenWidth/3, screenHeight);
        right = new Rectangle(screenWidth * 2/3, 0, screenWidth, screenHeight);

        setTotems();

        float minDistance = font.getSpaceXadvance() * 4; // Calcula la distancia mínima requerida entre los tótems

        for (String totem : goodTotems) { // Itera a través de los tótems buenos
            float x, y; // Coordenadas para la posición del tótem
            boolean tooClose; // Bandera para verificar si la posición es demasiado cercana a otra posición de tótem ya existente

            do {
                x = MathUtils.random(0, screenWidth - font.getSpaceXadvance() * totem.length()); // Genera una coordenada x aleatoria dentro de la pantalla
                y = MathUtils.random(0, screenHeight - font.getLineHeight()); // Genera una coordenada y aleatoria dentro de la pantalla

                tooClose = false; // Inicializa la bandera como falso
                for (Vector2 position : goodTotemPositions) { // Itera a través de las posiciones de tótem buenos existentes
                    if (Vector2.dst(x, y, position.x, position.y) < minDistance) { // Verifica si la posición generada está demasiado cerca de otra posición de tótem
                        tooClose = true; // Si es así, establece la bandera como verdadera y sale del ciclo for
                        break;
                    }
                }
            } while (tooClose); // Continúa generando nuevas posiciones de tótem hasta que una posición válida sea encontrada

            movingTotem(x, y, goodTotemPositions); // Llama a la función para mover el tótem a su posición generada
        }

        for (String totem : badTotems) { // Itera a través de los tótems malos
            float x, y; // Coordenadas para la posición del tótem
            boolean tooClose; // Bandera para verificar si la posición es demasiado cercana a otra posición de tótem ya existente

            do {
                x = MathUtils.random(0, screenWidth - font.getSpaceXadvance() * totem.length()); // Genera una coordenada x aleatoria dentro de la pantalla
                y = MathUtils.random(0, screenHeight - font.getLineHeight()); // Genera una coordenada y aleatoria dentro de la pantalla

                tooClose = false; // Inicializa la bandera como falso
                for (Vector2 position : badTotemPositions) { // Itera a través de las posiciones de tótem malos existentes
                    if (Vector2.dst(x, y, position.x, position.y) < minDistance) { // Verifica si la posición generada está demasiado cerca de otra posición de tótem
                        tooClose = true; // Si es así, establece la bandera como verdadera y sale del ciclo for
                        break;
                    }
                }
            } while (tooClose); // Continúa generando nuevas posiciones de tótem hasta que una posición válida sea encontrada

            movingTotem(x, y, badTotemPositions); // Llama a la función para mover el tótem a su posición generada
        }

        totemsCorrectesLabel = Utils.createLabel("Totems correctes: " + itemsCorrectes, Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .08f, fontSize);
        totemsIncorrectesLabel = Utils.createLabel("Totems incorrectes: " + itemsIncorrectes, Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .04f, fontSize);
        timerLabel = Utils.createLabel("00:00", Gdx.graphics.getWidth() * .475f, Gdx.graphics.getHeight() * .95f, fontSize + 40);

        stage.addActor(totemsCorrectesLabel);
        stage.addActor(totemsIncorrectesLabel);
        stage.addActor(timerLabel);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        TextureRegion IDLEDown = IDLEMarioDown.getKeyFrame(stateTime,true);
        TextureRegion walkFrameX = walkMario.getKeyFrame(stateTime, true);
        TextureRegion walkUP = walkMarioUP.getKeyFrame(stateTime, true);
        TextureRegion walkDown = walkMarioDown.getKeyFrame(stateTime, true);
        TextureRegion eggChange = eggAnimation.getKeyFrame(stateTime, true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        timerLabel.setText(String.format("%d:%02d", Math.round(stateTime / 60), Math.round(stateTime % 60)));

        batch.begin();
        batch.draw(background, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i = 0; i < goodTotemPositions.size(); i++) {
            Vector2 position = goodTotemPositions.get(i);
            String totem = goodTotems.get(i);
            //TextureRegion selectEgg = eggRegion[random_egg()]; // Aquí debes reemplazar "myTextureRegion" con la región de textura que deseas utilizar para este texto en particular
            float x = position.x;
            float y = position.y;
            float scrollAmount = scrollSpeed * scrollTimer;
            int visibleChars = Math.min(maxVisibleChars, totem.length() - currentPosition);
            String visibleText = "";
            if (currentPosition >= 0 && currentPosition < totem.length()) {
                visibleText = totem.substring(currentPosition, Math.min(currentPosition + visibleChars, totem.length()));
                font.draw(batch, visibleText, x, y);
            }

            // Aquí dibujamos la textura después de dibujar el texto
            //batch.draw(selectEgg, x + 80, y, selectEgg.getRegionWidth() + 40, selectEgg.getRegionHeight() + 40);
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), eggChange.getRegionHeight()-10,
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1; // Reset to last visible character position
                }
                timer = 0;
            }
            scrollTimer += delta;
            if (scrollAmount >= font.getSpaceXadvance() * visibleChars) {
                scrollTimer = 0;
            }
            float speed = 0.5f;
            scrollPosition += speed * delta;


            Rectangle bounds = new Rectangle(x, y, 200, 50);
            if (bounds.contains(playerRect)) {
                game.goodItem.play(1.0f);
                goodTotems.remove(i);
                goodTotemPositions.remove(i);
                itemsCorrectes--;
                totemsCorrectesLabel.setText("Totems correctes: " + itemsCorrectes);
            }
            font.draw(batch, visibleText, x, y);
        }

        for (int i = 0; i < badTotemPositions.size(); i++) {
            Vector2 position = badTotemPositions.get(i);
            String totem = badTotems.get(i);
            float x = position.x;
            float y = position.y;
            float scrollAmount = scrollSpeed * scrollTimer;
            //TextureRegion selectEgg = eggRegion[random_egg()];
            int visibleChars = Math.min(maxVisibleChars, totem.length() - currentPosition);
            System.out.println(currentPosition);
            String visibleText = "";
            if (currentPosition >= 0 && currentPosition < totem.length()) {
                try {
                    visibleText = totem.substring(currentPosition, currentPosition + visibleChars);
                } catch (StringIndexOutOfBoundsException e) {
                    visibleText = totem.substring(currentPosition);
                }
                font.draw(batch, visibleText, x, y);
            }
            //batch.draw(selectEgg, x + 80, y, selectEgg.getRegionWidth() + 40, selectEgg.getRegionHeight() + 40);
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), eggChange.getRegionHeight()-10,
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1; // Reset to last visible character position
                }
                timer = 0;
            }
            scrollTimer += delta;
            if (scrollAmount >= font.getSpaceXadvance() * visibleChars) {
                scrollTimer = 0;
            }
            float speed = 0.5f;
            scrollPosition += speed * delta;


            Rectangle bounds = new Rectangle(x, y, 200, 100);
            if (bounds.contains(playerRect)) {
                game.badItem.play(1.0f);
                badTotems.remove(i);
                badTotemPositions.remove(i);


                itemsIncorrectes--;
                totemsIncorrectesLabel.setText("Totems incorrectes: " + itemsIncorrectes);
            }

            font.draw(batch, visibleText, x, y);
        }

        batch.end();

        int direction = virtual_joystick_control();

        switch (direction) {

            //IDLE ANIMATION
            case 0:
                batch.begin();
                IDLEMarioDown = new Animation<>(0.25f, IDLEFrameDown);
                playerRect.setPosition(posx, posy);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(IDLEDown, posx, posy, 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();

                playerRect.setPosition(posx, posy);
                break;
            //GO UP ANIMATION
            case 1:
                IDLEMarioDown = new Animation<>(0.25f, walkFrameUP);
                batch.begin();
                posy += 20;
                playerRect.setPosition(posx, posy);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkUP, posx, posy, 0, 0,
                        walkUP.getRegionWidth(), walkUP.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();

                break;
            //GO DOWN ANIMATION
            case 2:
                IDLEMarioDown = new Animation<>(0.25f, walkFrameDown);
                batch.begin();
                posy -= 20;
                playerRect.setPosition(posx, posy);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkDown, posx, posy, 0, 0,
                        walkDown.getRegionWidth(), walkDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();

                break;
            //GO LEFT ANIMATION
            case 3:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                batch.begin();
                posx -= 20;
                playerRect.setPosition(posx, posy);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkFrameX, posx, posy, 0 + walkFrameX.getRegionWidth(), 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), -spriteSizeX, spriteSizeY, 0);
                playerRect.setPosition(posx, posy);
                batch.end();

                break;
            //GO RIGHT ANIMATION
            case 4:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                batch.begin();
                posx += 20;
                playerRect.setPosition(posx, posy);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkFrameX, posx, posy, 0, 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                playerRect.setPosition(posx, posy);
                batch.end();

                break;
        }



        if(itemsCorrectes == 0){
            game.setScreen(new EndScreen(game, stateTime, 5 - itemsCorrectes, 5 - itemsIncorrectes));
        }

        stage.draw();
        stage.act();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    // Función para colocar los totems y añadirlas a un arraylist con sus posiciones distintas
    public void movingTotem(float x, float y, ArrayList<Vector2> positions) {
        positions.add(new Vector2(x, y));
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


    public void setTotems(){
        JSONObject obj = new JSONObject();
        obj.put("cicleNom",game.cicle);
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_totems",obj);
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONObject result = objResponse.getJSONObject("result");

            JSONObject totemsGenerados = result.getJSONObject("totemsGenerados");

            JSONObject buenos = totemsGenerados.getJSONObject("buenos");
            JSONObject malos = totemsGenerados.getJSONObject("malos");

            // Recorremos los totems buenos y los añadimos al array de totems
            JSONArray totemsBuenos = buenos.getJSONArray("totems");
            for (int i = 0; i < 5; i++) {
                goodTotems.add(totemsBuenos.getJSONObject(i).getString("nom"));
            }

            // Recorremos los totems malos y los añadimos al array de totems
            JSONArray totemsMalos = malos.getJSONArray("totems");
            for (int i = 0; i < 5; i++) {
                badTotems.add(totemsMalos.getJSONObject(i).getString("nom"));
            }

            // Añadimos la cantidad actual de totems que tenga el array
            itemsCorrectes = goodTotems.size();
            itemsIncorrectes = badTotems.size();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
