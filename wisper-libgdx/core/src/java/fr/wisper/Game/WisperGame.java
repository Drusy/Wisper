package fr.wisper.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import fr.wisper.screens.SplashScreen;
import fr.wisper.utils.Debug;

public class WisperGame extends Game {
    FPSLogger fps = new FPSLogger();

    @Override
	public void create () {
        setScreen(new SplashScreen());

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
        super.resize(width, height);
    }
}
