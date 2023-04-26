package com.iem.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class HomeScreen extends ApplicationAdapter implements InputProcessor {

    private Stage stage;
    private TextField textField;

    @Override
    public void create () {
        // Inicializa el objeto Stage para manejar la entrada de usuario
        stage = new Stage();

        // Crea el objeto TextField y lo agrega al Stage
        textField = new TextField("", new Skin(Gdx.files.internal("uiskin.json")));
        textField.setPosition(100, 100);
        stage.addActor(textField);

        // Configura el objeto InputProcessor para manejar la entrada de usuario
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render () {
        // Limpia la pantalla con un color sólido
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibuja el Stage
        stage.act();
        stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // Este método se llama cuando el usuario presiona una tecla en el teclado
        // Puedes utilizarlo para manejar la entrada de texto en el TextField
        textField.appendText(String.valueOf(character));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    // Implementa el resto de los métodos de InputProcessor según tus necesidades

}
