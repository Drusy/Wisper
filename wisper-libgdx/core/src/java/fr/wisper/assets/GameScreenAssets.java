package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public class GameScreenAssets {

    public static AnnotationAssetManager manager;

    public static void load() {
        manager = new AnnotationAssetManager();
        manager.load(GameScreenAssets.class);
    }

    public static void dispose() {
        manager.dispose();
    }

}