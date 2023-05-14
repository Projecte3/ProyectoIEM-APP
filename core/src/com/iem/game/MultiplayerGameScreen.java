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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.iem.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiplayerGameScreen extends ScreenAdapter {
    public static WebSocket socket;
    String address;
    int port;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    ArrayList<String> goodTotems = new ArrayList<>();
    ArrayList<String> badTotems = new ArrayList<>();
    ArrayList<Vector2> goodTotemPositions = new ArrayList<>();
    ArrayList<Vector2> badTotemPositions = new ArrayList<>();

    int itemsCorrectes;
    int itemsIncorrectes;
    float timer;

    static proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;
    int backgroundWidth = 3008;
    int backgroundHeight = 2624;

    //ANIMATION ATTRIBUTES
    Texture walkSheet, background, egg;
    public static float bgPosX;
    public static float bgPosY;

    TextureRegion[] IDLEFrameDown = new TextureRegion[1];
    TextureRegion[] IDLEFrameUp = new TextureRegion[1];
    TextureRegion[] IDLEFrameX = new TextureRegion[4];

    TextureRegion[] walkFrame = new TextureRegion[4];
    TextureRegion[] walkFrameUP = new TextureRegion[5];
    TextureRegion[] walkFrameDown = new TextureRegion[5];

    TextureRegion[] eggRegion = new TextureRegion[4];

    Animation<TextureRegion> IDLEMarioDown,IDLEMarioDown2,IDLEMarioDown3,IDLEMarioDown4, IDLEMarioUP,IDLEMarioX, walkMario, walkMarioUP, walkMarioDown, eggAnimation;

    float stateTime;
    int spriteSizeX, spriteSizeY;
    SpriteBatch batch;
     float posx;
     float posy;

    Rectangle playerRect;
    float playerSpeed;

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

    int bgMovement = 5;

    int fontSize;

    PositionThread positionThread = new PositionThread();

    HashMap<String, int[]> playersPositions = new HashMap<String, int[]>();

    public MultiplayerGameScreen(proyectoIEM game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1080, camera);

        address = "proyecteiem-api-production.up.railway.app";
        port = 443;

        socket = WebSockets.newSocket(WebSockets.toSecureWebSocketUrl(address, port));

        socket.setSendGracefully(false);
        socket.addListener(new WSListener());
        socket.connect();

        connectar(socket);


        positionThread.start();
    }

    @Override
    public void show() {
        stage = new Stage();

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                spriteSizeX = 5;
                spriteSizeY = 5;
                playerSpeed = 2;
                font = Utils.createFontMarquee(35);
                break;
            case Desktop:
                fontSize = 20;
                spriteSizeX = 2;
                spriteSizeY = 2;
                font = Utils.createFontMarquee(20);
                break;
        }

        walkSheet = new Texture(Gdx.files.internal("mario.png"));
        background = new Texture(Gdx.files.internal("village.png"));
        egg = new Texture(Gdx.files.internal("eggs.png"));
        posx = Gdx.graphics.getWidth() / 2;
        posy = Gdx.graphics.getHeight() / 2;
        bgPosX = 0;
        bgPosY = 0;
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
        IDLEMarioDown2 = new Animation<>(0.25f, IDLEFrameDown);
        IDLEMarioDown3 = new Animation<>(0.25f, IDLEFrameDown);
        IDLEMarioDown4 = new Animation<>(0.25f, IDLEFrameDown);
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
        TextureRegion IDLEDown2 = IDLEMarioDown2.getKeyFrame(stateTime, true);
        TextureRegion IDLEDown3 = IDLEMarioDown3.getKeyFrame(stateTime, true);
        TextureRegion IDLEDown4 = IDLEMarioDown4.getKeyFrame(stateTime, true);
        TextureRegion walkFrameX = walkMario.getKeyFrame(stateTime, true);
        TextureRegion walkUP = walkMarioUP.getKeyFrame(stateTime, true);
        TextureRegion walkDown = walkMarioDown.getKeyFrame(stateTime, true);
        TextureRegion eggChange = eggAnimation.getKeyFrame(stateTime, true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // TIMER
        int minutes = (int)(stateTime / 60);
        int seconds = Math.floorMod((int)stateTime, 60);
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
        stateTime += Gdx.graphics.getDeltaTime();

        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.position.set(posx, posy, 0);

        camera.update();

        batch.begin();
        batch.draw(background, bgPosX , bgPosY, backgroundWidth, backgroundHeight);

        for (int i = 0; i < goodTotemPositions.size(); i++) {
            Vector2 position = goodTotemPositions.get(i);
            String totem = goodTotems.get(i);
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
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), eggChange.getRegionHeight()-10,
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1;
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
                removeTotem(goodTotems.get(i));
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
            int visibleChars = Math.min(maxVisibleChars, totem.length() - currentPosition);
            String visibleText = "";
            if (currentPosition >= 0 && currentPosition < totem.length()) {
                try {
                    visibleText = totem.substring(currentPosition, currentPosition + visibleChars);
                } catch (StringIndexOutOfBoundsException e) {
                    visibleText = totem.substring(currentPosition);
                }
                font.draw(batch, visibleText, x, y);
            }
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), eggChange.getRegionHeight()-10,
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1;
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
                removeTotem(badTotems.get(i));
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
                bgPosY -= bgMovement;

                for (int i = 0; i < goodTotemPositions.size(); i++) {
                    Vector2 position = goodTotemPositions.get(i);

                    position.y = position.y - bgMovement;
                }

                for (int i = 0; i < badTotemPositions.size(); i++) {
                    Vector2 position = badTotemPositions.get(i);

                    position.y = position.y - bgMovement;
                }

                for (String player: playersPositions.keySet()){
                    playersPositions.get(player)[1] = playersPositions.get(player)[1] - bgMovement;
                }

                if (camera.position.x < screenWidth / 2) {
                    camera.position.x = screenWidth / 2;
                } else if (camera.position.x > backgroundWidth - screenWidth / 2) {
                    camera.position.x = backgroundWidth - screenWidth / 2;
                }

                if (camera.position.y < screenHeight / 2) {
                    camera.position.y = screenHeight / 2;
                } else if (camera.position.y > backgroundHeight - screenHeight / 2) {
                    camera.position.y = backgroundHeight - screenHeight / 2;
                }

                batch.begin();
                batch.draw(background, bgPosX, bgPosY, backgroundWidth,backgroundHeight);

                reDrawGoodEggs(delta, eggChange);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                playerRect.setPosition(posx, posy);
                batch.draw(walkUP, posx, posy, 0, 0,
                        walkUP.getRegionWidth(), walkUP.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();
                break;

            //GO DOWN ANIMATION
            case 2:
                IDLEMarioDown = new Animation<>(0.25f, walkFrameDown);
                bgPosY += bgMovement;

                for (int i = 0; i < goodTotemPositions.size(); i++) {
                    Vector2 position = goodTotemPositions.get(i);

                    position.y = position.y + bgMovement;
                }

                for (int i = 0; i < badTotemPositions.size(); i++) {
                    Vector2 position = badTotemPositions.get(i);

                    position.y = position.y + bgMovement;
                }

                for (String player: playersPositions.keySet()){
                    playersPositions.get(player)[1] = playersPositions.get(player)[1] + bgMovement;
                }

                batch.begin();
                batch.draw(background, bgPosX, bgPosY, backgroundWidth,backgroundHeight);
                reDrawGoodEggs(delta, eggChange);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                playerRect.setPosition(posx, posy);
                batch.draw(walkDown, posx, posy, 0, 0,
                        walkDown.getRegionWidth(), walkDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.end();

                break;

            //GO LEFT ANIMATION
            case 3:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                bgPosX += bgMovement;

                for (int i = 0; i < goodTotemPositions.size(); i++) {
                    Vector2 position = goodTotemPositions.get(i);

                    position.x = position.x + bgMovement;
                }

                for (int i = 0; i < badTotemPositions.size(); i++) {
                    Vector2 position = badTotemPositions.get(i);

                    position.x = position.x + bgMovement;
                }

                for (String player: playersPositions.keySet()){
                    playersPositions.get(player)[0] = playersPositions.get(player)[0] + bgMovement;
                }

                batch.begin();
                batch.draw(background, bgPosX, bgPosY, backgroundWidth,backgroundHeight);
                reDrawGoodEggs(delta, eggChange);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkFrameX, posx, posy, walkFrameX.getRegionWidth(), 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), -spriteSizeX, spriteSizeY, 0);
                playerRect.setPosition(posx, posy);
                batch.end();

                break;

            //GO RIGHT ANIMATION
            case 4:
                IDLEMarioDown = new Animation<>(0.25f, walkFrame);
                bgPosX -= bgMovement;
                for (int i = 0; i < goodTotemPositions.size() ; i++) {
                    Vector2 position = goodTotemPositions.get(i);

                    position.x = position.x - bgMovement;
                }

                for (int i = 0; i < badTotemPositions.size(); i++) {
                    Vector2 position = badTotemPositions.get(i);

                    position.x = position.x - bgMovement;
                }

                for (String player: playersPositions.keySet()){
                    playersPositions.get(player)[0] = playersPositions.get(player)[0] - bgMovement;
                }

                batch.begin();
                batch.draw(background, bgPosX, bgPosY, backgroundWidth,backgroundHeight);
                reDrawGoodEggs(delta, eggChange);
                playerRect.setSize(spriteSizeX, spriteSizeY);
                batch.draw(walkFrameX, posx, posy, 0, 0,
                        walkFrameX.getRegionWidth(), walkFrameX.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                playerRect.setPosition(posx, posy);
                batch.end();

                break;
        }

        // RENDER DE LOS OTROS JUGADORES

        String[] players = new String[playersPositions.size()];
        playersPositions.keySet().toArray(players);

        batch.begin();
        switch (players.length){
            case 1:
                System.out.println("test1: x: "+(float) playersPositions.get(players[0])[0]+", y: "+(float) playersPositions.get(players[0])[1]);
                batch.draw(IDLEDown2, (float) playersPositions.get(players[0])[0], (float) playersPositions.get(players[0])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                break;
            case 2:
                System.out.println("test1: x: "+(float) playersPositions.get(players[0])[0]+", y: "+(float) playersPositions.get(players[0])[1]);
                System.out.println("test2: x: "+(float) playersPositions.get(players[1])[0]+", y: "+(float) playersPositions.get(players[1])[1]);

                batch.draw(IDLEDown2, (float) playersPositions.get(players[0])[0], (float) playersPositions.get(players[0])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.draw(IDLEDown3, (float) playersPositions.get(players[1])[0], (float) playersPositions.get(players[1])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                break;
            case 3:
                System.out.println("test: x: "+(float) playersPositions.get(players[0])[0]+", y: "+(float) playersPositions.get(players[0])[1]);
                System.out.println("test2: x: "+(float) playersPositions.get(players[1])[0]+", y: "+(float) playersPositions.get(players[1])[1]);
                System.out.println("test3: x: "+(float) playersPositions.get(players[2])[0]+", y: "+(float) playersPositions.get(players[2])[1]);
                batch.draw(IDLEDown2, (float) playersPositions.get(players[0])[0], (float) playersPositions.get(players[0])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.draw(IDLEDown3, (float) playersPositions.get(players[1])[0], (float) playersPositions.get(players[1])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
                batch.draw(IDLEDown4, (float) playersPositions.get(players[2])[0], (float) playersPositions.get(players[2])[1], 0, 0,
                        IDLEDown.getRegionWidth(), IDLEDown.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
        }
        batch.end();




        /*
        try {
            Thread.sleep(200);
            sendPosition(posx, posy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */

        if(itemsCorrectes == 0){
            desconnectar(socket);
            game.setScreen(new EndScreen(game, stateTime, 5 - itemsCorrectes, 5 - itemsIncorrectes));
        }

        stage.draw();
        stage.act();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void movingTotem(float x, float y, ArrayList<Vector2> positions) {
        positions.add(new Vector2(x, y));
    }

    protected int virtual_joystick_control() {
        for(int i=0;i<10;i++)
            if (Gdx.input.isTouched(i)) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);

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

    public void setTotems(Map<String, Map<String, int[]>> map){
        for (Map.Entry<String, Map<String, int[]>> entry : map.entrySet()) {
            if(game.cicle.equals(entry.getKey())){
                Map<String, int[]> subMap = entry.getValue();
                for (Map.Entry<String, int[]> subEntry : subMap.entrySet()) {
                    goodTotems.add(subEntry.getKey());
                    movingTotem(subEntry.getValue()[0], subEntry.getValue()[1], goodTotemPositions);
                }
            } else {
                Map<String, int[]> subMap = entry.getValue();
                for (Map.Entry<String, int[]> subEntry : subMap.entrySet()) {
                    badTotems.add(subEntry.getKey());
                    movingTotem(subEntry.getValue()[0], subEntry.getValue()[1], badTotemPositions);
                }
            }
        }

        // Añadimos la cantidad actual de totems que tenga el array
        itemsCorrectes = goodTotems.size();
        itemsIncorrectes = badTotems.size();
    }

    class WSListener implements WebSocketListener {

        @Override
        public boolean onOpen(WebSocket webSocket) {
            System.out.println("Opening...");
            return false;
        }

        @Override
        public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
            System.out.println("Closing...");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, String packet) {
            JSONObject obj = new JSONObject(packet);

            switch (obj.getString("type")){
                case "llistaTotems":
                    JSONObject totems = obj.getJSONObject("totemsServer");
                    Map<String, Map<String, int[]>> mapTotems = new HashMap<>();

                    for (String cicles : totems.keySet()) {
                        JSONObject ocupacions = totems.getJSONObject(cicles);
                        Map<String, int[]> subMap = new HashMap<>();
                        for (String ocupacio : ocupacions.keySet()) {
                            int[] arr = new int[2];
                            arr[0] = ocupacions.getJSONArray(ocupacio).getInt(0);
                            arr[1] = ocupacions.getJSONArray(ocupacio).getInt(1);
                            subMap.put(ocupacio, arr);
                        }
                        mapTotems.put(cicles, subMap);
                    }

                    setTotems(mapTotems);
                    break;

                case "player_positions":
                    JSONObject players = obj.getJSONObject("message");
                    for (String player: players.keySet()) {
                        if (!player.equals(game.alies)){

                            int[] arr = new int[2];
                            arr[0] = players.getJSONArray(player).getInt(0);
                            arr[1] = players.getJSONArray(player).getInt(1);
                            playersPositions.put(player,arr);
                        }
                    }

                    break;
            }

            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, byte[] packet) {
            System.out.println("Message:");
            return false;
        }

        @Override
        public boolean onError(WebSocket webSocket, Throwable error) {
            System.out.println("ERROR:"+error.toString());
            return false;
        }
    }

    public void connectar(WebSocket socket) {
        JSONObject obj = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("nom_jugador", game.alies);
        user.put("cicle", game.cicle);

        obj.put("type", "info_usuari");
        obj.put("message", user);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        socket.send(obj.toString());
    }

    public void desconnectar(WebSocket socket) {
        JSONObject obj = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("nom_jugador", game.alies);
        user.put("cicle", game.cicle);

        obj.put("type", "desconnectar");
        obj.put("message", user);

        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */

        socket.send(obj.toString());

        PositionThread.flag = false;
        positionThread.stop();
    }
    public void removeTotem(String totemName) {
        JSONObject obj = new JSONObject();
        JSONObject totem = new JSONObject();
        totem.put("totem", totemName);

        obj.put("type", "remove_totem");
        obj.put("message", totem);

        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */

        socket.send(obj.toString());
    }
    public static void sendPosition() {


        JSONObject obj = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("jugador", game.alies);
        user.put("pos_x", bgPosX);
        user.put("pos_y", bgPosY);

        obj.put("type", "pos_jugador");
        obj.put("message", user);

        /*
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        */

        socket.send(obj.toString());
    }

    public void reDrawGoodEggs(float delta, TextureRegion eggChange){
        for (int i = 0; i < goodTotemPositions.size(); i++) {
            Vector2 position = goodTotemPositions.get(i);
            String totem = goodTotems.get(i);
            float x = position.x;
            float y = position.y;


            float scrollAmount = scrollSpeed * scrollTimer;
            int visibleChars = Math.min(maxVisibleChars, totem.length() - currentPosition);
            String visibleText;
            if (currentPosition >= 0 && currentPosition < totem.length()) {
                visibleText = totem.substring(currentPosition, Math.min(currentPosition + visibleChars, totem.length()));
                font.draw(batch, visibleText, x, y);
            }

            // Aquí dibujamos la textura después de dibujar el texto
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), (eggChange.getRegionHeight()-10),
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1;
                }
                timer = 0;
            }
            scrollTimer += delta;
            if (scrollAmount >= font.getSpaceXadvance() * visibleChars) {
                scrollTimer = 0;
            }
            float speed = 0.5f;
            scrollPosition += speed * delta;

        }
        for (int i = 0; i < badTotemPositions.size(); i++) {
            Vector2 position = badTotemPositions.get(i);
            String totem = badTotems.get(i);
            float x = position.x;
            float y = position.y;


            float scrollAmount = scrollSpeed * scrollTimer;
            int visibleChars = Math.min(maxVisibleChars, totem.length() - currentPosition);
            String visibleText;
            if (currentPosition >= 0 && currentPosition < totem.length()) {
                visibleText = totem.substring(currentPosition, Math.min(currentPosition + visibleChars, totem.length()));
                font.draw(batch, visibleText, x, y);
            }

            // Aquí dibujamos la textura después de dibujar el texto
            batch.draw(eggChange, x + 120, y, eggChange.getRegionWidth(), (eggChange.getRegionHeight()-10),
                    eggChange.getRegionWidth(), eggChange.getRegionHeight(), spriteSizeX, spriteSizeY, 0);
            timer += delta;
            if (timer >= delay) {
                currentPosition++;
                if (currentPosition >= totem.length()) {
                    currentPosition = visibleChars - 1;
                }
                timer = 0;
            }
            scrollTimer += delta;
            if (scrollAmount >= font.getSpaceXadvance() * visibleChars) {
                scrollTimer = 0;
            }
            float speed = 0.5f;
            scrollPosition += speed * delta;

        }

    }

}
