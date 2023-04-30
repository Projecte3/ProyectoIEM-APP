package com.iem.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.iem.utils.GifDecoder;

import java.util.Random;

public class InputScreen extends ScreenAdapter {

    proyectoIEM game;
    OrthographicCamera camera;
    FitViewport viewport;

    static final float BUTTON_WIDTH_PERCENT = 0.20f;
    static final float TEXTFIELD_WIDTH_PERCENT = 0.50f;
    int fontSize;

    TextField inputNom;
    Stage stage;
    float elapsed;

    String[] arrayAlies = {"Mario", "Luigi", "Princesa Peach", "Yoshi", "Toad", "Bowser", "Wario", "Donkey Kong", "Daisy", "Birdo", "Waluigi", "Goomba", "Koopa Troopa", "Boo", "Bullet Bill", "Chain Chomp", "King Boo", "Petey Piranha", "Piranha Plant", "Shy Guy", "Dry Bones", "Hammer Bro", "Lakitu", "Monty Mole", "Blooper", "Bob-omb", "Thwomp", "Whomp", "Rosalina", "Captain Toad", "Pauline", "Cappy", "Poochy"};

    public InputScreen(proyectoIEM game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(2048, 1090, camera);
    }


    @Override
    public void show(){
        stage = new Stage();

        switch (Gdx.app.getType()){
            case Android:
                fontSize = 40;
                break;
            case Desktop:
                fontSize = 20;
                break;
        }

        // Calcula el ancho y la altura de los botones en función de la pantalla
        float buttonWidth = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
        float buttonHeight = Gdx.graphics.getHeight() * 0.12f;

        float textfieldWidth = Gdx.graphics.getWidth() * TEXTFIELD_WIDTH_PERCENT;
        float textfieldHeight = Gdx.graphics.getHeight() * 0.25f;

        inputNom = createTextField("Introduce un alias", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * .40f, textfieldWidth, textfieldHeight, fontSize, new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return true;
            }
        });
        stage.addActor(inputNom);

        stage.addActor(createButton("Introducir", Gdx.graphics.getWidth() * .40f, Gdx.graphics.getHeight() * .15f, buttonWidth, buttonHeight, fontSize,new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                if(inputNom.getText().equals("")){
                    game.alies = generarAlias() + Math.floor(Math.random() * 3);
                } else {
                    game.alies = inputNom.getText();
                }
                game.setScreen(new TitleScreen(game));
            }
        }));

        stage.addActor(createButton("Volver", Gdx.graphics.getWidth() * .01f, Gdx.graphics.getHeight() * .85f,  buttonWidth, buttonHeight, fontSize,new ClickListener(){
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
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.begin();
        game.batch.draw(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("initBackground.gif").read()).getKeyFrame(elapsed), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.draw();
        stage.act();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public String generarAlias() {
        Random rand = new Random();
        int indice = rand.nextInt(arrayAlies.length);
        return arrayAlies[indice];
    }

    public TextField createTextField(String labelText, float x, float y, float width, float height, int size, InputListener listener) {
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/8-BIT WONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = generator.generateFont(parameter);
        style.fontColor = Color.BLACK;
        style.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/textfield_cursor.png"))));
        style.selection = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/textfield_selection.png"))));
        style.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("UI/textfield_background.png"))));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = generator.generateFont(parameter);
        skin.add("default", labelStyle);

        Label label = new Label(labelText, skin);
        label.setPosition(x, y + height); // Coloca la etiqueta encima del cuadro de texto
        label.setWidth(width);
        label.setAlignment(Align.center);

        // Crea el cuadro de texto
        TextField textField = new TextField("", style);
        textField.setPosition(x, y);
        textField.setWidth(width);
        textField.setHeight(height);
        textField.addListener(listener);

        // Agrega la etiqueta y el cuadro de texto al stage
        stage.addActor(label);
        stage.addActor(textField);

        return textField;
    }

    public Button createButton(String labelStr, float x, float y, float width, float height, int size, ClickListener listener) {
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
