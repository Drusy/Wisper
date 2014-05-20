package fr.wisper.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ExitDialog extends Dialog {
    private final Integer YES = 0;
    private final Integer NO = 1;
    public ExitDialog(String title, Skin skin) {
        super(title, skin);

        text(new Label("Do you want to leave Wisper ?", skin, "bold"));
        button(new TextButton("Yes", skin, "bold"), YES);
        button(new TextButton("NO", skin, "bold"), NO);
        padTop(100);
        padBottom(30);
    }

    @Override
    protected void result(Object object) {
        if (object == YES) {
            Gdx.app.exit();
        }

        super.result(object);
    }
}