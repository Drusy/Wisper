package fr.wisper.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

public class ParticleEffectAccessor implements TweenAccessor<ParticleEffect> {
    public static final int X = 0;
    public static final int Y = 1;

    @Override
    public int getValues(ParticleEffect target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case X:
                returnValues[0] = target.getEmitters().first().getX();

                return 1;
            case Y:
                returnValues[0] = target.getEmitters().first().getY();

                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(ParticleEffect target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case X:
                target.setPosition(
                    newValues[0],
                    target.getEmitters().first().getY());

                break;
            case Y:
                target.setPosition(
                    target.getEmitters().first().getX(),
                    newValues[0]);
                break;
            default:
                assert false;
        }
    }
}
