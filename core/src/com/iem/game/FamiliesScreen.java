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

public class FamiliesScreen extends ScreenAdapter {

    proyectoIEM game;
    ArrayList<String> listaFamilies = new ArrayList<String>();

    TextButton.TextButtonStyle textButtonStyle;
    Stage stage;
    int fontSize;
    float elapsed;

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

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                break;
            case Desktop:
                fontSize = 40;
                break;
        }

        textButtonStyle.font = Utils.createFont(fontSize);

        // Calcula el ancho y la altura de los botones en función de la pantalla
        float buttonWidth = Gdx.graphics.getWidth() * proyectoIEM.BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.10f;
        stage.addActor(Utils.createButton("Enrere", Gdx.graphics.getWidth() * .03f, Gdx.graphics.getHeight() * .82f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));

        float yLista = .70f;
        float xLista = .50f;
        float espacio = .15f;

        for (int i = 0; i < listaFamilies.size(); i++) {
            final TextButton button=new TextButton("",textButtonStyle);
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
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/familiesTitle.png")),Gdx.graphics.getWidth()* .25f,Gdx.graphics.getHeight() * .80f,Gdx.graphics.getWidth() *.5f, Gdx.graphics.getHeight() * .15f);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}