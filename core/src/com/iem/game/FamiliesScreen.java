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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FamiliesScreen extends ScreenAdapter {

    proyectoIEM game;
    private Vector backgroundSprite;

    ArrayList<String> listaFamilies = new ArrayList<String>();

    int familiaId = 1;


    private TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;

    public FamiliesScreen(proyectoIEM game) {
        this.game = game;
        JSONObject test = new JSONObject();
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_families",test);
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONArray families = objResponse.getJSONArray("result");
            for (int i = 0; i < families.length(); i++) {
                JSONObject familia = families.getJSONObject(i);
                String familiaStr = familia.getString("nom");

                listaFamilies.add(familiaStr);
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

        float yLista = .60f;
        float xLista = .40f;
        float espacio = .15f;

        for (int i = 0; i < listaFamilies.size(); i++) {
            final TextButton button=new TextButton("Finish",textButtonStyle);
            button.setColor(Color.WHITE);
            button.setText(listaFamilies.get(i));
            button.setPosition(Gdx.graphics.getWidth() * xLista,Gdx.graphics.getHeight() * yLista);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    System.out.println(button.getText());
                    game.setScreen(new CiclesScreen(game,  button.getText().toString()));
                }
            });
            stage.addActor(button);
            yLista -= espacio;
        }


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.draw(game.batch, "FAMILIES", Gdx.graphics.getWidth() * .40f, Gdx.graphics.getHeight() * .90f);

        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}