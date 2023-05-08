package com.iem.game;

import static com.iem.game.RankingScreen.BUTTON_WIDTH_PERCENT;
import static com.iem.utils.Utils.createButton;
import static com.iem.utils.Utils.createLabel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.iem.utils.APIPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EndScreen extends ScreenAdapter {

    proyectoIEM game;
    Stage stage;
    Button guardar;
    String dispositiu;
    int fontSize;
    boolean saved;

    float elapsed;
    float time;
    int totalItemsC;
    int totalItemsInc;

    public EndScreen(proyectoIEM game, float time, int itemsC, int itemsInc) {
        this.game = game;
        this.time = time;
        this.totalItemsC = itemsC;
        this.totalItemsInc = itemsInc;
    }

    @Override
    public void show() {
        stage = new Stage();
        switch (Gdx.app.getType()){
            case Android:
                fontSize = 60;
                dispositiu = "Android";
                break;
            case Desktop:
                fontSize = 25;
                dispositiu = "Desktop";
                break;
        }

        float buttonWidth = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.10f;

        stage.addActor(createLabel("Temps emprat: " + String.format("%d:%02d", Math.round(time / 60), Math.round(time % 60)), Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.6f, fontSize));
        stage.addActor(createLabel("Items correctes recollits: " + totalItemsC, Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.5f, fontSize));
        stage.addActor(createLabel("Items incorrectes recollits: " + totalItemsInc, Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.4f, fontSize));

        stage.addActor(createButton("Tornar a inici", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .15f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));
        guardar = createButton("Guardar record", Gdx.graphics.getWidth() * .50f, Gdx.graphics.getHeight() * .15f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                try {
                    game.sound.play(1.0f);
                    guardarRecord();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.addActor(guardar);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(new Texture(Gdx.files.internal("UI/gameoverBackground.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/gameOverTitle.png")),Gdx.graphics.getWidth()* .20f,Gdx.graphics.getHeight() * .80f,Gdx.graphics.getWidth() *.6f, Gdx.graphics.getHeight() * .15f);
        game.batch.end();

        if(saved){
            guardar.setTouchable(Touchable.disabled);
        }

        stage.draw();
        stage.act();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void guardarRecord() throws JSONException {
        JSONObject test = new JSONObject();
        test.put("nom_jugador",game.alies);
        test.put("cicle",game.cicle);
        test.put("items_correctes",totalItemsC);
        test.put("items_incorrectes",totalItemsInc);
        test.put("temps_emprat",Double.parseDouble(String.valueOf(time)));
        test.put("dispositiu",dispositiu);

        try{
            StringBuffer sb = new APIPost().sendPost("https://proyecteiem-api-production.up.railway.app/set_record",test);
            JSONObject objResponse = new JSONObject(sb.toString());
            if(objResponse.getString("status").equals("OK")){
                stage.addActor(createLabel("Record guardat", Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.20f, fontSize));
                saved = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
