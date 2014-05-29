package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
            SplashScreen = new AssetDescriptor<Texture>("splash/splash.png", Texture.class),
            SettingsButton = new AssetDescriptor<Texture>("ui/settings-icon.png", Texture.class);

    @Asset
    public static final AssetDescriptor<TextureAtlas>
            Atlas = new AssetDescriptor<TextureAtlas>("ui/atlas.pack", TextureAtlas.class);

    @Asset
    public static final AssetDescriptor<Skin>
            GlobalSkin = new AssetDescriptor<Skin>("ui/skin.json", Skin.class, new SkinLoader.SkinParameter("ui/atlas.pack"));

    public static void dispose() {
        manager.dispose();
    }

}