package fr.wisper.utils;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Config {
    public static final String GAME_NAME = "Wisper";
    public static final String GAME_VERSION = "v0.1";
    public static final int APP_WIDTH = 1280;
    public static final int APP_HEIGHT = 768;
    public static final String DEFAULT_SAVE_FOLDER = Config.GAME_NAME + "/saves";
    public static final float ANIMATION_DURATION = 1.5f;

    public static boolean isAndroid() {
        return (Gdx.app.getType() == Application.ApplicationType.Android);
    }
}
