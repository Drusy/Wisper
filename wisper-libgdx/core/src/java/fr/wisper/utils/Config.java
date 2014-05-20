package fr.wisper.utils;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Config {
    public static final String GAME_NAME = "Wisper";
    public static final String GAME_VERSION = "v0.1";
    public static final int APP_WIDTH = 1280;
    public static final int APP_HEIGHT = 768;
    public static final String DEFAULT_SAVE_FOLDER = Config.GAME_NAME + "/saves";
    public static final float ANIMATION_DURATION = 1.5f;
    public static final float WISPER_SPEED = 150;
    public static final float DOUBLE_TAP_INTERVAL = 500f;
    public static final float WISPER_DASH_DISTANCE = 300f;
    public static final float WISPER_DASH_DURATION = 0.25f;
    public static final long WISPER_DASH_TIMEOUT = 2000;

    public static boolean isAndroid() {
        return (Gdx.app.getType() == Application.ApplicationType.Android);
    }

    public static Vector2 getProjectedCoordinates(int screenX, int screenY, Viewport viewport) {
        Vector2 touchPos = new Vector2(screenX, screenY);
        //touchPos = getCamera().unproject(touchPos);
        float xRatio = (float) Config.APP_WIDTH / (float) viewport.getViewportWidth();
        float yRatio = (float) Config.APP_HEIGHT / (float) viewport.getViewportHeight();

        touchPos.x -= (Gdx.graphics.getWidth() - viewport.getViewportWidth()) / 2;
        touchPos.x *= xRatio;

        touchPos.y = Gdx.graphics.getHeight() - touchPos.y;
        touchPos.y -= (Gdx.graphics.getHeight() - viewport.getViewportHeight()) / 2;
        touchPos.y *= yRatio;

        return touchPos;
    }
}
