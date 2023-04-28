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

public class CiclesScreen extends ScreenAdapter {

    proyectoIEM game;

    ArrayList<String> listaCicles = new ArrayList<String>();

    String familiaNom = "";

    private TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;

    public CiclesScreen(proyectoIEM game, String familiaNom) {
        this.familiaNom = familiaNom;
        this.game = game;
        JSONObject test = new JSONObject();
        test.put("familiaNom", familiaNom);
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
        textButtonStyle.font = Utils.font(50);

        float yLista = .70f;
        float xLista = .50f;
        float espacio = .15f;

        for (int i = 0; i < listaCicles.size(); i++) {
            final TextButton button=new TextButton("",textButtonStyle);
            button.setColor(Color.WHITE);
            button.setText(listaCicles.get(i));
            button.setPosition(Gdx.graphics.getWidth() * xLista,Gdx.graphics.getHeight() * yLista);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    game.cicle = button.getText().toString();
                    game.setScreen(new TitleScreen(game));
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
        game.batch.draw(new Texture(Gdx.files.internal("UI/ciclesTitle.png")),Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .85f,1100, 200);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public void setFamiliaNom(String familiaNom) {
        this.familiaNom = familiaNom;
    }
}