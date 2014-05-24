package fr.wisper.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.tween.TableAccessor;
import fr.wisper.utils.Config;

public class WisperChooseMenu implements Screen {
    // Stage
    private ExtendedStage stage;
    private Table table;
    private Skin skin;

    // Tween
    private TweenManager tweenManager;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Manage camera
        WisperGame.Camera.update();

        // Stage
        stage.act(delta);
        stage.draw();

        // Update animations
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        ScalingViewport stageViewport = new ScalingViewport(
                Scaling.fit,
                WisperGame.VirtualViewport.getVirtualWidth(),
                WisperGame.VirtualViewport.getVirtualHeight(),
                WisperGame.Camera);

        stage.setViewport(stageViewport);
        stage.getViewport().update(width, height, true);
        table.invalidateHierarchy();
    }

    @Override
    public void show() {
        // Stage
        stage = new ExtendedStage(this);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        skin = new Skin(Gdx.files.internal("ui/skin.json"), new TextureAtlas("ui/atlas.pack"));

        // Table
        table = new Table(skin);
        table.setFillParent(true);
        createTable();

        // Animations
        initAnimations();
    }

    private void initAnimations() {
        tweenManager = new TweenManager();

        Tween.registerAccessor(Table.class, new TableAccessor());
        Tween.set(table, TableAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(table, TableAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).start(tweenManager);

        tweenManager.update(Float.MIN_VALUE);
    }

    public void fadeToMenu() {
        Tween.set(table, TableAccessor.ALPHA).target(1).start(tweenManager);

        Tween.to(table, TableAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        }).start(tweenManager);
    }

    private void createTable() {
        List<String> list = new List<String>(skin);
        list.setItems(new String[] {"Black Wisper", "Blue Wisper", "Red Wisper"});

        ScrollPane scrollPane = new ScrollPane(list, skin);

        TextButton start = new TextButton("Start", skin, "bold");
        start.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeToMenu();
            }
        });
        start.pad(10);

        table.padTop(50);
        table.add(new Label("Select your Wisper", skin, "big-bold")).colspan(2).expandX().spaceBottom(125).row();
        table.add(scrollPane).padLeft(10).minWidth(250).top().left();
        table.add(start).uniformX().expandX().expandY().center().row();
        table.add(start).colspan(2).expandX().bottom();
        table.padBottom(50);

        stage.addActor(table);
    }

    @Override
    public void hide() {
        dispose();
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
        skin.dispose();
    }

    private class ExtendedStage extends Stage {
        private WisperChooseMenu wisperChooseMenu;
        private boolean fading = false;

        public ExtendedStage(WisperChooseMenu wisperChooseMenu) {
            super();

            this.wisperChooseMenu = wisperChooseMenu;
        }

        @Override
        public boolean keyDown(int keyCode) {
            if(keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.BACK){
                if (!fading) {
                    wisperChooseMenu.fadeToMenu();
                    fading = true;
                }
            }

            return super.keyDown(keyCode);
        }
    }
}