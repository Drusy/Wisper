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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.MenuAssets;
import fr.wisper.dialog.ExitDialog;
import fr.wisper.tween.ImageAccessor;
import fr.wisper.tween.ParticleEffectAccessor;
import fr.wisper.tween.SpriteAccessor;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

public class MainMenu implements Screen {
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

    // Particle effect
    private ParticleEffect particleEffect;
    private boolean isParticleOn = true;

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
        if (isParticleOn) {
            particleEffect.draw(batch, delta);
        }
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
        skin = new Skin(Gdx.files.internal("ui/skin.json"), new TextureAtlas("ui/atlas.pack"));
        stage = new ExtendedStage(skin, this);
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
            isParticleOn = false;

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

        // Particle effects
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/black-wisper-noadditive.p"), Gdx.files.internal("particles"));
        particleEffect.setPosition(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2);
        particleEffect.start();

        Debug.Log("" + particleEffect.getEmitters().size);
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

    private void moveParticleEffect(int x, int y) {
        Tween.to(particleEffect, ParticleEffectAccessor.X, Config.ANIMATION_DURATION).target(x).start(tweenManager);
        Tween.to(particleEffect, ParticleEffectAccessor.Y, Config.ANIMATION_DURATION).target(y).start(tweenManager);
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
        particleEffect.dispose();
        MenuAssets.dispose();
    }

    private class ExtendedStage extends Stage {
        private Skin skin;
        private ExitDialog dialog;
        private MainMenu mainMenu;

        public ExtendedStage(Skin skin, MainMenu mainMenu) {
            super();

            this.skin = skin;
            this.dialog = new ExitDialog("Confirm Exit", this.skin);
            this.mainMenu = mainMenu;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector2 touchPos = Config.getProjectedCoordinates(screenX, screenY, getViewport());

            mainMenu.moveParticleEffect((int)touchPos.x , (int)touchPos.y);

            return super.touchDown(screenX, screenY, pointer, button);
        }

        @Override
        public boolean keyDown(int keyCode) {
            if(keyCode == Input.Keys.ESCAPE || keyCode == Input.Keys.BACK){
                dialog.show(this);
            }

            return super.keyDown(keyCode);
        }
    }
}
