package fr.wisper.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.wisper.tween.SpriteAccessor;

public class SplashScreen implements Screen {
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        batch.begin();
        splash.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Texture splashTexture = new Texture("splash/splash.png");
        batch = new SpriteBatch();
        splash = new Sprite(splashTexture);
        tweenManager = new TweenManager();

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(0).delay(2).start(tweenManager);
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

    }
}
