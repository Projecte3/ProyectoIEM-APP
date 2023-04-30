package com.iem.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class Utils {
    public static Dialog dialog(){
        Skin skin = new Skin();

        Dialog dialog = new Dialog("Warning", skin, "dialog") {
            public void result(Object obj) {
                System.out.println("result "+obj);
            }
        };
        dialog.text("Are you sure you want to quit?");
        dialog.button("Yes", true); //sends "true" as the result
        dialog.button("No", false);  //sends "false" as the result
        dialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed

        return dialog;
    }

    public static Button createButton(String labelStr, float x, float y, float width, float height, int size, ClickListener listener) {
        Skin skin = new Skin();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = createFont(size);
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

        // Devolver el botón creado
        return button;
    }

    public static BitmapFont createFont(int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;

        return generator.generateFont(parameter);
    }

    public static Label createLabel(String str, float x, float y, int size){
        Skin skin = new Skin();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = createFont(size);
        skin.add("default", labelStyle);

        Label label = new Label(str, skin);
        label.setPosition(x,y,Align.left);

        return label;
    }

    public static TextField createTextField(Stage stage, String labelText, float x, float y, float width, float height, int size, InputListener listener) {
        Skin skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = generator.generateFont(parameter);
        style.fontColor = Color.WHITE;
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

        return textField;
    }

}
