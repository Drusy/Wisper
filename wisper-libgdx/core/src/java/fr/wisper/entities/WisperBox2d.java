package fr.wisper.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import fr.wisper.utils.Config;

public class WisperBox2d {
    // Box2d
    private Body wisperBody;
    public Vector2 movement = new Vector2();

    // Wisper
    private Wisper wisper;

    public WisperBox2d(String particleFile, World world) {
        wisper = new Wisper(particleFile);
        wisperBody = createWisperBody(world);
    }

    public float getAngle() {
        return  wisperBody.getAngle();
    }

    public WisperBox2d(int particleId, World world) {
        switch (particleId) {
            case Wisper.BLACK_WISPER:
                wisper = new Wisper("particles/black-wisper-small-noadditive.p");
                break;
            case Wisper.BLUE_WISPER:
                wisper = new Wisper("particles/blue-wisper-small-noadditive.p");
                break;
            case Wisper.RED_WISPER:
                wisper = new Wisper("particles/red-wisper-small-noadditive.p");
                break;
            default:
                break;
        }

        wisperBody = createWisperBody(world);
    }

    public Body getWisperBody() {
        return wisperBody;
    }

    public Vector2 getPosition() {
        return wisperBody.getPosition();
    }

    public void resetBody() {
        wisperBody.setLinearVelocity(0, 0);
        wisperBody.setAngularVelocity(0);
    }

    public boolean isComplete() {
        return wisper.isComplete();
    }

    public void explode() {
        wisper.explode();
    }

    public void applyForceToCenter() {
        wisperBody.applyForceToCenter(movement, true);
    }

    public void draw(SpriteBatch batch, float delta) {
        wisper.setPosition(wisperBody.getPosition().x - wisper.getWisperOffset() / 2, wisperBody.getPosition().y);
        wisper.draw(batch, delta);
    }

    private Body createWisperBody(World world) {
        CircleShape shape = new CircleShape();
        shape.setRadius(1.5f); // Meters

        BodyDef wisperBodyDef = new BodyDef();
        wisperBodyDef.type = BodyDef.BodyType.DynamicBody;
        wisperBodyDef.position.set(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3f; // kg
        fixtureDef.friction = 1f; // [0 - 1]
        fixtureDef.restitution = 0f; // velocity loose

        Body body = world.createBody(wisperBodyDef);
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        shape.dispose();

        body.setUserData(this);

        return body;
    }

    public void dispose() {
        wisper.dispose();
    }

}
