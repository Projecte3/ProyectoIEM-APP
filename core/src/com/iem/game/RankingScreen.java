package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.iem.utils.APIPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class RankingScreen extends ScreenAdapter {

    proyectoIEM game;
    private Vector backgroundSprite;

    ArrayList<String> listaPlayers = new ArrayList<String>();

    int element_inici = 1; // TODO: Hacer que se pida en la pantalla
    int nombre_elements = 20; // TODO: Hacer que se pida en la pantalla

    int pagina = 0;

    TextButton button;

    private TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;

    public RankingScreen(proyectoIEM game) throws JSONException {
        this.game = game;
        JSONObject test = new JSONObject();
        test.put("element_inici", element_inici);
        test.put("nombre_elements", nombre_elements);
        int cnt = 1;
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_ranking",test);
            System.out.println(sb.toString());
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONArray players = objResponse.getJSONArray("result");
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);
                System.out.println("jugador: "+player.toString());

                String nom_jugador = player.getString("nom_jugador");
                int puntuacion = player.getInt("puntuacio");
                String playerStr = cnt + ". " + nom_jugador +" "+puntuacion;
                cnt++;
                listaPlayers.add(playerStr);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void show(){
        stage = new Stage();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;

        button=new TextButton("",textButtonStyle);
        button.setColor(Color.WHITE);
        button.setText("Siguiente Página");
        button.setHeight(100);
        button.setWidth(500);
        button.setPosition(Gdx.graphics.getWidth() * .50f,Gdx.graphics.getHeight() * .08f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pagina++;
            }
        });
        stage.addActor(button);

        button=new TextButton("",textButtonStyle);
        button.setColor(Color.WHITE);
        button.setText("Página Anterior");
        button.setHeight(100);
        button.setWidth(500);
        button.setPosition(Gdx.graphics.getWidth() * .20f,Gdx.graphics.getHeight() * .08f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(pagina != 0){
                    pagina--;
                }

            }
        });
        stage.addActor(button);

        button=new TextButton("",textButtonStyle);
        button.setColor(Color.WHITE);
        button.setText("<<");
        button.setHeight(100);
        button.setWidth(500);
        button.setPosition(Gdx.graphics.getWidth() * .01f,Gdx.graphics.getHeight() * .90f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new TitleScreen(game));

            }
        });
        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.draw(game.batch, "RANKING", Gdx.graphics.getWidth() * .10f, Gdx.graphics.getHeight() * .90f);
        float yLista = .75f;
        float xLista = .10f;
        float espacio = .12f;

        int posLista = 10 * pagina;
        int cnt = 0;
        for (int i = posLista; i < listaPlayers.size(); i++) {
            if (cnt == 10){ // Se suma +1 porque el numero del jugador no coincide con la posicion en la lista
                break;
            }
            if (cnt == 5){
                yLista = .75f;
                xLista = .50f;
            }
            game.font.draw(game.batch, listaPlayers.get(i), Gdx.graphics.getWidth() * xLista, Gdx.graphics.getHeight() * yLista);
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
