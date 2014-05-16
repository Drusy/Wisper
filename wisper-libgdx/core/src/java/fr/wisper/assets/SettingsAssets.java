package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public class SettingsAssets {

    public static AnnotationAssetManager manager;

    public static void load() {
        manager = new AnnotationAssetManager();
        manager.load(SettingsAssets.class);
    }

    @Asset
    public static final AssetDescriptor<Texture>
            SettingsSplash = new AssetDescriptor<Texture>("splash/settings.png", Texture.class);

    public static void dispose() {
        manager.dispose();
    }

}