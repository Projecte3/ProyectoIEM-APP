package com.iem.game;

import static com.iem.utils.Utils.createButton;
import static com.iem.utils.Utils.createFont;
import static com.iem.utils.Utils.createLabel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.APIPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class RankingScreen extends ScreenAdapter {

    proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;

    static final float BUTTON_WIDTH_PERCENT = 0.20f;

    int fontSize;
    ArrayList<String> listaPlayers = new ArrayList<String>();

    int element_inici = 1; // TODO: Hacer que se pida en la pantalla
    int nombre_elements = 20; // TODO: Hacer que se pida en la pantalla
    int pagina = 0;

    Stage stage;
    float elapsed;

    boolean flag;

    public RankingScreen(proyectoIEM game) throws JSONException {
        this.game = game;

        // To adjust the viewport on all devices
        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1090, camera);

        // Request
        JSONObject test = new JSONObject();
        test.put("element_inici", element_inici);
        test.put("nombre_elements", nombre_elements);
        int cnt = 1;
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_ranking",test);
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONArray players = objResponse.getJSONArray("result");
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);
                if(player.getInt("ocult") == 0){
                    String nom_jugador = player.getString("nom_jugador");
                    int puntuacion = player.getInt("puntuacio");
                    String playerStr = cnt + ". " + nom_jugador +" "+puntuacion;
                    cnt++;
                    listaPlayers.add(playerStr);
                }
            }
            flag = false;
        } catch (Exception e) {
            flag = true;
        }
    }

    @Override
    public void show(){
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

        stage.addActor(createButton("Siguiente", Gdx.graphics.getWidth() * .55f, Gdx.graphics.getHeight() * .08f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                pagina++;
            }
        }));

        stage.addActor(createButton("Anterior", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .08f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                if(pagina != 0){
                    pagina--;
                }
            }
        }));

        stage.addActor(createButton("Enrere", Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .89f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));

        if (flag){
            stage.addActor(createLabel("No hi ha connexió\n amb el servidor", Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.5f, fontSize + 10));
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("ranking.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("ranking.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/rankingTitle.png")),Gdx.graphics.getWidth()* .30f,Gdx.graphics.getHeight() * .85f,Gdx.graphics.getWidth() *.4f, Gdx.graphics.getHeight() * .1f);

        float yLista = .75f;
        float xLista = .30f;
        float espacio = .12f;

        int posLista = 10 * pagina;
        int cnt = 0;
        for (int i = posLista; i < listaPlayers.size(); i++) {
            if (cnt == 10){ // Se suma +1 porque el numero del jugador no coincide con la posicion en la lista
                break;
            }
            if (cnt == 5){
                yLista = .75f;
                xLista = .55f;
            }
            createFont(fontSize).draw(game.batch, listaPlayers.get(i), Gdx.graphics.getWidth() * xLista, Gdx.graphics.getHeight() * yLista);
            yLista -= espacio;
            cnt++;
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
