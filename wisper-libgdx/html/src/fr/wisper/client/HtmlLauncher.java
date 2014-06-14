package fr.wisper.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import fr.wisper.Game.WisperGame;
import fr.wisper.utils.Config;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.APP_WIDTH, Config.APP_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new WisperGame();
        }
}