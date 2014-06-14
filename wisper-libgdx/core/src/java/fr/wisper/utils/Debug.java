package fr.wisper.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Debug {
    static final private String DebugTag = "[Wisper Debug]";

    static public void PrintDebugInformation() {
        long javaHeap = Gdx.app.getJavaHeap();
        long nativeHeap = Gdx.app.getNativeHeap();
        Application.ApplicationType appType = Gdx.app.getType();

        Gdx.app.log(DebugTag, "------ Debug informations ------");
        Gdx.app.log(DebugTag, "Java Heap : " + (javaHeap / 1000000f));
        Gdx.app.log(DebugTag, "Native Heap : " + (nativeHeap / 1000000f));
        Gdx.app.log(DebugTag, "Application Type : " + appType.toString());
        Gdx.app.log(DebugTag, "--------------------------------");
    }

    static public void Log(String logLine) {
        Gdx.app.log(DebugTag, logLine);
    }
}
