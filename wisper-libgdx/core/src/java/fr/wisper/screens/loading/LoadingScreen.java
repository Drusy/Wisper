package fr.wisper.screens.loading;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import fr.wisper.Game.WisperGame;
import fr.wisper.assets.LoaderAssets;
import fr.wisper.screens.gamescreen.FadingScreen;
import fr.wisper.utils.Config;
import fr.wisper.utils.Debug;

/**
 * @author Mats Svensson
 */
public class LoadingScreen implements Screen {
    private final int LoadingBarWidth = 450;
    private final int LoadingBarHeight = 450;

    private Stage stage;
    private FadingScreen nextScreen;
    private WisperGame game;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;

    public LoadingScreen(WisperGame game) {
        // Assets
        LoaderAssets.load();
        LoaderAssets.manager.finishLoading();

        // Stage
        this.game = game;
        stage = new Stage();

        // Get our textureatlas from the manager
        TextureAtlas atlas = LoaderAssets.manager.get(LoaderAssets.LoaderPack);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        //Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
        //anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        //loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Add all the actors to the stage
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        initLoadingScreen();
    }

    @Override
    public void show() {
        percent = 0f;
    }

    private void initLoadingScreen() {
        // Place the logo in the middle of the screen and 100 px up
        logo.setX((Config.APP_WIDTH - logo.getWidth()) / 2);
        logo.setY((Config.APP_HEIGHT - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((Config.APP_WIDTH - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((Config.APP_HEIGHT - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = LoadingBarWidth - 10;

        // The rest of the hidden bar
        loadingBg.setSize(LoadingBarWidth, LoadingBarHeight);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    public void setNextScreen(FadingScreen screen) {
        Screen screenToLoad = this;

        screen.load();
        nextScreen = screen;

        if (screen.getAssetManager() == null || screen.getAssetManager().update()) {
            screenToLoad = screen;
        }

        game.setScreen(screenToLoad);
    }

    @Override
    public void resize(int width, int height) {
        WisperGame.Camera.zoom = 1f;
        WisperGame.Camera.updateViewport();

        ScalingViewport stageViewport = new ScalingViewport(
                Scaling.fit,
                WisperGame.VirtualViewport.getVirtualWidth(),
                WisperGame.VirtualViewport.getVirtualHeight(),
                WisperGame.Camera);

        stage.setViewport(stageViewport);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Interpolate the percentage to make it more smooth
        //percent = Interpolation.linear.apply(percent, nextScreen.getAssetManager().getProgress(), 0.1f);
        percent = nextScreen.getAssetManager().getProgress();

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(LoadingBarWidth - LoadingBarWidth * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();

        if (nextScreen.getAssetManager().update()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(nextScreen);
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        LoaderAssets.dispose();
    }
}