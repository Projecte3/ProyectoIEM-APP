package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.iem.utils.APIPost;
import com.iem.utils.GifDecoder;
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
    int fontSize;
    Stage stage;
    float elapsed;

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

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                break;
            case Desktop:
                fontSize = 40;
                break;
        }

        // Calcula el ancho y la altura de los botones en función de la pantalla
        float buttonWidth = Gdx.graphics.getWidth() * proyectoIEM.BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.10f;

        stage.addActor(Utils.createButton("Enrere", Gdx.graphics.getWidth() * .03f, Gdx.graphics.getHeight() * .82f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new FamiliesScreen(game));
            }
        }));

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Utils.createFont(fontSize);

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
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.begin();
        // game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("initBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/ciclesTitle.png")),Gdx.graphics.getWidth()* .25f,Gdx.graphics.getHeight() * .80f,Gdx.graphics.getWidth() *.5f, Gdx.graphics.getHeight() * .15f);
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

}