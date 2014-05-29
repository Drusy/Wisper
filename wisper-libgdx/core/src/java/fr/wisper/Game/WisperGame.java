package fr.wisper.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import fr.wisper.camera.MultipleVirtualViewportBuilder;
import fr.wisper.camera.OrthographicCameraWithVirtualViewport;
import fr.wisper.camera.VirtualViewport;
import fr.wisper.screens.gamescreen.MainMenu;
import fr.wisper.screens.loading.LoadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

public class WisperGame extends Game {
    // Fps
    private FPSLogger fps;

    // Preferences
    public static Preferences preferences;

    // Loader
    private LoadingScreen loader;

    // Camera
    static public OrthographicCameraWithVirtualViewport Camera;
    static public MultipleVirtualViewportBuilder MultipleVirtualViewportBuilder;
    static public VirtualViewport VirtualViewport;

    @Override
	public void create () {
        // Preferences
        preferences = Gdx.app.getPreferences(Config.GAME_NAME);

        // Fps
        fps = new FPSLogger();

        // Camera
        MultipleVirtualViewportBuilder = new MultipleVirtualViewportBuilder(
                Config.APP_WIDTH, Config.APP_HEIGHT,
                Config.APP_WIDTH, Config.APP_HEIGHT);
        VirtualViewport = MultipleVirtualViewportBuilder.getVirtualViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Camera = new OrthographicCameraWithVirtualViewport(VirtualViewport);
        Camera.position.set(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2, 0f);

        // Loader
        loader = new LoadingScreen(this);
        loader.setNextScreen(new MainMenu());

        Debug.PrintDebugInformation();
	}

    public LoadingScreen getLoader() {
        return loader;
    }

	@Override
	public void render () {
        super.render();

        fps.log();
	}

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();

        loader.dispose();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void resize(int width, int height) {
        VirtualViewport = MultipleVirtualViewportBuilder.getVirtualViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Camera.setVirtualViewport(VirtualViewport);
        Camera.updateViewport();
        Camera.position.set(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2, 0f);

        super.resize(width, height);
    }
}
