package fr.wisper.animations.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyAccessor implements TweenAccessor<Body> {
    public static final int X = 0;
    public static final int Y = 1;

    @Override
    public int getValues(Body target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case X:
                returnValues[0] = target.getPosition().x;
                return 1;
            case Y:
                returnValues[0] = target.getPosition().y;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Body target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case X:
                target.setTransform(newValues[0], target.getPosition().y, target.getAngle());
                break;
            case Y:
                target.setTransform(target.getPosition().x, newValues[0], target.getAngle());
                break;
            default:
                assert false;
        }
    }
}
