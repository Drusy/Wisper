package fr.wisper.screens.gamescreen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.SettingsAssets;
import fr.wisper.animations.tween.SpriteAccessor;
import fr.wisper.animations.tween.TableAccessor;
import fr.wisper.screens.loading.LoadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;
import fr.wisper.utils.ExtendedStage;

public class SettingsMenu implements FadingScreen {
    public static final String SAVE_FOLDER = "save-folder";
    public static final String V_SYNC = "v-sync";

    // Background image
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    // Stage
    private ExtendedStage<SettingsMenu> stage;
    private Table table;
    private Skin skin;

    public static FileHandle levelDirectory() {
        String prefsDir = WisperGame.preferences.getString(SAVE_FOLDER, Config.DEFAULT_SAVE_FOLDER).trim();

        return Gdx.files.absolute(prefsDir);
    }

    public static boolean vSync() {
        return WisperGame.preferences.getBoolean(V_SYNC, true);
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
        WisperGame.Camera.zoom = 1f;
        WisperGame.Camera.updateViewport();

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
        skin = SettingsAssets.manager.get(SettingsAssets.GlobalSkin);
        stage = new ExtendedStage(this, new MainMenu());
        table = new Table(skin);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        final CheckBox vSyncCheckBox = new CheckBox("", skin);
        vSyncCheckBox.setChecked(vSync());

        final TextField saveFolderInput = new TextField(levelDirectory().path(), skin);
        saveFolderInput.setMessageText("Saves folder");

        final TextButton backButton = new TextButton("Save configuration", skin, "medium");
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
                    WisperGame.preferences.putBoolean(V_SYNC, vSyncCheckBox.isChecked());
                    WisperGame.preferences.flush();

                    Debug.Log("[Settings] vSync " + (vSync() ? "enabled" : "disabled"));
                } else if(event.getListenerActor() == backButton) {
                    String actualLevelDirectory;

                    if (saveFolderInput.getText().trim().isEmpty()) {
                        actualLevelDirectory = Gdx.files.getExternalStoragePath() + Config.DEFAULT_SAVE_FOLDER;
                    } else {
                        actualLevelDirectory = saveFolderInput.getText().trim();
                    }

                    WisperGame.preferences.putString(SAVE_FOLDER, actualLevelDirectory);
                    WisperGame.preferences.flush();
                    Debug.Log("[Settings] Saved");

                    fadeTo(new MainMenu());
                }
            }
        };
        vSyncCheckBox.addListener(buttonHandler);
        backButton.addListener(buttonHandler);
    }

    public void fadeTo(final FadingScreen screen) {
        Tween.set(splash, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.set(table, TableAccessor.ALPHA).target(1).start(tweenManager);

        Tween.to(splash, SpriteAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).start(tweenManager);
        Tween.to(table, TableAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                LoadingScreen loader = ((WisperGame) Gdx.app.getApplicationListener()).getLoader();
                loader.setNextScreen(screen);
            }
        }).start(tweenManager);
    }

    @Override
    public AssetManager getAssetManager() {
        return SettingsAssets.manager;
    }

    @Override
    public void load() {
        SettingsAssets.load();
    }

    private void createTable(CheckBox vSyncCheckBox, TextField saveFolderInput, TextButton backButton) {
        table.padTop(100);
        table.add(new Label("Preferences", skin, "big-bold")).spaceBottom(125).colspan(2).expandX().row();
        table.add(new Label("Vertical Synchronization", skin, "medium-bold")).expandX();
        table.add(vSyncCheckBox).left().expandX();
        table.row();
        table.add(new Label("Saves folder", skin, "medium-bold")).expandX().top();
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
}