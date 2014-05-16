package fr.wisper.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.MenuAssets;
import fr.wisper.tween.ImageAccessor;
import fr.wisper.tween.SpriteAccessor;
import fr.wisper.utils.Config;

public class MainMenu implements Screen {
    // Stage
    private Stage stage;
    private Group group;

    // Buttons
    private Image startImageButton;
    private Image closeImageButton;
    private Image settingsImageButton;

    // Background image
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

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

        // Act stage
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
    }

    @Override
    public void show() {
        // Apply preferences
        applyPreferences();

        // Assets
        MenuAssets.load();
        MenuAssets.manager.finishLoading();

        // Buttons
        initButtons();

        // Stage
        stage = new Stage();
        stage.addActor(group);
        stage.addActor(settingsImageButton);
        Gdx.input.setInputProcessor(stage);

        // Background image
        batch = new SpriteBatch();
        splash = new Sprite(MenuAssets.manager.get(MenuAssets.SplashScreen));
        splash.setSize(Config.APP_WIDTH, Config.APP_HEIGHT);

        // Animations
        initAnimations();
    }

    private void applyPreferences() {
        Gdx.graphics.setVSync(SettingsMenu.vSync());
    }

    private void initButtons() {
        startImageButton = new Image(MenuAssets.manager.get(MenuAssets.NewGameButton));
        startImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        startImageButton.addAction(Actions.moveBy(0, 150));

        closeImageButton = new Image(MenuAssets.manager.get(MenuAssets.CloseWisperButton));
        closeImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        settingsImageButton = new Image(MenuAssets.manager.get(MenuAssets.SettingsButton));
        settingsImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Tween.set(splash, SpriteAccessor.ALPHA).target(1).start(tweenManager);
                Tween.set(startImageButton, ImageAccessor.ALPHA).target(1).start(tweenManager);
                Tween.set(closeImageButton, ImageAccessor.ALPHA).target(1).start(tweenManager);
                Tween.set(settingsImageButton, ImageAccessor.ALPHA).target(1).start(tweenManager);

                Tween.to(closeImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).start(tweenManager);
                Tween.to(settingsImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).start(tweenManager);
                Tween.to(startImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).start(tweenManager);
                Tween.to(splash, SpriteAccessor.ALPHA, Config.ANIMATION_DURATION / 3f).target(0).setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsMenu());
                    }
                }).start(tweenManager);
            }
        });

        settingsImageButton.setPosition(
            Config.APP_WIDTH - settingsImageButton.getWidth() - 40,
            40);

        group = new Group();
        group.addActor(startImageButton);
        group.addActor(closeImageButton);
        group.addAction(Actions.moveBy(75, 75));
    }

    private void initAnimations() {
        tweenManager = new TweenManager();

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Image.class, new ImageAccessor());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(startImageButton, ImageAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(closeImageButton, ImageAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(settingsImageButton, ImageAccessor.ALPHA).target(0).start(tweenManager);

        Tween.to(splash, SpriteAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).start(tweenManager);
        Tween.to(startImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).delay(Config.ANIMATION_DURATION / 2f).start(tweenManager);
        Tween.to(closeImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).delay(Config.ANIMATION_DURATION / 2f).start(tweenManager);
        Tween.to(settingsImageButton, ImageAccessor.ALPHA, Config.ANIMATION_DURATION).target(1).delay(Config.ANIMATION_DURATION / 2f).start(tweenManager);

        tweenManager.update(Float.MIN_VALUE);
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
        MenuAssets.dispose();
    }
}
