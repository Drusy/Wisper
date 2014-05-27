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

    public void applyForceToCenter() {
        wisperBody.applyForceToCenter(movement, true);
    }

    public void draw(SpriteBatch batch, float delta) {
        wisper.setPosition(wisperBody.getPosition().x - .5f, wisperBody.getPosition().y);
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
        fixtureDef.friction = .25f; // [0 - 1]
        fixtureDef.restitution = .5f; // velocity loose

        Body body = world.createBody(wisperBodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);

        return body;
    }

    public void dispose() {
        wisper.dispose();
    }

}
