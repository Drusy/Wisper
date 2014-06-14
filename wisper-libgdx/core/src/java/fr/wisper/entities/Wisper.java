package fr.wisper.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import fr.wisper.animations.tween.ParticleEffectAccessor;
import fr.wisper.assets.MenuAssets;
import fr.wisper.dialog.SpeechBubble;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class Wisper extends Actor {
    public static final int BLACK_WISPER = 0;
    public static final int BLUE_WISPER = 1;
    public static final int RED_WISPER = 2;

    protected ParticleEffect particleEffect;
    protected boolean isParticleOn = true;
    protected boolean isDashUp = true;
    protected Timer timer = new Timer();
    protected Timer.Task timerTask;
    protected float offset;
    protected SpeechBubble bubbleSpeech;

    private List<String> speechList = new ArrayList<String>();

    public Wisper(String particleFile) {
        particleEffect = new ParticleEffect();
        init(particleFile);
    }

    public void setPosition(float x, float y) {
        particleEffect.setPosition(x - offset, y);
    }

    public void scale(float scaleValue) {
        float scaling;

        for (ParticleEmitter emitter : particleEffect.getEmitters()) {
            scaling = emitter.getScale().getHighMax();
            emitter.getScale().setHigh(scaling * scaleValue);

            scaling = emitter.getScale().getLowMax();
            emitter.getScale().setLow(scaling * scaleValue);

            scaling = emitter.getVelocity().getHighMax();
            emitter.getVelocity().setHigh(scaling * scaleValue);

            scaling = emitter.getVelocity().getLowMax();
            emitter.getVelocity().setLow(scaling * scaleValue);

            scaling = emitter.getXOffsetValue().getLowMax();
            emitter.getXOffsetValue().setLow(scaling * scaleValue);

            scaling = emitter.getYOffsetValue().getLowMax();
            emitter.getYOffsetValue().setLow(scaling * scaleValue);
        }
    }

    public void draw(Batch batch, float delta) {
        if (isParticleOn) {
            particleEffect.draw(batch, delta);

            if (bubbleSpeech != null && bubbleSpeech.isAlive()) {
                bubbleSpeech.act(delta);
                bubbleSpeech.draw(batch, delta);
            } else if (!speechList.isEmpty()) {
                speech(speechList.get(0));
                speechList.remove(0);
            }
        }
    }

    public boolean isComplete() {
        return particleEffect.isComplete();
    }

    public void startIntroSpeech() {
        speechList.add("Hello, I'm a Wisper");
        speechList.add("Click the world to make me move");
        speechList.add("Double click to dash!");
    }

    public void speech(String string) {
        NinePatch ninePatch = MenuAssets.manager.get(MenuAssets.BubbleAtlas).createPatch("bubble");
        BitmapFont bubbleFont = MenuAssets.manager.get(MenuAssets.BubbleFont);
        bubbleSpeech = new SpeechBubble(ninePatch, bubbleFont);
        bubbleSpeech.init(string, getX(), getY());
        bubbleSpeech.setFollow(this);
        bubbleSpeech.setColor(new Color(0.3f, 0.3f, 0.3f, 0.7f));
    }

    public float getOffset() {
        return offset;
    }

    public void stopDraw() {
        speechList.clear();
        isParticleOn = false;
    }

    private void init(String particleFile) {
        particleEffect.load(Gdx.files.internal(particleFile), Gdx.files.internal("particles"));
        particleEffect.setPosition(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2);
        particleEffect.start();

        offset = particleEffect.getEmitters().first().getXOffsetValue().getLowMax() / 2;
    }

    @Override
    public float getX() {
        return (int)particleEffect.getEmitters().first().getX();
    }

    @Override
    public float getY() {
        return (int)particleEffect.getEmitters().first().getY();
    }

    public void moveTo(float x, float y, TweenManager tweenManager, TweenCallback callback) {
        //Vector2 particlePos = Config.getProjectedCoordinates(getX(), getY(), viewport);
        //Vector2 requestedPos = Config.getProjectedCoordinates(x, y, viewport);

        Vector2 particlePos = new Vector2(getX(), getY());
        Vector2 requestedPos = new Vector2(x, y);

        double distance = Math.sqrt(
                (float)Math.pow(particlePos.x - requestedPos.x, 2) +
                (float)Math.pow(particlePos.y - requestedPos.y, 2));
        double duration = distance / Config.WISPER_SPEED;

        moveToWithDuration(x, y, tweenManager, duration, Quad.OUT, callback);
    }

    public void moveToWithDuration(float x, float y, TweenManager tweenManager, double duration, TweenEquation equation, TweenCallback callback) {
        tweenManager.killTarget(particleEffect);
        Tween.to(particleEffect, ParticleEffectAccessor.X, (float)duration)
                .target(x - (particleEffect.getEmitters().first().getXOffsetValue().getLowMax() / 2))
                .ease(equation).start(tweenManager);
        Tween.to(particleEffect, ParticleEffectAccessor.Y, (float)duration).target(y)
                .ease(equation).start(tweenManager).setCallback(callback);
    }

    public void dash(final float x, final float y, final TweenManager tweenManager) {
        if (isDashUp) {
            Vector2 particlePos = new Vector2(getX(), getY());
            Vector2 requestedPos = new Vector2(x, y);

            double distance = Math.max(
                    Math.sqrt(Math.pow(particlePos.x - requestedPos.x, 2) + Math.pow(particlePos.y - requestedPos.y, 2)),
                    1);
            double dashDistance = Math.min(distance, Config.WISPER_DASH_DISTANCE);
            float alpha = (float)dashDistance / (float)distance;

            Vector2 AB = new Vector2(requestedPos.x - particlePos.x, requestedPos.y - particlePos.y);
            Vector2 ABPrim = new Vector2(alpha * AB.x, alpha * AB.y);
            Vector2 BPrim = new Vector2(ABPrim.x + particlePos.x, ABPrim.y + particlePos.y);

            tweenManager.killTarget(particleEffect);
            Tween.to(particleEffect, ParticleEffectAccessor.X, Config.WISPER_DASH_DURATION)
                    .target(BPrim.x - (particleEffect.getEmitters().first().getXOffsetValue().getLowMax() / 2))
                    .ease(Quad.OUT).start(tweenManager);
            Tween.to(particleEffect, ParticleEffectAccessor.Y, Config.WISPER_DASH_DURATION).target(BPrim.y)
                    .ease(Quad.OUT).setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    moveTo(x, y, tweenManager, null);
                }
            }).start(tweenManager);

            timerTask = new Timer.Task() {
                @Override
                public void run() {
                    isDashUp = true;
                }
            };
            isDashUp = false;
            timer.scheduleTask(timerTask, (long) Config.WISPER_DASH_TIMEOUT);
        } else {
            moveTo(x, y, tweenManager, null);
            Debug.Log("Dash not ready yet, " + (timerTask.getExecuteTimeMillis() - System.nanoTime() / 1000000) + "ms remaining");
        }
    }

    public void explode() {
        init("particles/spark.p");
    }

    public void dispose() {
        stopDraw();
        if (bubbleSpeech != null) {
            bubbleSpeech.dispose();
        }
        particleEffect.dispose();
    }
}
