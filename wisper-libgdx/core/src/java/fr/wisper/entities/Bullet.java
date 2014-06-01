package fr.wisper.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import fr.wisper.utils.Config;

public class Bullet extends Actor {
    public static final int BLACK_BULLET = 0;
    public static final int BLUE_BULLET = 1;
    public static final int RED_BULLET = 2;

    protected ParticleEffect particleEffect;
    protected boolean isParticleOn = true;
    protected float offset;

    public Bullet(String particleFile) {
        particleEffect = new ParticleEffect();
        init(particleFile);
    }

    public void setPosition(float x, float y) {
        particleEffect.setPosition(x - offset, y);
    }

    public void draw(Batch batch, float delta) {
        if (isParticleOn) {
            particleEffect.draw(batch, delta);
        }
    }

    public boolean isComplete() {
        return particleEffect.isComplete();
    }


    public float getOffset() {
        return offset;
    }

    public void stopDraw() {
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

    public void dispose() {
        stopDraw();
        particleEffect.dispose();
    }
}
