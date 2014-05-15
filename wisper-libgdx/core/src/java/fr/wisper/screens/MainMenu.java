package fr.wisper.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.wisper.tween.ImageAccessor;
import fr.wisper.tween.SpriteAccessor;

public class MainMenu implements Screen {
    // Stage
    private Stage stage;
    private Group group;

    // Buttons
    private Image startImageButton;
    private Image closeImageButton;

    // Background image
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    // Textures
    Texture closeTexture = new Texture("ui/close-button.png");
    Texture startTexture = new Texture("ui/start-button.png");

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update animations
        tweenManager.update(delta);

        // Display background image
        batch.begin();
        splash.draw(batch);
        group.draw(batch, 1);
        batch.end();

        // Display table
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        // Buttons
        startImageButton = new Image(startTexture);
        startImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start Game");
            }
        });
        startImageButton.addAction(Actions.moveBy(0, 150));

        closeImageButton = new Image(closeTexture);
        closeImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        group = new Group();
        group.addActor(closeImageButton);
        group.addActor(startImageButton);
        group.addAction(Actions.moveTo(75, 75));

        // Stage
        stage = new Stage();
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);

        // Background image
        Texture splashTexture = new Texture("splash/splash.png");
        batch = new SpriteBatch();
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Animations
        tweenManager = new TweenManager();
        float animationTime = 2f;

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Image.class, new ImageAccessor());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(startImageButton, ImageAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(closeImageButton, ImageAccessor.ALPHA).target(0).start(tweenManager);

        Tween.to(splash, SpriteAccessor.ALPHA, animationTime).target(1).start(tweenManager);
        Tween.to(startImageButton, ImageAccessor.ALPHA, animationTime).target(1).delay(animationTime / 2f).start(tweenManager);
        Tween.to(closeImageButton, ImageAccessor.ALPHA, animationTime).target(1).delay(animationTime / 2f).start(tweenManager);
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
        batch.dispose();
        splash.getTexture().dispose();
        startTexture.dispose();
        closeTexture.dispose();
    }
}
