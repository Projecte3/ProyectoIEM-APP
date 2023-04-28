package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class RankingScreen extends ScreenAdapter {

    proyectoIEM game;

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


        stage.addActor(createButton("Anterior", Gdx.graphics.getWidth() * .55f, Gdx.graphics.getHeight() * .08f, 500, 150, 50,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                pagina++;
            }
        }));


        stage.addActor(createButton("Siguiente", Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .08f, 500, 150, 50,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                if(pagina != 0){
                    pagina--;
                }
            }
        }));

        stage.addActor(createButton("Volver", Gdx.graphics.getWidth() * .15f, Gdx.graphics.getHeight() * .85f, 500, 150, 50,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                game.sound.play(1.0f);
                game.setScreen(new TitleScreen(game));
            }
        }));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(new Texture(Gdx.files.internal("ranking.png")),0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(new Texture(Gdx.files.internal("UI/rankingTitle.png")),Gdx.graphics.getWidth() * .35f, Gdx.graphics.getHeight() * .85f,1100, 200);
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

    public Button createButton(String labelStr, float x, float y, float width, float height, int size,ClickListener listener) {
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/8-BIT WONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = generator.generateFont(parameter);
        skin.add("default", labelStyle);

        // Crear el estilo del botón
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/button_up.png"))));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/button_down.png"))));

        // Crear el botón con el estilo y las dimensiones especificadas
        Button button = new Button(buttonStyle);
        button.setPosition(x, y);
        button.setWidth(width);
        button.setHeight(height);

        // Crear la etiqueta con el texto especificado y el estilo por defecto
        Label label = new Label(labelStr, skin);
        label.setAlignment(Align.center); // centrar el texto en la etiqueta

        // Agregar la etiqueta al botón
        button.add(label);

        // Agregar el listener al botón
        button.addListener(listener);

        // Agregar el botón al escenario
        stage.addActor(button);

        // Devolver el botón creado
        return button;
    }
}
