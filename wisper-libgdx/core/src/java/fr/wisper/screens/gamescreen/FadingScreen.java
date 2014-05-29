package fr.wisper.screens.gamescreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public interface FadingScreen extends Screen {
    public void fadeTo(FadingScreen screen);

    public AssetManager getAssetManager();

    public void load();
}
