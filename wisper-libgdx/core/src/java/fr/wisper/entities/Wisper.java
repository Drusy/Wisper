package fr.wisper.entities;


import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.wisper.tween.ParticleEffectAccessor;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

import javax.xml.bind.ValidationException;
import java.util.Timer;
import java.util.TimerTask;

public class Wisper {
    private ParticleEffect particleEffect;
    private boolean isParticleOn = true;
    private boolean isDashUp = true;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    public Wisper(String particleFile) {
        init(particleFile);
    }

    public void draw(Batch batch, float delta) {
        if (isParticleOn) {
            particleEffect.draw(batch, delta);
        }
    }

    public void stopDraw() {
        isParticleOn = false;
    }

    public void init(String particleFile) {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal(particleFile), Gdx.files.internal("particles"));
        particleEffect.setPosition(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2);
        particleEffect.start();
    }

    public int getX() {
        return (int)particleEffect.getEmitters().first().getX();
    }

    public int getY() {
        return (int)particleEffect.getEmitters().first().getY();
    }

    public void moveTo(int x, int y, TweenManager tweenManager, Viewport viewport) {
        //Vector2 particlePos = Config.getProjectedCoordinates(getX(), getY(), viewport);
        //Vector2 requestedPos = Config.getProjectedCoordinates(x, y, viewport);

        Vector2 particlePos = new Vector2(getX(), getY());
        Vector2 requestedPos = new Vector2(x, y);

        double distance = Math.sqrt(
                (float)Math.pow(particlePos.x - requestedPos.x, 2) +
                (float)Math.pow(particlePos.y - requestedPos.y, 2));
        double duration = distance / Config.WISPER_SPEED;

        tweenManager.killTarget(particleEffect);
        Tween.to(particleEffect, ParticleEffectAccessor.X, (float)duration).target(x).start(tweenManager);
        Tween.to(particleEffect, ParticleEffectAccessor.Y, (float)duration).target(y).start(tweenManager);
    }

    public void dash(int x, int y, TweenManager tweenManager) {
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
            Tween.to(particleEffect, ParticleEffectAccessor.X, Config.WISPER_DASH_DURATION).target(BPrim.x).start(tweenManager);
            Tween.to(particleEffect, ParticleEffectAccessor.Y, Config.WISPER_DASH_DURATION).target(BPrim.y).start(tweenManager);

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    isDashUp = true;
                }
            };
            isDashUp = false;
            timer.schedule(timerTask, Config.WISPER_DASH_TIMEOUT);
        } else {
            Debug.Log("Dash not ready yet, " + (timerTask.scheduledExecutionTime() - System.currentTimeMillis()) + "ms remaining");
        }
    }

    public void dispose() {
        particleEffect.dispose();
    }
}
