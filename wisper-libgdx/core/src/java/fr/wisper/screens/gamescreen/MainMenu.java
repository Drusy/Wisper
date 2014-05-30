package fr.wisper.screens.gamescreen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.MenuAssets;
import fr.wisper.dialog.ExitDialog;
import fr.wisper.entities.Wisper;
import fr.wisper.animations.tween.ImageAccessor;
import fr.wisper.animations.tween.ParticleEffectAccessor;
import fr.wisper.animations.tween.SpriteAccessor;
import fr.wisper.screens.loading.LoadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

public class MainMenu implements FadingScreen {
    // Stage
    private ExtendedStage stage;
    private Group group;
    private Skin skin;

    // Buttons
    private Image startImageButton;
    private Image closeImageButton;
    private Image settingsImageButton;

    // Background image
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    // Black Wisper
    Wisper wisper;
    ClickListener wisperClickListener;
    long lastClickTime = 0;

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
        wisper.draw(batch, delta);
        batch.end();

        // Act stage
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
    }

    @Override
    public void show() {
        // Apply preferences
        applyPreferences();

        // Buttons
        initButtons();

        // Stage
        skin = MenuAssets.manager.get(MenuAssets.GlobalSkin);
        stage = new ExtendedStage(skin);
        stage.addActor(group);
        stage.addActor(settingsImageButton);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        // Background image
        batch = new SpriteBatch();
        splash = new Sprite(MenuAssets.manager.get(MenuAssets.SplashScreen));
        splash.setSize(Config.APP_WIDTH, Config.APP_HEIGHT);

        // Animations
        initAnimations();

        // Init wisper
        initWisper();
    }

    private void initWisper() {
        wisper = new Wisper("particles/black-wisper-noadditive.p");
        wisperClickListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime < Config.DOUBLE_TAP_INTERVAL) {
                    wisper.dash((int) x, (int) y, tweenManager);
                } else {
                    wisper.moveTo((int) x, (int) y, tweenManager, null);
                }
                lastClickTime = currentTime;

                return super.touchDown(event, x, y, pointer, button);
            }
        };
        stage.addListener(wisperClickListener);
    }

    private void applyPreferences() {
        Gdx.graphics.setVSync(SettingsMenu.vSync());
    }

    private void initButtons() {
        startImageButton = new Image(MenuAssets.manager.get(MenuAssets.NewGameButton));
        startImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeTo(new WisperChooseMenu());
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
                fadeTo(new SettingsMenu());
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

    public void fadeTo(final FadingScreen screen) {
        wisper.stopDraw();

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
                LoadingScreen loader = ((WisperGame) Gdx.app.getApplicationListener()).getLoader();
                loader.setNextScreen(screen);
            }
        }).start(tweenManager);
    }

    @Override
    public AssetManager getAssetManager() {
        return MenuAssets.manager;
    }

    @Override
    public void load() {
        MenuAssets.load();
    }

    private void initAnimations() {
        tweenManager = new TweenManager();

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Image.class, new ImageAccessor());
        Tween.registerAccessor(ParticleEffect.class, new ParticleEffectAccessor());

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
        skin.dispose();
        wisper.dispose();
        MenuAssets.dispose();
    }

    private class ExtendedStage extends Stage {
        private Skin skin;
        private ExitDialog dialog;

        public ExtendedStage(Skin skin) {
            super();

            this.skin = skin;
            this.dialog = new ExitDialog("Confirm Exit", this.skin);
        }

        /*
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector2 touchPos = Config.getProjectedCoordinates(screenX, screenY, getViewport());

            wisper.moveTo((int)touchPos.x, (int)touchPos.y, tweenManager, getViewport());
            return super.touchDown(screenX, screenY, pointer, button);
        }
        */

        @Override
        public boolean keyDown(int keyCode) {
            if(keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.BACK){
                dialog.show(this);
            }

            return super.keyDown(keyCode);
        }
    }
}
