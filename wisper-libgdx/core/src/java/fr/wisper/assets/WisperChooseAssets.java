package fr.wisper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public class WisperChooseAssets {

    public static AnnotationAssetManager manager;

    public static void load() {
        manager = new AnnotationAssetManager();
        manager.load(WisperChooseAssets.class);
    }


    @Asset
    public static final AssetDescriptor<Skin>
            GlobalSkin = new AssetDescriptor<Skin>("ui/skin.json", Skin.class, new SkinLoader.SkinParameter("ui/atlas.pack"));


    public static void dispose() {
        manager.dispose();
    }

}