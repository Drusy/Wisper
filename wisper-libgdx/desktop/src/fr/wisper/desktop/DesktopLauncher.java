package fr.wisper.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.wisper.Game.WisperGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Wisper";
        config.width = 1280;
        config.height = 768;

        config.addIcon("icons/wisper_16_windows.png", Files.FileType.Local);
        config.addIcon("icons/wisper_32_pc.png", Files.FileType.Local);
        config.addIcon("icons/wisper_128_mac.png", Files.FileType.Local);

		new LwjglApplication(new WisperGame(), config);
	}
}
