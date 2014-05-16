package fr.wisper.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import fr.wisper.camera.MultipleVirtualViewportBuilder;
import fr.wisper.camera.OrthographicCameraWithVirtualViewport;
import fr.wisper.camera.VirtualViewport;
import fr.wisper.screens.MainMenu;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

public class WisperGame extends Game {
    // Fps
    private FPSLogger fps;

    // Camera
    static public OrthographicCameraWithVirtualViewport Camera;
    static public MultipleVirtualViewportBuilder MultipleVirtualViewportBuilder;
    static public VirtualViewport VirtualViewport;

    @Override
	public void create () {
        // Fps
        fps = new FPSLogger();

        // Camera
        MultipleVirtualViewportBuilder = new MultipleVirtualViewportBuilder(
                Config.APP_WIDTH, Config.APP_HEIGHT,
                Config.APP_WIDTH, Config.APP_HEIGHT);
        VirtualViewport = MultipleVirtualViewportBuilder.getVirtualViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Camera = new OrthographicCameraWithVirtualViewport(VirtualViewport);
        Camera.position.set(Config.APP_WIDTH / 2, Config.APP_HEIGHT / 2, 0f);

        setScreen(new MainMenu());

        Debug.PrintDebugInformation();
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
