package fr.wisper.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.SettingsAssets;
import fr.wisper.tween.SpriteAccessor;
import fr.wisper.tween.TableAccessor;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

public class SettingsMenu implements Screen {
    public static final String SAVE_FOLDER = "save-folder";
    public static final String V_SYNC = "v-sync";

    // Background image
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    // Stage
    private ExtendedStage stage;
    private Table table;
    private Skin skin;

    public static FileHandle levelDirectory() {
        String prefsDir = Gdx.app.getPreferences(Config.GAME_NAME).getString(SAVE_FOLDER, Config.DEFAULT_SAVE_FOLDER).trim();

        return Gdx.files.absolute(prefsDir);
    }

    public static boolean vSync() {
        return Gdx.app.getPreferences(Config.GAME_NAME).getBoolean(V_SYNC, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Manage camera
        WisperGame.Camera.update();
        batch.setProjectionMatrix(WisperGame.Camera.combined);

        // Display background image
        batch.begin();
        splash.draw(batch);
        batch.end();

        // Display menu
        stage.draw();
        stage.act(delta);

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
        // Assets
        SettingsAssets.load();
        SettingsAssets.manager.finishLoading();

        // Stage
        skin = new Skin(Gdx.files.internal("ui/skin.json"), new TextureAtlas("ui/atlas.pack"));
        stage = new ExtendedStage(this);
        table = new Table(skin);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        final CheckBox vSyncCheckBox = new CheckBox("", skin);
        vSyncCheckBox.setChecked(vSync());

        final TextField saveFolderInput = new TextField(levelDirectory().path(), skin);
        saveFolderInput.setMessageText("Saves folder");

        final TextButton backButton = new TextButton("Save configuration", skin);
        backButton.pad(10);

        // Background image
        batch = new SpriteBatch();
        splash = new Sprite(SettingsAssets.manager.get(SettingsAssets.SettingsSplash));
        splash.setSize(Config.APP_WIDTH, Config.APP_HEIGHT);

        // Listener
        manageListener(vSyncCheckBox, saveFolderInput, backButton);

        // Table
        createTable(vSyncCheckBox, saveFolderInput, backButton);
        stage.addActor(table);

        // Animations
        initAnimations();
    }

    private void initAnimations() {
        tweenManager = new TweenManager();

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Table.class, new TableAccessor());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(table, TableAccessor.ALPHA).target(0).start(tweenManager);

        Tween.to(splash, SpriteAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).start(tweenManager);
        Tween.to(table, TableAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).start(tweenManager);

        tweenManager.update(Float.MIN_VALUE);
    }

    private void manageListener(final CheckBox vSyncCheckBox, final TextField saveFolderInput, final TextButton backButton) {
        ClickListener buttonHandler = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(event.getListenerActor() == vSyncCheckBox) {
                    Gdx.app.getPreferences(Config.GAME_NAME).putBoolean(V_SYNC, vSyncCheckBox.isChecked());
                    Gdx.app.getPreferences(Config.GAME_NAME).flush();

                    Debug.Log("[Settings] vSync " + (vSync() ? "enabled" : "disabled"));
                } else if(event.getListenerActor() == backButton) {
                    String actualLevelDirectory;

                    if (saveFolderInput.getText().trim().isEmpty()) {
                        actualLevelDirectory = Gdx.files.getExternalStoragePath() + Config.DEFAULT_SAVE_FOLDER;
                    } else {
                        actualLevelDirectory = saveFolderInput.getText().trim();
                    }

                    Gdx.app.getPreferences(Config.GAME_NAME).putString(SAVE_FOLDER, actualLevelDirectory);
                    Gdx.app.getPreferences(Config.GAME_NAME).flush();
                    Debug.Log("[Settings] Saved");

                    fadeToMenu();
                }
            }
        };
        vSyncCheckBox.addListener(buttonHandler);
        backButton.addListener(buttonHandler);
    }

    public void fadeToMenu() {
        Tween.set(splash, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.set(table, TableAccessor.ALPHA).target(1).start(tweenManager);

        Tween.to(splash, SpriteAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).start(tweenManager);
        Tween.to(table, TableAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        }).start(tweenManager);
    }

    private void createTable(CheckBox vSyncCheckBox, TextField saveFolderInput, TextButton backButton) {
        table.padTop(100);
        table.add(new Label("Preferences", skin, "big")).spaceBottom(125).colspan(2).expandX().row();
        table.add(new Label("Vertical Synchronization", skin, "bold")).expandX();
        table.add(vSyncCheckBox).left().expandX();
        table.row();
        table.add(new Label("Saves folder", skin, "bold")).expandX().top();
        table.add(saveFolderInput).top().left().fillX().row();
        table.row();
        table.add(backButton).colspan(2).bottom().spaceBottom(125).expandY();
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
        batch.dispose();
        skin.dispose();
        stage.dispose();
        SettingsAssets.dispose();
    }

    private class ExtendedStage extends Stage {
        private SettingsMenu settingsMenu;
        private boolean fading = false;

        public ExtendedStage(SettingsMenu settingsMenu) {
            super();

            this.settingsMenu = settingsMenu;
        }

        @Override
        public boolean keyDown(int keyCode) {
            if(keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.BACK){
                if (!fading) {
                    settingsMenu.fadeToMenu();
                    fading = true;
                }
            }

            return super.keyDown(keyCode);
        }
    }
}