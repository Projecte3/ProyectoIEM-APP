package com.iem.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DialogBox extends Group {
    private NinePatch background;
    private Label label;

    public DialogBox(String text, NinePatch background) {
        this.background = background;
        label = new Label(text, new Label.LabelStyle(createFont(10), Color.BLACK));
        label.setWrap(true);
        addActor(label);
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    public BitmapFont createFont(int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/8-BIT WONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderWidth = 2f;
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;

        return generator.generateFont(parameter);
    }
}
