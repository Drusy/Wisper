package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public class LoaderAssets {

    public static AnnotationAssetManager manager;

    public static void load() {
        manager = new AnnotationAssetManager();
        manager.load(LoaderAssets.class);
    }

    @Asset
    public static final AssetDescriptor<TextureAtlas>
            LoaderPack = new AssetDescriptor<TextureAtlas>("loader/loader.pack", TextureAtlas.class);

    public static void dispose() {
        manager.dispose();
    }

}