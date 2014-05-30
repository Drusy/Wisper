package fr.wisper.screens.gamescreen;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.GameScreenAssets;
import fr.wisper.entities.Wisper;
import fr.wisper.entities.WisperBox2d;
import fr.wisper.screens.loading.LoadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;
import fr.wisper.utils.ExtendedStage;
import net.dermetfan.utils.libgdx.box2d.Box2DMapObjectParser;

public class GameScreen implements FadingScreen {
    // Stage
    private ExtendedStage<GameScreen> stage;

    // Box2d
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    WisperBox2d wisper;

    // Batch
    private SpriteBatch batch;
    private Array<Body> bodies = new Array<Body>();
    private int chosenWisper = Wisper.BLACK_WISPER;

    // Tween
    private TweenManager tweenManager;

    public GameScreen(int chosenWisper) {
        this.chosenWisper = chosenWisper;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Manage camera
        WisperGame.Camera.update();

        // Stage
        stage.act(delta);
        stage.draw();

        // Box2D
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        wisper.applyForceToCenter();
        debugRenderer.render(world, WisperGame.Camera.combined);

        // Batch
        batch.setProjectionMatrix(WisperGame.Camera.combined);
        world.getBodies(bodies);
        batch.begin();
        for (Body body : bodies) {
            if (body.getUserData() != null && body.getUserData() instanceof WisperBox2d) {
                ((WisperBox2d) body.getUserData()).draw(batch, delta);
            }
        }
        batch.end();

        // Update animations
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        WisperGame.Camera.zoom = 0.1f;
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
        // Stage
        stage = new ExtendedStage(this, new WisperChooseMenu());
        initInputListener();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        // Box2d
        world = new World(new Vector2(0, -9.81f), true); // newton -9.81f
        debugRenderer = new Box2DDebugRenderer();
        createGroundShape();

        // Wisper & Batch
        batch = new SpriteBatch();
        switch (chosenWisper) {
            case Wisper.BLACK_WISPER:
                wisper = new WisperBox2d("particles/black-wisper-small-noadditive.p", world);
                break;
            case Wisper.BLUE_WISPER:
                wisper = new WisperBox2d("particles/blue-wisper-small-noadditive.p", world);
                break;
            case Wisper.RED_WISPER:
                wisper = new WisperBox2d("particles/red-wisper-small-noadditive.p", world);
                break;
            default:
                break;
        }


        // Animations
        initAnimations();
    }

    private void initInputListener() {
        stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                wisper.movement.y = 500f;

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                wisper.movement.y = 0f;

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }


    private void createGroundShape() {
        ChainShape shape = new ChainShape();
        shape.createChain(new Vector2[]{new Vector2(0, Config.APP_HEIGHT / 2), new Vector2(Config.APP_WIDTH, Config.APP_HEIGHT / 2)});

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, -30); // Meters

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = .5f; // [0 - 1]
        fixtureDef.restitution = 0f; // velocity loose

        world.createBody(groundBodyDef).createFixture(fixtureDef);
        shape.dispose();
    }

    private void initAnimations() {
        tweenManager = new TweenManager();
    }

    public void fadeTo(final FadingScreen screen) {
        LoadingScreen loader = ((WisperGame) Gdx.app.getApplicationListener()).getLoader();
        loader.setNextScreen(screen);
    }

    @Override
    public AssetManager getAssetManager() {
        return GameScreenAssets.manager;
    }

    @Override
    public void load() {
        GameScreenAssets.load();
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
        wisper.dispose();
        stage.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}