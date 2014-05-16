package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public class MenuAssets {

    public static AnnotationAssetManager manager;

    public static void load() {
        manager = new AnnotationAssetManager();
        manager.load(MenuAssets.class);
    }

    @Asset
    public static final AssetDescriptor<Texture>
            CloseWisperButton = new AssetDescriptor<Texture>("ui/close-button.png", Texture.class),
            NewGameButton = new AssetDescriptor<Texture>("ui/start-button.png", Texture.class),
            SplashScreen = new AssetDescriptor<Texture>("splash/splash.png", Texture.class);

    public static void dispose() {
        manager.dispose();
    }

}