package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.iem.utils.APIPost;
import com.iem.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FamiliesScreen extends ScreenAdapter {

    proyectoIEM game;
    ArrayList<String> listaFamilies = new ArrayList<String>();

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
        textButtonStyle.font = Utils.font(50);

        float yLista = .70f;
        float xLista = .50f;
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
                    game.setScreen(new CiclesScreen(game, button.getText().toString()));
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
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")), 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/familiesTitle.png")),Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .85f,1100, 200);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}