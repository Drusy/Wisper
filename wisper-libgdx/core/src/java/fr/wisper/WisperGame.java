package fr.wisper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WisperGame extends ApplicationAdapter {
    FPSLogger fps = new FPSLogger();

    private static final int    FRAME_COLS = 6;
    private static final int    FRAME_ROWS = 5;

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    SpriteBatch spriteBatch;
    TextureRegion currentFrame;

    float stateTime;

    @Override
	public void create () {
        walkSheet = new Texture(Gdx.files.internal("sprites/animation_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);
        spriteBatch = new SpriteBatch();
        stateTime = 0f;

        Debug.PrintDebugInformation();
	}

	@Override
	public void render () {
        fps.log();

        if (Gdx.input.isTouched()) {
            System.out.println("Touch (" + Gdx.input.getX() + ", " + Gdx.input.getY() + ")");
        }

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);
        spriteBatch.end();
	}
}
