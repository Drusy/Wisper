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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.animations.tween.BodyAccessor;
import fr.wisper.assets.GameScreenAssets;
import fr.wisper.entities.Wisper;
import fr.wisper.entities.WisperBox2d;
import fr.wisper.screens.loading.LoadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;
import fr.wisper.utils.ExtendedStage;
import net.dermetfan.utils.libgdx.box2d.Box2DMapObjectParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements FadingScreen {
    // Global stuff
    long lastClickTime = 0;
    boolean isDashUp = true;
    boolean isMoveUp = true;
    private Timer timer;
    private Timer.Task timerTask;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;

    // Stage
    private ExtendedStage<GameScreen> stage;

    // Box2d
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;
    Box2DMapObjectParser box2dParser;
    WisperBox2d wisper;
    private MouseJointDef jointDef;
    private MouseJoint joint;
    private List<WisperBox2d> toRemove = new ArrayList<WisperBox2d>();

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

        // Clear world
        for (WisperBox2d entity : toRemove) {
            world.destroyBody(entity.getWisperBody());
            entity.dispose();
        }
        toRemove.clear();

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
                if (((WisperBox2d) body.getUserData()).isComplete()) {
                    toRemove.add(((WisperBox2d) body.getUserData()));
                } else {
                    ((WisperBox2d) body.getUserData()).draw(batch, delta);
                }
            }
        }
        batch.end();

        // Update animations
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        WisperGame.Camera.zoom = Config.GAME_RATIO;
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
        initContactListener();

        // Wisper & Batch
        timer = new Timer();
        batch = new SpriteBatch();
        wisper = new WisperBox2d(chosenWisper, world);

        // Tiled map
        TiledMap map = new TmxMapLoader().load("tiledmap/tiledmap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, Config.GAME_RATIO);
        box2dParser = new Box2DMapObjectParser(Config.GAME_RATIO);
        box2dParser.load(world, map);

        // Joint
        jointDef = new MouseJointDef();
        jointDef.bodyA = box2dParser.getBodies().values().next();
        jointDef.collideConnected = true;
        jointDef.bodyB = wisper.getWisperBody();

        // Animations
        initAnimations();
    }

    private void moveWisperTo(float x, float y, float maxForce, float dampingRation) {
        if (isMoveUp) {
            jointDef.maxForce = maxForce; // 500
            jointDef.dampingRatio = dampingRation; // 15

            if (joint != null) {
                world.destroyJoint(joint);
                joint = null;
            }
            jointDef.target.set(wisper.getPosition());
            joint = (MouseJoint) world.createJoint(jointDef);
            joint.setTarget(new Vector2(x, y));
        } else {
            Debug.Log("Wait until dash is over to move !");
        }

    }

    private void dashWisperTo(float x, float y) {
        if (isDashUp && isMoveUp) {
            Vector2 particlePos = wisper.getPosition();
            Vector2 requestedPos = new Vector2(x, y);

            double distance = Math.max(
                    Math.sqrt(Math.pow(particlePos.x - requestedPos.x, 2) + Math.pow(particlePos.y - requestedPos.y, 2)),
                    1)  / Config.GAME_RATIO;
            double dashDistance = Math.min(distance, Config.WISPER_DASH_DISTANCE);
            float alpha = (float)dashDistance / (float)distance;

            Vector2 AB = new Vector2(requestedPos.x - particlePos.x, requestedPos.y - particlePos.y);
            Vector2 ABPrim = new Vector2(alpha * AB.x, alpha * AB.y);
            Vector2 BPrim = new Vector2(ABPrim.x + particlePos.x, ABPrim.y + particlePos.y);

            moveWisperTo(BPrim.x, BPrim.y, Config.BOX2D_WISPER_DASH_FORCE, Config.BOX2D_WISPER_DASH_DAMPING);

            timerTask = new Timer.Task() {
                @Override
                public void run() {
                    isDashUp = true;
                }
            };

            isDashUp = false;
            timer.scheduleTask(timerTask, Config.WISPER_DASH_TIMEOUT);

            isMoveUp = false;
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    isMoveUp = true;
                }
            }, Config.BOX2D_WISPER_DASH_TIME);
        } else {
            moveWisperTo(x, y, Config.BOX2D_WISPER_MOVE_FORCE, Config.BOX2D_WISPER_MOVE_DAMPING);
            Debug.Log("Dash not ready yet, " + (timerTask.getExecuteTimeMillis() - System.nanoTime() / 1000000) + "ms remaining");
        }
    }

    private void wisperShotWithDirection(float x, float y) {
        WisperBox2d bullet = new WisperBox2d(Wisper.BLUE_WISPER, world);
        bullet.getWisperBody().setBullet(true);
        float forceFactor = 75;
        float xForce = (x - wisper.getPosition().x) * forceFactor;
        float yForce = (y - wisper.getPosition().y) * forceFactor;

        float xOffset = xForce < 0 ? -1 : 1;
        float yOffset = yForce < 0 ? -1 : 1;

        bullet.getWisperBody().setTransform(wisper.getPosition().x + xOffset, wisper.getPosition().y + yOffset, wisper.getAngle());

        bullet.getWisperBody().applyLinearImpulse(
                new Vector2(xForce, yForce),
                bullet.getPosition(),
                true);
    }

    private void initInputListener() {
        stage.addListener(new ClickListener() {
            private int dragCount = 0;

            private void resetDrag() {
                dragCount = 0;
            }

            @Override
            public boolean touchDown(InputEvent event, final float x, final float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dragCount > 7) {
                    wisperShotWithDirection(x, y);
                    resetDrag();
                } else {
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastClickTime < Config.DOUBLE_TAP_INTERVAL) {
                        dashWisperTo(x, y);
                    } else {
                        moveWisperTo(x, y, Config.BOX2D_WISPER_MOVE_FORCE, Config.BOX2D_WISPER_MOVE_DAMPING);
                    }
                    lastClickTime = currentTime;
                }
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                ++dragCount;

                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    private void initContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                Debug.Log("Contact between " + bodyA.toString() + " and " + bodyB.toString());

                if (bodyA.getUserData() != null && bodyA.getUserData() instanceof WisperBox2d
                        && bodyA.getUserData() != wisper && bodyB.isBullet()) {
                    toRemove.add(((WisperBox2d) bodyB.getUserData()));
                    ((WisperBox2d)bodyA.getUserData()).explode();
                } else if (bodyB.getUserData() != null && bodyB.getUserData() instanceof WisperBox2d
                        && bodyB.getUserData() != wisper && bodyA.isBullet()) {
                    toRemove.add(((WisperBox2d) bodyA.getUserData()));
                    ((WisperBox2d)bodyB.getUserData()).explode();
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

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