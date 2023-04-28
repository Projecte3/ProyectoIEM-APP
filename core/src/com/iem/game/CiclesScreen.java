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

public class CiclesScreen extends ScreenAdapter {

    proyectoIEM game;
    private Vector backgroundSprite;

    ArrayList<String> listaCicles = new ArrayList<String>();

    int familiaId = 1;


    private TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;

    public CiclesScreen(proyectoIEM game) {
        this.game = game;
        JSONObject test = new JSONObject();
        test.put("familiaId", familiaId);
        try {
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/get_cicles",test);
            JSONObject objResponse = new JSONObject(sb.toString());
            JSONArray cicles = objResponse.getJSONArray("result");
            for (int i = 0; i < cicles.length(); i++) {
                JSONObject cicle = cicles.getJSONObject(i);
                String cicleStr = cicle.getString("nom");
                listaCicles.add(cicleStr);
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

        for (int i = 0; i < listaCicles.size(); i++) {
            final TextButton button=new TextButton("Finish",textButtonStyle);
            button.setColor(Color.WHITE);
            button.setText(listaCicles.get(i));
            button.setPosition(Gdx.graphics.getWidth() * xLista,Gdx.graphics.getHeight() * yLista);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    System.out.println(button.getText());
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
        game.font.draw(game.batch, "CICLES", Gdx.graphics.getWidth() * .40f, Gdx.graphics.getHeight() * .90f);

        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}