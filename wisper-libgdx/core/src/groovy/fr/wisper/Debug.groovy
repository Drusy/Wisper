package fr.wisper

import com.badlogic.gdx.Gdx

class Debug {
    static def PrintDebugInformation() {
        def javaHeap = Gdx.app.getJavaHeap()
        def nativeHeap = Gdx.app.getNativeHeap()
        def appType = Gdx.app.getType()

        Gdx.app.log("Debug", "------ Debug informations ------");
        Gdx.app.log("Debug", "Java Heap : ${javaHeap / 1000000f}");
        Gdx.app.log("Debug", "Native Heap : ${nativeHeap/ 1000000f}");
        Gdx.app.log("Debug", "Application Type : ${appType}");
        Gdx.app.log("Debug", "--------------------------------");
    }
}
