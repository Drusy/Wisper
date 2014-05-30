package fr.wisper.screens.gamescreen;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.animations.tween.BodyAccessor;
import fr.wisper.assets.GameScreenAssets;
import fr.wisper.entities.Wisper;
import fr.wisper.entities.WisperBox2d;
import fr.wisper.screens.loading.LoadingScreen;
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
    private OrthogonalTiledMapRenderer mapRenderer;
    Box2DMapObjectParser box2dParser;
    WisperBox2d wisper;
    private MouseJointDef jointDef;
    private MouseJoint joint;

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

        mapRenderer.setView(WisperGame.Camera);
        mapRenderer.getSpriteBatch().setProjectionMatrix(WisperGame.Camera.combined);
        mapRenderer.render();
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
        WisperGame.Camera.zoom = .1f;
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
        world = new World(new Vector2(0, 0f), true); // newton -9.81f
        debugRenderer = new Box2DDebugRenderer();

        // Wisper & Batch
        batch = new SpriteBatch();
        wisper = new WisperBox2d(chosenWisper, world);

        // Tiled map
        TiledMap map = new TmxMapLoader().load("tiledmap/tiledmap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, .1f);
        box2dParser = new Box2DMapObjectParser(.1f);
        box2dParser.load(world, map);

        // Joint
        jointDef = new MouseJointDef();
        jointDef.bodyA = box2dParser.getBodies().values().next();
        jointDef.collideConnected = true;
        jointDef.maxForce = 500;
        jointDef.dampingRatio = 15f;
        jointDef.bodyB = wisper.getWisperBody();

        // Animations
        initAnimations();
    }

    private void initInputListener() {
        stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, final float x, final float y, int pointer, int button) {
                //wisper.moveTo(x, y, tweenManager);
                wisper.getWisperBody().setActive(true);
                wisper.getWisperBody().setFixedRotation(true);
                world.QueryAABB(new QueryCallback() {
                    @Override
                    public boolean reportFixture(Fixture fixture) {
                        jointDef.bodyB = fixture.getBody();
                        jointDef.target.set(x, y);

                        joint = (MouseJoint) world.createJoint(jointDef);

                        return false;
                    }
                }, x, y, x, y);

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (joint != null) {
                    world.destroyJoint(joint);
                    joint = null;
                    wisper.getWisperBody().setActive(false);
                }


                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (joint != null) {
                    joint.setTarget(new Vector2(x, y));
                }

                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    private void initAnimations() {
        tweenManager = new TweenManager();

        Tween.registerAccessor(Body.class, new BodyAccessor());
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
        mapRenderer.dispose();
    }
}