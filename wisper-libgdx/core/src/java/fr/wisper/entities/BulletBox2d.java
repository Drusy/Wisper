package fr.wisper.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import fr.wisper.utils.Config;

public class BulletBox2d extends AbstractBox2dWrapper {
    // Wisper
    private Bullet bullet;

    public BulletBox2d(int particleId, World world) {
        switch (particleId) {
            case Bullet.BLACK_BULLET:
                bullet = new Bullet("particles/black-bullet-small-noadditive.p");
                break;
            case Bullet.BLUE_BULLET:
                bullet = new Bullet("particles/blue-bullet-small-noadditive.p");
                break;
            case Bullet.RED_BULLET:
                bullet = new Bullet("particles/red-bullet-small-noadditive.p");
                break;
            default:
                break;
        }

        body = createBulletBody(world);
        body.setBullet(true);
    }

    @Override
    public void draw(Batch batch, float delta) {
        bullet.setPosition(body.getPosition().x - bullet.getOffset() / 2, body.getPosition().y);
        bullet.draw(batch, delta);
    }

    private Body createBulletBody(World world) {
        CircleShape shape = new CircleShape();
        shape.setRadius(1f); // Meters

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

    @Override
    public void dispose() {
        bullet.dispose();
    }

    @Override
    public boolean isComplete() {
        return bullet.isComplete();
    }
}
