package fr.wisper.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.wisper.Game.WisperGame;
import fr.wisper.utils.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.useGL30 = true;
        config.vSyncEnabled = true;
        config.title = Config.GAME_NAME + " " + Config.GAME_VERSION;
        config.width = Config.APP_WIDTH;
        config.height = Config.APP_HEIGHT;

        config.addIcon("icons/wisper_16_windows.png", Files.FileType.Local);
        config.addIcon("icons/wisper_32_pc.png", Files.FileType.Local);
        config.addIcon("icons/wisper_128_mac.png", Files.FileType.Local);

		new LwjglApplication(new WisperGame(), config);
	}
}
