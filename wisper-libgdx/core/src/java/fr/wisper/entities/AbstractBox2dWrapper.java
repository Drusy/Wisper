package fr.wisper.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractBox2dWrapper extends Actor {
    protected Body body;

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void resetBody() {
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
    }

    public float getAngle() {
        return  body.getAngle();
    }

    public abstract boolean isComplete();
    public abstract void dispose();
    @Override
    public abstract void draw(Batch batch, float parentAlpha);
}
