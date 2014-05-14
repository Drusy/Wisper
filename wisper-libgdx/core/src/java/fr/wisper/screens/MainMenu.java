package fr.wisper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.wisper.Game.WisperGame;

public class MainMenu implements Screen {
    private Stage stage;
    private TextureAtlas atlas;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private Label heading;
    private Skin skin;
    private BitmapFont white;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Table.drawDebug(stage);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        atlas = new TextureAtlas("ui/button.pack");
        skin = new Skin(atlas);
        table = new Table(skin);
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("btn_black");
        textButtonStyle.down = skin.getDrawable("btn_black");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;
        //textButtonStyle.fontColor = Color.BLACK;
        //textButtonStyle.fontColor = Color.BLACK;

        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.pad(10);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        buttonPlay = new TextButton("Play", textButtonStyle);
        buttonPlay.pad(10);

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.BLACK);

        heading = new Label(WisperGame.GAME_NAME, headingStyle);
        heading.setFontScale(1.5f);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(heading);
        table.getCell(heading).spaceBottom(100);
        table.row();
        table.columnDefaults(2);
        table.add(buttonPlay);
        table.getCell(buttonPlay).spaceBottom(50);
        table.row();
        table.add(buttonExit);
        //table.debug();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        white.dispose();
    }
}
