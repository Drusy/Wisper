package fr.wisper.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import fr.wisper.utils.Config;

public class WisperBox2d extends AbstractBox2dWrapper {
    // Wisper
    private Wisper wisper;
    private int wisperType;

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
        wisperType = particleId;

        body = createWisperBody(world);
    }

    public int getType() {
        return wisperType;
    }

    @Override
    public boolean isComplete() {
        return wisper.isComplete();
    }

    public void explode() {
        wisper.explode();
    }

    @Override
    public void draw(Batch batch, float delta) {
        wisper.setPosition(body.getPosition().x - wisper.getOffset() / 2, body.getPosition().y);
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
        body.setGravityScale(0);

        return body;
    }

    @Override
    public void dispose() {
        wisper.dispose();
    }

}
